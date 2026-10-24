package com.crlink.smartpark.system.model.query;

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
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 企业入驻审核
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CompanyAudit查询对象", description="企业入驻审核")
public class CompanyAuditQuery extends BaseQuery implements Serializable {


    @ApiModelProperty(value = "申请入驻园区ID")
    private String parkOrgId;

    @ApiModelProperty(value = "企业名称")
    private String name;

    @ApiModelProperty(value = "企业类型:0-物业公司；1-其他；2-科研院所；3-事业单位；4-机关单位；5-科技型企业")
    private Integer companyType;

    @ApiModelProperty(value = "状态 1待审核 2通过 3拒绝 4已取消")
    private Integer auditStatus;

    @ApiModelProperty(value = "企业信息ids")
    private Set<String> companyIds;

    @ApiModelProperty(value = "企业信息id")
    private String companyInformationId;




}
