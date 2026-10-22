package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.PinyinComparator;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.enums.DevicePersonTypeEnum;
import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.ExcelExportUtils;
import com.crlink.smartpark.core.utils.ExcelImportRollbackListener;
import com.crlink.smartpark.core.utils.ExcelImportUtils;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyFileUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.MyStrUtil;
import com.crlink.smartpark.core.utils.RedisUtil;
import com.crlink.smartpark.core.utils.SortUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.device.dto.PersonDeviceDTO;
import com.crlink.smartpark.device.feign.CardProvider;
import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import com.crlink.smartpark.system.common.CacheUtil;
import com.crlink.smartpark.system.controller.request.EmployeeTreeSelectQuery;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeDictDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeReqDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeTreeVO;
import com.crlink.smartpark.system.dto.employee.ImportEmployeeDTO;
import com.crlink.smartpark.system.dto.employee.PersonFingerReqDTO;
import com.crlink.smartpark.system.dto.wxuser.EmployeeInfoVO;
import com.crlink.smartpark.system.entity.Company;
import com.crlink.smartpark.system.entity.Employee;
import com.crlink.smartpark.system.entity.EmployeeFinger;
import com.crlink.smartpark.system.entity.Job;
import com.crlink.smartpark.system.entity.JobEmployeeRelation;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.entity.Tag;
import com.crlink.smartpark.system.model.EmployeeExcelExportDTO;
import com.crlink.smartpark.system.model.EmployeeExcelImportDTO;
import com.crlink.smartpark.system.query.EmployeeQuery;
import com.crlink.smartpark.system.service.common.OrganizationManageService;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.parkmanagement.AsyncService;
import com.crlink.smartpark.system.service.parkmanagement.CompanyService;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeDeviceService;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeFingerService;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeManageService;
import com.crlink.smartpark.system.service.parkmanagement.EmployeeService;
import com.crlink.smartpark.system.service.parkmanagement.JobEmployeeRelationService;
import com.crlink.smartpark.system.service.parkmanagement.JobService;
import com.crlink.smartpark.system.service.parkmanagement.TagService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.crlink.smartpark.system.vo.TreeDictVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.CacheConstant.CACHE_GROUP_EMPLOYEE;
import static com.crlink.smartpark.common.constants.CacheConstant.CacheKeyGenerator;
import static com.crlink.smartpark.common.constants.Constants.EMPLOYEE_FACE_PIC_DIR;
import static com.crlink.smartpark.common.constants.Constants.Redis.IMPORT_EMPLOYEE_PREFIX;
import static com.crlink.smartpark.common.constants.Constants.SPECIAL_COMPANY_TYPE;
import static com.crlink.smartpark.common.constants.Constants.USER_MOBILE;
import static com.crlink.smartpark.common.constants.enums.JobCodeEnum.ADMINISTRATION_JOB_CODE;
import static com.crlink.smartpark.common.constants.enums.JobCodeEnum.CLEANER_JOB_CODE;
import static com.crlink.smartpark.common.constants.enums.JobCodeEnum.GREEN_JOB_CODE;
import static com.crlink.smartpark.common.constants.enums.JobCodeEnum.REPAIR_JOB_CODE;
import static com.crlink.smartpark.common.constants.enums.JobCodeEnum.SECURITY_JOB_CODE;
import static com.crlink.smartpark.customer.constants.Constant.ORDER_TYPE_COMPLAINT;
import static com.crlink.smartpark.customer.constants.Constant.ORDER_TYPE_REPAIR;
import static java.util.stream.Collectors.joining;

@Service
@Slf4j
public class EmployeeManageServiceImpl implements EmployeeManageService {
    @Autowired
    private TagService tagService;
    @Autowired
    private JobEmployeeRelationService jobRelationService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeFingerService employeeFingerService;
    @Autowired
    private OrganizationManageService organizationManageService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private JobService jobService;
    @Resource
    private EmployeeDeviceService employeeDeviceService;
    @Resource
    private DeviceOperateProvider deviceOperateProvider;
    @Resource
    private CardProvider cardProvider;
    @Autowired
    private AsyncService asyncService;

    @Resource
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_GROUP_EMPLOYEE, allEntries = true)
    public void saveOrUpdateEmployee(EmployeeReqDTO reqDTO) {
        //同一个园区下手机号不能重复
        checkParkMobileRepeat(reqDTO);
        boolean notUpdateIcc = true;
        Employee employee = null;
        if (StrUtil.isBlank(reqDTO.getId())) {
            employee = MyBeanUtils.copy(reqDTO, Employee::new);
            Long cardId = deviceOperateProvider.generateCardId();
            employee.setCardNo(MyStrUtil.parseIdToNo(cardId));
            employee.setDevicePersonId(deviceOperateProvider.generatePersonId());
        } else {
            employee = employeeService.getById(reqDTO.getId());
            AssertUtil.requireNotNull(employee, "100071");
            notUpdateIcc = reqDTO.getName().equals(employee.getName()) && reqDTO.getMobile().equals(employee.getMobile())
                    && reqDTO.getPics().equals(employee.getPics());
            BeanUtils.copyProperties(reqDTO, employee);
        }
        if (StrUtil.isNotBlank(reqDTO.getId())) {
            jobRelationService.remove(
                    Wrappers.<JobEmployeeRelation>lambdaQuery().eq(JobEmployeeRelation::getEmployeeId, reqDTO.getId()));
            //编辑员工时，所属园区、企业不支持编辑
            employee.setParkOrgId(null);
            employee.setCompanyId(null);
        }
        EntityUserUtil.setAllDynamic(employee);
        employeeService.saveOrUpdate(employee);
        String employeeId = employee.getId();
        List<JobEmployeeRelation> relations = reqDTO.getJobIds().parallelStream().map(t -> {
            JobEmployeeRelation relation = new JobEmployeeRelation();
            relation.setJobId(t);
            relation.setEmployeeId(employeeId);
            return relation;
        }).collect(Collectors.toList());
        jobRelationService.saveBatch(relations);

//        if (StrUtil.isBlank(reqDTO.getId())) {
//            log.info("请求大华添加人员, {}", employee);
//            String errMsg = employeeDeviceService.addEmployee(employee);
//            if (Objects.nonNull(errMsg)) {
//                throw new ResolinkRuntimeException(Message.code("60010", errMsg));
//            } else {
//                employeeDeviceService.pushEmployee(employee);
//            }
//        } else {
//            //数据有改变才调用大华平台
//            if (!notUpdateIcc) {
//                log.info("请求大华修改人员, {}", employee);
//                String errMsg = employeeDeviceService.updateEmployee(employee);
//                if (Objects.nonNull(errMsg)) {
//                    throw new ResolinkRuntimeException(Message.code("60010", errMsg));
//                }
//            }
//        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addFinger(PersonFingerReqDTO reqDTO) {
        Employee employee = Optional.ofNullable(employeeService.getById(reqDTO.getEmployeeId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        List<EmployeeFinger> fingers = new LinkedList<>();
        reqDTO.getFingers().forEach(data -> {
            EmployeeFinger finger = new EmployeeFinger();
            finger.setEmployeeId(reqDTO.getEmployeeId());
            finger.setFingerData(data.getFinger());
            finger.setFingerIndex(data.getFingerIndex());
            EntityUserUtil.setAll(finger);
            fingers.add(finger);
        });
        employeeFingerService.remove(Wrappers.<EmployeeFinger>lambdaQuery()
                .eq(EmployeeFinger::getEmployeeId, reqDTO.getEmployeeId()));
        employeeFingerService.saveBatch(fingers);
        employee.setFingers(reqDTO.getFingers());
//        String errMsg = employeeDeviceService.updateEmployee(employee);
//        if (Objects.nonNull(errMsg)) {
//            throw new ResolinkRuntimeException(Message.code("60010", errMsg));
//        }
        return true;
    }

    private void initPersonDeviceInfoNew(Employee employee, String directory) {
        DictVO dictVO = (DictVO) redisUtil.leftPopList(IMPORT_EMPLOYEE_PREFIX.getBuildCode(directory));
        if (dictVO == null) {
            try {
                Long cardId = deviceOperateProvider.generateCardId();
                employee.setCardNo(MyStrUtil.lowerCaseFirstChar(cardId.toString()));
                employee.setDevicePersonId(deviceOperateProvider.generatePersonId());
            } catch (Exception e) {
                log.error("导入员工时获取卡号，人员id失败！:{}", employee.getName());
            }
        } else {
            employee.setCardNo(dictVO.getLabel());
            employee.setDevicePersonId((Long) dictVO.getValue());
        }

    }

    @Override
    @Id2objHandle
    public PageInfo<EmployeeDTO> listEmployeePage(EmployeeQuery employeeQuery) {
        Set<String> parkOrgIds = CacheUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        employeeQuery.setParkOrgIds(parkOrgIds);
        LambdaQueryWrapper<Employee> queryWrapper = buildQuery(employeeQuery);
        employeeQuery.startPage();
        List<Employee> employees = employeeService.list(queryWrapper);
        MyPageInfoUtils.isEmptyPage(employees);
        List<EmployeeDTO> employeeDTOS = this.employeesConvert2DTOs(employees, EmployeeDTO::new);
        return employeeQuery.createPageInfo(employeeDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_GROUP_EMPLOYEE, allEntries = true)
    public void removeEmployeeById(String id) {
        Employee employee = Optional.ofNullable(employeeService.getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        if (cardProvider.containsEmployeeRelationCard(id)) {
            throw new ResolinkRuntimeException(Message.code("30009", employee.getName()));
        }
        jobRelationService.remove(Wrappers.<JobEmployeeRelation>lambdaQuery().eq(JobEmployeeRelation::getEmployeeId, id));
        employeeFingerService.remove(Wrappers.<EmployeeFinger>lambdaQuery().eq(EmployeeFinger::getEmployeeId, id));
        employeeService.removeById(id);
        //设备删除人员、删除卡片（人员在大华平台存在时才调用）
//        if (employee.getDevicePersonId() != null && employeeDeviceService.personExistById(employee.getDevicePersonId())) {
//            String errMsg = employeeDeviceService.delEmployee(employee.getDevicePersonId(), employee.getCardNo());
//            if (Objects.nonNull(errMsg)) {
//                throw new ResolinkRuntimeException(Message.code("60010", errMsg));
//            }
//        }
    }

    @Override
    @CacheEvict(value = CACHE_GROUP_EMPLOYEE, allEntries = true)
    public void updateEmployeeStatus(UpdateStatusReqDTO statusReqDTO) {
        //员工所在企业已禁用，不可修改员工状态
        String id = statusReqDTO.getId();
        Employee entity = employeeService.lambdaQuery().select(Employee::getId, Employee::getCompanyId, Employee::getStatus)
                .eq(Employee::getId, id).one();
        if (Objects.nonNull(entity)) {
            Company company = companyService.lambdaQuery().select(Company::getId, Company::getStatus)
                    .eq(Company::getId, entity.getCompanyId()).one();
            if (Objects.nonNull(company) && company.getStatus().equals(StatusEnum.DISABLE.code)) {
                throw new ResolinkRuntimeException("30006");
            }
        }
        employeeService.update(Wrappers.<Employee>lambdaUpdate()
                .eq(Employee::getId, id).set(Employee::getStatus, statusReqDTO.getStatus())
                .set(Employee::getUpdateBy, SystemUtil.getInstance().getCurrentUserId()));
        String errMsg = employeeDeviceService.updateStatus(id, statusReqDTO.getStatus());
        if (Objects.nonNull(errMsg)) {
            //回退DB的修改
            employeeService.update(Wrappers.<Employee>lambdaUpdate()
                    .eq(Employee::getId, id).set(Employee::getStatus, entity.getStatus())
                    .set(Employee::getUpdateBy, SystemUtil.getInstance().getCurrentUserId()));
            throw new ResolinkRuntimeException(Message.code("60010", errMsg));
        }
    }

    @Override
    public Boolean download(EmployeeQuery employeeQuery) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrFakeIds();
        employeeQuery.setParkOrgIds(parkOrgIds);
        String fileName = "员工管理";
        ExcelExportUtils.export(() -> employeeService.list(buildQuery(employeeQuery)),
                employees -> employeesConvert2DTOs(employees, EmployeeExcelExportDTO::new), fileName,
                EmployeeExcelExportDTO.class);
        return true;
    }

    @Override
    @CacheEvict(value = CACHE_GROUP_EMPLOYEE, allEntries = true)
    public FileImportErrorInfoDTO importEmployees(MultipartFile file, String parkOrgId, String companyId) {
        String employeeFileFolder = handleEmployeeFile(file);
        Collection<File> files = FileUtils.listFiles(new File(employeeFileFolder), new String[]{"xlsx"}, true);
        File excelFile = CollUtil.getFirst(files);
        if (Objects.isNull(excelFile) || !excelFile.exists()) {
            deleteFile(employeeFileFolder);
            throw new ResolinkRuntimeException("10022");
        }
        List<Employee> successSaveEmployees = new ArrayList<>();
        ExcelImportRollbackListener<EmployeeExcelImportDTO> importListener =
                new ExcelImportRollbackListener<>(EmployeeExcelImportDTO.class,
                        // 业务参数校验方法,并单行保存数据
                        t -> {
                            t.setParkOrgId(parkOrgId);
                            t.setCompanyId(companyId);
                            return validAndOperateExcelDTO(t, successSaveEmployees);
                        }, null);
        ExcelImportUtils.importExcel(excelFile, EmployeeExcelImportDTO.class, importListener);
        // 删除文件
        deleteFile(employeeFileFolder);
        //下发成功保存的员工到设备
        employeeDeviceService.batchAddEmployees(successSaveEmployees);
        // easyExcel插件多线程导入数据库
        FileImportErrorInfoDTO result = importListener.getImportResult();
        return result;
    }

    @Override
    public FileImportErrorInfoDTO importEmployeesNew(ImportEmployeeDTO dto) {
        final String localFileParentFolder = FileUtils.getTempDirectory().getAbsolutePath() + File.separator + makeFile(dto.getTime())
                + File.separator + dto.getTime();
        String unFilePath = localFileParentFolder;
        File excelFile;
        try {
            excelFile = new File(unFilePath + File.separator + "staffInformation.xlsx");
        } catch (Exception e) {
            log.error("文件不存在", e);
            throw new ResolinkRuntimeException("10022");
        }
        List<Employee> successSaveEmployees = new ArrayList<>();
        ExcelImportRollbackListener<EmployeeExcelImportDTO> importListener =
                new ExcelImportRollbackListener<>(EmployeeExcelImportDTO.class,
                        // 业务参数校验方法,并单行保存数据
                        t -> {
                            t.setParkOrgId(dto.getParkOrgId());
                            t.setCompanyId(dto.getCompanyId());
                            t.setUploadPic(localFileParentFolder + File.separator + t.getPics());
                            return validAndOperateExcelDTONew(t, successSaveEmployees, dto.getTime());
                        }, null);
        ExcelImportUtils.importExcel(excelFile, EmployeeExcelImportDTO.class, importListener);
        List<PersonDeviceDTO> personList = Lists.newArrayList();
        for (Employee e : successSaveEmployees) {
            File file = new File(e.getUploadPic());
            log.info("员工照片上传开始,fileKey:{}", file.getName());
            asyncService.uploadMinio(e.getPics(), file);
            personList.add(buildPersonDTONew(e, e.getUploadPic()));
        }
        // 异步导入大华ICC平台
        asyncService.uploadIcc(personList);
        FileImportErrorInfoDTO result = importListener.getImportResult();
        redisUtil.delete(IMPORT_EMPLOYEE_PREFIX.getBuildCode(dto.getTime()));
        return result;
    }

    @Override
    public String enableDevicePerson(List<Long> personIds) {
        return employeeDeviceService.enablePerson(personIds);
    }

    @Override
    public String unableDevicePerson(List<Long> personIds) {
        return employeeDeviceService.unablePerson(personIds);
    }

    @Override
    public Boolean updateEmployeeCardNo(String id, String cardNo) {
        return employeeService.update(Wrappers.<Employee>lambdaUpdate()
                .eq(Employee::getId, id)
                .set(Employee::getCardNo, cardNo)
                .set(Employee::getUpdateBy, SystemUtil.getInstance().getCurrentUserId()));
    }

    private void deleteFile(String employeeFileFolder) {
        ThreadUtil.execAsync(() -> {
            try {
                Thread.sleep(500);
                FileUtils.forceDelete(new File(employeeFileFolder).getParentFile());
                //FileUtils.forceDelete(new File(employeeFileFolder + ".zip"));
            } catch (Exception e) {
                log.error("文件删除失败", e);
            }
        });
    }

    private void get(String fileDir) {
        //ExcelFileUtil.
        Collection<File> files = FileUtils.listFiles(new File(fileDir), new String[]{"xlsx"}, true);
        CollUtil.getFirst(files);
    }

    /**
     * 导入校验
     * v13迭代：导入规则调整：同一园区下，若员工手机号相同，则以系统已有数据为准，对应行将不做导入处理。
     *
     * @param t
     * @param successSaveEmployee
     * @return
     */
    private String validAndOperateExcelDTO(EmployeeExcelImportDTO t, List<Employee> successSaveEmployee) {
//        Map<String,
//                Employee> employeeMap = employeeService.lambdaQuery().select(Employee::getId, Employee::getCompanyId)
//                .eq(Employee::getParkOrgId, t.getParkOrgId()).eq(Employee::getMobile, t.getMobile()).list()
//                .parallelStream().collect(Collectors.toMap(Employee::getCompanyId, Function.identity()));
//        if (ObjectUtil.isNotEmpty(employeeMap)) {
//            Employee employee = employeeMap.get(t.getCompanyId());
//            if (Objects.nonNull(employee)) {
//                t.setId(employeeMap.get(t.getCompanyId()).getId());
//            } else {
//                return "该手机号的员工已经在其他公司！";
//            }
//        }
        //v13 同一园区下，若员工手机号相同，则以系统已有数据为准，对应行将不做导入处理
        Employee one = employeeService.lambdaQuery().select(Employee::getId)
                .eq(Employee::getParkOrgId, t.getParkOrgId())
                .eq(Employee::getMobile, t.getMobile()).last("limit 1").one();
        if (Objects.nonNull(one)) {
            return "该手机号的员工已存在！";
        }
        Employee employee = MyBeanUtils.copy(t, Employee::new);
        employee.setTagIds(queryEmployeeTags(employee, t.getTagNames()));
        saveEmployeeJobIds(employee, t.getJobIds());
        EntityUserUtil.setAllDynamic(employee);
        boolean result = employeeService.saveOrUpdate(employee);
        if (result) {
            successSaveEmployee.add(employee);
        }
        return result ? null : "插入数据失败！";
    }


    private String validAndOperateExcelDTONew(EmployeeExcelImportDTO
                                                      t, List<Employee> successSaveEmployee, String directory) {
        Employee one = employeeService.lambdaQuery().select(Employee::getId)
                .eq(Employee::getParkOrgId, t.getParkOrgId())
                .eq(Employee::getMobile, t.getMobile()).last("limit 1").one();
        if (Objects.nonNull(one)) {
            return "该手机号的员工已存在！";
        }
        String fileKey = "employee_face/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.EXCEL_FILE_TIME_PATTERN)) + "_" + t.getPics();
        Employee employee = MyBeanUtils.copy(t, Employee::new);
        employee.setTagIds(queryEmployeeTags(employee, t.getTagNames()));
        saveEmployeeJobIds(employee, t.getJobIds());
        EntityUserUtil.setAllDynamic(employee);
        employee.setPics(fileKey);
        initPersonDeviceInfoNew(employee, directory);
        boolean result = employeeService.saveOrUpdate(employee);
        if (result) {
            successSaveEmployee.add(employee);
        }
        return result ? null : "插入数据失败！";
    }

    private PersonDeviceDTO buildPersonDTONew(Employee employee, String imgName) {
        PersonDeviceDTO personDTO = new PersonDeviceDTO();
        personDTO.setCompanyId(employee.getCompanyId());
        personDTO.setTagIds(employee.getTagIds());
        personDTO.setDevicePersonId(employee.getDevicePersonId());
        personDTO.setPersonName(employee.getName());
        personDTO.setMobilePhone(employee.getMobile());
        personDTO.setCardNo(employee.getCardNo());
        LocalDateTime now = LocalDateTime.now();
        personDTO.setStartTime(now);
        personDTO.setEndTime(now.plusYears(10));
        personDTO.setPersonType(DevicePersonTypeEnum.NORMAL);
        personDTO.setPicFileName(imgName);
        personDTO.setFingers(employee.getFingers());
        return personDTO;
    }

    /**
     * 上传压缩包并解压，返回时间戳
     *
     * @param file
     * @return
     */
    @Override
    public String handleEmployeeZip(MultipartFile file) {
        try {
            String directory = String.valueOf(System.currentTimeMillis());
            String localFileParentFolder = FileUtils.getTempDirectory().getAbsolutePath() + File.separator + makeFile(directory);
            String localFileDirectory = localFileParentFolder + File.separator + "员工导入模板";
            // 检测文件目录是否存在 不存在则创建
            File dest = new File(localFileParentFolder);
            if (!dest.exists()) {
                dest.mkdirs();
            }
            String originalFilename = file.getOriginalFilename();
            String fileName = localFileParentFolder + File.separator + originalFilename;
            File zipFile = new File(fileName);
            file.transferTo(zipFile);
            log.info("压缩包文件临时保存路径：{}", localFileParentFolder);
            //File outFile = ZipUtil.unzip(fileName, Charset.forName("GBK"));
            ZipFile zip = new ZipFile(zipFile);
            /*zip4j默认用GBK编码去解压,这里设置编码为GBK的*/
            zip.setFileNameCharset("GBK");
            zip.extractAll(localFileParentFolder);
            log.info("压缩包解压成功：{}", localFileParentFolder);
            File f = new File(localFileDirectory);
            if (f.isDirectory() && f.exists()) {
                f.renameTo(new File(localFileParentFolder + File.separator + directory));
            }
            String path = localFileParentFolder + File.separator + directory + File.separator + "face";
            int size = FileUtil.listFileNames(path).size();
            //1.批量获取员工主卡号、设备人员id（devicePersonId）
            //2.redis redisKey= import_employee:cards_devicePersonIds:directory\
            List<Long> cardNos = deviceOperateProvider.generateCardIdBatch(size);
            List<Long> personIds = deviceOperateProvider.generatePersonIdBatch(size);
            List<DictVO> values = Lists.newArrayListWithCapacity(size);
            for (int i = 0; i < size; i++) {
                String cardNo = MyStrUtil.parseIdToNo(cardNos.get(i));
                values.add(new DictVO(cardNo, personIds.get(i)));
            }
            redisUtil.setListByLeftPush(IMPORT_EMPLOYEE_PREFIX.getBuildCode(directory), values);
            return directory;
        } catch (Exception e) {
            log.error("压缩包上传失败", e);
            throw new ResolinkRuntimeException("10008");
        }
    }

    private String makeFile(String directory) {
        return String.join(File.separator, EMPLOYEE_FACE_PIC_DIR, directory);
    }

    private List<String> queryEmployeeTags(Employee employee, List<String> tags) {
        if (ObjectUtil.isEmpty(tags)) {
            return null;
        }
        Map<String, String> name2IdMap = tagService.name2Id(tags, employee.getCompanyId());
        if (ObjectUtil.isNotEmpty(name2IdMap)) {
            return Lists.newArrayList(name2IdMap.values());
        }
        return null;

    }

    private void saveEmployeeJobIds(Employee employee, List<String> jobName) {
        if (ObjectUtil.isEmpty(jobName)) {
            return;
        }
        Map<String, Integer> jobCodeMap = DictUtil.getValues("JOBCODE");
        List<Integer> jobCodes = jobName.parallelStream().map(jobCodeMap::get).collect(Collectors.toList());
        Map<Integer, String> jobCode2JobIdByCompanyId =
                jobService.jobCode2JobIdByCompanyId(employee.getCompanyId(), jobCodes);
        List<JobEmployeeRelation> relations = jobCode2JobIdByCompanyId.values().parallelStream().map(t -> {
            JobEmployeeRelation relation = new JobEmployeeRelation();
            relation.setJobId(t);
            relation.setEmployeeId(employee.getId());
            return relation;
        }).collect(Collectors.toList());
        jobRelationService.saveBatch(relations);
    }

    private String handleEmployeeFile(MultipartFile file) {
        try {
            String localFileParentFolder = FileUtils.getTempDirectory().getAbsolutePath() + File.separator + EMPLOYEE_FACE_PIC_DIR;
            log.info("文件上传路径：{}", localFileParentFolder);
            String originalFilename = file.getOriginalFilename();
            String fileName = localFileParentFolder + File.separator + originalFilename;
            File dest = new File(fileName);
            // 检测文件目录是否存在 不存在则创建
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            MyFileUtils.unZipFiles(dest, localFileParentFolder);
            moveFacePictureFile(localFileParentFolder);
            return localFileParentFolder + File.separator + StrUtil.sub(originalFilename, 0, originalFilename.lastIndexOf(".zip"));

        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new ResolinkRuntimeException("10008");
        }
    }

    private void moveFacePictureFile(String localFileParentFolder) {
        File fileDir = new File(localFileParentFolder);
        File[] listFiles = fileDir.listFiles();
        for (File f : listFiles) {
            //解压后的目录
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (File fc : files) {
                    if (fc.isDirectory()) {
                        File parentFile = fc.getParentFile().getParentFile();
                        log.info("人脸图片原始路径：{}", fc.getAbsolutePath());
                        FileUtil.move(fc, parentFile, true);
                        log.info("人脸图片路径：{}", parentFile.getAbsolutePath());
                    }
                }
            }
        }
        List<File> files = FileUtil.loopFiles(new File(localFileParentFolder), 2, null);
        for (File file : files) {
            log.info("文件：{}", file.getAbsolutePath());
        }
    }

    @Override
    @Cacheable(value = CACHE_GROUP_EMPLOYEE, keyGenerator = CacheKeyGenerator)
    public List<DictVO> listEmployeeDict(Set<String> companyIds, String name) {
        List<DictVO> list = queryEmployeeDict(companyIds, name).parallelStream()
                .map(t -> new DictVO(t.getName(), t.getId())).collect(Collectors.toList());
        List<DictVO> result = SortUtils.pinYinSort(list, DictVO::getLabel);
        return result;
    }

    @Override
    public List<EmployeeTreeVO> queryEmployeeDicts(String parkOrgId, String companyId, Integer companyType,
                                                   String employeeName, Integer[] jobCodes) {
        // Set<String> parkOrgIds = null;
        // if (StringUtils.hasText(parkOrgId)) {
        // parkOrgIds = Sets.newHashSet(parkOrgId);
        // }
        Set<String> parkOrgIds = getParkOrgIds(parkOrgId, null);
        List<Employee> employees =
                employeeService.listEmployeeByLikeName(parkOrgIds, employeeName, companyType, companyId);
        Set<String> tagIds = employees.parallelStream().filter(t -> CollectionUtil.isNotEmpty(t.getTagIds()))
                .flatMap(t -> t.getTagIds().stream()).collect(Collectors.toSet());
        Set<String> employeeIds = employees.parallelStream().map(Employee::getId).collect(Collectors.toSet());
        Map<String, String> id2NameMap =
                employees.parallelStream().collect(Collectors.toMap(Employee::getId, Employee::getName));
        Map<String, List<String>> employeeId2JobIdListMap = jobRelationService.employeeId2JobIdListMap(employeeIds);
        Set<String> jobIdList = employeeId2JobIdListMap.values().parallelStream().flatMap(jobIds -> jobIds.stream())
                .collect(Collectors.toSet());
        Map<String, Integer> jobId2JobCodeMap =
                (Map<String, Integer>) jobService.id2ValueMap(jobIdList, Job::getJobCode);
        Map<String, String> tagId2NameMap = tagService.id2Name(tagIds);
        tagId2NameMap.put("0", "#");
        List<EmployeeDictDTO> dict = Lists.newArrayList();
        employees.parallelStream()
                .filter(e -> filterJob(e.getId(), jobCodes, employeeId2JobIdListMap, jobId2JobCodeMap)).forEach(t -> {
            List<String> etagIds = Optional.ofNullable(t.getTagIds()).orElse(Lists.newArrayList("0"));
            for (String etagId : etagIds) {
                EmployeeDictDTO dto = MyBeanUtils.copy(t, EmployeeDictDTO::new);
                dto.setTagId(etagId);
                dto.setTagName(tagId2NameMap.get(etagId));
                dict.add(dto);
            }
        });
        Map<String, String> companyMap = dict.parallelStream()
                .collect(Collectors.toMap(EmployeeDictDTO::getCompanyId, EmployeeDictDTO::getCompanyName, (k1, k2) -> k1));
        Map<String, List<EmployeeDictDTO>> companiesGroup =
                dict.parallelStream().collect(Collectors.groupingBy(EmployeeDictDTO::getCompanyId, Collectors.toList()));
        List<EmployeeTreeVO> result = Lists.newArrayList();
        companiesGroup.forEach((k, v) -> {
            EmployeeTreeVO companyTree = new EmployeeTreeVO(companyMap.get(k), k);
            List<EmployeeTreeVO> tags = companyTree.getChildren();
            Map<String, List<EmployeeDictDTO>> tagGroup =
                    v.parallelStream().collect(Collectors.groupingBy(EmployeeDictDTO::getTagId, Collectors.toList()));
            tagGroup.forEach((tid, eList) -> {
                EmployeeTreeVO tagTree = new EmployeeTreeVO(tagId2NameMap.get(tid), tid);
                List<EmployeeTreeVO> es = tagTree.getChildren();
                Map<String, List<EmployeeDictDTO>> employeeGroup =
                        eList.parallelStream().collect(Collectors.groupingBy(EmployeeDictDTO::getId, Collectors.toList()));
                employeeGroup.forEach((eid, eGroup) -> {
                    es.add(new EmployeeTreeVO(id2NameMap.get(eid), eid));
                });
                tags.add(tagTree);
            });
            result.add(companyTree);
        });
        return result;
    }

    /**
     * 查询员工通用接口
     *
     * @param orgId        组织Id
     * @param parkOrgId    园区Id
     * @param companyId    公司Id
     * @param companyType  公司类型：物业/普通
     * @param employeeName 员工名
     * @param jobCodes     岗位对应字典value
     * @return
     */
    @Override
    public List<SimpleNode> selectEmployeesByCondition(String orgId, String parkOrgId, String companyId,
                                                       Integer companyType, String employeeName, Integer[] jobCodes) {
        // Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(orgId);
        Set<String> parkOrgIds = getParkOrgIds(parkOrgId, orgId);
        List<Employee> employees =
                employeeService.listEmployeeByLikeName(parkOrgIds, employeeName, companyType, companyId);
        Set<String> employeeIds = employees.parallelStream().map(Employee::getId).collect(Collectors.toSet());
        // 根据岗位过滤
        Map<String, List<String>> employeeId2JobIdListMap = jobRelationService.employeeId2JobIdListMap(employeeIds);
        Set<String> jobIdList = employeeId2JobIdListMap.values().parallelStream().flatMap(jobIds -> jobIds.stream())
                .collect(Collectors.toSet());
        Map<String, Integer> jobId2JobCodeMap =
                (Map<String, Integer>) jobService.id2ValueMap(jobIdList, Job::getJobCode);
        List<SimpleNode> result = employees.parallelStream()
                .filter(e -> filterJob(e.getId(), jobCodes, employeeId2JobIdListMap, jobId2JobCodeMap)).map(e -> {
                    SimpleNode node = new SimpleNode(e.getId(), e.getName());
                    setEmployeeExtraInfo(node, e);
                    return node;
                }).collect(Collectors.toList());
        return result;
    }

    @Override
    public Set<TreeDictVO> selectCompanyOrTagByConditions(String
                                                                  orgId, Set<String> parkOrgIds, Set<String> companyIds,
                                                          Integer companyType) {
        if (ObjectUtil.isEmpty(parkOrgIds)) {
            parkOrgIds = SystemUtil.getInstance().getParkOrgIds(orgId);
        }
        if (ObjectUtil.isEmpty(parkOrgIds)) {
            return Collections.emptySet();
        }
        Map<String, String> orgId2NameMap =
                (Map<String, String>) organizationService.id2ValueMap(parkOrgIds, Organization::getOrgName);
        List<Company> companies =
                companyService.lambdaQuery().select(Company::getId, Company::getName, Company::getParkOrgId)
                        .eq(Objects.nonNull(companyType), Company::getCompanyType, companyType)
                        .in(Company::getParkOrgId, parkOrgIds).in(ObjectUtil.isNotEmpty(companyIds), Company::getId, companyIds)
                        .list();
        Map<String, Map<String, String>> parkOrgId2CompanyId2CompanyMap = companies.parallelStream()
                .collect(Collectors.groupingBy(Company::getParkOrgId, Collectors.toMap(Company::getId, Company::getName)));
        Map<String,
                Map<String, String>> companyId2TagId2TagMap = tagService.lambdaQuery()
                .select(Tag::getId, Tag::getTagName, Tag::getCompanyId).eq(Tag::getStatus, StatusEnum.ENABLE.code)
                .in(Tag::getParkOrgId, parkOrgIds).list().parallelStream()
                .collect(Collectors.groupingBy(Tag::getCompanyId, Collectors.toMap(Tag::getId, Tag::getTagName)));
        Set<TreeDictVO> result = Sets.newHashSet();
        orgId2NameMap.forEach((parkOrgId, parKName) -> {
            TreeDictVO parkDictVO =
                    new TreeDictVO(parkOrgId, orgId2NameMap.get(parkOrgId), SimpleNode.TypeEnum.PARK.code);
            parkDictVO.setExtraInfo(parkOrgId);
            Map<String, String> companyMap = parkOrgId2CompanyId2CompanyMap.get(parkOrgId);
            if (ObjectUtil.isNotEmpty(companyMap)) {
                companyMap.forEach((companyId, companyName) -> {
                    TreeDictVO companyDictVO = new TreeDictVO(companyId, companyName, SimpleNode.TypeEnum.COMPANY.code);
                    companyDictVO.setExtraInfo(parkOrgId);
                    Map<String, String> tagMap = companyId2TagId2TagMap.get(companyId);
                    if (ObjectUtil.isNotEmpty(tagMap)) {
                        tagMap.forEach((tagId, TagName) -> {
                            TreeDictVO tagDictVO = new TreeDictVO(tagId, TagName, SimpleNode.TypeEnum.Tag.code);
                            tagDictVO.setExtraInfo(parkOrgId);
                            companyDictVO.getChildren().add(tagDictVO);
                        });
                    }
                    parkDictVO.getChildren().add(companyDictVO);
                });
            }
            result.add(parkDictVO);
        });
        return result;
    }

    @Override
    @Cacheable(value = CACHE_GROUP_EMPLOYEE, keyGenerator = CacheKeyGenerator)
    public PageInfo<DictVO> selectEmployeesByIdAndType(EmployeeTreeSelectQuery query) {
        LambdaQueryChainWrapper<Employee> wrapper =
                employeeService.lambdaQuery().select(Employee::getId, Employee::getName, Employee::getMobile)
                        .eq(Employee::getStatus, StatusEnum.ENABLE.code);
        wrapper.eq(Employee::getParkOrgId, query.getParkOrgId());
        wrapper.in(CollUtil.isNotEmpty(query.getCompanyIds()), Employee::getCompanyId, query.getCompanyIds());
        if (CollUtil.isNotEmpty(query.getTagIds())) {
            String sql = buildQueryEmployeeSqlByTagIds(query.getTagIds());
            wrapper.apply(sql);
        }
        wrapper.like(StringUtils.hasText(query.getName()), Employee::getName, query.getName());
        // 过滤岗位相关的员工
        if (ObjectUtil.isNotEmpty(query.getJobCodes())) {
            Map<Integer, Set<String>> jobCode2JobIdsMap =
                    jobService.jobCode2JobIds(query.getParkOrgId(), query.getJobCodes());
            Set<String> jobIds =
                    jobCode2JobIdsMap.values().parallelStream().flatMap(e -> e.stream()).collect(Collectors.toSet());
            Set<String> relationEmployees = jobRelationService.jobId2EmployeeIdsMap(jobIds).values().parallelStream()
                    .flatMap(e -> e.stream()).collect(Collectors.toSet());
            MyPageInfoUtils.isEmptyPage(relationEmployees);
            wrapper.in(Employee::getId, relationEmployees);
        }
        query.startPage();
        List<Employee> list = wrapper.list();
        List<DictVO> result = list.parallelStream().map(t -> {
            SimpleNode simpleNode = new SimpleNode(t.getId(), t.getName());
            Map<String, Object> extraInfo = Maps.newHashMap();
            extraInfo.put(USER_MOBILE, t.getMobile());
            simpleNode.setExtraInfo(extraInfo);
            return simpleNode;
        }).sorted((n1, n2) -> new PinyinComparator().compare(n1.getLabel(), n2.getLabel()))
                .collect(Collectors.toList());
        return query.createPageInfo(result);
    }

    private String buildQueryEmployeeSqlByTagIds(Set<String> tagIds) {
        List<String> ids = new ArrayList<>(tagIds);
        StringBuilder sqlBuilder = new StringBuilder("  (");
        for (int i = 0; i < ids.size(); i++) {
            if (i != 0) {
                sqlBuilder.append(" OR ");
            }
            sqlBuilder.append(" JSON_CONTAINS(tag_ids,").append("JSON_QUOTE('").append(ids.get(i)).append("')").append(")");
        }
        sqlBuilder.append(")");
        String sql = sqlBuilder.toString();
        return sql;
    }

    @Override
    public Map<String, String> queryEmployeeParkOrgId2NameByMobile(String mobile) {
        EmployeeQueryDTO queryDTO = EmployeeQueryDTO.mobileBuildDTO(mobile, "park_org_id");
        queryDTO.setShowParkOrgName(true);
        Map<String, EmployeeDTO> employeeDTOMap =
                employeeService.queryEmployeeByCondition(queryDTO, SystemUtil.getInstance().getCurrentHeadOrgId());
        if (ObjectUtil.isEmpty(employeeDTOMap)) {
            return Collections.emptyMap();
        }
        Map<String, String> parkOrgMap = employeeDTOMap.values().parallelStream()
                .collect(Collectors.toMap(EmployeeDTO::getParkOrgId, EmployeeDTO::getParkOrgName));
        return parkOrgMap;
    }

    private Set<String> getParkOrgIds(String parkOrgId, String orgId) {
        if (StringUtils.hasText(parkOrgId)) {
            return Sets.newHashSet(parkOrgId);
        } else {
            return SystemUtil.getInstance().getParkOrgIds(orgId);
        }
    }

    private void setEmployeeExtraInfo(SimpleNode node, Employee employee) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("companyName", employee.getCompanyName());
        jsonObject.put("mobile", employee.getMobile());
        node.setExtraInfo(jsonObject);
    }

    private boolean filterJob(String employeeId, Integer[] containJobCodes,
                              Map<String, List<String>> employeeId2JobIdListMap, Map<String, Integer> jobId2JobCodeMap) {
        List<String> jobIds = employeeId2JobIdListMap.get(employeeId);
        if (containJobCodes == null) {
            return true;
        }
        if (ObjectUtil.isNotEmpty(jobIds)) {
            List<Integer> jobCodes =
                    jobIds.parallelStream().map(jobId -> jobId2JobCodeMap.get(jobId)).collect(Collectors.toList());
            return CollectionUtil.containsAny(Arrays.asList(containJobCodes), jobCodes);

        }
        return false;
    }

    @Override
    @Cacheable(cacheNames = CACHE_GROUP_EMPLOYEE,
            key = "'listSpecialEmployeeDict_orgId:'+#orgId+'_name:'+#name+'_userId:'+#userId")
    public List<DictVO> listSpecialEmployeeDict(String orgId, String name, String userId) {
        Set<String> parkOrgIds = organizationManageService.getAllParkOrgIdsByOrgId(orgId, userId);
        Map<String, String> companyId2NameMap = companyService.lambdaQuery().select(Company::getId, Company::getName)
                .in(Company::getParkOrgId, parkOrgIds).eq(Company::getCompanyType, SPECIAL_COMPANY_TYPE).list()
                .parallelStream().collect(Collectors.toMap(Company::getId, Company::getName));
        if (CollectionUtil.isEmpty(companyId2NameMap)) {
            return Collections.emptyList();
        }
        List<DictVO> employees = queryEmployeeDict(companyId2NameMap.keySet(), name).parallelStream().map(t -> {
            SimpleNode simpleNode = new SimpleNode(t.getId(), t.getName());
            HashMap<String, Object> extraInfo = new HashMap<>(2);
            extraInfo.put("mobile", t.getMobile());
            extraInfo.put("companyName", companyId2NameMap.get(t.getCompanyId()));
            simpleNode.setExtraInfo(extraInfo);
            return simpleNode;
        }).collect(Collectors.toList());
        List<DictVO> result = SortUtils.pinYinSort(employees, DictVO::getLabel);
        return result;
    }

    private List<Employee> queryEmployeeDict(Set<String> companyIds, String name) {
        List<Employee> employees = employeeService.lambdaQuery()
                .select(Employee::getId, Employee::getName, Employee::getCompanyId, Employee::getMobile)
                .in(Employee::getCompanyId, companyIds).like(StringUtils.hasText(name), Employee::getName, name)
                .eq(Employee::getStatus, StatusEnum.ENABLE.getCode()).orderByAsc(Employee::getName).last("limit 20").list();
        return employees;
    }

    /**
     * 投诉、报修派单时获取处理人下拉列表
     *
     * @param companyId 物业公司Id
     * @param orderType 投诉、报修类型 ：1 投诉，2 报修
     * @param name      员工名称
     * @return
     */
    @Override
    // @Cacheable(cacheNames = CACHE_GROUP_EMPLOYEE, key =
    // "'listHandlerDict_companyId:'+#companyId+'_orderType:'+orderType+'_name'+#name")
    public List<DictVO> listHandlerDict(String companyId, int orderType, String name) {
        Map<String,
                String> employees = employeeService.lambdaQuery().select(Employee::getId, Employee::getName)
                .eq(Employee::getCompanyId, companyId).eq(Employee::getStatus, StatusEnum.ENABLE.getCode())
                .like(StringUtils.hasText(name), Employee::getName, name).orderByAsc(Employee::getName).list()
                .parallelStream().collect(Collectors.toMap(Employee::getId, Employee::getName));
        // 获取岗位字典数据
        Map<Object, String> jobNameMap = DictUtil.getByType("JOBCODE").values().parallelStream()
                .collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        Map<String, Set<Integer>> jobCodeByEmployeeIds = employeeService.getJobCodeByEmployeeIds(employees.keySet());
        if (CollectionUtil.isEmpty(jobCodeByEmployeeIds)) {
            return Lists.newLinkedList();
        }
        Set<String> resultEmployees = Sets.newHashSet();
        Map<String, Set<Integer>> resultMap = Maps.newHashMap();
        filterHandlerEmployee(jobCodeByEmployeeIds, orderType, resultEmployees, resultMap);
        if (CollectionUtil.isEmpty(resultEmployees)) {
            return Lists.newLinkedList();
        }
        // 拼接员工名称+岗位名称
        List<DictVO> result = resultEmployees.stream().map(t -> {
            StringBuilder handlerName = new StringBuilder(employees.get(t) + "(");
            String jobLabel =
                    resultMap.get(t).parallelStream().map(jobCode -> jobNameMap.get(jobCode)).collect(joining(","));
            handlerName.append(jobLabel).append(")");
            DictVO vo = new DictVO(handlerName.toString(), t);
            return vo;
        }).collect(Collectors.toList());
        // List<DictVO> result = resultEmployees.stream().map(t -> new DictVO(employees.get(t),
        // t)).collect(Collectors.toList());
        return result;
    }

    @Override
    // @Id2objHandle
    public List<EmployeeInfoVO> listEmployeeInfosByMobile(String mobile) {
        EmployeeQueryDTO queryDTO = EmployeeQueryDTO.mobileBuildDTO(mobile, "park_org_id", "mobile", "company_id");
        queryDTO.setShowCompanyName(true);
        queryDTO.setShowParkOrgName(true);
        queryDTO.setShowJobCodes(true);
        Map<String, EmployeeDTO> employeeDTOMap =
                employeeService.queryEmployeeByCondition(queryDTO, SystemUtil.getInstance().getCurrentHeadOrgId());
        List<EmployeeInfoVO> employeeInfos =
                MyBeanUtils.copyList(ListUtil.toList(employeeDTOMap.values()), EmployeeInfoVO::new, (s, t) -> {
                    t.setEmployeeId(s.getId());
                });
        return employeeInfos;
    }

    private void checkParkMobileRepeat(EmployeeReqDTO reqDTO) {
        Employee employee = employeeService.lambdaQuery().select(Employee::getId)
                .ne(StringUtils.hasText(reqDTO.getId()), Employee::getId, reqDTO.getId())
                .eq(Employee::getParkOrgId, reqDTO.getParkOrgId()).eq(Employee::getMobile, reqDTO.getMobile())
                .last("limit 1").one();
        if (Objects.nonNull(employee)) {
            throw new ResolinkRuntimeException("40025");
        }
    }

    /**
     * 过滤出有投诉、报修相关岗位的员工
     *
     * @param jobCodeByEmployeeIds
     * @param orderType            投诉、报修类型
     * @param resultEmployees
     * @param resultMap
     */
    private void filterHandlerEmployee(Map<String, Set<Integer>> jobCodeByEmployeeIds, Integer orderType,
                                       Set<String> resultEmployees, Map<String, Set<Integer>> resultMap) {
        jobCodeByEmployeeIds.forEach((k, v) -> {
            if (CollectionUtil.isNotEmpty(v)) {
                // 报修
                if (orderType.equals(ORDER_TYPE_REPAIR) && v.contains(REPAIR_JOB_CODE.code)) {
                    resultEmployees.add(k);
                    resultMap.put(k, v);
                }
                // 投诉
                if (orderType.equals(ORDER_TYPE_COMPLAINT)) {
                    // Set<Integer> complaintJobCodes = Arrays.stream(ComplaintJobTypeEnum.values())
                    // .map(t -> t.code).collect(Collectors.toSet());
                    Set<Integer> complaintJobCodes = Sets.newHashSet(REPAIR_JOB_CODE.code, CLEANER_JOB_CODE.code,
                            SECURITY_JOB_CODE.code, GREEN_JOB_CODE.code, ADMINISTRATION_JOB_CODE.code);
                    if (CollectionUtil.containsAny(v, complaintJobCodes)) {
                        resultEmployees.add(k);
                        resultMap.put(k, v);
                    }
                }
            }
        });
    }

    private <E> List<E> employeesConvert2DTOs(List<Employee> employees, Supplier<E> supplier) {
        Set<String> tagIds = employees.parallelStream().filter(t -> CollectionUtil.isNotEmpty(t.getTagIds()))
                .flatMap(t -> t.getTagIds().stream()).collect(Collectors.toSet());
        Set<String> employeeIds = employees.parallelStream().map(Employee::getId).collect(Collectors.toSet());
        Map<String, List<String>> employeeId2JobIdListMap = jobRelationService.employeeId2JobIdListMap(employeeIds);
        Set<String> jobIdList = employeeId2JobIdListMap.values().parallelStream().flatMap(jobIds -> jobIds.stream())
                .collect(Collectors.toSet());
        Map<String, Integer> jobId2JobCodeMap =
                (Map<String, Integer>) jobService.id2ValueMap(jobIdList, Job::getJobCode);
        Map<String, String> tagId2NameMap = tagService.id2Name(tagIds);
        Map<String, String> finalTagId2NameMap = tagId2NameMap;
        List<E> result = employees.parallelStream().map(t -> {
            E e = MyBeanUtils.copy(t, supplier);
            List<String> jobIds = employeeId2JobIdListMap.get(t.getId());
            if (jobIds != null) {
                List<Integer> jobCodes = jobIds.parallelStream().map(jobId -> jobId2JobCodeMap.get(jobId))
                        .filter(Objects::nonNull).collect(Collectors.toList());
                try {
                    ReflectUtil.setFieldValue(e, "jobCodes", jobCodes);
                    ReflectUtil.setFieldValue(e, "jobIds", jobIds);
                } catch (Exception exception) {
                }
            }
            List<String> etagIds = t.getTagIds();
            if (etagIds != null) {
                List<String> tagNames =
                        etagIds.parallelStream().map(tagId -> finalTagId2NameMap.get(tagId)).collect(Collectors.toList());
                try {
                    ReflectUtil.setFieldValue(e, "tagNames", tagNames);
                    ReflectUtil.setFieldValue(e, "tagIds", etagIds);
                } catch (Exception exception) {
                }
            }
            return e;
        }).collect(Collectors.toList());
        return result;
    }

    private LambdaQueryWrapper<Employee> buildQuery(EmployeeQuery employeeQuery) {
        String jobId = employeeQuery.getJobId();
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        if (jobId != null) {
            Set<String> employeeIds = jobRelationService.lambdaQuery().select(JobEmployeeRelation::getEmployeeId)
                    .eq(JobEmployeeRelation::getJobId, jobId).list().parallelStream()
                    .map(JobEmployeeRelation::getEmployeeId).collect(Collectors.toSet());
            queryWrapper.in(CollectionUtil.isNotEmpty(employeeIds), Employee::getId, employeeIds);
        }
        queryWrapper.like(StringUtils.hasText(employeeQuery.getName()), Employee::getName, employeeQuery.getName())
                .like(StringUtils.hasText(employeeQuery.getMobile()), Employee::getMobile, employeeQuery.getMobile())
                .eq(StringUtils.hasText(employeeQuery.getCompanyId()), Employee::getCompanyId, employeeQuery.getCompanyId())
                .eq(employeeQuery.getStatus() != null, Employee::getStatus, employeeQuery.getStatus());
        if (StringUtils.hasText(employeeQuery.getParkOrgId())) {
            queryWrapper.eq(Employee::getParkOrgId, employeeQuery.getParkOrgId());

        } else {
            queryWrapper.in(Employee::getParkOrgId, employeeQuery.getParkOrgIds());
        }
        queryWrapper.orderByDesc(Employee::getCreateTime);
        return queryWrapper;
    }

}
