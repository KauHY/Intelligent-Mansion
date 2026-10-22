package com.crlink.smartpark.system.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.bean.wx.WxCode2SessionDTO;
import com.crlink.smartpark.common.bean.wx.WxOaSessionDTO;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.ProxyConnectionUtil;
import com.crlink.smartpark.system.entity.WxOaMobileRelation;
import com.crlink.smartpark.system.query.WxOaMobileRelationQuery;
import com.crlink.smartpark.system.model.WxOaMobileRelationReqDTO;
import com.crlink.smartpark.system.service.WxOaMobileRelationService;
import com.crlink.smartpark.system.mapper.WxOaMobileRelationMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;


import java.util.*;
import java.util.stream.Collectors;


/**
 * 公众号用户openId与手机号关联表 服务实现类
 * */
@Service
@Slf4j
public class WxOaMobileRelationServiceImpl extends ServiceImpl<WxOaMobileRelationMapper, WxOaMobileRelation> implements WxOaMobileRelationService {

    @Value("${param.wx.oa.appID:wx14a0e643373f29ea}")
    private String appID;
    @Value("${param.wx.oa.appSecret:779cd06dec69df02dbadd18e41410feb}")
    private String appSecret;

    @Value("${param.wx.timeOut:60000}")
    private Integer timeOut;
    @Value("${param.wx.proxyIP:}")
    private String proxyIP;
    @Value("${param.wx.proxyPort:80}")
    private Integer proxyPort;

    String OA_CODE2SESSION_URI_FORMAT = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";


    @Override
    public Boolean bind(WxOaMobileRelationReqDTO reqDTO) {
        String url = String.format(OA_CODE2SESSION_URI_FORMAT, appID, appSecret, reqDTO.getJsCode());
        WxOaSessionDTO code2SessionDTO = getOaCodeSession(url);
        WxOaMobileRelation entity = MyBeanUtils.copy(reqDTO, WxOaMobileRelation::new);
        entity.setAppId(appID);
        entity.setOpenId(code2SessionDTO.getOpenid());
        return this.saveOrUpdate(entity);
    }

    @Override
    public Boolean unbind(WxOaMobileRelationReqDTO reqDTO) {
        return remove(Wrappers.<WxOaMobileRelation>lambdaQuery()
                .eq(WxOaMobileRelation::getAppId, appID)
                .eq(WxOaMobileRelation::getMobile, reqDTO.getMobile()));
    }

    @Override
    public Boolean changeBind(WxOaMobileRelationReqDTO reqDTO) {
        WxOaMobileRelation one = this.lambdaQuery().eq(WxOaMobileRelation::getMobile, reqDTO.getMobile()).one();
        if (Objects.nonNull(one)) {
            one.setMobile(reqDTO.getMobile());
            one.setUpdateTime(LocalDateTime.now());
            return updateById(one);
        } else {
            return bind(reqDTO);
        }
    }

    @Override
    public Set<String> getOpenIdsByMobilesAndAppId(WxOaMobileRelationQuery query) {
        return lambdaQuery().select(WxOaMobileRelation::getOpenId).eq(WxOaMobileRelation::getAppId, query.getAppId())
                .in(WxOaMobileRelation::getMobile, query.getMobiles()).list().stream().map(WxOaMobileRelation::getOpenId).collect(Collectors.toSet());
    }

    /**
     * 获取openid
     *
     * @return
     */
    private WxOaSessionDTO getOaCodeSession(String url) {
        log.info("请求微信平台url：{}", url);
        String result = ProxyConnectionUtil.get(url, timeOut, proxyIP, proxyPort);
        log.info("请求微信平台结果：{}", result);
        AssertUtil.requireTrue(StringUtils.hasText(result), "10011");
        WxOaSessionDTO dto = JSONUtil.toBean(result, WxOaSessionDTO.class);
        AssertUtil.requireTrue(StringUtils.hasText(dto.getOpenid()), "10011", JSONUtil.parseObj(result).getStr("errmsg"));
        return dto;
    }
}
