package com.crlink.smartpark.common.entity;

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

/**
 * <p>
 * 编号生成器
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("com_number_seq")
@ApiModel(value="NumberSeq数据库对象", description="编号生成器")
public class NumberSeq implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "number_name", type = IdType.ASSIGN_ID)
    private String numberName;

    private LocalDateTime dateTime;

    private Integer value;


}
