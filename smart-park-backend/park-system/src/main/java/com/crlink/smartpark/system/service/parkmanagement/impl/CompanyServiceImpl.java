package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.constants.enums.StatusEnum;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.contract.dto.ContractHouseStatusDTO;
import com.crlink.smartpark.contract.feign.ContractProvider;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.common.CacheUtil;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.company.CompanyBindUpdateDTO;
import com.crlink.smartpark.system.dto.company.CompanyQueryDTO;
import com.crlink.smartpark.system.dto.company.CompanySaveDTO;
import com.crlink.smartpark.system.entity.*;
import com.crlink.smartpark.system.enums.CompanyTaxLevelEnum;
import com.crlink.smartpark.system.mapper.CompanyMapper;
import com.crlink.smartpark.system.model.CompanyExcelExportDTO;
import com.crlink.smartpark.system.model.CompanyExcelImportDTO;
import com.crlink.smartpark.system.query.CompanyBindListQuery;
import com.crlink.smartpark.system.query.CompanyQuery;
import com.crlink.smartpark.system.service.CompanyInformationService;
import com.crlink.smartpark.system.service.common.CommonService;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.parkmanagement.*;
import com.crlink.smartpark.system.vo.*;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.DictConstant.INDUSTRY_FIELD;

/**
 * <p>
 * 入驻企业表 服务实现类
 * </p>
 * */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    /**
     * 物业公司type
     */
    public static final int PROPERTY_COMPANY_TYPE = 0;
    public static final int COMMON_COMPANY_TYPE = 1;

    @Autowired
    private OrganizationService organizationService;
    @Resource
    private CommonService commonService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeManageService employeeManageService;
    @Autowired
    private CompanyInformationService companyInformationService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private JobService jobService;

    @Autowired
    private TagService tagService;

    @Value("${picture.tmp.path:}")
    private String pictureTmpPath;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Company, ?> sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, ?> companyId2ValueMap = this.lambdaQuery().select(Company::getId, sValue).in(Company::getId, ids)
                .list().parallelStream().collect(Collectors.toMap(Company::getId, sValue));
        return companyId2ValueMap;
    }

    @Override
    public Map<String, Company> id2CompanyMap(Set<String> ids, SFunction<Company, ?>... sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        sValue = ArrayUtils.add(sValue, Company::getId);
        Map<String, Company> id2Company = this.lambdaQuery().select(sValue).in(Company::getId, ids).list()
                .parallelStream().collect(Collectors.toMap(Company::getId, Function.identity()));
        return id2Company;
    }

    @Override
    public Map<String, Company> code2CompanyMap(Set<String> codes, SFunction<Company, ?>... sValue) {
        if (CollectionUtil.isEmpty(codes)) {
            return Collections.emptyMap();
        }
        sValue = ArrayUtils.add(sValue, Company::getId);
        Map<String, Company> id2Company = this.lambdaQuery()
                .select(sValue)
                .in(Company::getCode, codes)
                .list().parallelStream().collect(Collectors.toMap(Company::getId, Function.identity()));
        return id2Company;
    }

    /**
     * 分页查询
     *
     * @param companyQuery 查询对象
     * @return 分页结果
     */
    @Override
    public PageInfo<CompanyListVO> listCompanyPage(CompanyQuery companyQuery) {
        if (StringUtils.isBlank(companyQuery.getParkOrgId())) {
            Set<String> parkOrgIds;
            if (SystemUtil.getInstance().getSource().equals(Constants.USER_BUSINESS_SOURCE)) {
                CompanyInformation companyInformation = companyInformationService.lambdaQuery().eq(CompanyInformation::getBusinessUserId, SystemUtil.getInstance().getCurrentUserId()).one();
                MyPageInfoUtils.isEmptyPage(companyInformation);
                if (companyInformation != null) {
                    companyQuery.setCode(companyInformation.getCode());
                }
                parkOrgIds = SystemUtil.getInstance().getTenantParkOrgIds();
            } else {
                parkOrgIds = CacheUtil.getInstance().getParkOrgIds();
            }
            companyQuery.setParkOrgIds(parkOrgIds);
        }
        companyQuery.startPage();
        List<CompanyListVO> list = baseMapper.listCompany(companyQuery);
        PageInfo<CompanyListVO> page = new PageInfo<>(list);
        return page;
    }

    @Override
    public void saveOrUpdateCompany(CompanySaveDTO companySaveDTO) {
        checkSaveParam(companySaveDTO);
        Company company = new Company();
        BeanUtils.copyProperties(companySaveDTO, company);
        //状态默认为启用
        company.setStatus(StatusEnum.ENABLE.code);
        company.setSource(DictConstant.AddDataSource.ADMIN_ADDED.getCode());
        EntityUserUtil.setAll(company, StringUtils.isBlank(companySaveDTO.getId()));
        AssertUtil.requireTrueOrDataExp(saveOrUpdate(company));
    }


    @Override
    public Boolean removeCompanyById(String id) {
        Employee employee = employeeService.lambdaQuery().select(Employee::getId).eq(Employee::getCompanyId, id)
                .last("limit 1").one();
        if (Objects.nonNull(employee)) {
            throw new ResolinkRuntimeException("30008");
        } else {
            Job job = jobService.lambdaQuery().select(Job::getId).eq(Job::getCompanyId, id).last("limit 1").one();
            if (Objects.nonNull(job)) {
                throw new ResolinkRuntimeException("30008");
            } else {
                Tag tag = tagService.lambdaQuery().select(Tag::getId).eq(Tag::getCompanyId, id).last("limit 1").one();
                if (Objects.nonNull(tag)) {
                    throw new ResolinkRuntimeException("30008");
                }
            }
        }
        return this.removeById(id);
    }

    /**
     * 修改企业状态
     *
     * @param statusReqDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCompanyStatus(UpdateStatusReqDTO statusReqDTO) {
        Integer status = statusReqDTO.getStatus();
        boolean result = this.lambdaUpdate().eq(Company::getId, statusReqDTO.getId())
                .set(Company::getStatus, status)
                .set(Company::getUpdateBy, SystemUtil.getInstance().getCurrentUserId())
                .update();

        List<Long> personIds = employeeService.lambdaQuery().select(Employee::getId, Employee::getDevicePersonId)
                .eq(Employee::getCompanyId, statusReqDTO.getId())
                .eq(StatusEnum.DISABLE.getCode().equals(status), Employee::getStatus, StatusEnum.ENABLE.getCode()).list()
                .stream().map(Employee::getDevicePersonId).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(personIds)) {
            employeeService.lambdaUpdate().eq(Employee::getCompanyId, statusReqDTO.getId())
                    .set(Employee::getStatus, status)
                    .set(Employee::getUpdateBy, SystemUtil.getInstance().getCurrentUserId())
                    .update();
            String errMsg = null;
            if (StatusEnum.ENABLE.getCode().equals(status)) {
                errMsg = employeeManageService.enableDevicePerson(personIds);
            } else if (StatusEnum.DISABLE.getCode().equals(status)) {
                errMsg = employeeManageService.unableDevicePerson(personIds);
            }
            if (Objects.nonNull(errMsg)) {
                throw new ResolinkRuntimeException(Message.code("60010", errMsg));
            }
        }
        return result;
    }

    @Override
    public CompanyVO getCompanyById(String id) {
        Company company = getById(id);
        if (company == null) {
            return null;
        }
        CompanyVO vo = new CompanyVO();
        BeanUtils.copyProperties(company, vo);
        return vo;
    }

    @Override
    public void updateHouseCount(String companyId, Integer count) {
        Company company = getById(companyId);
        if (company == null) {
            return;
        }
        Integer originCount = company.getHouseCount();
        Integer updateCount = originCount + count;
        company.setHouseCount(updateCount);
        EntityUserUtil.setUpdateBy(company);
        AssertUtil.requireTrueOrDataExp(updateById(company));
    }

    @Override
    public List<SimpleNode> listCompanyDictByParkOrgId(String parkOrgId, boolean filterUnBindClientInfo) {
        List<Company> list =
                this.list(Wrappers.<Company>lambdaQuery().select(Company::getId, Company::getName, Company::getCompanyType)
                        .eq(Company::getParkOrgId, parkOrgId).isNull(filterUnBindClientInfo, Company::getVisitorClientType));
        List<SimpleNode> nodes = list.parallelStream()
                .map(t -> new SimpleNode(t.getId(), t.getName(), t.getCompanyType())).collect(Collectors.toList());
        List<SimpleNode> result = SortUtils.pinYinSort(nodes, SimpleNode::getLabel);
        return result;
    }

    @Override
    public PageInfo<CompanyBindListVO> pageBindList(CompanyBindListQuery query) {
        Set<String> parkOrgIds = CacheUtil.getInstance().getParkOrgIds();
        if (CollectionUtil.isEmpty(parkOrgIds)) {
            return new PageInfo<>();
        }
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Company::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrRetEmpty());
        wrapper.orderByDesc(Company::getVisitorClientTime);
        wrapper.isNotNull(Company::getVisitorClientType);
        wrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), Company::getParkOrgId, query.getParkOrgId());
        wrapper.like(StringUtils.isNotBlank(query.getName()), Company::getName, query.getName());
        query.startPage();
        List<Company> list = list(wrapper);
        // appId字典值
        Integer appIdClientType = DictUtil.getValue(DictConstant.CompanyBindClientType.APPID);
        // h5字典值
//        Integer h5ClientType = DictUtil.getValue(DictConstant.CompanyBindClientType.H5);
        // 园区名map
        Map<String, String> parkNamesMap = commonService.getNamesByIdsAndType(
                list.stream().map(Company::getParkOrgId).collect(Collectors.toSet()), SimpleNode.TypeEnum.PARK.getCode());
        List<CompanyBindListVO> voList = MyBeanUtils.copyList(list, CompanyBindListVO::new, (s, t) -> {
            t.setParkOrgName(parkNamesMap.get(s.getParkOrgId()));
            t.setVisitorClientTypeLabel(
                    DictUtil.getLabel(DictConstant.CompanyBindClientType.class, s.getVisitorClientType()));
            if (appIdClientType.equals(s.getVisitorClientType())) {
                t.setVisitorClientValue(s.getVisitorClientAppid());
            }
//            else if (h5ClientType.equals(s.getVisitorClientType())) {
//                t.setVisitorClientValue(s.getVisitorClientUrl());
//            }
        });
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(voList);
        return pageInfo;
    }

    @Override
    public void updateClientInfo(CompanyBindUpdateDTO dto) {
        //重复添加验证
        if (dto.isSaveOperation()) {
            LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Company::getId, dto.getId());
            wrapper.isNotNull(Company::getVisitorClientType);
            AssertUtil.requireTrue(this.count(wrapper) <= 0, "70020");
        }

        Company company = getById(dto.getId());
        AssertUtil.requireNotNullOrParamExp(company);
        BeanUtils.copyProperties(dto, company);
        if (DictUtil.getValue(DictConstant.CompanyBindClientType.APPID).equals(dto.getVisitorClientType())) {
            company.setVisitorClientAppid(dto.getVisitorClientValue());
        }
//        else if (DictUtil.getValue(DictConstant.CompanyBindClientType.H5).equals(dto.getVisitorClientType())) {
//            company.setVisitorClientUrl(dto.getVisitorClientValue());
//        }
        if (dto.isSaveOperation()) {
            company.setVisitorClientTime(LocalDateTime.now());
        }
        AssertUtil.requireTrueOrDataExp(updateById(company));
    }

    @Override
    public void cleanClientBindInfo(String companyId) {
        Company company = getById(companyId);
        AssertUtil.requireNotNullOrParamExp(company);
        boolean updateResult = update(
                Wrappers.<Company>lambdaUpdate().eq(Company::getId, companyId).set(Company::getVisitorClientType, null));
        AssertUtil.requireTrueOrDataExp(updateResult);
    }

    @Override
    public List<DictVO> listCompanyDictByType(String parkOrgId, Integer companyType) {
        LambdaQueryChainWrapper<Company> wrapper = this.lambdaQuery();
        wrapper.select(Company::getId, Company::getName).eq(Company::getParkOrgId, parkOrgId)
                .eq(Company::getStatus, StatusEnum.ENABLE.code);
        if (Objects.nonNull(companyType)) {
            // 物业公司
            if (PROPERTY_COMPANY_TYPE == companyType) {
                wrapper.eq(Company::getCompanyType, PROPERTY_COMPANY_TYPE);
            }
            // 普通企业
            if (COMMON_COMPANY_TYPE == companyType) {
                wrapper.eq(Company::getCompanyType, COMMON_COMPANY_TYPE);
            }
        }
        List<DictVO> list =
                wrapper.list().parallelStream().map(t -> new DictVO(t.getName(), t.getId())).collect(Collectors.toList());
        List<DictVO> result = SortUtils.pinYinSort(list, DictVO::getLabel);
        return result;
    }

    @Override
    public List<DictVO> listParkDictByCode(String code) {
        Set<String> parkOrgIds = this.lambdaQuery().select(Company::getId, Company::getParkOrgId).eq(Company::getCode, code).list().parallelStream().map(Company::getParkOrgId).collect(Collectors.toSet());
        List<DictVO> result = null;
        if (CollectionUtil.isNotEmpty(parkOrgIds)) {
            List<Organization> organizationList = organizationService.lambdaQuery().eq(Organization::getStatus, StatusEnum.ENABLE.code).in(Organization::getId, parkOrgIds).list();
            List<DictVO> list =
                    organizationList.parallelStream().map(t -> new DictVO(t.getOrgName(), t.getId())).collect(Collectors.toList());
            result = SortUtils.pinYinSort(list, DictVO::getLabel);
        }
        return result;

    }

    @Override
    public List<DictVO> listPropertyCompanyDictByParkOrgId(String parkOrgId) {
        List<DictVO> list = this.lambdaQuery().select(Company::getId, Company::getName)
                .eq(Company::getCompanyType, PROPERTY_COMPANY_TYPE).eq(Company::getParkOrgId, parkOrgId)
                .eq(Company::getStatus, StatusEnum.ENABLE.code).list()
                .parallelStream().map(t -> new DictVO(t.getName(), t.getId())).collect(Collectors.toList());
        List<DictVO> result = SortUtils.pinYinSort(list, DictVO::getLabel);
        return result;
    }

    @Override
    public List<DictVO> curUserPropertyCompanyDict() {
        String mobile = SystemUtil.getInstance().getWxUserMobile();
        Set<String> companyIds =
                employeeService.lambdaQuery().select(Employee::getCompanyId).eq(Employee::getMobile, mobile).list()
                        .parallelStream().map(Employee::getCompanyId).collect(Collectors.toSet());

        List<DictVO> result = Lists.newArrayList();
        this.id2CompanyMap(companyIds, Company::getName, Company::getCompanyType).forEach((k, v) -> {
            if (v.getCompanyType().equals(PROPERTY_COMPANY_TYPE) && v.getStatus().equals(StatusEnum.ENABLE.code)) {
                result.add(new DictVO(v.getName(), k));
            }
        });
        return result;
    }

    @Override
    public Set<String> findIdsByName(String companyName) {
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Company::getId);
        wrapper.like(Company::getName, companyName);
        return list(wrapper).stream().map(Company::getId).collect(Collectors.toSet());
    }

    @Override
    public Set<String> findIdsByBusinessUserId(String businessUserId) {
        CompanyInformation companyInformation = companyInformationService.lambdaQuery().select(CompanyInformation::getCode).eq(CompanyInformation::getBusinessUserId, businessUserId).one();
        if (companyInformation == null) {
            return new HashSet<>();
        }
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Company::getId);
        wrapper.like(Company::getCode, companyInformation.getCode());
        return list(wrapper).stream().map(Company::getId).collect(Collectors.toSet());
    }

    @Override
    public CompanyClientInfoVO getClientBindInfo(String id) {
        Company company = getById(id);
        CompanyClientInfoVO vo = MyBeanUtils.copy(company, CompanyClientInfoVO::new);
        // appId字典值
        Integer appIdClientType = DictUtil.getValue(DictConstant.CompanyBindClientType.APPID);
        // h5字典值
//        Integer h5ClientType = DictUtil.getValue(DictConstant.CompanyBindClientType.H5);
        if (appIdClientType.equals(company.getVisitorClientType())) {
            vo.setVisitorClientValue(company.getVisitorClientAppid());
        }
//        else if (h5ClientType.equals(company.getVisitorClientType())) {
//            vo.setVisitorClientValue(company.getVisitorClientUrl());
//        }
        return vo;
    }

    public void checkSaveParam(CompanySaveDTO companySaveDTO) {
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Company::getName, companySaveDTO.getName()).eq(Company::getParkOrgId, companySaveDTO.getParkOrgId());
        Company existCompany = getOne(wrapper, false);
        if (existCompany != null && !existCompany.getId().equals(companySaveDTO.getId())) {
            throw new ResolinkRuntimeException("30001");
        }
        wrapper.clear();
        wrapper.eq(Company::getCode, companySaveDTO.getCode()).eq(Company::getParkOrgId, companySaveDTO.getParkOrgId());
        existCompany = getOne(wrapper, false);
        if (existCompany != null && !existCompany.getId().equals(companySaveDTO.getId())) {
            throw new ResolinkRuntimeException("30002");
        }
        AssertUtil.requireNotNullOrParamExp(organizationService.getById(companySaveDTO.getParkOrgId()));
    }

    @Override
    public Map<String, Map<String, Object>> queryHouseColumns(CompanyQueryDTO dto) {
        HashSet<String> columns = CollectionUtil.newHashSet(dto.getColumns());
        columns.add("id");
        QueryWrapper<Company> queryWrapper =
                Wrappers.<Company>query().select(columns.toArray(new String[columns.size()]))
                        .eq(StringUtils.isNotBlank(dto.getParkOrgId()), "park_org_id", dto.getParkOrgId())
                        .in(CollectionUtil.isNotEmpty(dto.getCompanyIds()), "id", dto.getCompanyIds());
        List<Map<String, Object>> listMaps = this.listMaps(queryWrapper);
        Map<String, Map<String, Object>> result = Maps.newHashMap();
        listMaps.forEach(t -> {
            String id = (String) t.get("id");
            result.put(id, t);
        });
        return result;
    }

    @Override
    public void export(CompanyQuery query) {
        if (StringUtils.isBlank(query.getParkOrgId())) {
            Set<String> parkOrgIds = CacheUtil.getInstance().getParkOrgIds();
            query.setParkOrgIds(parkOrgIds);
        }
        List<CompanyListVO> result = baseMapper.listCompany(query);
        String fileName = "入驻企业";
        ExcelExportUtils.export(() -> result, this::convertToExcel, fileName, CompanyExcelExportDTO.class);
    }

    @Override
    public CompanyOverviewDTO getCompanyOverview(String parkOrgId) {
        Future<BigDecimal> future = ThreadUtil.execAsync(() -> getHouseUseRate());
        List<Company> companies = this.lambdaQuery().select(Company::getId, Company::getCorporateTax, Company::getIndustryField)
                .eq(Company::getParkOrgId, parkOrgId).eq(Company::getStatus, StatusEnum.ENABLE.code).list();
        CompanyOverviewDTO dto = new CompanyOverviewDTO();
        Map<Integer, String> industryFieldMap = DictUtil.getLabels(INDUSTRY_FIELD);
        for (Integer key : industryFieldMap.keySet()) {
            dto.getIndustryType().put(key, BigDecimal.ZERO);
        }
        if (CollUtil.isEmpty(companies)) {
            return dto;
        }
        dto.setCompanyTotal(companies.size());
        Map<Integer, Long> taxGroup = companies.parallelStream()
                .collect(Collectors.groupingBy(t -> CompanyTaxLevelEnum.getTaxLevel(t.getCorporateTax()), Collectors.counting()));
        Map<Integer, BigDecimal> taxType = dto.getTaxType();
        taxGroup.forEach((k, v) -> {
            taxType.put(k, NumberUtil.div(v, dto.getCompanyTotal(), 4));
        });
        dto.setEmployeeTotal(queryEmployeeCount(companies));
        Map<Integer, Long> industryMap = companies.parallelStream().filter(t -> Objects.nonNull(t.getIndustryField()))
                .collect(Collectors.groupingBy(Company::getIndustryField, Collectors.counting()));
        Map<Integer, BigDecimal> industryType = dto.getIndustryType();
        industryMap.forEach((k, v) -> {
            industryType.put(k, NumberUtil.div(v, dto.getCompanyTotal(), 4));
        });
        BigDecimal occupancyRate = BigDecimal.ZERO;
        try {
            occupancyRate = future.get();
        } catch (Exception e) {
            log.error("入驻率计算异常", e);
        }
        dto.setOccupancyRate(occupancyRate);
        return dto;
    }

    private Integer queryEmployeeCount(Collection<Company> companies) {
        Set<String> companyIds = companies.parallelStream().map(Company::getId).collect(Collectors.toSet());
        Integer count = employeeService.lambdaQuery().eq(Employee::getStatus, StatusEnum.ENABLE.code).in(Employee::getCompanyId, companyIds).count();
        return count;
    }

    /**
     * 22.12.2
     * 入驻房间数：取值于“合同管理—房屋状态”为“已租”、“已售”房间总数加房屋属性”为“自用”的房间总数
     * <p>
     * 入驻率=入驻房间数/房间总数（“房屋属性”为“自用”、“租赁”和“销售”的房间总数；）
     *
     * @return
     */
    @Override
    public BigDecimal getHouseUseRate() {
        Integer houseTotal = houseService.lambdaQuery().count();
        //自用
        Integer selfUserTotal = houseService.lambdaQuery().eq(House::getHouseProperty, 0).count();
        //租赁属性房屋
        Set<String> houseIds = houseService.lambdaQuery().select(House::getId).eq(House::getHouseProperty, 1).list()
                .parallelStream().map(House::getId).collect(Collectors.toSet());
        Map<String, ContractHouseStatusDTO> relationMap = contractProvider.getContractHouseRelationFor3D(houseIds);
        AtomicInteger rented = new AtomicInteger(0);
        relationMap.forEach((houseId, relation) -> {
            Integer houseStatus = relation.getHouseStatus();
            if (Objects.nonNull(houseStatus) && (houseStatus == 2 || houseStatus == 4)) {
                rented.getAndIncrement();
            }
        });
        //已租+已售
        int rentedNum = rented.get();
        return houseTotal == 0 ? BigDecimal.ZERO : NumberUtil.div(BigDecimal.valueOf(selfUserTotal + rentedNum), houseTotal, 4);
    }

    @Override
    public FileImportErrorInfoDTO importCompany(MultipartFile file, String parkOrgId) {
        String filename = file.getOriginalFilename();
        if (filename.endsWith(".xls") || filename.endsWith(".xlsx") || filename.endsWith(".csv")) {
            ExcelImportRollbackListener<CompanyExcelImportDTO> importListener = new ExcelImportRollbackListener<>(CompanyExcelImportDTO.class,
                    //业务参数校验方法,并单行保存数据
                    t -> validAndOperateExcelDTO(t, parkOrgId),
                    null
            );
            ExcelImportUtils.importExcel(file, CompanyExcelImportDTO.class, importListener);
            return importListener.getImportResult();
        }
        throw new ResolinkRuntimeException("10014");
    }

    private void deleteFile(String employeeFileFolder) {
        ThreadUtil.execAsync(() -> {
            try {
                Thread.sleep(500);
                FileUtils.forceDelete(new File(employeeFileFolder));
                FileUtils.forceDelete(new File(employeeFileFolder + ".zip"));
            } catch (Exception e) {
                log.error("文件删除失败", e);
            }
        });
    }

    /**
     * 房屋导入校验
     *
     * @param dto
     * @param parkOrgId
     * @return
     */
    private String validAndOperateExcelDTO(CompanyExcelImportDTO dto, String parkOrgId) {
        dto.setParkOrgId(parkOrgId);
        //
        if (StringUtils.isNotBlank(dto.getCode()) && checkCompanyName(dto)) {
            return "同一园区企业名称已经存在！";
        }

        if (StringUtils.isNotBlank(dto.getCode()) && checkCompanyCode(dto)) {
            return "同一园区统一社会信用代码已经存在！";
        }
        Company company = MyBeanUtils.copy(dto, Company::new);
        //状态默认为启用
        company.setStatus(StatusEnum.ENABLE.code);
        company.setSource(DictConstant.AddDataSource.ADMIN_ADDED.getCode());
        EntityUserUtil.setAllDynamic(company);
        return this.saveOrUpdate(company) ? null : "插入数据失败！";
    }

    private boolean checkCompanyName(CompanyExcelImportDTO dto) {
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Company::getName, dto.getName()).eq(Company::getParkOrgId, dto.getParkOrgId());
        Company existCompany = getOne(wrapper, false);
        if (existCompany != null && !existCompany.getId().equals(dto.getId())) {
            return true;
        }
        return false;
    }

    private boolean checkCompanyCode(CompanyExcelImportDTO dto) {
        LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Company::getCode, dto.getCode()).eq(Company::getParkOrgId, dto.getParkOrgId());
        Company existCompany = getOne(wrapper, false);
        if (existCompany != null && !existCompany.getId().equals(dto.getId())) {
            return true;
        }
        return false;
    }


    private List<CompanyExcelExportDTO> convertToExcel(List<CompanyListVO> entities) {
        return MyBeanUtils.copyList(entities, CompanyExcelExportDTO::new, (s, t) -> {

        });
    }
}
