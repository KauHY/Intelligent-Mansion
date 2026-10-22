package com.crlink.smartpark.business.dto.chance;

import com.crlink.smartpark.common.constants.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@Data
public class BusinessChanceReqDTO implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 商机名称
     */

    @ApiModelProperty("商机名称")
    @Length(min = 1, max = 30, message = "商机名称不能超过30字")
    private String chanceName;

    @ApiModelProperty("商机来源：1商机录入，2入驻申请")
    private Integer businessSource;

    @ApiModelProperty("园区id")
    @NotNull
    private String parkOrgId;

    @ApiModelProperty("联系人")
    @NotNull
    @Length(max = 30, message = "联系人不能超过30字")
    private String contactName;

    @ApiModelProperty("联系人电话")
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "请输入正确的手机号码！")
    private String contactPhone;

    @ApiModelProperty("联系人邮箱")
    private String contactMail;

    @ApiModelProperty("需求数量")
    private Integer quantityNumber;

    @ApiModelProperty("期望价格（元/㎡/月）")
//    private Integer expectedPrice;
    private Double expectedPrice;

    @ApiModelProperty("商机阶段：1机会评估，2需求分析，3报价，4谈判，5达成意向，6签订合同，7赢单关闭")
    @NotNull
    private Integer chanceStage;

    @ApiModelProperty("线索来源：1老客户介绍，2政府资源，3公共资源，4第三方资源，5内部资源，6网络资源，7招商人员资源")
    private Integer threadSource;

    @ApiModelProperty("跟进人Id")
    private String followEmployeeId;

    @ApiModelProperty("备注")
    @Length(max = 999, message = "备注最多999字符")
    private String remark;

    @ApiModelProperty("意向房源:房屋Id集合")
    private List<String> houseIds;
}
