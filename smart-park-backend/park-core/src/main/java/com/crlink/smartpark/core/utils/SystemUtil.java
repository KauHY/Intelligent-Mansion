package com.crlink.smartpark.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.message.EmptyPageException;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.dto.wxuser.EmployeeInfoVO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.feign.OrganizationProvider;
import com.crlink.smartpark.system.vo.RsUserLoginVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.crlink.smartpark.common.constants.CacheConstant.CACHE_GROUP_EMPLOYEE;
import static com.crlink.smartpark.common.constants.Constants.*;

/**
 * @ClassName SystemUtil
 * @description:
 **/
@Slf4j
@Component
public class SystemUtil {

    @Resource
    private RedisUtil redisUtil;


    private static SystemUtil instance;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private CommonProvider commonProvider;

    @Autowired
    private EmployeeProvider employeeProvider;


    public SystemUtil() {
        instance = this;
    }

    /**
     * 获取当前对象
     */
    public static SystemUtil getInstance() {
        return instance;
    }

    /**
     * 获取当前用户编号
     */
    public String getCurrentUserId() {
        JSONObject header = parseHeader();
        String userId = header.getStr(USER_ID);
        return userId;
    }

    /**
     * 获取当前用户姓名
     */
    public String getCurrentUsername() {
        JSONObject header = parseHeader();
        return header.getStr(USER_NAME);
    }

    /**
     * 获取当前tokenId
     */
    public String getCurrentTokenId() {
        JSONObject header = parseHeader();
        return header.getStr(TOKEN_ID);
    }

    /**
     * 获取当前tokenId
     */
    public String getCurrentHeadOrgId() {
        JSONObject header = parseHeader();
        return header.getStr(HEAD_ORG_ID);
    }

    public String getCurrentHeadOrgName() {
        JSONObject header = parseHeader();
        return header.getStr(HEAD_ORG_NAME);
    }

    @Deprecated
    public RsUserLoginVO getCurrentUserInfo() {
        return (RsUserLoginVO) redisUtil.getObject(Constants.Redis.USER_INFO_PREFIX.getBuildCode(getCurrentTokenId()));
    }

    public JSONObject getWxUserInfo() {
        String userInfoStr = (String) redisUtil.getObject(Redis.WX_USER_INFO_PREFIX.getBuildCode(getCurrentTokenId()));
        JSONObject jsonObject = JSONUtil.parseObj(userInfoStr);
        return jsonObject;
    }

    //小程序使用
    public String getEmployeeCompanyId() {
        String companyId = null;
        String employeeId = this.getEmployeeId();
        try {
            companyId = this.getEmployeeInfo(employeeId).getCompanyId();
        } catch (Exception e) {
            log.error("查询员工信息出错！，employeeId:{}", employeeId, e);
        }
        return companyId;
    }

    //小程序使用
    public String getEmployeeParkOrgId() {
        String parkOrgId = null;
        String employeeId = this.getEmployeeId();
        try {
            parkOrgId = this.getEmployeeInfo(employeeId).getParkOrgId();
        } catch (Exception e) {
            log.error("查询员工信息出错！，employeeId:{}", employeeId, e);
            throw e;
        }
        return parkOrgId;
    }

    @Cacheable(cacheNames = CACHE_GROUP_EMPLOYEE, key = "'getEmployeeInfo_employeeId:'+#employeeId")
    public EmployeeInfoVO getEmployeeInfo(String employeeId) {
        Map<String, Map<String, Object>> employeeColumns = null;
        try {
            EmployeeQueryDTO queryDTO = EmployeeQueryDTO.employeeIdsBuildDTO(Sets.newHashSet(employeeId), "company_id", "park_org_id", "name");
            employeeColumns = employeeProvider.queryEmployeeColumns(queryDTO);
        } catch (Exception e) {
            throw new ResolinkRuntimeException("10002");
        }
        Map<String, Object> employee = Optional.ofNullable(employeeColumns.get(employeeId)).orElseThrow(() -> new ResolinkRuntimeException("40024"));
        EmployeeInfoVO employeeInfo = new EmployeeInfoVO();
        employeeInfo.setEmployeeId(employeeId);
        employeeInfo.setCompanyId((String) employee.get("company_id"));
        employeeInfo.setParkOrgId((String) employee.get("park_org_id"));
        employeeInfo.setName((String) employee.get("name"));
        Set<Integer> jocCodes = employeeProvider.getJobCodeByEmployeeIds(Sets.newHashSet(employeeId)).get(employeeId);
        employeeInfo.setJobCodes(jocCodes);
        return employeeInfo;
    }

    public Set<Integer> getEmployeeJobCodes() {
        String employeeId = getEmployeeId();
        Set<Integer> jocCodes = employeeProvider.getJobCodeByEmployeeIds(Sets.newHashSet(employeeId)).get(employeeId);
        return jocCodes;
    }


    //public String getUserMobile() {
    //    String mobile = getWxUserInfo().getStr(USER_MOBILE);
    //    if (StringUtils.isEmpty(mobile)) {
    //        try {
    //            mobile = getCurrentUserInfo().getMobile();
    //        } catch (Exception e) {
    //        }
    //    }
    //    return mobile;
    //
    //}

    public String getWxUserMobile() {
        JSONObject jsonObject = parseHeader();
        String wxUserMobile = jsonObject.getStr(USER_MOBILE);
        return wxUserMobile;
    }

    public List<EmployeeInfoVO> getWxUserEmployeeInfo() {
        String wxUserMobile = getWxUserMobile();
        Map<String, Map<String, Object>> employeeColumns = null;
        List<EmployeeInfoVO> userEmployeeInfo = Lists.newArrayList();
        try {
            EmployeeQueryDTO queryDTO = EmployeeQueryDTO.mobileBuildDTO(wxUserMobile, "company_id", "park_org_id");
            employeeColumns = employeeProvider.queryEmployeeColumns(queryDTO);
            if (ObjectUtil.isEmpty(employeeColumns)) {
                return userEmployeeInfo;
            }
            employeeColumns.forEach((employeeId, employee) -> {
                EmployeeInfoVO employeeInfo = new EmployeeInfoVO();
                employeeInfo.setEmployeeId(employeeId);
                employeeInfo.setCompanyId((String) employee.get("company_id"));
                employeeInfo.setParkOrgId((String) employee.get("park_org_id"));
                userEmployeeInfo.add(employeeInfo);
            });
            return userEmployeeInfo;
        } catch (Exception e) {
            throw new ResolinkRuntimeException("10002");
        }
    }

    /**
     * 获取租户的园区id集合
     *
     * @return
     */
    public Set<String> getTenantParkOrgIds() {
        String headOrgId = this.getCurrentHeadOrgId();
        Set<String> tenantParkOrgIds = organizationProvider.getTenantParkOrgIds(headOrgId);
        return tenantParkOrgIds;
    }

    public Set<String> getTenantParkOrgIds(String headOrgId) {
        Set<String> tenantParkOrgIds = organizationProvider.getTenantParkOrgIds(headOrgId);
        return tenantParkOrgIds;
    }

    public Set<String> getParkOrgIds() {
        String userId = this.getCurrentUserId();
        Object result = null;
        try {
            result = redisUtil.getObject(Constants.Redis.USER_PARK_ORG_IDS.getBuildCode(userId));
        } catch (Exception e) {
            log.info("【查询用户所属园区parkOrgId 出错】:{}", userId, e);
        }
        Set<String> parkOrgIds = ((Set<String>) result);
        if (CollectionUtil.isEmpty(parkOrgIds)) {
            parkOrgIds = organizationProvider.getUserParkOrgIds();
        }
        return parkOrgIds;
    }

    public Set<String> getParkOrgIdsOrRetEmpty() {
        Set<String> ids = getParkOrgIds();
        if (CollUtil.isEmpty(ids)) {
            throw new EmptyPageException();
        }
        return ids;
    }

    public Set<String> getParkOrgIdsOrFakeIds() {
        Set<String> ids = getParkOrgIds();
        if (CollUtil.isEmpty(ids)) {
            return Sets.newHashSet("-1");
        }
        return ids;
    }

    /**
     * 后台获取当前用户权限范围内的园区
     *
     * @param orgId
     * @return
     */
    public Set<String> getParkOrgIds(String orgId) {
        Set<String> parkOrgIds;
        if (StringUtils.hasText(orgId)) {
            parkOrgIds = commonProvider.getAllParkOrgIdsByOrgId(orgId);
        } else {
            parkOrgIds = this.getParkOrgIds();
        }
        return parkOrgIds;
    }

    public String getEmployeeId() {
        JSONObject header = parseHeader();
        return header.getStr(EMPLOYEE_ID);
    }

    public String getEmployeeIdOrEx() {
        String employeeId = getEmployeeId();
        AssertUtil.requireTrue(StrUtil.isNotBlank(employeeId), "70017");
        return employeeId;
    }

    public String getSource() {
        JSONObject header = parseHeader();
        return header.getStr(HEADER_SOURCE);
    }

    public JSONObject parseHeader() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String header = request.getHeader(Constants.HEADER_USER_INFO);
        JSONObject jsonObject = JSONUtil.parseObj(header);
        return jsonObject;
    }

    public String getParkOrgIdByEmployeeId() {
        String employeeId = getEmployeeId();
        MyPageInfoUtils.isEmptyPage(employeeId);
        String parkOrgId = this.getEmployeeInfo(employeeId).getParkOrgId();
        MyPageInfoUtils.isEmptyPage(parkOrgId);
        return parkOrgId;
    }

    /**
     * 获取当前所在公司
     */
    public String getCurCompanyId() {
        JSONObject header = parseHeader();
        return header.getStr(COMPANY_ID);
    }

    /**
     * 获取当前所在公司名
     */
    public String getCurCompanyName() {
        JSONObject header = parseHeader();
        return header.getStr(COMPANY_NAME);
    }

}

