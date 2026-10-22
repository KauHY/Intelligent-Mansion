package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.visitor.dto.ApartmentPersonDTO;
import com.crlink.smartpark.visitor.entity.ApartmentPerson;
import com.crlink.smartpark.visitor.model.dto.ApartmentPersonReqDTO;
import com.crlink.smartpark.visitor.model.dto.UpdateApartmentPersonReqDTO;
import com.crlink.smartpark.visitor.model.query.ApartmentPersonQuery;
import com.crlink.smartpark.visitor.model.vo.ApartmentPersonVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 公寓人员管理 服务类
 * </p>
 * */
public interface ApartmentPersonService extends IService<ApartmentPerson> {

    /**
     * 根据Id查询值
     *
     * @param ids
     * @param sValue
     * @param <T>
     * @return
     */
    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<ApartmentPerson, T> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateApartmentPerson(ApartmentPersonReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    ApartmentPersonDTO getApartmentPersonById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<ApartmentPersonVO> listApartmentPersonPage(ApartmentPersonQuery query);

    Boolean updateStatus(UpdateApartmentPersonReqDTO statusReqDTO);

    void exportPerson(ApartmentPersonQuery query);

    FileImportErrorInfoDTO importPerson(MultipartFile file, String parkOrgId, String companyId);

    ApartmentPersonDTO findByEmployeeId(String employeeId);

    Boolean removePersonById(String id);


    String existNormalPersonByEmployeeId(String employeeId);

    Set<String> findHouseIdsByEmployeeId(String employeeId);

    boolean existsDevice(String deviceId);

}
