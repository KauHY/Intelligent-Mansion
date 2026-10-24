package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.customer.dto.InfoDTO;
import com.crlink.smartpark.customer.dto.InformationDTO;
import com.crlink.smartpark.customer.entity.Information;
import com.crlink.smartpark.customer.model.query.InformationQuery;
import com.crlink.smartpark.customer.vo.WxInformationListVO;
import com.github.pagehelper.PageInfo;

import java.util.LinkedList;

/**
 * <p>
 * 资讯信息表 服务类
 * </p>
 * */
public interface InformationService extends IService<Information> {


    /**
     * 保存或更新数据
     *
     * @param dto 存储对象
     * @return 实体对象
     */

    Boolean saveOrUpdateInformation(InfoDTO dto);


    PageInfo<InformationDTO> listInformationPage(InformationQuery condition);


    PageInfo<WxInformationListVO> listWxInformationPage(BaseQuery<String> query, String source);

    boolean removeInfoById(String id);

    InformationDTO getInformationDetailById(String id);

    WxInformationListVO getWxInformationDetailById(String id);

    LinkedList<Integer> getPutIntoTypeList(Integer putIntoType);
}
