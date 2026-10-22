package com.crlink.smartpark.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.PinyinComparator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.business.dto.CustomerTagDTO;
import com.crlink.smartpark.business.entity.CustomerInfo;
import com.crlink.smartpark.business.entity.CustomerTag;
import com.crlink.smartpark.business.mapper.CustomerTagMapper;
import com.crlink.smartpark.business.query.CustomerTagQuery;
import com.crlink.smartpark.business.service.CustomerInfoService;
import com.crlink.smartpark.business.service.CustomerTagService;
import com.crlink.smartpark.business.vo.CustomerTagVO;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户标签 服务实现类
 * </p>
 * */
@Service
public class CustomerTagServiceImpl extends ServiceImpl<CustomerTagMapper, CustomerTag> implements CustomerTagService {

    @Autowired
    CustomerInfoService customerInfoService;

    /**
     * 保存或更新数据
     *
     * @param dto 存储对象
     * @return 存储对象
     */
    @Override
    public Boolean saveOrUpdateCustomerTag(CustomerTagDTO dto) {
        checkTagName(dto.getId(), dto.getName());
        CustomerTag customerTag = MyBeanUtils.copy(dto, CustomerTag::new);
        if (StringUtils.isNotBlank(dto.getId())) {
            EntityUserUtil.setUpdateBy(customerTag);
        } else {
            EntityUserUtil.setAll(customerTag);
        }
        return this.saveOrUpdate(customerTag);
    }


    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<CustomerTagVO> listCustomerTagPage(CustomerTagQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getParkOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        query.startPage();
        List<CustomerTag> list = this.lambdaQuery()
                .select(CustomerTag::getId, CustomerTag::getName, CustomerTag::getParkOrgId, CustomerTag::getUpdateTime)
                .like(StringUtils.isNotBlank(query.getName()), CustomerTag::getName, query.getName())
                .in(CustomerTag::getParkOrgId, parkOrgIds)
                .orderByDesc(CustomerTag::getCreateTime, CustomerTag::getUpdateTime).list();
        MyPageInfoUtils.isEmptyPage(list);
        List<CustomerTagVO> result = MyBeanUtils.copyList(list, CustomerTagVO::new);
        return query.createPageInfo(result);
    }

    /**
     * 删除客户标签
     *
     * @param id
     * @return
     */
    @Override
    public Boolean removeCustomerTag(String id) {
        CustomerInfo customerInfo = customerInfoService.getOne(
                Wrappers.<CustomerInfo>lambdaQuery().select(CustomerInfo::getId)
                        .eq(CustomerInfo::getCustomerTagId, id), false);
        if (Objects.nonNull(customerInfo)) {
            throw new ResolinkRuntimeException("90007");
        }
        return this.removeById(id);
    }


    /**
     * 获取客户标签名称
     *
     * @param customerTagIds
     * @return
     */
    @Override
    public Map<String, String> getCustomerTagNames(Collection<String> customerTagIds) {
        if (CollectionUtil.isEmpty(customerTagIds)) {
            return Collections.emptyMap();
        }
        Map<String, String> result = this.lambdaQuery().select(CustomerTag::getId, CustomerTag::getName).in(CustomerTag::getId, customerTagIds)
                .list().parallelStream().collect(Collectors.toMap(CustomerTag::getId, CustomerTag::getName));
        return result;
    }


    /**
     * 客户编辑、新增时：标签下拉框
     * 结果按标签名称升序排列
     *
     * @param parkOrgId
     * @return
     */
    @Override
    public List<DictVO> listCustomerTagDict(String parkOrgId) {
        List<CustomerTag> list = this.lambdaQuery().select(CustomerTag::getId, CustomerTag::getName)
                .eq(CustomerTag::getParkOrgId, parkOrgId).list();
        List<DictVO> tagNames = list.stream()
                .map(tag -> new SimpleNode(tag.getId(), tag.getName()))
                .sorted((n1, n2) -> new PinyinComparator().compare(n1.getLabel(), n2.getLabel()))
                .collect(Collectors.toList());
        return tagNames;
    }

    /**
     * 同一租户下的标签名称不允许重复，不同租户的可以重复
     *
     * @param tagId
     * @param name
     */
    private void checkTagName(String tagId, String name) {
        Set<String> parkOrgIds = SystemUtil.getInstance()
                .getParkOrgIds(SystemUtil.getInstance().getCurrentHeadOrgId());
        Integer count = this.lambdaQuery()
                .ne(StringUtils.isNotBlank(tagId), CustomerTag::getId, tagId)
                .eq(CustomerTag::getName, name)
                .in(CustomerTag::getParkOrgId, parkOrgIds)
                .last("limit 1").count();
        if (count > 0) {
            throw new ResolinkRuntimeException("90008");
        }
    }
}
