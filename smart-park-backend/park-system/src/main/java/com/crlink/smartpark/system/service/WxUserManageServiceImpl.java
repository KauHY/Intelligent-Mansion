package com.crlink.smartpark.system.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.crlink.smartpark.common.bean.wx.WxCode2SessionDTO;
import com.crlink.smartpark.common.bean.wx.WxTelDTO;
import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.ProxyConnectionUtil;
import com.crlink.smartpark.core.utils.RedisUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.core.utils.TokenUtil;
import com.crlink.smartpark.system.dto.employee.EmployeeDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.dto.wxuser.EmployeeInfoVO;
import com.crlink.smartpark.system.dto.wxuser.WxUserDTO;
import com.crlink.smartpark.system.dto.wxuser.WxUserVO;
import com.crlink.smartpark.system.entity.*;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.common.RsUserService;
import com.crlink.smartpark.system.service.common.UserInfoService;
import com.crlink.smartpark.system.service.parkmanagement.CompanyService;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Security;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.*;
import static com.crlink.smartpark.common.constants.Constants.Redis.WX_USER_INFO_PREFIX;

@Slf4j
@Service
public class WxUserManageServiceImpl implements WxUserManageService {


    //    @Value("${param.wx.c.appId:wx1d92aa93493ed40a}")
    //@Value("${param.wx.c.appId:wx5766e6a4e09a4958}")
    //public String C_APP_ID;
    //    @Value("${param.wx.c.appSecret:f43f18bc3c7d4315a09ad1584cd23362}")
    //@Value("${param.wx.c.appSecret:f2b22be36b7e240ea27101c11486937f}")
    //public String C_APPLET_SECRET;

    //@Value("${param.wx.b.appId:wx9ac3e3b15a24d658}")
    //public String B_APP_ID;
    //@Value("${param.wx.b.appSecret:12517eac9c8ec5f2f57d39daca6dafc5}")
    //public String B_APPLET_SECRET;
    @Value("${param.wx.timeOut:60000}")
    private Integer timeOut; //请求超时时间：单位毫秒
    @Value("${param.wx.proxyIP:}")
    private String proxyIP;
    @Value("${param.wx.proxyPort:80}")
    private Integer proxyPort;


    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private RsUserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrganizationService organizationService;


    @Autowired
    private RedisUtil redisUtil;

    private static String CODE2SESSION_URI_FORMAT = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code&connect_redirect=1";

    public static final String WX_USER_NICKNAME_PREFIX = "wx_";


    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxUserVO authParkManagementLogin(WxUserDTO wxUserDTO) {
        Organization organization = organizationService.lambdaQuery().select(Organization::getHeadOrgId, Organization::getBAppSecret).eq(Organization::getBAppId, wxUserDTO.getAppId()).one();
        //Organization organization = organizationService.lambdaQuery().select(Organization::getHeadOrgId, Organization::getBAppSecret).eq(Organization::getBAppId, "wx9ac3e3b15a24d658").one();
        AssertUtil.requireNotNull(organization, "10012");
        String url = String.format(CODE2SESSION_URI_FORMAT, wxUserDTO.getAppId(), organization.getBAppSecret(), wxUserDTO.getCode());
        WxCode2SessionDTO code2SessionDTO = getCode2Session(url);
        String mobile = getPhoneNumber(wxUserDTO.getEncryptedData(), wxUserDTO.getIv(), code2SessionDTO.getSession_key());
        //String mobile = "17665354037";
        log.error("wxlogin1:" + mobile);
        log.error("wxlogin2:" + organization.getHeadOrgId());
        checkUserRelationEmployee(mobile, organization.getHeadOrgId());
        List<RsUserInfo> userInfos = userInfoService.listUserByMobile(mobile, StatusEnum.ENABLE.getCode());
        RsUserInfo userInfo = checkParkManagementUser(userInfos);
        String tokenId = IdUtil.simpleUUID();
        String wxToken = tokenUtil.wxToken(tokenId, userInfo.getUserId(), organization.getHeadOrgId(), mobile, USER_WX_SOURCE_B);
        WxUserVO wxUserInfo = getWxUserInfo(wxToken, userInfo, organization.getHeadOrgId());
        JSONObject jsonObject = buildWxUserInfo(userInfo.getUserId(), wxUserInfo);
        CompletableFuture.runAsync(() -> {
            redisUtil.setObject(WX_USER_INFO_PREFIX.getBuildCode(tokenId), JSONUtil.toJsonStr(jsonObject), OVERTIME, TimeUnit.MINUTES);
        });
        wxUserInfo = new WxUserVO(wxToken);
        return wxUserInfo;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxUserVO authParkVisitorLogin(WxUserDTO wxUserDTO) {
        Organization organization = organizationService.lambdaQuery().select(Organization::getHeadOrgId, Organization::getCAppSecret).eq(Organization::getCAppId, wxUserDTO.getAppId()).one();
        //Organization organization = organizationService.lambdaQuery().select(Organization::getHeadOrgId, Organization::getCAppSecret).eq(Organization::getCAppId, "wx5766e6a4e09a4958").one();
        AssertUtil.requireNotNull(organization, "10012");
        String url = String.format(CODE2SESSION_URI_FORMAT, wxUserDTO.getAppId(), organization.getCAppSecret(), wxUserDTO.getCode());
        WxCode2SessionDTO code2SessionDTO = getCode2Session(url);
        String mobile = getPhoneNumber(wxUserDTO.getEncryptedData(), wxUserDTO.getIv(), code2SessionDTO.getSession_key());
        //String mobile = "17665354037";
        List<RsUserInfo> userInfos = userInfoService.listUserByMobile(mobile, StatusEnum.ENABLE.getCode());
        RsUserInfo userInfo;
        if (ObjectUtils.isEmpty(userInfos)) {
            String nickname = WX_USER_NICKNAME_PREFIX + mobile;
            RsUser user = createWxTempUser(nickname);
            userInfo = createWxTempUserInfo(user.getId(), mobile, nickname);
            userService.save(user);
            userInfoService.save(userInfo);
        } else {
            userInfo = userInfos.get(0);
        }
        String tokenId = IdUtil.simpleUUID();
        String wxToken = tokenUtil.wxToken(tokenId, userInfo.getUserId(), organization.getHeadOrgId(), mobile, USER_WX_SOURCE_C);
        WxUserVO wxUserInfo = getWxUserInfo(wxToken, userInfo, organization.getHeadOrgId());
        JSONObject jsonObject = buildWxUserInfo(userInfo.getUserId(), wxUserInfo);
        CompletableFuture.runAsync(() -> {
            redisUtil.setObject(WX_USER_INFO_PREFIX.getBuildCode(tokenId), JSONUtil.toJsonStr(jsonObject), OVERTIME, TimeUnit.MINUTES);
        });
        wxUserInfo = new WxUserVO(wxToken);
        return wxUserInfo;

    }

    @Override
    public WxUserVO queryWxUserInfo() {
        JSONObject header = SystemUtil.getInstance().parseHeader();
        String userId = header.getStr(USER_ID);
        //header.getStr(Constants.TOKEN_ID);
        //header.getStr(Constants.USER_MOBILE);
        String headOrgId = header.getStr(HEAD_ORG_ID);
        RsUserInfo userInfo = userInfoService.lambdaQuery().eq(RsUserInfo::getUserId, userId).one();
        WxUserVO wxUserInfo = getWxUserInfo(null, userInfo, headOrgId);
        return wxUserInfo;
    }

    private RsUserInfo checkParkManagementUser(List<RsUserInfo> userInfos) {
        if (ObjectUtils.isEmpty(userInfos)) {
            throw new ResolinkRuntimeException("40019");
        }
        RsUserInfo userInfo = userInfos.get(0);
        String userId = userInfo.getUserId();
        Integer userType = (Integer) userService.id2ValueMap(Sets.newHashSet(userId), RsUser::getUserType).get(userId);
        if (TENANT_USER != userType) {
            throw new ResolinkRuntimeException("40019");
        }
        return userInfo;
    }


    /**
     * 手机号是否关联状态为启用的员工
     *
     * @param mobile
     */
    private void checkUserRelationEmployee(String mobile, String headOrgId) {
        EmployeeQueryDTO queryDTO = EmployeeQueryDTO.mobileBuildDTO(mobile);
        Map<String, EmployeeDTO> employeeDTOMap = employeeService.queryEmployeeByCondition(queryDTO, headOrgId);
        if (ObjectUtil.isEmpty(employeeDTOMap)) {
            throw new ResolinkRuntimeException("40023");
        }
    }

    private JSONObject buildWxUserInfo(String userId, WxUserVO wxUserInfo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(USER_ID, userId);
        jsonObject.put(USER_MOBILE, wxUserInfo.getMobile());
        jsonObject.put(WX_USER_INFO, wxUserInfo);
        return jsonObject;
    }


    private RsUser createWxTempUser(String nickname) {
        RsUser user = new RsUser();
        String userId = new DefaultIdentifierGenerator().nextId(user).toString();
        user.setId(userId);
        user.setUsername(nickname);
        user.setPassword(IdUtil.simpleUUID());
        user.setUserType(WECHAT_TEMP_USER);
        user.setCreateBy(userId);
        user.setUpdateBy(userId);
        return user;
    }

    private RsUserInfo createWxTempUserInfo(String userId, String mobile, String nickname) {
        RsUserInfo userInfo = new RsUserInfo();
        userInfo.setMobile(mobile)
                .setUserId(userId)
                .setNickname(nickname)
                .setCreateBy(userId)
                .setUpdateBy(userId);
        return userInfo;
    }

    private WxUserVO getWxUserInfo(String token, RsUserInfo userInfo, String headOrgId) {
        WxUserVO vo = new WxUserVO();
        String curName = StringUtils.hasText(userInfo.getRealName()) ? userInfo.getRealName() : userInfo.getNickname();
        vo.setToken(token);
        vo.setName(curName);
        vo.setMobile(userInfo.getMobile());
        vo.setAvatarUrl(userInfo.getAvatarUrl());
        List<DictVO> employeeInfo = buildEmployeeInfo(userInfo.getMobile(), headOrgId);
        vo.setEmployeeCompanies(employeeInfo);
        return vo;
    }

    private List<DictVO> buildEmployeeInfo(String mobile, String headOrgId) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getTenantParkOrgIds(headOrgId);
        if (ObjectUtil.isEmpty(parkOrgIds)) {
            return null;
        }
        List<Employee> employees = employeeService.lambdaQuery().select(Employee::getId, Employee::getParkOrgId, Employee::getCompanyId)
                .eq(Employee::getMobile, mobile).eq(Employee::getStatus, StatusEnum.ENABLE.getCode()).in(Employee::getParkOrgId, parkOrgIds).list();
        Map<String, String> employeeParkOrgMap = Maps.newHashMap();
        Map<String, String> employeeCompanyMap = employees.parallelStream().map(t -> {
            employeeParkOrgMap.put(t.getId(), t.getParkOrgId());
            return t;
        }).collect(Collectors.toMap(Employee::getId, Employee::getCompanyId));
        Map<String, Set<Integer>> employeeJobCodesMap = employeeService.getJobCodeByEmployeeIds(employeeParkOrgMap.keySet());
        Map<String, String> parkOrgId2NameMap = (Map<String, String>) organizationService.id2ValueMap(employeeParkOrgMap.values(), Organization::getOrgName);
        Map<String, String> companyId2NameMap = (Map<String, String>) companyService.id2ValueMap(employeeCompanyMap.values(), Company::getName);
        List<DictVO> employeeCompanies = employees.parallelStream().map(t -> {
            String companyId = employeeCompanyMap.get(t.getId());
            String parkOrgId = employeeParkOrgMap.get(t.getId());
            String companyName = companyId2NameMap.get(companyId);
            String parkOrgName = parkOrgId2NameMap.get(parkOrgId);
            String label = new StringBuilder(companyName).append("(")
                    .append(parkOrgName).append(")").toString();
            EmployeeInfoVO employeeInfoVO = new EmployeeInfoVO();
            employeeInfoVO.setEmployeeId(t.getId());
            employeeInfoVO.setCompanyId(companyId);
            employeeInfoVO.setCompanyName(companyName);
            employeeInfoVO.setParkOrgId(parkOrgId);
            employeeInfoVO.setParkOrgName(parkOrgName);
            employeeInfoVO.setJobCodes(employeeJobCodesMap.get(t.getId()));
            SimpleNode simpleNode = new SimpleNode(companyId, label);
            simpleNode.setExtraInfo(employeeInfoVO);
            return simpleNode;
        }).collect(Collectors.toList());
        return employeeCompanies;
    }

    /**
     * 获取openid，sessionKey等
     *
     * @return
     */
    private WxCode2SessionDTO getCode2Session(String url) {
        log.info("请求微信平台url：{}", url);
        String result = ProxyConnectionUtil.get(url, timeOut, proxyIP, proxyPort);
        log.info("请求微信平台结果：{}", result);
        AssertUtil.requireTrue(StringUtils.hasText(result), "10011");
        WxCode2SessionDTO dto = JSONUtil.toBean(result, WxCode2SessionDTO.class);
        AssertUtil.requireTrue(StringUtils.hasText(dto.getOpenid()), "10011", JSONUtil.parseObj(result).getStr("errmsg"));
        return dto;
    }

    /**
     * 获取手机号
     *
     * @param encryptedData
     * @param iv
     * @return
     */
    private String getPhoneNumberByCode(String url, String encryptedData, String iv) {
        String sessionKey = getCode2Session(url).getSession_key();
        return getPhoneNumber(encryptedData, iv, sessionKey);
    }

    /**
     * 获取手机号
     *
     * @param encryptedData
     * @param iv
     * @param sessionKey
     * @return
     */
    private String getPhoneNumber(String encryptedData, String iv, String sessionKey) {
        String result = wxDecrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
        log.info("decrypted result:{}, iv:{}, sessionKey:{}", result, iv, sessionKey);

        WxTelDTO wxTelDTO = JSONUtil.toBean(result, WxTelDTO.class);
        AssertUtil.requireTrue(wxTelDTO != null && !StringUtils.isEmpty(wxTelDTO.getPurePhoneNumber()), "10001");
        return wxTelDTO.getPurePhoneNumber();
    }

    public String wxDecrypt(byte[] content, byte[] keyByte, byte[] ivByte) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            Key sKeySpec = new SecretKeySpec(keyByte, "AES");
            IvParameterSpec ivp = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, ivp);
            return new String(cipher.doFinal(content), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("解密异常：", e);
            throw new ResolinkRuntimeException("10001");
        }
    }


}
