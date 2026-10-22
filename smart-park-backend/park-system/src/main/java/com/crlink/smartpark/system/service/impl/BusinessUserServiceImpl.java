package com.crlink.smartpark.system.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.SmsConstant;
import com.crlink.smartpark.common.constants.enums.OrgTypeEnum;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.dto.BusinessUserDTO;
import com.crlink.smartpark.system.dto.menu.MenuDTO;
import com.crlink.smartpark.system.dto.user.ResetPwdForForgetDTO;
import com.crlink.smartpark.system.dto.user.RsAuthLoginDTO;
import com.crlink.smartpark.system.dto.user.RsUserValidateDTO;
import com.crlink.smartpark.system.dto.user.UpdatePwdDTO;
import com.crlink.smartpark.system.entity.*;
import com.crlink.smartpark.system.mapper.BusinessUserMapper;
import com.crlink.smartpark.system.model.BusinessUserReqDTO;
import com.crlink.smartpark.system.model.query.BusinessUserMobileDTO;
import com.crlink.smartpark.system.model.query.BusinessUserQuery;
import com.crlink.smartpark.system.service.BusinessUserService;
import com.crlink.smartpark.system.service.CompanyInformationService;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.vo.BusinessUserLoginVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crlink.smartpark.common.constants.Constants.SYSTEM_SALT;
import static com.crlink.smartpark.common.constants.Constants.USER_BUSINESS_SOURCE;

/**
 * 企业端用户表 服务实现类
 * */
@Service
public class BusinessUserServiceImpl extends ServiceImpl<BusinessUserMapper, BusinessUser>
        implements BusinessUserService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CompanyInformationService companyInformationService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<BusinessUser, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<BusinessUser> lambdaQuery = this.lambdaQuery().in(BusinessUser::getId, ids);
        if (flag) {
            lambdaQuery.select(BusinessUser::getId, sValue);
        }
        Stream<BusinessUser> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(BusinessUser::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(BusinessUser::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateBusinessUser(BusinessUserReqDTO reqDTO) {
        // 校验验证码
        String oldValidateCode =
                (String) redisUtil.getObject(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(reqDTO.getValidateCodeKey()));
        if (!reqDTO.getValidateCode().equalsIgnoreCase(oldValidateCode)) {
            redisUtil.delete(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(reqDTO.getValidateCodeKey()));
            throw new ResolinkRuntimeException("40004");
        }
        // 校验短信验证码
//        String smsCode =
//                (String) redisUtil.getObject(Constants.Redis.BUSINESS_REGISTER_CODE_PREFIX.getBuildCode(reqDTO.getMobile()));
//        if (!reqDTO.getSmsCode().equals(smsCode)) {
//            throw new ResolinkRuntimeException("40026");
//        }

        // 校验注册租户存不存在
        int countOrg = organizationService.lambdaQuery().eq(Organization::getId, reqDTO.getHeadOrgId())
                .eq(Organization::getParentId, OrgTypeEnum.COMPANY_ORGANIZATION.getCode()).count();
        if (countOrg < 1) {
            throw new ResolinkRuntimeException("700004");
        }
        // 校验用户名 手机号不允许重复
        int countByName = this.lambdaQuery().eq(BusinessUser::getUsername, reqDTO.getUsername()).count();
        if (countByName > 0) {
            throw new ResolinkRuntimeException("40027");
        }
        int countByMobile = this.lambdaQuery().eq(BusinessUser::getMobile, reqDTO.getMobile()).count();
        if (countByMobile > 0) {
            throw new ResolinkRuntimeException("40028");
        }
        String encodePwd = StringEncodeUtil.Str2SHA256(reqDTO.getPassword() + SYSTEM_SALT);
        BusinessUser entity = MyBeanUtils.copy(reqDTO, BusinessUser::new);
        entity.setPassword(encodePwd);
        return this.saveOrUpdate(entity);
    }

    @Override
    public BusinessUserLoginVO login(RsAuthLoginDTO rsAuthLoginDTO) {
        String oldValidateCode = (String) redisUtil
                .getObject(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(rsAuthLoginDTO.getValidateCodeKey()));
        if (!rsAuthLoginDTO.getValidateCode().equalsIgnoreCase(oldValidateCode)) {
            redisUtil.delete(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(rsAuthLoginDTO.getValidateCodeKey()));
            throw new ResolinkRuntimeException("40004");
        }
        redisUtil.delete(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(rsAuthLoginDTO.getValidateCodeKey()));
        // 密码后端加salt
        String encodePwd = StringEncodeUtil.Str2SHA256(rsAuthLoginDTO.getPassword() + SYSTEM_SALT);
        // 校验用户
        BusinessUser user = this.lambdaQuery().eq(BusinessUser::getUsername, rsAuthLoginDTO.getUsername())
                .eq(BusinessUser::getPassword, encodePwd).one();
        if (user == null) {
            throw new ResolinkRuntimeException("40005");
        }
        String tokenId = IdUtil.simpleUUID();
        String token = JwtBaseUtil.generateBusinessUserToken(tokenId, user.getId(), user.getHeadOrgId(),
                user.getUsername(), user.getMobile(), USER_BUSINESS_SOURCE);
        BusinessUserLoginVO businessUserLoginVO = MyBeanUtils.copy(user, BusinessUserLoginVO::new, "password");
        businessUserLoginVO.setAccessToken(token);
        businessUserLoginVO.setAccessId(tokenId);
        CompanyInformation companyInformation = companyInformationService.lambdaQuery().eq(CompanyInformation::getBusinessUserId, user.getId()).one();
        if (companyInformation != null) {
            businessUserLoginVO.setCode(companyInformation.getCode());
        }
        CompletableFuture.runAsync(() -> {
            redisUtil.setObject(Constants.Redis.USER_ACCESS_TOKEN_PREFIX.getBuildCode(tokenId), token,
                    Constants.OVERTIME, TimeUnit.MINUTES);
            redisUtil.setObject(Constants.Redis.USER_BUSINESS_INFO_PREFIX.getBuildCode(tokenId), businessUserLoginVO,
                    Constants.OVERTIME, TimeUnit.MINUTES);
        });
        return businessUserLoginVO;
    }

    @Override
    public Boolean getSmsCode(BusinessUserMobileDTO reqDTO) {
        String code = String.format("%06d", new Random().nextInt(9999));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        boolean flag = SendSmsUtil.sendSms(SmsConstant.SIGN_NAME, SmsConstant.COMPANY_REGISTER_CODE, reqDTO.getMobile(),
                JSONUtil.toJsonStr(jsonObject));
        if (flag) {
            String smsCodeKey = Constants.Redis.BUSINESS_REGISTER_CODE_PREFIX.getBuildCode(reqDTO.getMobile());
            redisUtil.setObject(smsCodeKey, code, 15L, TimeUnit.MINUTES);
        }
        return flag;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<BusinessUserDTO> listBusinessUserPage(BusinessUserQuery query) {
        query.startPage();
        List<BusinessUser> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<BusinessUserDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public Boolean getResetPwdSmsCode(BusinessUserMobileDTO reqDTO) {
        String code = String.format("%06d", new Random().nextInt(999999));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        boolean flag = SendSmsUtil.sendSms(SmsConstant.SIGN_NAME, SmsConstant.COMPANY_TEMPLATE_CODE, reqDTO.getMobile(),
                JSONUtil.toJsonStr(jsonObject));
        if (flag) {
            String smsCodeKey = Constants.Redis.RESET_PWD_CODE_PREFIX.getBuildCode(reqDTO.getMobile());
            redisUtil.setObject(smsCodeKey, code, 15L, TimeUnit.MINUTES);
        }
        return flag;
    }

    @Override
    public String validateIdentity(RsUserValidateDTO userPwdResetDTO) {
        // 验证码校验
        String oldValidateCode = (String) redisUtil
                .getObject(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(userPwdResetDTO.getValidateCodeKey()));
        redisUtil.delete(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(userPwdResetDTO.getValidateCodeKey()));
        AssertUtil.requireTrue(userPwdResetDTO.getValidateCode().equalsIgnoreCase(oldValidateCode), "40004");
        // 手机验证码验证
        String smsCode = (String) redisUtil
                .getObject(Constants.Redis.RESET_PWD_CODE_PREFIX.getBuildCode(userPwdResetDTO.getMobilePhone()));
        AssertUtil.requireTrue(userPwdResetDTO.getTelValidateCode().equals(smsCode), "40012");
        BusinessUser rsUser = this.getOne(
                Wrappers.<BusinessUser>lambdaQuery().eq(BusinessUser::getUsername, userPwdResetDTO.getUsername()), false);
        AssertUtil.requireNotNull(rsUser, "40003");
        AssertUtil.requireTrue(userPwdResetDTO.getMobilePhone().equals(rsUser.getMobile()), "40013");
        String validateData = SimpleJwtUtil.signToken(userPwdResetDTO.getUsername());
        return validateData;
    }

    @Override
    public Boolean resetPwdForForget(ResetPwdForForgetDTO resetPwdForForgetDTO) {
        AssertUtil.requireTrueOrParamExp(
                resetPwdForForgetDTO.getNewPassword().equals(resetPwdForForgetDTO.getNewPasswordAgain()));
        String username = SimpleJwtUtil.checkToken(resetPwdForForgetDTO.getValidateToken());
        // 密码加密,密码后端加salt
        String encodePwd = StringEncodeUtil.Str2SHA256(resetPwdForForgetDTO.getNewPassword() + SYSTEM_SALT);
        return this.update(Wrappers.<BusinessUser>lambdaUpdate().set(BusinessUser::getPassword, encodePwd)
                .eq(BusinessUser::getUsername, username));
    }

    @Override
    public Boolean updatePwd(UpdatePwdDTO updatePwdDTO) {
        // 密码加密,密码后端加salt
        String encodePwd = StringEncodeUtil.Str2SHA256(updatePwdDTO.getOldPassword() + SYSTEM_SALT);
        // 校验用户原密码是否正确
        String userId = SystemUtil.getInstance().getCurrentUserId();
//        String userId = updatePwdDTO.getId();
//        if (!userId.equals(SystemUtil.getInstance().getCurrentUserId())) {
//            throw new ResolinkRuntimeException("100071");
//        }
        int countUser = this.count(Wrappers.<BusinessUser>query().lambda().eq(BusinessUser::getId, userId)
                .eq(BusinessUser::getPassword, encodePwd));
        if (countUser <= 0) {
            throw new ResolinkRuntimeException("40014");
        }
        if (!updatePwdDTO.getNewPassword().equals(updatePwdDTO.getNewPasswordAgain())) {
            throw new ResolinkRuntimeException("40015");
        }
        String encodeNewPwd = StringEncodeUtil.Str2SHA256(updatePwdDTO.getNewPassword() + SYSTEM_SALT);
        return passwordCommit(userId, encodeNewPwd);
    }

    private Boolean passwordCommit(String userId, String encodePwd) {
        BusinessUser user = new BusinessUser();
        user.setId(userId);
        user.setPassword(encodePwd);
        String currentUserId = SystemUtil.getInstance().getCurrentUserId();
        user.setUpdateBy(currentUserId);
        return this.updateById(user);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<BusinessUser> getConditionWrapper(BusinessUserQuery query) {
        LambdaQueryWrapper<BusinessUser> queryWrapper = new LambdaQueryWrapper<BusinessUser>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     *
     * @return DTO集合
     */
    private List<BusinessUserDTO> entitiesToDTOs(List<BusinessUser> entities) {
        return MyBeanUtils.copyList(entities, BusinessUserDTO::new, (s, t) -> {

        });
    }
}
