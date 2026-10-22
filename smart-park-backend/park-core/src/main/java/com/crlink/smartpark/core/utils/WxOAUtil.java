package com.crlink.smartpark.core.utils;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.crlink.smartpark.common.bean.wx.WxOAAccessTokenResult;
import com.crlink.smartpark.common.bean.wx.WxOATemplateReqDTO;
import com.crlink.smartpark.common.bean.wx.WxOATemplateResult;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WxOAUtil {

    public static RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        WxOAUtil.redisUtil = redisUtil;
    }

    @Value("${param.wx.timeOut:60000}")
    private Integer timeOut; //请求超时时间：单位毫秒
    @Value("${param.wx.proxyIP:}")
    private String proxyIP;
    @Value("${param.wx.proxyPort:80}")
    private Integer proxyPort;
    @Value("${param.wx.oa.appID:wx14a0e643373f29ea}")
    private String appID;
    @Value("${param.wx.oa.appSecret:779cd06dec69df02dbadd18e41410feb}")
    private String appSecret;

    public static final String WX_OA_TOKEN_URL_FORMAT = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    public static final String WX_OA_TEMPLATE_URL_FORMAT = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=%s";
    public static final String WX_OA_SEND_MSG_TEMPLATE_URL_FORMAT = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    public static final Long WX_OA_ACCESS_TOKEN_EXPIRE_SECOND = 7100L;
    public static final Long WX_OA_ACCESS_TOKEN_EXPIRE_SECOND_BACK = 7200L;
    public static final String BACKUP = "BackUp";
    public static final String PUNCH_REMINDER_CH = "打卡提醒";

    public void init() {
        setAccessToken();
        getPunchReminderTemplateId();
    }

    public String getAccessToken() {
        Object token = redisUtil.getObject(Constants.Redis.WX_OA_ACCESS_TOKEN_PREFIX.getCode());
        if (Objects.nonNull(token)) {
            return token.toString();
        }
        return (String) redisUtil.getObject(Constants.Redis.WX_OA_ACCESS_TOKEN_PREFIX.getCode() + BACKUP);
    }

    public void setAccessToken() {
        WxOAAccessTokenResult dto = (WxOAAccessTokenResult) getByProxy(String.format(WX_OA_TOKEN_URL_FORMAT, appID, appSecret), WxOAAccessTokenResult.class);
        redisUtil.setObject(Constants.Redis.WX_OA_ACCESS_TOKEN_PREFIX.getCode(), dto.getAccessToken(), WX_OA_ACCESS_TOKEN_EXPIRE_SECOND, TimeUnit.SECONDS);
        redisUtil.setObject(Constants.Redis.WX_OA_ACCESS_TOKEN_PREFIX.getCode() + BACKUP, dto.getAccessToken(), WX_OA_ACCESS_TOKEN_EXPIRE_SECOND_BACK, TimeUnit.SECONDS);
    }
    //WX_OA_PUNCH_REMINDER_TEMPLATE_PREFIX

    public String getPunchReminderTemplateId() {
        Object punchReminderTemplateId = redisUtil.getObject(Constants.Redis.WX_OA_PUNCH_REMINDER_TEMPLATE_PREFIX.getCode());
        if (Objects.nonNull(punchReminderTemplateId)) {
            return punchReminderTemplateId.toString();
        }
        return setPunchReminderTemplate();
    }

    public String setPunchReminderTemplate() {
//        WxOATemplateResult withAccessToken = (WxOATemplateResult) getWithAccessToken(WX_OA_TEMPLATE_URL_FORMAT, WxOATemplateResult.class);
//        List<WxOATemplateResult.Template> templateList = withAccessToken.getTemplateList();
//        WxOATemplateResult.Template first = templateList.stream().filter(e -> PUNCH_REMINDER_CH.equals(e.getTitle())).findFirst().orElseThrow(() -> new ResolinkRuntimeException("10026"));
//        redisUtil.setObject(Constants.Redis.WX_OA_PUNCH_REMINDER_TEMPLATE_PREFIX.getCode(), first.getTemplateId());
//        return first.getTemplateId();
        return "";
    }


    private Object getWithAccessToken(String url, Class clazz) {
        return getByProxy(String.format(url, getAccessToken()), clazz);
    }

    private Object getByProxy(String url, Class clazz) {
        return JSONUtil.toBean(getByProxy(url), clazz);
    }

    private String getByProxy(String url) {
        log.info("请求微信公众号平台url：{}", url);
        String result = ProxyConnectionUtil.get(url, timeOut, proxyIP, proxyPort);
        log.info("请求微信公众号平台结果：{}", result);
        AssertUtil.requireTrue(StringUtils.hasText(result), "10011");
        return result;
    }

    private String postByProxy(String url, String paramStr) {
        String result = ProxyConnectionUtil.post(url, timeOut, paramStr, proxyIP, proxyPort);
        System.out.println(result);
        return result;
    }


    public void sendMsgByTemplate(List<WxOATemplateReqDTO> list) {
        String url = String.format(WX_OA_SEND_MSG_TEMPLATE_URL_FORMAT, getAccessToken());
        for (WxOATemplateReqDTO wxOATemplateReqDTO : list) {
            postByProxy(url, JSONUtil.parseObj(wxOATemplateReqDTO).toString());
        }
    }

    public void sendMsgByTemplate(Set<String> openIds, String punchTemplateId) {
        JSONObject data = new JSONObject();
        JSONObject json = new JSONObject();
        json.put("value", "别忘记打卡哦~");
        data.put("color", "#173177");
        data.put("first", json);
        //       List<WxOATemplateReqDTO> reqDTOs = openIds.stream().map(openId -> WxOATemplateReqDTO.buildReqDTO(openId, punchTemplateId, "wx5766e6a4e09a4958", "pages/attendance/park-attendance", data)).collect(Collectors.toList());
        List<WxOATemplateReqDTO> reqDTOs = openIds.stream().map(openId -> WxOATemplateReqDTO.buildReqTestDTO(openId, punchTemplateId, "#FF0000", "http://weixin.qq.com/download", data)).collect(Collectors.toList());
        sendMsgByTemplate(reqDTOs);
    }

}
