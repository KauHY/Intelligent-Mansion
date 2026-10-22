package com.crlink.smartpark.enterprise.model.query;

import com.crlink.smartpark.BaseQuery;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 投标记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TenderRecord查询对象", description="投标记录")
public class TenderRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "招标id")
    @NotBlank(message = "招标id不能为空")
    private String purchaseId;

}
