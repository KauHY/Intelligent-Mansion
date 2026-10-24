package com.crlink.smartpark.inspection.controller;

import cn.hutool.core.io.FileUtil;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.inspection.controller.request.CircuitQuery;
import com.crlink.smartpark.inspection.controller.request.CircuitRelatePointReqDTO;
import com.crlink.smartpark.inspection.controller.request.InspectionCircuitReqDTO;
import com.crlink.smartpark.inspection.dto.InspectionCircuitDTO;
import com.crlink.smartpark.inspection.service.InspectionCircuitService;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * park-inspection
 * 巡检路线 前端控制器
 * */
@Api(tags = "巡检管理-巡检路线")
@Slf4j
@RestController
@RequestMapping("/inspection/inspection-circuit")
public class InspectionCircuitController {


    @Autowired
    private InspectionCircuitService inspectionCircuitService;


    @ApiOperation(value = "巡检路线-新增", tags = "巡检路线")
    @PostMapping("/create")
    public ResponseResult<Boolean> create(@RequestBody @Validated InspectionCircuitReqDTO reqDTO) {
        reqDTO.setId(null);
        Boolean result = inspectionCircuitService.saveOrUpdateCircuit(reqDTO);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "巡检路线-删除", tags = "巡检路线")
    @DeleteMapping("/remove/{id}")
    public ResponseResult<Boolean> remove(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        Boolean result = inspectionCircuitService.deleteCircuit(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "巡检路线-根据ID查询", tags = "巡检路线")
    @GetMapping("/findById/{id}")
    public ResponseResult<InspectionCircuitDTO> findById(@PathVariable("id") @NotNull(message = "id不能为空") String id) {
        InspectionCircuitDTO result = inspectionCircuitService.getInspectionCircuitById(id);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "巡检路线-修改", tags = "巡检路线")
    @PostMapping("/modify")
    public ResponseResult<Boolean> modify(@RequestBody InspectionCircuitReqDTO reqDTO) {
        AssertUtil.requireTrue(StringUtils.hasText(reqDTO.getId()), "10007");
        Boolean result = inspectionCircuitService.saveOrUpdateCircuit(reqDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "巡检路线-分页查询", tags = "巡检路线")
    @PostMapping("/listCircuitPage")
    public ResponseResult<PageInfo<InspectionCircuitDTO>> listCircuitPage(@RequestBody CircuitQuery circuitQuery) {
        PageInfo<InspectionCircuitDTO> result = inspectionCircuitService.listCircuitPage(circuitQuery);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "巡检路线-关联巡检点", tags = "巡检路线")
    @PostMapping("/circuitRelatePoints")
    public ResponseResult<Boolean> circuitRelatePoints(@RequestBody CircuitRelatePointReqDTO dto) {
        Boolean result = inspectionCircuitService.circuitRelatePoints(dto);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "选择巡检路线", tags = "巡检路线")
    @GetMapping("/selectCircuits")
    @ApiImplicitParam(name = "type", value = "巡检类型")
    public ResponseResult<List<DictVO>> selectCircuits(String parkOrgId, Integer type) {
        List<DictVO> result = inspectionCircuitService.selectCircuits(parkOrgId, type);
        return ResponseResult.success(result);
    }

    //@ApiOperation(value = "test", tags = "巡检路线")
    //@PostMapping("/test")
    //public ResponseResult<List<InspectionCircuit>> test(@RequestBody InspectionCircuitReqDTO reqDTO) {
    //    reqDTO.setId(null);
    //    List<InspectionCircuit> result = inspectionCircuitService.test(reqDTO);
    //    return ResponseResult.success(result);
    //}
    public static void main(String[] args) {
        //String s = Convert.toStr(LocalDateTime.now());
        //LocalDateTime localDateTime = Convert.toLocalDateTime("20:00");
        //String[] a = {"1", "2"};
        //ArrayList<String> a = Lists.newArrayList("1", "2", "3");
        //Array convert = Convert.convert(Array.class, a);
        //Optional convert = Convert.convert(Optional.class, "20:00", Optional.of("1"));
        //List<?> objects = Convert.toList(int.class, "2,3");
        //StaticLog.info("hello:{}", "hello");
        //Convert.convert()
        //Convert.
        //InspectionCircuitDTO dto = new InspectionCircuitDTO();
        //dto.setId("1");
        //Map<Object, InspectionCircuitDTO> id = IterUtil.fieldValueMap(Lists.newArrayList(dto), "id");
        //;
        //HashMap<String, String> map = new HashMap<>();
        //Class<?> typeArgument = ClassUtil.getTypeArgument(InspectionCircuitService.class, 0);
        //int[] b = new int[1];
        //Object convert = new ArrayConverter(b.getClass()).convert(a, null);
        System.out.println(FileUtil.isWindows());
    }


}

