package com.crlink.smartpark.visitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.visitor.entity.CarAccessRecord;
import com.crlink.smartpark.visitor.entity.CarAccessRecordRel;
import com.crlink.smartpark.visitor.model.vo.CarAccessGroupVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆通行记录
 * * @date 2022-10-12
 */
public interface CarAccessRecordMapper extends BaseMapper<CarAccessRecord> {

    /**
     * 根据gatewayType对车行进出统计分组
     * @param parkingLotId
     * @return
     */
    List<CarAccessGroupVO> selectAccessGatewayGroup(String parkingLotId);

    /**
     * 获取最近一次的入口记录
     *
     * @param carNum
     * @param parkingLotId
     * @return
     */
    CarAccessRecordRel getLatestEnterRecord(@Param("carNum") String carNum, @Param("parkingLotId") String parkingLotId);

}
