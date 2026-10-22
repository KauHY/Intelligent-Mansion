package com.crlink.smartpark.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.system.dto.user.RsUserQueryDTO;
import com.crlink.smartpark.system.entity.RsUser;
import com.crlink.smartpark.system.vo.RsUserInfoVO;
import com.crlink.smartpark.system.vo.RsUserListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 * */
public interface RsUserMapper extends BaseMapper<RsUser> {

    /**
     * 分页查询
     *
     * @param params 查询对象
     * @return 分页结果
     */
    List<RsUserInfoVO> listPage(@Param("params") RsUserQueryDTO params, @Param("orgIds") Set<String> orgIds);

    /**
     * 分页查询
     *
     * @param params 查询对象
     * @return 分页结果
     */
    List<RsUserListVO> userListBy(@Param("params") RsUserQueryDTO params);


    /**
     * 查询权限内所有的用户
     *
     * @param queryDTO
     * @param orgIds
     * @return
     */
    //List<UserDictDTO> listUserByOrg(@Param("params") RsUserDictQueryDTO queryDTO, @Param("orgIds") Set<String> orgIds);


}
