package com.crlink.smartpark.customer.service;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.customer.entity.ProGoodsManager;
import com.crlink.smartpark.customer.controller.request.query.ProGoodsManagerQuery;
import com.crlink.smartpark.customer.model.dto.ProGoodsManagerReqDTO;
import com.crlink.smartpark.customer.dto.ProGoodsManagerDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.customer.model.dto.ProGoodsSettingsReqDTO;
import com.github.pagehelper.PageInfo;

import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 物品管理 服务类
 * </p>
 * */
public interface ProGoodsManagerService extends IService<ProGoodsManager> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProGoodsManager, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateProGoodsManager(ProGoodsManagerReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    ProGoodsManagerDTO getProGoodsManagerById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ProGoodsManagerDTO> listProGoodsManagerPage(ProGoodsManagerQuery query);

    /**
     * 根据ID删除物品数据
     *
     * @param id:
     * @return java.lang.Boolean
     * @MethodName removeGoodsById
     */
    Boolean removeGoodsById(String id);

    /**
     * 设置库存下限预警值
     *
     * @param reqDTO:
     * @return java.lang.Boolean
     * @MethodName settingMinStock
     */
    Boolean settingMinStock(ProGoodsSettingsReqDTO reqDTO);

    /**
     * 导出
     *
     * @param reqDTO:
     * @return java.lang.Boolean
     * @MethodName settingMinStock
     */
    void export(ProGoodsManagerQuery reqDTO);

    /**
     * 物品导入
     * @param file: 文件
     * @return  com.crlink.smartpark.common.dto.FileImportErrorInfoDTO
     * @MethodName importGoods
     */
    FileImportErrorInfoDTO importGoods(MultipartFile file,String parkOrgId);
}
