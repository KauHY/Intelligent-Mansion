package com.crlink.smartpark.customer.model.vo;

import com.crlink.smartpark.system.vo.DictVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 领用权限设置
 * </p>
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceivePermissionVo implements Serializable {

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "所属园区名称")
    private String parkOrgName;

    @ApiModelProperty(value = "公司id,逗号分隔")
    private String companyId;

    @ApiModelProperty(value = "公司名称,逗号分隔")
    private String companyName;

    @ApiModelProperty(value = "员工id,逗号分隔")
    private String employeeId;

    @ApiModelProperty(value = "员工名称,逗号分隔")
    private String employeeName;

    @ApiModelProperty(value = "领用授权员工对象")
    private List<DictVO> employeeIdVo;


}
