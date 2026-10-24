package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.customer.entity.EquipmentLedger;
import com.crlink.smartpark.customer.model.query.EquipmentLedgerQuery;
import com.crlink.smartpark.customer.model.dto.EquipmentLedgerReqDTO;
import com.crlink.smartpark.customer.dto.EquipmentLedgerDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 设备台账 服务类
 * </p>
 * */
public interface EquipmentLedgerService extends IService<EquipmentLedger> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<EquipmentLedger, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateEquipmentLedger(EquipmentLedgerReqDTO reqDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    EquipmentLedgerDTO getEquipmentLedgerById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<EquipmentLedgerDTO> listEquipmentLedgerPage(EquipmentLedgerQuery query);

    void export(EquipmentLedgerQuery query);

    /**
     * 设备台账导入
     *
     * @param file
     * @param parkOrgId
     */
    FileImportErrorInfoDTO importEquipment(MultipartFile file, String parkOrgId);

    Boolean scrap(String id);

    Boolean hasAssociate(String categoryId);
}
