package com.crlink.smartpark.enterprise.service;

import com.crlink.smartpark.enterprise.model.query.GroupCompanyRelationQuery;
import com.crlink.smartpark.industrial.dto.GroupCompanyRelationDTO;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 公司分组关联表 服务类
 * </p>
 * */
public interface GroupCompanyRelationService{



    PageInfo<GroupCompanyRelationDTO> listGroupCompanyPage(GroupCompanyRelationQuery query);
}
