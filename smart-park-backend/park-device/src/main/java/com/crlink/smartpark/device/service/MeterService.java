package com.crlink.smartpark.device.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.device.dto.MeterDTO;
import com.crlink.smartpark.device.entity.DeviceInfo;
import com.crlink.smartpark.device.model.MeterExchangeReqDTO;
import com.crlink.smartpark.device.model.MeterReqDTO;
import com.crlink.smartpark.device.model.MeterStatusUpdateReqDTO;
import com.crlink.smartpark.device.model.query.MeterQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 仪表（电表和水表） 服务类
 * </p>
 *
  */
public interface MeterService {


    <T> Map<String, T> id2ValueMap(Collection<String> ids, Function<MeterDTO, T> sValue);


    Map<String, MeterDTO> queryMeterByDeviceIds(Collection<String> deviceIds);


    Collection<String> getMeterDeviceIdByCondition(MeterQuery query);

    /**
     * 保存或更新数据
     *
     * @return
     */
    boolean saveOrUpdateMeter(MeterReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    MeterDTO getMeterById(String id);

    MeterDTO getMeterByDeviceId(String parkOrgId, String deviceId);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeterDTO> listMeterPage(MeterQuery query);

    void download(MeterQuery query);

    FileImportErrorInfoDTO importMeters(MultipartFile file, Integer category, String parkOrgId);

    /**
     * 换表
     *
     * @param reqDTO
     * @return
     */
    Boolean exchangeMeter(MeterExchangeReqDTO reqDTO);

    /**
     * 更新状态
     *
     * @param reqDTO
     * @return
     */
    ResponseResult updateMeterStatus(MeterStatusUpdateReqDTO reqDTO);

    Boolean removeMeterById(String id);


    default List<MeterDTO> entitiesToDTOs(List<DeviceInfo> entities) {
        List<MeterDTO> result = entities.parallelStream().map(t -> {
            Map<String, Object> extension = t.getExtension();
            MeterDTO meterDTO = BeanUtil.toBean(extension, MeterDTO.class);
            BeanUtil.copyProperties(t, meterDTO, CopyOptions.create().setIgnoreNullValue(true));
            return meterDTO;
        }).collect(Collectors.toList());
        return result;
    }

}
