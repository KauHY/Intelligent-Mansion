package com.crlink.smartpark.business.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.business.dto.CustomerDistributionReqDTO;
import com.crlink.smartpark.business.dto.CustomerInfoDTO;
import com.crlink.smartpark.business.dto.CustomerInfoWxReqDTO;
import com.crlink.smartpark.business.dto.chance.AnalysisOverview;
import com.crlink.smartpark.business.dto.chance.BusinessStatisticQuery;
import com.crlink.smartpark.business.entity.CustomerInfo;
import com.crlink.smartpark.business.mapper.CustomerInfoMapper;
import com.crlink.smartpark.business.model.excel.CustomerInfoExcelVO;
import com.crlink.smartpark.business.model.excel.CustomerInfoImportDTO;
import com.crlink.smartpark.business.query.CustomerInfoQuery;
import com.crlink.smartpark.business.service.CustomerInfoService;
import com.crlink.smartpark.business.service.CustomerTagService;
import com.crlink.smartpark.business.vo.CustomerInfoListVO;
import com.crlink.smartpark.business.vo.CustomerInfoVO;
import com.crlink.smartpark.business.vo.CustomerInfoWxVO;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.SMALL_DATE_FORMAT;

/**
 * <p>
 * 商机管理—客户表 服务实现类
 * </p>
 * */
@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements CustomerInfoService {

    public static final String CUSTOMER_CODE_PREFIX = "XF";
    public static final int MY_CUSTOMER = 1;
    public static final String FILE_NAME = "客户列表";

    @Autowired
    CustomerTagService customerTagService;

    @Autowired
    CommonProvider commonProvider;

    @Autowired
    EmployeeProvider employeeProvider;

    @Autowired
    NumberProvider numberProvider;

    /**
     * 后台新增、编辑
     *
     * @param dto 存储对象
     * @return 存储对象
     */

    @Override
    public Boolean saveOrUpdateCustomer(CustomerInfoDTO dto) {
        String id = dto.getId();
        checkCustomerNameAndCode(id, dto.getName(), dto.getCode(), dto.getParkOrgId());
        CustomerInfo customerInfo = MyBeanUtils.copy(dto, CustomerInfo::new);
        getCustomerCode(customerInfo);
        EntityUserUtil.setAllDynamic(customerInfo);
        return this.saveOrUpdate(customerInfo);
    }


    /**
     * 小程序新增、编辑
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean saveOrUpdateWxCustomer(CustomerInfoWxReqDTO dto) {
        String id = dto.getId();
        checkCustomerNameAndCode(id, dto.getName(), dto.getCode(), dto.getParkOrgId());
        CustomerInfo customerInfo = MyBeanUtils.copy(dto, CustomerInfo::new);
        getCustomerCode(customerInfo);
        //捞出客户标签id,业务联系人，防止置空
        if (StringUtils.isNotBlank(id)) {
            CustomerInfo entity = this.lambdaQuery().select(
                    CustomerInfo::getId, CustomerInfo::getCustomerTagId, CustomerInfo::getBusinessContactsList)
                    .eq(CustomerInfo::getId, id).one();
            customerInfo.setCustomerTagId(entity.getCustomerTagId());
            customerInfo.setBusinessContactsList(entity.getBusinessContactsList());
        }
        EntityUserUtil.setAllDynamic(customerInfo);
        return this.saveOrUpdate(customerInfo);
    }


    /**
     * 后台通过id查询客户
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    @Id2objHandle
    public CustomerInfoVO getCustomerInfoById(String id) {
        CustomerInfo customerInfo = this.getById(id);
        CustomerInfoVO customerInfoVO = MyBeanUtils.copy(customerInfo, CustomerInfoVO::new);
        return customerInfoVO;
    }

    /**
     * 小程序通过id查询客户
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    @Id2objHandle
    public CustomerInfoWxVO getWxCustomerInfoById(String id) {
        CustomerInfo customerInfo = this.getById(id);
        return MyBeanUtils.copy(customerInfo, CustomerInfoWxVO::new);
    }

    /**
     * 后台分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<CustomerInfoListVO> listCustomerPage(CustomerInfoQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getParkOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryChainWrapper<CustomerInfo> wrapper = getConditionWrapper(query, parkOrgIds, false);
        query.startPage();
        List<CustomerInfo> list = wrapper.list();
        MyPageInfoUtils.isEmptyPage(list);
        List<CustomerInfoListVO> result = customerInfo2CustomerInfoVO(list, CustomerInfoListVO::new, false);
        return query.createPageInfo(result);
    }


    /**
     * 小程序分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    @Override
    public PageInfo<CustomerInfoWxVO> listCustomerWxPage(CustomerInfoQuery query) {
        LambdaQueryWrapper<CustomerInfo> wrapper = getWxConditionWrapper(query);
        query.startPage();
        List<CustomerInfo> list = this.list(wrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<CustomerInfoWxVO> result = MyBeanUtils.copyList(list, CustomerInfoWxVO::new);
        return query.createPageInfo(result);
    }

    @Override
    public Boolean removeCustomerInfo(String id) {
        return this.removeById(id);
    }

    @Override
    public Boolean customerDistribution(CustomerDistributionReqDTO dto) {
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setId(dto.getCustomerId());
        customerInfo.setEmployeeId(dto.getEmployeeId());
        return this.updateById(customerInfo);
    }


    /**
     * 客户导出
     *
     * @param customerInfoQuery
     */
    @Override
    public void export(CustomerInfoQuery customerInfoQuery) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(customerInfoQuery.getParkOrgId());
        LambdaQueryChainWrapper<CustomerInfo> wrapper = getConditionWrapper(customerInfoQuery, parkOrgIds, true);
        ExcelExportUtils.export(wrapper::list, sourceList ->
                        customerInfo2CustomerInfoVO(sourceList, CustomerInfoExcelVO::new, true),
                FILE_NAME, CustomerInfoExcelVO.class);
    }


    /**
     * 客户导入
     *
     * @param file
     * @param parkOrgId
     * @return
     */
    @Override
    public FileImportErrorInfoDTO importCustomerInfo(MultipartFile file, String parkOrgId) {
        String filename = file.getOriginalFilename();
        if (filename.endsWith(".xls") || filename.endsWith(".xlsx") || filename.endsWith(".csv")) {
            ExcelImportRollbackListener<CustomerInfoImportDTO> importListener = new ExcelImportRollbackListener<>(CustomerInfoImportDTO.class,
                    //业务参数校验方法,并单行保存数据
                    t -> validAndOperateExcelDTO(t, parkOrgId),
                    null
            );
            ExcelImportUtils.importExcel(file, CustomerInfoImportDTO.class, importListener);
            return importListener.getImportResult();
        }
        throw new ResolinkRuntimeException("10014");
    }


    /**
     * 商机分析-客户行业分析
     *
     * @param query
     * @return
     */
    @Override
    public AnalysisOverview getCustomerIndustry(BusinessStatisticQuery query) {
        AnalysisOverview result = new AnalysisOverview();
        Map<Integer, String> dictTypeMaps = DictUtil.getLabels("INDUSTRY_FIELD");
        Map<String, Long> businessData = new LinkedHashMap<>();
        result.setBusinessData(businessData);
//        result.setTotalAmount(0);
        dictTypeMaps.forEach((k, v) -> businessData.put(v, 0L));
        LambdaQueryChainWrapper<CustomerInfo> queryWrapper = this.lambdaQuery();
        queryWrapper.select(CustomerInfo::getId, CustomerInfo::getIndustryField)
                .eq(CustomerInfo::getParkOrgId, query.getParkOrgId())
                .isNotNull(CustomerInfo::getIndustryField);
        if (Objects.nonNull(query.getCreateTimeStart()) && Objects.nonNull(query.getCreateTimeEnd())) {
            queryWrapper.between(CustomerInfo::getCreateTime, query.getCreateTimeStart(), query.getCreateTimeEnd());
        }
        List<CustomerInfo> customerInfoList = queryWrapper.list();
        if (CollectionUtil.isNotEmpty(customerInfoList)) {
            result.setTotalAmount(customerInfoList.size());
            Map<Integer, Long> collectCountMap = customerInfoList.stream().collect(
                    Collectors.groupingBy(CustomerInfo::getIndustryField, Collectors.counting()));
//            collectCountMap.forEach(businessData::put);
            collectCountMap.forEach((k, v) -> businessData.put(dictTypeMaps.get(k), v));
        }
        return result;
    }

    /**
     * 校验要导入的数据
     * v12导入只作新增
     *
     * @param dto
     * @param parkOrgId
     * @return
     */
    private String validAndOperateExcelDTO(CustomerInfoImportDTO dto, String parkOrgId) {
        dto.setParkOrgId(parkOrgId);
        CustomerInfo afterCustomerInfo = MyBeanUtils.copy(dto, CustomerInfo::new);
        checkCustomerNameAndCode(null, afterCustomerInfo.getName(), afterCustomerInfo.getCode(), parkOrgId);
        EntityUserUtil.setAll(afterCustomerInfo);
        //新增数据时获取编号
        getCustomerCode(afterCustomerInfo);
        return this.saveOrUpdate(afterCustomerInfo) ? null : "插入数据失败！";
    }

    /**
     * 同一园区下，若客户名称相同、但统一社会信用代码不相同，则以系统已有数据为准，对应行将不做导入处理。
     *
     * @param dto
     * @param parkOrgId
     * @return
     */
    private Boolean checkName(CustomerInfoImportDTO dto, String parkOrgId) {
        CustomerInfo oneByName = this.lambdaQuery()
                .select(CustomerInfo::getId, CustomerInfo::getCode, CustomerInfo::getName)
                .eq(CustomerInfo::getName, dto.getName())
                .eq(CustomerInfo::getParkOrgId, parkOrgId)
                .last("limit 1").one();
        if (Objects.nonNull(oneByName)) {
            return false;
        }
        return true;
    }

    /**
     * 同一园区下，若统一社会信用代码相同，则以待导入的数据为准更新系统已有数据。
     *
     * @param entity
     * @param parkOrgId
     * @return
     */
    private Boolean checkCode(CustomerInfo entity, String parkOrgId) {
        CustomerInfo oneByCode = this.lambdaQuery()
                .select(CustomerInfo::getId, CustomerInfo::getCode, CustomerInfo::getName)
                .eq(CustomerInfo::getCode, entity.getCode())
                .eq(CustomerInfo::getParkOrgId, parkOrgId)
                .last("limit 1").one();
        if (Objects.nonNull(oneByCode)) {
            entity.setId(oneByCode.getId());
            return true;
        }
        return false;
    }

    /**
     * 后台查询条件
     *
     * @param condition 前端查询条件对象
     * @return 查询条件
     */
    private LambdaQueryChainWrapper<CustomerInfo> getConditionWrapper(CustomerInfoQuery condition, Set<String> parkOrgIds, Boolean isExport) {
        LambdaQueryChainWrapper<CustomerInfo> queryWrapper = this.lambdaQuery();
        if (!isExport) {
            queryWrapper.select(CustomerInfo::getId, CustomerInfo::getCustomerCode, CustomerInfo::getParkOrgId, CustomerInfo::getCode,
                    CustomerInfo::getName, CustomerInfo::getCustomerTagId, CustomerInfo::getType, CustomerInfo::getStatus,
                    CustomerInfo::getContacts, CustomerInfo::getContactsTel, CustomerInfo::getCreateTime);
        }
        queryWrapper.like(StringUtils.isNotBlank(condition.getName()), CustomerInfo::getName, condition.getName())
                .in(CustomerInfo::getParkOrgId, parkOrgIds)
                .orderByDesc(CustomerInfo::getCreateTime);
        return queryWrapper;
    }

    /**
     * 小程序查询条件
     *
     * @param condition 前端查询条件对象
     * @return 查询条件
     */
    private LambdaQueryWrapper<CustomerInfo> getWxConditionWrapper(CustomerInfoQuery condition) {
        String currentEmployeeId = SystemUtil.getInstance().getEmployeeId();
        String parkOrgId = SystemUtil.getInstance().getParkOrgIdByEmployeeId();
        LambdaQueryWrapper<CustomerInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CustomerInfo::getId, CustomerInfo::getCustomerCode, CustomerInfo::getParkOrgId,
                CustomerInfo::getCode, CustomerInfo::getName, CustomerInfo::getType, CustomerInfo::getStatus,
                CustomerInfo::getContacts, CustomerInfo::getContactsTel, CustomerInfo::getCreateTime)
                .eq(CustomerInfo::getParkOrgId, parkOrgId);
        if (Objects.nonNull(condition.getDataType()) && condition.getDataType().equals(MY_CUSTOMER)) {
            queryWrapper.eq(CustomerInfo::getEmployeeId, currentEmployeeId);
        }
        queryWrapper.eq(Objects.nonNull(condition.getType()), CustomerInfo::getType, condition.getType())
                .eq(Objects.nonNull(condition.getStatus()), CustomerInfo::getStatus, condition.getStatus())
                .orderByDesc(CustomerInfo::getCreateTime);
        return queryWrapper;
    }


    /**
     * 检验客户名称及统一社会信用代码是否重复
     * 同一园区内名称不允许重复，需进行唯一性校验
     * 同一园区内的企业同一社会信用代码不允许重复，需进行唯一性校验
     *
     * @param id
     * @param name
     * @param code
     * @param parkOrgId
     */
    private void checkCustomerNameAndCode(String id, String name, String code, String parkOrgId) {
        Integer nameCount = this.lambdaQuery()
                .ne(StringUtils.isNotBlank(id), CustomerInfo::getId, id)
                .eq(CustomerInfo::getName, name)
                .eq(CustomerInfo::getParkOrgId, parkOrgId)
                .count();
        if (nameCount > 0) {
            throw new ResolinkRuntimeException(Message.code("90004"));
        }
        Integer codeCount = this.lambdaQuery().ne(StringUtils.isNotBlank(id), CustomerInfo::getId, id)
                .eq(CustomerInfo::getCode, code)
                .eq(CustomerInfo::getParkOrgId, parkOrgId)
                .count();
        if (codeCount > 0) {
            throw new ResolinkRuntimeException(Message.code("90006"));
        }
    }


    private <T> List<T> customerInfo2CustomerInfoVO(List<CustomerInfo> list, Supplier<T> supplier, Boolean IsExport) {
        Set<String> tagIds = list.parallelStream().filter(Objects::nonNull)
                .map(CustomerInfo::getCustomerTagId).collect(Collectors.toSet());
        Map<String, String> tagIds2NameMap = customerTagService.getCustomerTagNames(tagIds);
        Map<String, String> parkOrgNameMap = null;
        if (IsExport) {
            Set<String> parkOrgIds = list.parallelStream().map(CustomerInfo::getParkOrgId).collect(Collectors.toSet());
            parkOrgNameMap = commonProvider.getNamesByIdsAndType(parkOrgIds, SimpleNode.TypeEnum.PARK.getCode());
        }
        Map<String, String> finalParkOrgNameMap = parkOrgNameMap;
        List<T> result = MyBeanUtils.copyList(list, supplier, (s, t) -> {
            MetaObject meta = SystemMetaObject.forObject(t);
            try {
                String customerTagId = s.getCustomerTagId();
                meta.setValue("tel", s.getContactsTel());
                if (StringUtils.isNotBlank(customerTagId) && CollectionUtil.isNotEmpty(tagIds2NameMap)) {
                    meta.setValue("tagName", tagIds2NameMap.get(customerTagId));
                }
                if (CollectionUtil.isNotEmpty(finalParkOrgNameMap)) {
                    meta.setValue("parkOrgName", finalParkOrgNameMap.get(s.getParkOrgId()));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        return result;
    }


    /**
     * 获取客户编码
     *
     * @param customerInfo
     */
    private void getCustomerCode(CustomerInfo customerInfo) {
        if (StringUtils.isBlank(customerInfo.getId())) {
            //客户编码
            NumberSeqDTO numberSeq = numberProvider.getNumberSeq(CUSTOMER_CODE_PREFIX);
            String customerCode = new StringBuilder(CUSTOMER_CODE_PREFIX).append(numberSeq.getDateTime().format(DateTimeFormatter.ofPattern(SMALL_DATE_FORMAT)))
                    .append(numberSeq.getValue()).toString();
            customerInfo.setCustomerCode(customerCode);
        }
    }
}
