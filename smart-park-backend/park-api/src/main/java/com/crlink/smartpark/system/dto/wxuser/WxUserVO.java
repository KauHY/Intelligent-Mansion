package com.crlink.smartpark.system.dto.wxuser;

import com.crlink.smartpark.system.vo.DictVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WxUserVO {
    private String token;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    @ApiModelProperty(value = "名字")
    private String name;

    private String mobile;

    //@ApiModelProperty(value = "企业列表")
    //private Set<String> companyNames;
    @ApiModelProperty(value = "员工企业和员工信息")
    private List<DictVO> employeeCompanies;

    public WxUserVO(String token) {
        this.token = token;
    }
}
