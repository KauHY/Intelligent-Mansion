package com.crlink.smartpark.visitor.model.dto;

import com.crlink.smartpark.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** * @desc
 */
@Data
public class VisitorRecordQuery extends BaseQuery {

    /**
     * 公司ID
     */
    @ApiModelProperty("公司ID")
    private String companyId;
}
