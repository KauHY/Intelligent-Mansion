package com.crlink.smartpark.system.controller.parkmanagement;

import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.job.JobDTO;
import com.crlink.smartpark.system.dto.job.JobReqDTO;
import com.crlink.smartpark.system.query.JobQuery;
import com.crlink.smartpark.system.service.parkmanagement.JobManageService;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 园区运营-工作岗位
 * */
@Slf4j
@Api(tags = "园区运营平台-工作岗位")
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobManageService jobManageService;

    /**
     * 工作岗位-新增
     *
     * @return
     */
    @ApiOperation(value = "工作岗位-新增", tags = "工作岗位")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@Valid @RequestBody JobReqDTO jobDTO) {
        jobDTO.setId(null);
        Boolean result = jobManageService.saveOrUpdateJob(jobDTO);
        return ResponseResult.success(result);
    }

    /**
     * 【工作岗位】删除
     */
    @ApiOperation(value = "工作岗位-删除", tags = "工作岗位")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = jobManageService.removeJobById(id);
        return ResponseResult.success(result);
    }

    ///**
    //* 【工作岗位】根据ID查询
    //*
    //* @param id 主键
    //* @return 工作岗位实体
    //*/
    //@ApiOperation(value = "工作岗位-根据ID查询", tags = "工作岗位")
    //@GetMapping("/findById/{id}")
    //public ResponseResult<JobDTO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
    //    return ResponseResult.success(jobService.getJobById(id));
    //}

    /**
     * 编辑岗位
     */
    @ApiOperation(value = "编辑岗位", tags = "工作岗位")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody JobReqDTO jobDTO) {
        AssertUtil.requireNotNull(jobDTO.getId(), "10007");
        Boolean result = jobManageService.saveOrUpdateJob(jobDTO);
        return ResponseResult.success(result);
    }

    /**
     * 工作岗位-分页查询
     *
     * @param jobQuery 查询参数
     * @return 分页数据
     */
    @ApiOperation(value = "工作岗位-分页查询", tags = "工作岗位")
    @PostMapping("/listJobPage")
    public ResponseResult<PageInfo<JobDTO>> listJobPage(@RequestBody JobQuery jobQuery) {
        PageInfo<JobDTO> result = jobManageService.listJobPage(jobQuery);
        return ResponseResult.success(result);
    }

    /**
     * 工作岗位-下拉列表
     *
     * @return 分页数据
     */
    @ApiOperation(value = "工作岗位-下拉列表", tags = "工作岗位")
    @GetMapping("/listJobDict")
    public ResponseResult<List<DictVO>> listJobDict(String companyId) {
        List<DictVO> result = jobManageService.listJobDict(companyId);
        return ResponseResult.success(result);
    }


}

