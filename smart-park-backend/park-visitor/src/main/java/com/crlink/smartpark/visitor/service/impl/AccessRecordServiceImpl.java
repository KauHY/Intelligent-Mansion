package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.attendance.dto.AttendanceExchangeHolidayDTO;
import com.crlink.smartpark.attendance.dto.AttendanceRuleDTO;
import com.crlink.smartpark.attendance.feign.AttendanceExchangeHolidayProvider;
import com.crlink.smartpark.attendance.feign.AttendanceRuleProvider;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.dto.FileDTO;
import com.crlink.smartpark.common.feign.FileProvider;
import com.crlink.smartpark.core.utils.CustomCellWriteHandler;
import com.crlink.smartpark.core.utils.ExcelExportUtils;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.RocketmqUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.device.feign.DahuaIccProvider;
import com.crlink.smartpark.visitor.dto.AccessRecordDTO;
import com.crlink.smartpark.visitor.entity.AccessRecord;
import com.crlink.smartpark.visitor.mapper.AccessRecordMapper;
import com.crlink.smartpark.visitor.model.excel.AccessAttendanceExportVO;
import com.crlink.smartpark.visitor.model.vo.AccessRecordListVO;
import com.crlink.smartpark.visitor.query.AccessRecordQuery;
import com.crlink.smartpark.visitor.service.AccessRecordService;
import com.crlink.smartpark.visitor.vo.AccessRecordVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.crlink.smartpark.common.constants.Constants.PERSON_ACCESS_DIR;
import static com.crlink.smartpark.common.constants.enums.RocketmqConstant.RocketmqTopicEnum.FACE_RECOGNIZE_TOPIC;

/**
 * <p>
 * 人行抓拍记录表 服务实现类
 * </p>
 * */
@Slf4j
@Service
public class AccessRecordServiceImpl extends ServiceImpl<AccessRecordMapper, AccessRecord> implements AccessRecordService {

    @Value("${scene_image_url.host:https://park.hakjkg.com}")
    private String host;


    @Resource
    private DahuaIccProvider dahuaIccProvider;

    @Autowired
    private FileProvider fileProvider;

    @Autowired
    private AttendanceRuleProvider attendanceRuleProvider;

    @Autowired
    private AttendanceExchangeHolidayProvider attendanceExchangeHolidayProvider;

    public static final String FILE_NAME_RECORD = "抓拍记录";
    public static final String FILE_NAME_ACCESS = "通行记录";

    public static final String GET_PIC_PREFIX = "/smartPark/park-common/file/get?key=";

    @Value("#{${access_record_mapping}}")
    private Map<String, String> accessRecordMapping;

    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public AccessRecordVO getAccessRecordById(String id) {
        if (StringUtils.isNotBlank(id)) {
            AccessRecord entity = getById(id);
            if (entity != null) {
                AccessRecordVO dto = new AccessRecordVO();
                BeanUtils.copyProperties(entity, dto);
                return dto;
            }
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    @Override
    public PageInfo<AccessRecordListVO> listPage(AccessRecordQuery query) {
        LambdaQueryWrapper<AccessRecord> wrapper = buildPageListRecordWrapper(query);
        query.startPage();
        List<AccessRecord> list = list(wrapper);
        return query.createPageInfo(listEntityToListVO(list));
    }

    /**
     * 导出
     *
     * @param query
     */
    @Override
    public void export(AccessRecordQuery query) {
        LambdaQueryWrapper<AccessRecord> wrapper = this.buildPageListRecordWrapper(query);
        ExcelExportUtils.export(() -> list(wrapper),
                this::listEntityToListVO,
                DictConstant.RecognizeResult.SUCCESS.getCode().equals(query.getIsPass()) ? FILE_NAME_ACCESS : FILE_NAME_RECORD
                , AccessRecordListVO.class);
    }

    @Override
    public void exportAttendance(AccessRecordQuery query) {

        List<AccessAttendanceExportVO> finalList = Lists.newArrayList();

        // 根据名称，获取考勤规则， 后续前端加一下入参即可
        List<AttendanceRuleDTO> ruleList = attendanceRuleProvider.getAllRules();
        AttendanceRuleDTO ruleDto = ruleList.stream()
                .filter(rule -> query.getAttendanceRuleName().equals(rule.getRuleName()))
                .findFirst().get();

        // 要选择开始和结束时间，区间内的考勤记录, 前端加一下校验即可
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate startDate = LocalDateTime.parse(query.getStartTime(), formatter).toLocalDate();
        LocalDate endDate = LocalDateTime.parse(query.getEndTime(), formatter).toLocalDate();

        // 法定节假日的获取
        Set<LocalDate> holidays = new HashSet<>();
        Set<LocalDate> workingWeekends = new HashSet<>();
        List<AttendanceExchangeHolidayDTO> holidayDTOList = attendanceExchangeHolidayProvider.getAllExchangeHolidays();
        if (CollectionUtils.isNotEmpty(holidayDTOList)) {
            for (AttendanceExchangeHolidayDTO holidayDTO : holidayDTOList) {
                if (holidayDTO.getIsHoliday()) {
                    holidays.add(LocalDate.parse(holidayDTO.getExchangeDate()));
                } else {
                    workingWeekends.add(LocalDate.parse(holidayDTO.getExchangeDate()));
                }
            }
        }

        List<Integer> workingDays = ruleDto.getDaysOfWork();

        // 通过标签筛选员工
        LambdaQueryWrapper<AccessRecord> wrapper = this.buildPageListRecordWrapper1(query);
        List<AccessRecord> wrapperList = list(wrapper);
        if (CollectionUtils.isEmpty(wrapperList)) {
            finalList.add(new AccessAttendanceExportVO());
            ExcelExportUtils.export(finalList, "门禁打卡记录", new CustomCellWriteHandler());
            return;
        }
        List<AccessRecord> accessRecordList1 = wrapperList.stream()
                .filter(record -> StringUtils.isNotBlank(record.getMobile()) && StringUtils.isNotBlank(record.getUserName()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(accessRecordList1)) {
            finalList.add(new AccessAttendanceExportVO());
            ExcelExportUtils.export(finalList, "门禁打卡记录", new CustomCellWriteHandler());
            return;
        }

        Map<String, List<AccessRecord>> mobileMap = accessRecordList1.stream().collect(Collectors.groupingBy(AccessRecord::getMobile));
        for (String key : mobileMap.keySet()) {

            List<AccessRecord> accessRecordList = mobileMap.get(key);
            if (CollectionUtil.isEmpty(accessRecordList)) {
                continue;
            }
            String userName = accessRecordList.get(0).getUserName();

            Map<LocalDate, List<AccessRecord>> groupedByDate = accessRecordList.stream()
                    .collect(Collectors.groupingBy(record -> record.getRecognizeTime().toLocalDate()));
            List<AccessAttendanceExportVO> summaries = IntStream.rangeClosed(0, (int) startDate.until(endDate).getDays())
                    .mapToObj(startDate::plusDays)
                    .filter(date -> isWorkingDay(date, holidays, workingWeekends, workingDays))
                    .map(date -> {

                        AccessAttendanceExportVO vo = new AccessAttendanceExportVO();
                        vo.setUserName(userName);
                        vo.setRecognizeDate(date.toString());
                        vo.setDayOfWeek(date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.CHINESE));

                        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss");
                        List<AccessRecord> dailyRecords = groupedByDate.getOrDefault(date, new ArrayList<>());

                        AccessRecord morningEarliest = null;
                        AccessRecord afternoonLatest = null;

                        for (AccessRecord record : dailyRecords) {
                            LocalDateTime noon = record.getRecognizeTime().toLocalDate().atTime(12, 0);
                            if (record.getRecognizeTime().isBefore(noon)) {
                                if (morningEarliest == null || record.getRecognizeTime().isBefore(morningEarliest.getRecognizeTime())) {
                                    morningEarliest = record;
                                }
                            } else {
                                if (afternoonLatest == null || record.getRecognizeTime().isAfter(afternoonLatest.getRecognizeTime())) {
                                    afternoonLatest = record;
                                }
                            }
                        }

                        if (morningEarliest == null) {
                            vo.setStatusOfPuncnIn(Constants.PunchStatus.ZERO.name);
                            vo.setEarliestTime("/");
                        } else {
                            vo.setStatusOfPuncnIn(compareDateTimeToTime(morningEarliest.getRecognizeTime(), ruleDto.getPunchInTime()) == 1 ? Constants.PunchStatus.LATE.name : Constants.PunchStatus.NORMAL.name);
                            vo.setEarliestTime(morningEarliest.getRecognizeTime().format(formatter1));
                        }

                        if (afternoonLatest == null) {
                            vo.setStatusOfPuncnOut(Constants.PunchStatus.ZERO.name);
                            vo.setLatestTime("/");
                        } else {
                            vo.setStatusOfPuncnOut(compareDateTimeToTime(afternoonLatest.getRecognizeTime(), ruleDto.getPunchOutTime()) == -1 ? Constants.PunchStatus.EARLY.name : Constants.PunchStatus.NORMAL.name);
                            vo.setLatestTime(afternoonLatest.getRecognizeTime().format(formatter1));
                        }

                        return vo;
                    })
                    .collect(Collectors.toList());

            finalList.addAll(summaries);
        }

        ExcelExportUtils.export(finalList, "门禁打卡记录", new CustomCellWriteHandler());
    }

    private int compareDateTimeToTime(LocalDateTime dateTime, LocalTime time) {
        LocalDateTime timeAsDateTime = LocalDateTime.of(dateTime.toLocalDate(), time);

        if (dateTime.isAfter(timeAsDateTime)) {
            return 1;
        } else if (dateTime.isBefore(timeAsDateTime)) {
            return -1;
        } else {
            return 0;
        }
    }

    private HorizontalCellStyleStrategy getHorizontalCellStyleStrategy() {
        // 创建一个 WriteCellStyle 对象用于设置表头的样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 15);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        HorizontalCellStyleStrategy strategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return strategy;
    }

    private boolean isWeekend(LocalDate date, List<Integer> days) {
        for (int index = 0; index < days.size(); index++) {
            if (date.getDayOfWeek() == DayOfWeek.of(days.get(index))) {
                return false;
            }
        }
        return true;
    }

    private boolean isWorkingDay(LocalDate date, Set<LocalDate> holidays, Set<LocalDate> workingWeekends, List<Integer> days) {
        boolean isHoliday = holidays.contains(date);

        boolean isWorkingWeekend = workingWeekends.contains(date);
        boolean isWeekend = isWeekend(date, days);

        return !isHoliday && (isWorkingWeekend || !isWeekend);
    }

    @Override
    public Boolean saveAccessRecord(AccessRecordDTO accessRecordDTO) {
        AccessRecord accessRecord = MyBeanUtils.copy(accessRecordDTO, AccessRecord::new);
        String sceneImageUrl = accessRecord.getSceneImageUrl();
        String faceImageUrl = accessRecord.getFaceImageUrl();
        try {
            if (StringUtils.isNotBlank(sceneImageUrl) && !sceneImageUrl.toLowerCase().startsWith("http")) {
                HashSet<String> urls = Sets.newHashSet(accessRecord.getSceneImageUrl());
                Map<String, String> fullOssUrls = dahuaIccProvider.getFullOssUrls(urls);
                if (CollectionUtil.isNotEmpty(fullOssUrls)) {
                    String fullUrl = fullOssUrls.get(sceneImageUrl);
                    String key = buildMinioUrl(fullUrl, sceneImageUrl);
                    String name = String.valueOf(System.currentTimeMillis());
                    String minioUrl = host + GET_PIC_PREFIX + key + "&name=" + name;
                    if (Objects.nonNull(key)) {
                        accessRecord.setSceneImageUrl(minioUrl);
                        accessRecord.setFaceImageUrl(minioUrl);
                        sceneImageUrl = key;
                    }
                    log.info("通行记录发送至3D处理，sceneImageUrl={}, fullUrl={}, minioUrl={}", sceneImageUrl, fullUrl, minioUrl);
                }
            }
        } catch (Exception e) {
            log.error("获取通行照片全路径异常", e);
        }
        // 映射成3D大屏需要的设备编码
        accessRecord.setDeviceId(accessRecordMapping.get(accessRecordDTO.getCreateBy()));
        RocketmqUtil.sendSyncMessageWithoutTag(FACE_RECOGNIZE_TOPIC, accessRecord);
        accessRecord.setDeviceId(accessRecordDTO.getDeviceId());
        accessRecord.setSceneImageUrl(sceneImageUrl);
        accessRecord.setFaceImageUrl(faceImageUrl);
        return save(accessRecord);
    }

    public String buildMinioUrl(String fullUrl, String sceneImageUrl) {
        byte[] bytes = null;
        try {
            bytes = HttpUtil.downloadBytes(fullUrl);
        } catch (Exception e) {
            log.error("人行抓拍照片存储minio异常！");
        }
        if (Objects.nonNull(bytes)) {
            String timeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.SMALL_DATE_FORMAT));
            String keyPrefix = PERSON_ACCESS_DIR + "/" + timeFormat + "/";
            String key = sceneImageUrl.substring(StringUtils.lastIndexOf(sceneImageUrl, "/") + 1);
            String fullKey = keyPrefix + key;
            FileDTO fileDTO = new FileDTO();
            fileDTO.setBytes(bytes);
            fileDTO.setRelativePath(fullKey);
            fileProvider.fileUpload2oss(fileDTO);
            return fullKey;
        }
        return null;
    }

    /**
     * 封装查询条件
     *
     * @param query 前端查询条件对象
     * @return 查询条件
     */
    public LambdaQueryWrapper<AccessRecord> buildPageListRecordWrapper(AccessRecordQuery query) {
        LambdaQueryWrapper<AccessRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AccessRecord::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds())
                .eq(StringUtils.isNotBlank(query.getMobile()), AccessRecord::getMobile, query.getMobile())
                .in(StringUtils.isNotBlank(query.getDeviceId()), AccessRecord::getDeviceId, query.getDeviceId())
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), AccessRecord::getParkOrgId, query.getParkOrgId())
                .eq(Objects.nonNull(query.getIsPass()), AccessRecord::getIsPass, query.getIsPass())
                .ge(Objects.nonNull(query.getStartTime()), AccessRecord::getRecognizeTime, query.getStartTime())
                .le(Objects.nonNull(query.getEndTime()), AccessRecord::getRecognizeTime, query.getEndTime())
                .like(StringUtils.isNotBlank(query.getUserName()), AccessRecord::getUserName, query.getUserName())
                .apply(StringUtils.isNotBlank(query.getLabelId()), "FIND_IN_SET(" + query.getLabelId() + ",label_ids)")
                .apply(StringUtils.isNotBlank(query.getLabel()), "FIND_IN_SET(" + query.getLabel() + ",labels)")
                .orderByDesc(AccessRecord::getRecognizeTime);
        return queryWrapper;
    }

    public LambdaQueryWrapper<AccessRecord> buildPageListRecordWrapper1(AccessRecordQuery query) {
        LambdaQueryWrapper<AccessRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AccessRecord::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds())
                .eq(StringUtils.isNotBlank(query.getMobile()), AccessRecord::getMobile, query.getMobile())
                .in(StringUtils.isNotBlank(query.getDeviceId()), AccessRecord::getDeviceId, query.getDeviceId())
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), AccessRecord::getParkOrgId, query.getParkOrgId())
                .eq(Objects.nonNull(query.getIsPass()), AccessRecord::getIsPass, query.getIsPass())
                .ge(Objects.nonNull(query.getStartTime()), AccessRecord::getRecognizeTime, query.getStartTime())
                .le(Objects.nonNull(query.getEndTime()), AccessRecord::getRecognizeTime, query.getEndTime())
                .like(StringUtils.isNotBlank(query.getUserName()), AccessRecord::getUserName, query.getUserName())
                .apply(StringUtils.isNotBlank(query.getLabelId()), "FIND_IN_SET(" + query.getLabelId() + ",label_ids)")
                .apply(StringUtils.isNotBlank(query.getLabel()), "FIND_IN_SET(" + query.getLabel() + ",labels)")
                .orderByDesc(AccessRecord::getRecognizeTime);
        return queryWrapper;
    }

    /***
     * entity转ListVO
     *
     * @param list entity集合
     * @return DTO集合
     */
    private List<AccessRecordListVO> listEntityToListVO(List<AccessRecord> list) {
//        Map<String, String> urlsMap = dahuaIccProvider.getFullOssUrls(list.stream()
//                .filter(e -> StrUtil.isNotBlank(e.getSceneImageUrl()) && !e.getSceneImageUrl().toLowerCase().startsWith("http"))
//                .map(AccessRecord::getSceneImageUrl).collect(Collectors.toSet()));
        return list.stream().map(entity -> {
            AccessRecordListVO temp = new AccessRecordListVO();
            BeanUtils.copyProperties(entity, temp);
            if (entity.getIsOverTemp() == null) {
                temp.setIsOverTemp("-");
            } else {
                temp.setIsOverTemp(entity.getIsOverTemp().equals(0) ? "正常" : "异常");
            }
//            if (urlsMap.containsKey(entity.getSceneImageUrl())) {
//                temp.setSceneImageUrl(urlsMap.get(entity.getSceneImageUrl()));
//            }
            if (entity.getSceneImageUrl().toLowerCase().startsWith("person")) {
                String minioUrl = host + GET_PIC_PREFIX + entity.getSceneImageUrl() + "&name=" + entity.getId();
                temp.setSceneImageUrl(minioUrl);
            }
            return temp;
        }).collect(Collectors.toList());
    }


    @Async
    @Override
    public void buildHistorySceneImageUrl(String startTime, String endTime) {
        log.info("处理人行记录异常数据开始");
        List<AccessRecord> list = this.lambdaQuery().select(AccessRecord::getId, AccessRecord::getSceneImageUrl)
                .le(AccessRecord::getCreateTime, endTime)
                .ge(AccessRecord::getCreateTime, startTime)
                .orderByDesc(AccessRecord::getCreateTime).list();
        Set<String> allUrls = list.parallelStream()
                .filter(t -> Objects.nonNull(t.getSceneImageUrl()) && !t.getSceneImageUrl().toLowerCase().startsWith("http"))
                .map(AccessRecord::getSceneImageUrl)
                .collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(allUrls)) {
            return;
        }
        Map<String, String> fullOssUrls = dahuaIccProvider.getFullOssUrls(allUrls);
        if (CollectionUtil.isEmpty(allUrls)) {
            return;
        }
        List<AccessRecord> collect = list.stream().map(t -> {
            String picUrl = fullOssUrls.get(t.getSceneImageUrl());
            String key = buildMinioUrl(picUrl, t.getSceneImageUrl());
            if (Objects.nonNull(key)) {
                t.setSceneImageUrl(key);
            }
            return t;
        }).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(collect)) {
            this.updateBatchById(collect, collect.size());
        }
        log.info("处理人行记录异常数据结束，count: {}", collect.size());
    }
}
