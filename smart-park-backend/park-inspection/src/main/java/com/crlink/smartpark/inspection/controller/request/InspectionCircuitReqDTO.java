package com.crlink.smartpark.inspection.controller.request;

import com.crlink.smartpark.core.validation.CustomValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InspectionCircuitReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区Id")
    @NotEmpty
    private String parkOrgId;

    @ApiModelProperty(value = "巡检路线名")
    @Length(max = 30, min = 1)
    private String circuitName;

    @ApiModelProperty(value = "巡检类型")
    @CustomValid({"1", "2"})
    private Integer type;

    @ApiModelProperty(value = "备注")
    @Length(max = 99)
    private String remark;

    @ApiModelProperty(value = "是否有序")
    @NotNull
    private Boolean orderly;

    //@JsonDeserialize(converter = StringToLocalDatetimeConverter.class)
    ////@JsonDeserialize(converter = DateTimeConfig.)
    //private LocalTime test;
    //
    //
    //public static class StringToLocalDatetimeConverter extends StdConverter<String, LocalTime> {
    //    @Override
    //    public LocalTime convert(String value) {
    //        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    //        return LocalTime.parse(value, timeFormatter);
    //    }
    //}


}

