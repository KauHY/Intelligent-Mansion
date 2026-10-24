package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.visitor.entity.AuditPersonConfig;
import com.crlink.smartpark.visitor.dto.AuditPersonConfigSaveDTO;
import com.crlink.smartpark.visitor.dto.AuditPersonConfigUpdateDTO;
import com.crlink.smartpark.visitor.query.AuditPersonConfigListQuery;
import com.crlink.smartpark.visitor.vo.AuditPersonConfigListVO;
import com.crlink.smartpark.visitor.vo.AuditPersonConfigVO;
import com.github.pagehelper.PageInfo;

/**
 * 访客审批人配置服务接口
 * */
public interface AuditPersonConfigService extends IService<AuditPersonConfig> {

    /**
    * 新增访客审批人配置
    *
    */
    void save(AuditPersonConfigSaveDTO dto);

    /**
    * 修改访客审批人配置
    *
    */
    void update(AuditPersonConfigUpdateDTO dto);

    /**
    * 删除访客审批人配置
    *
    */
    void removeAuditPersonConfigById(String id);

    /**
    * 根据ID获取访客审批人配置
    *
    */
    AuditPersonConfigVO getAuditPersonConfigById(String id);

    /**
    * 分页查询访客审批人配置
    *
    */
    PageInfo<AuditPersonConfigListVO> pageList(AuditPersonConfigListQuery query);

}
