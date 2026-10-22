package com.crlink.smartpark.system.service.parkmanagement;

import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.system.controller.request.EmployeeTreeSelectQuery;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.employee.*;
import com.crlink.smartpark.system.dto.wxuser.EmployeeInfoVO;
import com.crlink.smartpark.system.query.EmployeeQuery;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.crlink.smartpark.system.vo.TreeDictVO;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface EmployeeManageService {
    void saveOrUpdateEmployee(EmployeeReqDTO reqDTO);

    Boolean addFinger(PersonFingerReqDTO reqDTO);

    PageInfo<EmployeeDTO> listEmployeePage(EmployeeQuery employeeQuery);

    void removeEmployeeById(String id);

    void updateEmployeeStatus(UpdateStatusReqDTO statusReqDTO);

    Boolean download(EmployeeQuery employeeQuery);

    String handleEmployeeZip(MultipartFile file);

    List<DictVO> listEmployeeDict(Set<String> companyId, String name);

    /**
     * 查询物业公司员工
     *
     * @param orgId
     * @param name
     * @return
     */
    List<DictVO> listSpecialEmployeeDict(String orgId, String name, String userId);

    List<DictVO> listHandlerDict(String companyId, int orderType, String name);

    List<EmployeeTreeVO> queryEmployeeDicts(String parkOrgId, String companyId, Integer companyType, String employeeName, Integer[] jobCodes);

    /**
     * 根据手机号查询员工所有相关的园区
     *
     * @param mobile
     * @return
     */
    Map<String, String> queryEmployeeParkOrgId2NameByMobile(String mobile);




    List<SimpleNode> selectEmployeesByCondition(String orgId, String parkOrgId, String companyId, Integer companyType, String employeeName, Integer[] jobCodes);

    List<EmployeeInfoVO> listEmployeeInfosByMobile(String mobile);

    /**
     * 选人组件，园区、公司、标签树
     *
     * @param orgId
     * @param parkOrgIds
     * @param companyType
     * @return
     */
    Set<TreeDictVO> selectCompanyOrTagByConditions(String orgId, Set<String> parkOrgIds, Set<String> companyIds, Integer companyType);

    PageInfo<DictVO> selectEmployeesByIdAndType(EmployeeTreeSelectQuery query);

    /**
     * 导入员工
     *
     * @param file
     * @param parkOrgId
     * @param companyId
     * @return
     */
    FileImportErrorInfoDTO importEmployees(MultipartFile file, String parkOrgId, String companyId);

    FileImportErrorInfoDTO importEmployeesNew(ImportEmployeeDTO dto);

    String enableDevicePerson(List<Long> personIds);

    String unableDevicePerson(List<Long> personIds);

    Boolean updateEmployeeCardNo(String id, String cardNo);
}
