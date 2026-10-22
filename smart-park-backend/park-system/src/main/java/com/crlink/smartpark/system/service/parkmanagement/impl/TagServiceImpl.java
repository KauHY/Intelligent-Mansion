package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.common.CacheUtil;
import com.crlink.smartpark.system.dto.tag.TagDTO;
import com.crlink.smartpark.system.dto.tag.TagReqDTO;
import com.crlink.smartpark.system.entity.Company;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.entity.Tag;
import com.crlink.smartpark.system.mapper.TagMapper;
import com.crlink.smartpark.system.query.TagQuery;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.parkmanagement.CompanyService;
import com.crlink.smartpark.system.service.parkmanagement.TagService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.feign.AccessPolicyProvider;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 * */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private AccessPolicyProvider accessPolicyProvider;

    @Override
    public Map<String, String> id2Name(Set<String> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return new HashMap<>(2);
        }
        Map<String, String> id2NameMap = this.lambdaQuery()
                .select(Tag::getId, Tag::getTagName)
                .in(Tag::getId, ids)
                .list().parallelStream().collect(Collectors.toMap(Tag::getId, Tag::getTagName));
        return id2NameMap;
    }

    @Override
    public Map<String, String> name2Id(Collection<String> names, String companyId) {
        Map<String, String> name2IdMap = this.lambdaQuery()
                .select(Tag::getId, Tag::getTagName)
                .in(Tag::getTagName, names)
                .eq(Tag::getCompanyId, companyId)
                .list().parallelStream().collect(Collectors.toMap(Tag::getTagName, Tag::getId));
        return name2IdMap;
    }

    @Override
    public Boolean saveOrUpdateTag(TagReqDTO tagDTO) {
        Tag tag = new Tag();
        checkTagName(tagDTO);
        BeanUtils.copyProperties(tagDTO, tag);
        EntityUserUtil.setAllDynamic(tag);
        return this.saveOrUpdate(tag);
    }


    @Override
    public PageInfo<TagDTO> listTagPage(TagQuery tagQuery) {
        Set<String> parkOrgIds = CacheUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        tagQuery.startPage();
        List<Tag> tags = this.lambdaQuery()
                .like(StringUtils.hasText(tagQuery.getTagName()), Tag::getTagName, tagQuery.getTagName())
                .eq(StringUtils.hasText(tagQuery.getParkOrgId()), Tag::getParkOrgId, tagQuery.getParkOrgId())
                .eq(StringUtils.hasText(tagQuery.getCompanyId()), Tag::getCompanyId, tagQuery.getCompanyId())
                .in(Tag::getParkOrgId, parkOrgIds)
                .orderByDesc(Tag::getCreateTime)
                .list();
        MyPageInfoUtils.isEmptyPage(tags);
        return tagQuery.createPageInfo(tags2TagDTO(tags));
    }

    @Override
    public List<DictVO> listTagDict(TagQuery tagQuery) {
        List<Tag> tags = this.lambdaQuery()
                .select(Tag::getId, Tag::getTagName, Tag::getCompanyId)
                .eq(StringUtils.hasText(tagQuery.getParkOrgId()), Tag::getParkOrgId, tagQuery.getParkOrgId())
                .in(CollUtil.isNotEmpty(tagQuery.getCompanyIds()), Tag::getCompanyId, tagQuery.getCompanyIds()).orderByDesc(Tag::getCompanyId, Tag::getCreateTime).list();
        List<DictVO> result = tags.parallelStream().map(t -> {
                    String tagName = buildTagName(tagQuery, t);
                    DictVO dictVO = new DictVO(tagName, t.getId());
                    return dictVO;
                }
        ).collect(Collectors.toList());
        return result;
    }

    @Override
    public Boolean removeTagById(String id) {
        Boolean tagIdsExist = accessPolicyProvider.getTagIdsExist(id);
        if (tagIdsExist) {
            throw new ResolinkRuntimeException("30007");
        }
        return this.removeById(id);
    }

    private String buildTagName(TagQuery tagQuery, Tag tag) {
        String tagName = tag.getTagName();
        if (StrUtil.isBlank(tagQuery.getCompanyId()) && CollUtil.isEmpty(tagQuery.getCompanyIds())) {
            return tagName;
        }
        Set<String> companyIds = new HashSet<>();
        if (StrUtil.isNotBlank(tagQuery.getCompanyId())) {
            companyIds.add(tagQuery.getCompanyId());
        }
        if (CollUtil.isNotEmpty(tagQuery.getCompanyIds())) {
            companyIds.addAll(tagQuery.getCompanyIds());
        }
        List<Company> companies = companyService.lambdaQuery().select(Company::getId, Company::getName).in(Company::getId, companyIds).list();
        Map<String, String> companyNameMap = companies.stream().collect(Collectors.toMap(Company::getId, Company::getName));
        if (companyNameMap.containsKey(tag.getCompanyId())) {
            tagName = companyNameMap.get(tag.getCompanyId()) + "-" + tagName;
        }
        return tagName;
    }


    private List<TagDTO> tags2TagDTO(List<Tag> tags) {
        Set<String> companyIds = tags.parallelStream().map(Tag::getCompanyId).collect(Collectors.toSet());
        Set<String> parkOrgIds = tags.parallelStream().map(Tag::getParkOrgId).collect(Collectors.toSet());
        Map<String, String> orgId2OrgNameMap = (Map<String, String>) organizationService.id2ValueMap(parkOrgIds, Organization::getOrgName);
        Map<String, ?> companyId2NameMap = companyService.id2ValueMap(companyIds, Company::getName);
        List<TagDTO> result = tags.parallelStream().map(t -> {
            TagDTO tagDTO = new TagDTO();
            BeanUtils.copyProperties(t, tagDTO);
            tagDTO.setCompanyName((String) companyId2NameMap.get(t.getCompanyId()));
            tagDTO.setParkOrgName(orgId2OrgNameMap.get(t.getParkOrgId()));
            return tagDTO;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 检验标签名是否重复
     *
     * @param tagDTO
     */
    private void checkTagName(TagReqDTO tagDTO) {
        String tagName = tagDTO.getTagName();
        String id = tagDTO.getId();
        String headOrgId = SystemUtil.getInstance().getCurrentHeadOrgId();
        Integer count = this.lambdaQuery().eq(Tag::getTagName, tagName).ne(StringUtils.hasText(id), Tag::getId, id)
                .in(Tag::getParkOrgId, SystemUtil.getInstance().getParkOrgIds(headOrgId)).count();
        if (count > 0) {
            throw new ResolinkRuntimeException("30005");
        }
    }
}
