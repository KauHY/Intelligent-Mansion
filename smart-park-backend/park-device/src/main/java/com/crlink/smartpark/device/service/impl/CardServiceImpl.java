package com.crlink.smartpark.device.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.device.dto.CardDTO;
import com.crlink.smartpark.device.entity.Card;
import com.crlink.smartpark.device.mapper.CardMapper;
import com.crlink.smartpark.device.model.CardBoundReqDTO;
import com.crlink.smartpark.device.model.CardExchangeReqDTO;
import com.crlink.smartpark.device.model.CardReqDTO;
import com.crlink.smartpark.device.model.CardUpdateStatusReqDTO;
import com.crlink.smartpark.device.model.excel.CardExcelExportVO;
import com.crlink.smartpark.device.model.query.CardQuery;
import com.crlink.smartpark.device.service.CardService;
import com.crlink.smartpark.system.dto.employee.EmployeeDTO;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.field.EmployeeFiled;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.crlink.smartpark.device.common.CardConstant.CARD_EXPORT_FILENAME;
import static com.crlink.smartpark.device.common.CardConstant.CardStatusEnum.*;
import static com.crlink.smartpark.device.common.CardConstant.OperationTypeEnum.*;


/**
 * 卡片管理 服务实现类
 * */
@Slf4j
@Service
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {

    @Autowired
    private EmployeeProvider employeeProvider;


    @Override
    public <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<Card, T> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        Map<String, T> result = this.lambdaQuery().select(Card::getId, sValue)
                .in(Card::getId, ids).list().stream()
                .collect(HashMap::new, (map, card) -> map.put(card.getId(), sValue.apply(card)), HashMap::putAll);
        return result;
    }

    @Override
    public Boolean saveCard(CardReqDTO reqDTO) throws Exception {
        return true;
    }


    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public CardDTO getCardById(String id) {
        Card entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        CardDTO result = MyBeanUtils.copy(entity, CardDTO::new);
        return result;
    }


    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<CardDTO> listCardPage(CardQuery query) {
        LambdaQueryWrapper<Card> conditionWrapper = getConditionWrapper(query);
        query.startPage();
        List<Card> list = list(conditionWrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<CardDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public Boolean updateStatus(CardUpdateStatusReqDTO statusReqDTO) throws Exception {
        return true;
    }

    @Override
    public Boolean exchangeCard(CardExchangeReqDTO reqDTO) throws Exception {
        return true;
    }

    @Override
    public Boolean boundCard(CardBoundReqDTO reqDTO) throws Exception {
        return true;
    }

    @Override
    public Boolean removeCardById(String id) throws Exception {
        return true;
    }


    @Override
    public void export(CardQuery query) {
        ExcelExportUtils.export(() -> list(getConditionWrapper(query)),
                sourceList -> MyBeanUtils.copyList(this.entitiesToDTOs(sourceList), CardExcelExportVO::new,
                        (s, t) -> t.setLimitDate(s.getEffectStartDate().toString() + "-" + s.getEffectEndDate().toString()))
                , CARD_EXPORT_FILENAME, CardExcelExportVO.class);
    }


    @Override
    public Set<String> findAllCardByEmployeeId(String employeeId) {
        Set<String> carNos = lambdaQuery().select(Card::getId, Card::getCardNo).eq(Card::getEmployeeId, employeeId).eq(Card::getStatus, BINDING.status)
                .list().stream().map(Card::getCardNo).collect(Collectors.toSet());
        return CollUtil.isEmpty(carNos) ? Sets.newHashSet() : carNos;
    }

    @Override
    public Boolean containsEmployeeRelationCard(String employeeId) {
        Card card = lambdaQuery().select(Card::getId, Card::getCardNo)
                .in(Card::getStatus, BINDING.status, LOST.status)
                .eq(Card::getEmployeeId, employeeId)
                .last("limit 1").one();
        return Optional.ofNullable(card).isPresent();
    }


    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<Card> getConditionWrapper(CardQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getParkOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryWrapper<Card> queryWrapper = new LambdaQueryWrapper<Card>();
        //根据持卡人查询
        if (StringUtils.isNotBlank(query.getName())) {
            //跨服务查询符合的员工Id
            Map<String, Map<String, Object>> employeeColumns =
                    employeeProvider.queryEmployeeColumns(EmployeeQueryDTO.likeNameBuildDTO(query.getName()));
            Set<String> employeeIds = employeeColumns.keySet();
            MyPageInfoUtils.isEmptyPage(employeeIds);
            queryWrapper.in(Card::getEmployeeId, employeeIds);
        }
        LocalDate effectStartDate = query.getEffectStartDate();
        LocalDate effectEndDate = query.getEffectEndDate();
        //根据有效期查询
        if (Objects.nonNull(effectStartDate) && Objects.nonNull(effectEndDate)) {
            //查询生效时间段与查询时间段存在交集的
            queryWrapper.not(wrapper -> wrapper.ge(Card::getEffectStartDate, effectEndDate)
                    .or().le(Card::getEffectEndDate, effectStartDate));
        }
        queryWrapper.like(StringUtils.isNotBlank(query.getCardNo()), Card::getCardNo, query.getCardNo())
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), Card::getParkOrgId, query.getParkOrgId())
                .eq(StringUtils.isNotBlank(query.getCompanyId()), Card::getCompanyId, query.getCompanyId())
                .eq(Objects.nonNull(query.getStatus()), Card::getStatus, query.getStatus())
                .in(Card::getParkOrgId, parkOrgIds)
                .orderByDesc(Card::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<CardDTO> entitiesToDTOs(List<Card> entities) {
        Map<String, EmployeeDTO> mobiles = getMobileByEmployeeIds(entities);
        return MyBeanUtils.copyList(entities, CardDTO::new, (s, t) -> {
            //设置手机号
            EmployeeDTO employeeDTO = mobiles.get(s.getEmployeeId());
            if (Objects.nonNull(employeeDTO)) {
                t.setMobile(employeeDTO.getMobile());
            }
        });
    }

    /**
     * 根据员工id获取手机号
     *
     * @param list
     * @return
     */
    private Map<String, EmployeeDTO> getMobileByEmployeeIds(List<Card> list) {
        Set<String> employeeIds = list.parallelStream().map(Card::getEmployeeId).collect(Collectors.toSet());
        String[] queryColumn = new String[]{"name", "mobile"};
        EmployeeQueryDTO employeeQueryDTO = new EmployeeQueryDTO();
        employeeQueryDTO.setEmployeeIds(employeeIds);
        employeeQueryDTO.setColumns(queryColumn);
        return employeeProvider.queryEmployeeByCondition(employeeQueryDTO);
    }
}
