package com.crlink.smartpark.enterprise.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 公司分组关联表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GroupCompanyRelation请求对象", description="公司分组关联表")
public class GroupCompanyRelationReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "群组Id，对应多级分类表id", required = true)
    @NotBlank
    private String groupId;

    @ApiModelProperty(value = "公司id", required = true)
    @NotEmpty
    private Set<String> companyId;

    @ApiModelProperty(value = "备注")
    @Length(max = 100)
    private String remark;


}
