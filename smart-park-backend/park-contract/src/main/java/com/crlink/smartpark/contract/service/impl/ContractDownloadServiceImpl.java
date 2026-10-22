package com.crlink.smartpark.contract.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.crlink.itech.aiot.jeecgframework.poi.word.WordExportUtil;
import com.crlink.itech.aiot.jeecgframework.poi.word.entity.MyXWPFDocument;
import com.crlink.smartpark.common.dto.FileDTO;
import com.crlink.smartpark.common.feign.FileProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.contract.common.RentFeeConstants;
import com.crlink.smartpark.contract.dto.*;
import com.crlink.smartpark.contract.entity.Template;
import com.crlink.smartpark.contract.entity.TemplateLibrary;
import com.crlink.smartpark.contract.model.excel.ContractExportDTO;
import com.crlink.smartpark.contract.model.request.ContractDownloadDTO;
import com.crlink.smartpark.contract.model.request.ContractGenerateReqDTO;
import com.crlink.smartpark.contract.service.ContractDownloadService;
import com.crlink.smartpark.contract.service.ContractService;
import com.crlink.smartpark.contract.service.TemplateLibraryService;
import com.crlink.smartpark.contract.service.TemplateService;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.MapBeanUtil;
import com.crlink.smartpark.core.utils.NumberToCN;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.DATE_FORMATTER;
import static com.crlink.smartpark.common.constants.Constants.DATE_PATTERN_CN;
import static com.crlink.smartpark.common.constants.DictConstant.ContractType.*;

/**
 * @ClassName: ContractDownloadServiceImpl
 * @Package com.crlink.smartpark.contract.service.impl
 * @Description: 合同下载服务类
 * @Version 1.0
 */
@Service
@Slf4j
public class ContractDownloadServiceImpl implements ContractDownloadService {

    @Autowired
    private FileProvider fileProvider;

    @Autowired
    private ContractService contractService;

    @Autowired
    private TemplateLibraryService templateLibraryService;

    @Autowired
    private TemplateService templateService;

    public void download(ContractDownloadDTO downloadDto) {
        // downloadDto.setKey ("dmMtdXBsb2FkLTE2NDkzMTY2NjUyMDYtMTlfMTY0OTMxNzgwOTQxOQ==");
        byte[] fileByte = fileProvider.downloadByte(downloadDto.getKey());
        String fileOriginalName = fileProvider.getFileDetailByKeys(Sets.newHashSet(downloadDto.getKey())).getData()
                .get(downloadDto.getKey()).getFileOriginalName();
        String fileName = fileOriginalName.substring(0, fileOriginalName.lastIndexOf("."));
        InputStream is = new ByteArrayInputStream(fileByte);
        XWPFDocument document = new XWPFDocument();
        try {
            // is = POICacheManager.getFile(url);
            document = new MyXWPFDocument(is);
            WordExportUtil.exportWord07(document, getMap(downloadDto.getContractId(), downloadDto.getType()));

            // 输出
            HttpServletResponse response = buildResponse(fileName);
            OutputStream os = response.getOutputStream();
            if (is != null) {
                // ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // document.write (baos);
                // IOUtils.copy(document.getDocument ().newInputStream (), os);
                document.write(os);
            }
            // File savefile = new File("d:\\daima\\poi\\");
            // if (!savefile.exists()) {
            // savefile.mkdirs();
            // }
            // FileOutputStream fos = new FileOutputStream(savefile +imgFileName);
            // document.write(fos);
            // fos.close();
            //
        } catch (Exception e) {
            log.error("【文件解析失败】，key:{}", downloadDto.getKey(), e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                log.error("【文件关闭失败】，key:{}", downloadDto.getKey(), e);
            }
        }

    }

    @Override
    public Boolean generateContract(ContractGenerateReqDTO reqDTO) {
        Template template = templateService.getById(reqDTO.getContractTemplateId());
        ContractDTO contract = contractService.getContractById(reqDTO.getContractId());
        String templateKey = template.getTemplateKey();
        Map<String, Object> objectMap = convert(contract, contract.getType());
        byte[] fileByte = fileProvider.downloadByte(templateKey);
        String fileOriginalName = fileProvider.getFileDetailByKeys(Sets.newHashSet(templateKey)).getData()
                .get(templateKey).getFileOriginalName();
        String fileName = fileOriginalName.substring(0, fileOriginalName.lastIndexOf("."));
        InputStream is = new ByteArrayInputStream(fileByte);
        try {
            XWPFDocument document = new MyXWPFDocument(is);
            WordExportUtil.exportWord07(document, objectMap);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            document.write(os);
            String uploadKey = fileProvider.feignUploadBytes(new FileDTO().setBytes(os.toByteArray()).setFileOriginalName(fileOriginalName));
            templateService.lambdaUpdate().eq(Template::getId, reqDTO.getContractTemplateId())
                    .set(Template::getContractKey, uploadKey)
                    .set(Template::getContractKeyTime, LocalDateTime.now())
                    .set(Template::getUpdateBy, SystemUtil.getInstance().getCurrentUserId()).update();
        } catch (Exception e) {
            log.error("合同生成失败,合同信息：{}，模板信息：{}", contract, template, e);
            throw new ResolinkRuntimeException("300018");
        }
        return true;
    }


    @NotNull
    public static HttpServletResponse buildResponse(String fileName) {
        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        // 配置文件下载
        response.setHeader("content-type", MediaType.APPLICATION_OCTET_STREAM_VALUE);

        // 下载文件能正常显示中文
        response.setHeader("Content-disposition", "attachment;filename=" + buildFileName(fileName) + ".docx");

        return response;
    }

    @NotNull
    private static String buildFileName(String fileName) {
        try {
            fileName = URLEncoder.encode(fileName + LocalDateTime.now().format(DATE_FORMATTER),
                    StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("文件名编码异常", e);
        }
        return fileName;
    }

    // getMap
    private Map<String, Object> getMap(String id, String type) {

        ContractDTO contractDTO = contractService.getContractById(id);
        if (contractDTO != null) {
            ContractExportDTO exportDTO = new ContractExportDTO();
            BeanUtils.copyProperties(contractDTO, exportDTO);
            if (contractDTO.getExtension() != null) {
                BeanUtils.copyProperties(contractDTO.getExtension(), exportDTO);
                BeanUtils.copyProperties(contractDTO.getExtension(), exportDTO);
            }
            // 处理其他数据
            if (contractDTO.getHouseMessages() != null) {
                exportDTO.setHouseMessagesStr(getHouseStr(contractDTO.getHouseMessages()));
                exportDTO.setTotalFloorList(getTotalFloorStr(contractDTO.getHouseMessages()));
            }
        }
        Map<String, Object> mapObject = MapBeanUtil.object2Map(contractDTO);

        List<TemplateLibraryDTO> libraryDTO = templateLibraryService.listTemplateLibraryByType(type);


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = format.format(new Date());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("totalpreyear", "2660");
        map.put("totalthisyear", "3400");
        map.put("编号", "ht-1234-7890");
        map.put("甲方", "淮安智慧谷");
        map.put("邮编", "514200");
        map.put("电话", "1347181805");
        map.put("甲方联系人", "林总");
        map.put("乙方", "华润数科有限公司");
        map.put("乙方联系人", "林波总");
        map.put("乙方联系电话", "1347180005");
        map.put("乙方联系人身份证号码", "51420013471800051347180005");
        map.put("电话(年)", "1347181805");
        map.put("关联房屋", "2栋A座 1102");
        map.put("房屋/用途", "商用");
        map.put("建筑面积", "1228.20");
        map.put("合同开始日期", curTime);
        map.put("合同结束日期", LocalDate.now().plusDays(30));
        map.put("签约日期", LocalDateTime.now().plusDays(-30));
        map.put("type", "增值税");
        map.put("presum", "10801");

        map.put("thissum", "1650");
        map.put("curmonth", "1-12月");
        map.put("curmonth1", "1-11月");
        map.put("now", curTime);
        return map;
    }

    private Map<String, Object> convert(ContractDTO contract, Integer contractType) {
        List<TemplateLibrary> list = templateLibraryService.lambdaQuery()
                .select(TemplateLibrary::getLabel, TemplateLibrary::getFieldName, TemplateLibrary::getType)
                .eq(TemplateLibrary::getGroupIds, contractType).list();
        Map<String, Object> map = new HashMap<String, Object>();
        for (TemplateLibrary library : list) {
            if (Objects.equals("betweendate", library.getType())) {
                Object startDate = ReflectUtil.getFieldValue(contract, "startDate");
                String startDateStr = ((LocalDate) startDate).format(DateTimeFormatter.ofPattern(DATE_PATTERN_CN));
                Object endDate = ReflectUtil.getFieldValue(contract, "endDate");
                String endDateStr = ((LocalDate) endDate).format(DateTimeFormatter.ofPattern(DATE_PATTERN_CN));
                map.put(library.getLabel(), startDateStr + "-" + endDateStr);
            } else if (Objects.equals("calculate", library.getType())) {
                // 增值税  取值于收费项为“租金”的含税总金额-不含税总金额
                List<ContractBillingPlanDTO> planDTOS = contract.getContractBillingPlanDTOS();
                if (CollUtil.isNotEmpty(planDTOS)) {
//                    ContractBillingPlanDTO planDTO = planDTOS.get(0);
//                    Integer formulaType = planDTO.getFormulaType();
//                    BigDecimal val = formulaType == 1 ? planDTO.getFixedAmountWithTax().subtract(planDTO.getFixedAmountWithoutTax()) :
//                            planDTO.getAmountWithTax().subtract(planDTO.getAmountWithoutTax());
                    BigDecimal val = getCalculate(contractType, planDTOS);
                    map.put(library.getLabel(), val);
                }
            } else if (Objects.equals("list", library.getType())) {
                List<HouseMessage> houseMessages = contract.getHouseMessages();
                if (CollUtil.isEmpty(houseMessages)) {
                    continue;
                }
                if (Objects.equals("houseMessagesList", library.getFieldName())) {
                    String houseInfo = houseMessages.stream().map(t -> t.getBuildingName() + "-" + t.getHouseName()).collect(Collectors.joining(";"));
                    map.put(library.getLabel(), houseInfo);
                    if (contractType.equals(RENTAL_CONTRACT.getCode())) {
                        map.put(library.getLabel(), contract.getParkOrgName() + houseInfo);
                    }
                } else if (Objects.equals("houseFunctionList", library.getFieldName())) {
                    //产品要求房屋功能去重
                    String houseFunction = houseMessages.stream().distinct().filter(t -> StringUtils.isNotBlank(t.getHouseFunction()))
                            .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(HouseMessage::getHouseFunction))), ArrayList::new)).stream()
                            .map(t -> DictUtil.getLabelByTypeAndValue("HOUSE_FUNCTION", Integer.parseInt(t.getHouseFunction()))).collect(Collectors.joining(";"));
                    map.put(library.getLabel(), houseFunction);
                } else if (Objects.equals("totalFloorList", library.getFieldName())) {
//                    String totalFloorList = houseMessages.stream().map(t -> t.getBuildingName() + "-" + t.getTotalFloor()).collect(Collectors.joining(";"));
                    //产品要求楼栋去重
                    String totalFloorList = houseMessages.stream().collect(Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(HouseMessage::getBuildingName))), ArrayList::new)).stream()
                            .map(t -> t.getBuildingName() + "-" + t.getTotalFloor()).collect(Collectors.joining(";"));
                    map.put(library.getLabel(), totalFloorList);
                } else if (Objects.equals("floorList", library.getFieldName())) {
                    String floorList = houseMessages.stream().map(t -> t.getHouseName() + "-" + t.getFloor()).collect(Collectors.joining(";"));
                    map.put(library.getLabel(), floorList);
                }
            } else {
                Field field = ReflectUtil.getField(ContractDTO.class, library.getFieldName());
                Object fieldValue = ReflectUtil.getFieldValue(contract, field);
                if (Objects.isNull(field)) {
                    field = ReflectUtil.getField(Extension.class, library.getFieldName());
                    fieldValue = ReflectUtil.getFieldValue(contract.getExtension(), field);
                }
                if (Objects.isNull(field) && CollUtil.isNotEmpty(contract.getContractBillingPlanDTOS())) {
                    fieldValue = ReflectUtil.getFieldValue(contract.getContractBillingPlanDTOS().get(0), library.getFieldName());
                }
                if (Objects.isNull(fieldValue)) {
                    map.put(library.getLabel(), "");
                    continue;
                }
                if (Objects.equals("varchar", library.getType())) {
                    map.put(library.getLabel(), fieldValue);
                    if (Objects.equals("taxRate", library.getFieldName())) {
                        map.put(library.getLabel(), fieldValue + "%");
                    }
                } else if (Objects.equals("money", library.getType())) {
                    String valStr = new StringBuilder().append(fieldValue).append(" (大写").append(NumberToCN.number2CNMontrayUnit((BigDecimal) fieldValue)).append(")").toString();
//                    String s = NumberToCN.number2CNMontrayUnit((BigDecimal) fieldValue);
                    map.put(library.getLabel(), valStr);
                } else if (Objects.equals("number", library.getType())) {
                    String s = fieldValue.toString();
                    if (contractType.equals(RENTAL_CONTRACT.getCode())) {
                        s = NumberToCN.number2Capitalized(fieldValue.toString());
                    }
                    map.put(library.getLabel(), s);
                } else if (Objects.equals("date", library.getType())) {
                    String date = ((LocalDate) fieldValue).format(DateTimeFormatter.ofPattern(DATE_PATTERN_CN));
                    map.put(library.getLabel(), date);
                }
            }
        }
        log.info("word解析的映射：{}", map);
        return map;
    }


    private String getHouseStr(List<HouseMessage> houseMessages) {

        Map<String, List<HouseMessage>> map =
                houseMessages.stream().collect(Collectors.groupingBy(HouseMessage::getBuildingName));
        List<String> listReturns = new ArrayList<>();
        map.forEach((k, v) -> {
            Set<String> list = v.parallelStream().map(HouseMessage::getHouseName).collect(Collectors.toSet());
            listReturns.add(k + " " + StringUtils.join(list.toArray(), "、"));
        });

        return StringUtils.join(listReturns.toArray(), ";");
    }

    private String getTotalFloorStr(List<HouseMessage> houseMessages) {

        Map<String, List<HouseMessage>> map =
                houseMessages.stream().collect(Collectors.groupingBy(HouseMessage::getBuildingName));
        List<String> listReturns = new ArrayList<>();
        map.forEach((k, v) -> {

            listReturns.add(k + " " + v.get(0).getTotalFloor());
        });

        return StringUtils.join(listReturns.toArray(), ";");
    }


    /**
     * 物业合同-物业费收费标准：取收费项为“物业费”的第一个计费方案
     * 公寓合同-增值税：取收费项为“公寓租金”的第一个计费方案的的含税总金额-不含税总金额
     * 租赁合同-租赁费用标准：取收费项为“房屋租金”的第一个计费方案
     *
     * @param contractType
     * @param planDTOS
     * @return
     */
    private BigDecimal getCalculate(Integer contractType, List<ContractBillingPlanDTO> planDTOS) {
        if (contractType.equals(APARTMENT_CONTRACT.getCode())) {
            return convertPlanDTOList(contractType, planDTOS,
                    RentFeeConstants.ContractTypeToSchemeFeeType.APARTMENT_CONTRACT_1.getCalculationValues());
        } else if (contractType.equals(RENTAL_CONTRACT.getCode())) {
            return convertPlanDTOList(contractType, planDTOS,
                    RentFeeConstants.ContractTypeToSchemeFeeType.RENTAL_CONTRACT_1.getCalculationValues());
        } else if (contractType.equals(PROPERTY_CONTRACT.getCode())) {
            return convertPlanDTOList(contractType, planDTOS,
                    RentFeeConstants.ContractTypeToSchemeFeeType.PROPERTY_CONTRACT.getCalculationValues());
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal convertPlanDTOList(Integer contractType, List<ContractBillingPlanDTO> planDTOS, Set<Integer> calculationValues) {
        BigDecimal total = planDTOS.stream().filter(t -> Objects.nonNull(t.getChargeItemMessage())
                && calculationValues.contains(t.getChargeItemMessage().getFeeType()))
                .map(t -> t.getAmountWithTax().subtract(t.getAmountWithoutTax())).reduce(BigDecimal.ZERO, BigDecimal::add);
        for (ContractBillingPlanDTO t : planDTOS) {
            if (Objects.nonNull(t.getChargeItemMessage()) && calculationValues.contains(t.getChargeItemMessage().getFeeType())) {
                BigDecimal calculate = t.getAmountWithTax().subtract(t.getAmountWithoutTax());
                if (contractType.equals(APARTMENT_CONTRACT.getCode())) {
                    calculate = total;
                }
                t.setTaxRate(t.getChargeItemMessage().getTaxRate());
                planDTOS.set(0, t);
                return calculate;
            }
        }
        return BigDecimal.ZERO;
    }

}
