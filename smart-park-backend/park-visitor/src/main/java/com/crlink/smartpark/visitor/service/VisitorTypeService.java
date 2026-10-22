package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.dto.VisitorTypeDTO;
import com.crlink.smartpark.visitor.entity.VisitorType;
import com.crlink.smartpark.visitor.model.dto.VisitorTypeReqDTO;
import com.crlink.smartpark.visitor.model.query.VisitorTypeQuery;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 访客类型 服务类
 * </p>
 * */
public interface VisitorTypeService extends IService<VisitorType> {

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateVisitorType(VisitorTypeReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    VisitorTypeDTO getVisitorTypeById(String id);

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    PageInfo<VisitorTypeDTO> listVisitorTypePage(VisitorTypeQuery query);

    /**
     * 删除访客类型
     *
     * @param id
     * @return
     */
    Boolean removeVisitorTypeById(String id);

    /**
     * 根据入驻企业获取访客类型
     *
     * @param companyId
     * @return
     */
    List<DictVO> listVisitorTypeByCompany(String companyId);
}
