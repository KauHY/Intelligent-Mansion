package com.crlink.smartpark.system.service;

import com.crlink.smartpark.system.dto.menu.MenuDTO;
import com.crlink.smartpark.system.dto.user.ResetPwdForForgetDTO;
import com.crlink.smartpark.system.dto.user.RsAuthLoginDTO;
import com.crlink.smartpark.system.dto.user.RsUserValidateDTO;
import com.crlink.smartpark.system.dto.user.UpdatePwdDTO;
import com.crlink.smartpark.system.entity.BusinessUser;
import com.crlink.smartpark.system.model.query.BusinessUserMobileDTO;
import com.crlink.smartpark.system.model.query.BusinessUserQuery;
import com.crlink.smartpark.system.model.BusinessUserReqDTO;
import com.crlink.smartpark.system.dto.BusinessUserDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.vo.BusinessUserLoginVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 企业端用户表 服务类
 * </p>
 * */
public interface BusinessUserService extends IService<BusinessUser> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<BusinessUser, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateBusinessUser(BusinessUserReqDTO reqDTO);

    /**
     * 登陆
     *
     * @return 实体对象
     */
    BusinessUserLoginVO login(RsAuthLoginDTO rsAuthLoginDTO);

    /**
     *  注册获取短信验证码
     *
     * @param reqDTO 查询条件
     * @return   实体对象
     */
    Boolean getSmsCode(BusinessUserMobileDTO reqDTO);


    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<BusinessUserDTO> listBusinessUserPage(BusinessUserQuery query);

    /**
     * 忘记密码时获取短信验证码
     *
     * @param reqDTO
     * @return
     */
    Boolean getResetPwdSmsCode(BusinessUserMobileDTO reqDTO);
    /**
     * 忘记密码时验证身份
     *
     * @param userPwdResetDTO
     * @return
     */
    String validateIdentity(RsUserValidateDTO userPwdResetDTO);

    /**
     * 忘记密码时重置密码
     *
     * @param resetPwdForForgetDTO
     * @return
     */
    Boolean resetPwdForForget(ResetPwdForForgetDTO resetPwdForForgetDTO);

    /**
     * 修改密码
     *
     * @param updatePwdDTO
     * @return
     */
    Boolean updatePwd(UpdatePwdDTO updatePwdDTO);


}
