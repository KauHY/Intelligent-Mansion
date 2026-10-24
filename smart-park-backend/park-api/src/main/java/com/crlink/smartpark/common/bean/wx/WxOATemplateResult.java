package com.crlink.smartpark.common.bean.wx;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

@Data
public class WxOATemplateResult {

    @JSONField(name = "template_list")
    List<Template> templateList;

    @Data
    public static class Template{
        @JSONField(name = "template_id")
        private String templateId;
        private String title;
        @JSONField(name = "primary_industry")
        private String primaryIndustry;
        @JSONField(name = "deputy_industry")
        private String deputyIndustry;
        private String content;
        private String example;
    }
}
