package com.crlink.smartpark.system.service.parkmanagement;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.dto.tag.TagDTO;
import com.crlink.smartpark.system.dto.tag.TagReqDTO;
import com.crlink.smartpark.system.entity.Tag;
import com.crlink.smartpark.system.query.TagQuery;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 标签表 服务类
 * </p>
 * */
public interface TagService extends IService<Tag> {
    Map<String, String> id2Name(Set<String> ids);

    Map<String, String> name2Id(Collection<String> names, String companyId);

    Boolean saveOrUpdateTag(TagReqDTO tagDTO);

    PageInfo<TagDTO> listTagPage(TagQuery tagQuery);

    List<DictVO> listTagDict(TagQuery tagQuery);

    Boolean removeTagById(String id);
}
