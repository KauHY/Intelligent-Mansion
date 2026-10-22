package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.customer.dto.FormDTO;
import com.crlink.smartpark.customer.dto.FormEditorsDTO;
import com.crlink.smartpark.customer.entity.Form;
import com.crlink.smartpark.customer.model.FormReqDTO;
import com.crlink.smartpark.customer.model.dto.FormUpdateStatusReqDTO;
import com.crlink.smartpark.customer.model.query.FormQuery;
import com.github.pagehelper.PageInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_DATE_TIME_FORMAT;
import static com.crlink.smartpark.common.constants.Constants.DEFAULT_TIME_FORMAT;
import static com.crlink.smartpark.customer.common.FormConstant.FormCycleEnum.*;

/**
 * <p>
 * 信息统计表单 服务类
 * </p>
 * */
public interface FormService extends IService<Form> {

    /**
     * 根据Id查询值
     */
    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<Form, T> sValue);

    List<String> getEditorIdsById(String id);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateForm(FormReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    FormDTO getFormById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<FormDTO> listFormPage(FormQuery query);

    Boolean updateStatus(FormUpdateStatusReqDTO reqDTO);

    PageInfo<FormEditorsDTO> wxFormList(Integer status, BaseQuery<String> query);


    default LocalDateTime getDeadlineTime(Form form) {
        Integer cycle = form.getCycle();
        String deadlineTime = form.getDeadlineTime();
        Integer deadlineNum = form.getDeadlineNum();
        LocalDateTime now = LocalDateTime.now();
        if (cycle == EVERY_DAY.type) {
            LocalTime deadline = LocalTime.parse(deadlineTime, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
            return LocalDateTime.of(LocalDate.now(), deadline);
        } else if (cycle == EVERY_WEEK.type) {
            LocalTime deadline = LocalTime.parse(deadlineTime, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
            int difference = deadlineNum - now.getDayOfWeek().getValue();
//            return now.plusDays(difference);
            return LocalDateTime.of(LocalDate.now().plusDays(difference), deadline);
        } else if (cycle == EVERY_MONTH.type) {
            LocalTime deadline = LocalTime.parse(deadlineTime, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
            LocalDate localDate = LocalDate.of(now.getYear(), now.getMonth(), deadlineNum);
            LocalDateTime dateTime = LocalDateTime.of(localDate, deadline);
            return dateTime;
        } else {
            LocalDateTime dateTime = LocalDateTime.parse(deadlineTime, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
            return dateTime;
        }
    }
}
