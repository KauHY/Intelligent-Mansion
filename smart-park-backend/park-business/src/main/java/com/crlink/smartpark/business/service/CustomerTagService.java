package com.crlink.smartpark.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.business.dto.CustomerTagDTO;
import com.crlink.smartpark.business.entity.CustomerTag;
import com.crlink.smartpark.business.query.CustomerTagQuery;
import com.crlink.smartpark.business.vo.CustomerTagVO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 客户标签 服务类
 * </p>
 * */
public interface CustomerTagService extends IService<CustomerTag> {


    Boolean saveOrUpdateCustomerTag(CustomerTagDTO customerTagDTO);

    PageInfo<CustomerTagVO> listCustomerTagPage(CustomerTagQuery query);

    Boolean removeCustomerTag(String id);

    Map<String, String> getCustomerTagNames(Collection<String> customerTagIds);

    List<DictVO> listCustomerTagDict(String parkOrgId);
}
