package com.crlink.smartpark.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.business.query.ApplyForHouseQuery;
import com.crlink.smartpark.business.dto.ApplyForHouseReqDTO;
import com.crlink.smartpark.business.vo.ApplyForHouseVO;
import com.crlink.smartpark.business.entity.ApplyForHouse;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 看房申请表 服务类
 * </p>
 * */
public interface ApplyForHouseService extends IService<ApplyForHouse> {


    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    ApplyForHouseVO getApplyForHouseById(String id);

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    PageInfo<ApplyForHouseVO> listApplyForHousePage(ApplyForHouseQuery condition);

    /**
     * 新增
     *
     * @param applyForHouseDTO
     * @return
     */
    Boolean create(ApplyForHouseReqDTO applyForHouseDTO);

    Boolean accept(String id);

    Boolean returnApply(String id);

    PageInfo<ApplyForHouse> listApplyForHouseWxPage(BaseQuery<String> query);
}
