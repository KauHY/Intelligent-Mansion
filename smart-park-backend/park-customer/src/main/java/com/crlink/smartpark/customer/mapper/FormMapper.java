package com.crlink.smartpark.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.customer.dto.FormEditorsDTO;
import com.crlink.smartpark.customer.entity.Form;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 信息统计表单 Mapper 接口
 * </p>
 * */
public interface FormMapper extends BaseMapper<Form> {
    
    //我提交的
    List<FormEditorsDTO> querySubmittedForms(String employeeId);

    //我接收的
    List<FormEditorsDTO> queryReceivedForms(@Param("employeeId") String employeeId, @Param("title") String title);
}
