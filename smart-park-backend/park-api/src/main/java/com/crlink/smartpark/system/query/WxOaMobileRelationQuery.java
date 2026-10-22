package com.crlink.smartpark.system.query;

import com.crlink.smartpark.BaseQuery;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * <p>
 * 公众号用户openId与手机号关联表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WxOaMobileRelation查询对象", description="公众号用户openId与手机号关联表")
public class WxOaMobileRelationQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "公众号用户openId")
    private String openId;

    @ApiModelProperty(value = "手机号")
    private String mobile;
    private Set<String> mobiles;

    @ApiModelProperty(value = "公众号appId")
    @NotBlank(message = "公众号appId不能为空")
    private String appId;


    public static WxOaMobileRelationQuery buildQuery(String appId, Set<String> mobiles){
        WxOaMobileRelationQuery query = new WxOaMobileRelationQuery();
        query.setAppId(appId);
        query.setMobiles(mobiles);
        return query;
    }
}
