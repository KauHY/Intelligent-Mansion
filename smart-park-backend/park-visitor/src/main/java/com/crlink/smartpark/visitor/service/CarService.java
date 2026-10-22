package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.visitor.dto.CarSaveDTO;
import com.crlink.smartpark.visitor.entity.Car;
import com.crlink.smartpark.visitor.query.CarQuery;
import com.crlink.smartpark.visitor.vo.CarVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * <p>
 * 车辆信息 服务类
 * </p>
 * */
public interface CarService extends IService<Car> {

    /**
     *  根据id查询
     *
     * @param id 查询条件
     * @return   实体对象
     */
    CarVO getCarById(String id);

    /**
     * 根据carNum查询
     *
     * @param carNum
     * @return
     */
    CarVO getCarByCarNum(String carNum);

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    PageInfo<CarVO> listPage(CarQuery condition);

    /**
     * 新增或修改
     * @param dto
     */
    void saveOrUpdateCar(CarSaveDTO dto);

    void deleteCar(String id);

    /**
     * 导入Excel
     * @param file
     * @param parkOrgId
     * @param companyId
     * @param parkingLotIds
     * @return
     */
    FileImportErrorInfoDTO importExcel(MultipartFile file, String parkOrgId, String companyId, Set<String> parkingLotIds);

}
