package com.crlink.smartpark.device.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.device.controller.request.Device3dPageQuery;
import com.crlink.smartpark.device.controller.response.Device3dVO;
import com.crlink.smartpark.device.controller.response.DeviceStatusCountVO;
import com.crlink.smartpark.device.controller.response.DeviceStatusVO;
import com.crlink.smartpark.device.controller.response.LiquidVO;
import com.crlink.smartpark.device.controller.response.RoomEnvironmentVO;
import com.crlink.smartpark.device.service.Device3dService;
import com.crlink.smartpark.device.vo.DeviceInfoVO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 3D大屏的接口
 * 抄表记录
 * */
@RestController
@RequestMapping("/3d/device")
@Api(tags = "3D大屏-设备接口")
@Validated
@Slf4j
public class Device3dController {

    @Autowired
    private Device3dService device3dService;

    @ApiOperation(value = "1获取设备列表", tags = "3D大屏")
    @PostMapping("/list3dDevice")
    public ResponseResult<PageInfo<Device3dVO>> list3dDevice(@RequestBody Device3dPageQuery reqDTO) {
        PageInfo<Device3dVO> result = device3dService.list3dDevice(reqDTO);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "2获取设备状态")
    @GetMapping("/getDeviceTypeStatus")
    public ResponseResult<DeviceStatusCountVO> getDeviceTypeStatus(@RequestParam("parkOrgId") String parkOrgId,
                                                                   @RequestParam(value = "deviceType", required = false) Integer deviceType) {
        DeviceStatusCountVO result = device3dService.getDeviceTypeStatus(parkOrgId, deviceType);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "4获取设备状态")
    @GetMapping("/getDeviceInfoByDeviceId")
    public ResponseResult<DeviceInfoVO> getDeviceInfoByDeviceId(@RequestParam("parkOrgId") String parkOrgId,
                                                                @RequestParam(value = "deviceId") String deviceId) {
        DeviceInfoVO result = device3dService.getDeviceByDeviceId(parkOrgId, deviceId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "11、17报警设备在线状态统计")
    @GetMapping("/getDeviceWarningTotal")
    public ResponseResult<List<DeviceStatusCountVO>> getDeviceWarningTotal(@RequestParam("parkOrgId") String parkOrgId) {
        List<DeviceStatusCountVO> result = device3dService.getDeviceWarningTotal(parkOrgId);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "14获取设备总数")
    @GetMapping("/getDeviceTotal")
    public ResponseResult<Integer> getDeviceTotal(@RequestParam("parkOrgId") String parkOrgId) {
        Integer result = device3dService.getDeviceTotal(parkOrgId);
        return ResponseResult.success(result);
    }


    @ApiOperation(value = "23获取天气")
    @GetMapping("/getWeather")
    public ResponseResult<String> getWeather(@RequestParam(value = "location", required = false) String location) {
        String result = device3dService.getWeather(location);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "24获取室内环境数据")
    @GetMapping("/getRoomEnvironment")
    public ResponseResult<RoomEnvironmentVO> getRoomEnvironment() {
        return ResponseResult.success(device3dService.getRoomEnvironment());
    }

    @ApiOperation(value = "25获取室内状态数据")
    @GetMapping("/getRoomDeviceStatus")
    public ResponseResult<List<DeviceStatusVO>> getRoomDeviceStatus() {
        return ResponseResult.success(device3dService.getRoomDeviceStatus());
    }

    /**
     * 在菜单中配置3D大屏地址
     *
     * @param resp
     */
    @GetMapping("/3dMenuUrl/redirect")
    public void redirect(HttpServletResponse resp) {
        Map<String, DictVO> config = DictUtil.getByType("3D_MENU_URL_TYPE");
        String urlPrefix = config.get("TOKEN_URL").getLabel();
        String tokenUrl = urlPrefix + "/wisdomVallery/sys/ChangeUserToken";
        ImmutableMap<String, String> map = ImmutableMap.of("UserName", config.get("UserName").getLabel(), "PassWord", config.get("PassWord").getLabel());
        String body = JSONUtil.toJsonStr(map);
        String response = HttpUtil.post(tokenUrl, body, 500);
        cn.hutool.json.JSONObject responseObj = JSONUtil.parseObj(response);
        String token = null;
        if (Objects.equals(200, responseObj.getInt("code"))) {
            token = responseObj.getStr("message");
            String redirectUri = config.get("MENU_URL").getLabel() + "/bim/index.html#/?" +
                    "BuildingOrgID=" + config.get("parkOrgId").getLabel() +
                    "&OrgName=%E6%B7%AE%E5%AE%89%E6%99%BA%E6%85%A7%E8%B0%B7" +
                    "&external_link=1" +
                    "&BimBaseToken=" + token;
            try {
                resp.sendRedirect(redirectUri);
            } catch (IOException e) {
                log.error("3d大屏重定向接口失败", e);
                throw new ResolinkRuntimeException("10028");
            }
        }
    }

    @ApiOperation(value = "34获取液位数据")
    @GetMapping("/getLiquidList")
    public ResponseResult<List<LiquidVO>> getLiquidList() {
        return ResponseResult.success(device3dService.getLiquidList());
    }

    private RoomEnvironmentVO getAirQuality(RoomEnvironmentVO environmentVO) {
        try {
            String airQuality = device3dService.getAirQuality(null);
            JSONObject body = JSONObject.parseObject(airQuality);
            if (Objects.nonNull(body) && Objects.equals(HttpStatus.OK.value(), body.getInteger("code"))) {
                JSONObject object = body.getJSONObject("now");
                environmentVO.setComfort(object.getString("category"));
                environmentVO.setPM(object.getString("pm2p5"));
            }
        } catch (Exception e) {
            log.error("查询空气质量接口出错！", e);
        }
        return environmentVO;
    }
}

