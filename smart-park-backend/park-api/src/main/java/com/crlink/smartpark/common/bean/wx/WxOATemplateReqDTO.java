package com.crlink.smartpark.common.bean.wx;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class WxOATemplateReqDTO {
    /**
     * openId
     */
    private String touser;
    private String template_id;
    private Miniprogram miniprogram;
    private JSONObject data;

    private String url;
    private String topcolor;

    @Data
    public static class Miniprogram{
        private String appid;
        private String pagepath;
    }

    public static WxOATemplateReqDTO buildReqDTO(String touser, String template_id, String appId, String pagePath, JSONObject data){
        WxOATemplateReqDTO wxOATemplateReqDTO = new WxOATemplateReqDTO();
        wxOATemplateReqDTO.setTouser(touser);
        wxOATemplateReqDTO.setTemplate_id(template_id);
        wxOATemplateReqDTO.setData(data);
        if (StringUtils.isNotBlank(appId)) {
            Miniprogram miniprogram = new Miniprogram();
            miniprogram.setAppid(appId);
            miniprogram.setPagepath(pagePath);
            wxOATemplateReqDTO.setMiniprogram(miniprogram);
        }
        return wxOATemplateReqDTO;
    }

    public static WxOATemplateReqDTO buildReqTestDTO(String touser, String template_id, String topcolor, String url, JSONObject data){
        WxOATemplateReqDTO wxOATemplateReqDTO = new WxOATemplateReqDTO();
        wxOATemplateReqDTO.setTouser(touser);
        wxOATemplateReqDTO.setTemplate_id(template_id);
        wxOATemplateReqDTO.setData(data);
        wxOATemplateReqDTO.setTopcolor(topcolor);
        wxOATemplateReqDTO.setUrl(url);
        return wxOATemplateReqDTO;
    }

}
