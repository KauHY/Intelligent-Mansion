package com.crlink.smartpark.system.service;

import com.crlink.smartpark.system.entity.WxOaMobileRelation;
import com.crlink.smartpark.system.query.WxOaMobileRelationQuery;
import com.crlink.smartpark.system.model.WxOaMobileRelationReqDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 公众号用户openId与手机号关联表 服务类
 * </p>
 * */
public interface WxOaMobileRelationService extends IService<WxOaMobileRelation> {


    Boolean bind(WxOaMobileRelationReqDTO reqDTO);
    Boolean unbind(WxOaMobileRelationReqDTO reqDTO);
    Boolean changeBind(WxOaMobileRelationReqDTO reqDTO);

    Set<String> getOpenIdsByMobilesAndAppId(WxOaMobileRelationQuery query);
}
