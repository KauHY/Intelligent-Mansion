package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.system.dto.house.BuildingSaveDTO;
import com.crlink.smartpark.system.query.WxOaMobileRelationQuery;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.builiding.BuildingVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 楼栋表 服务类
 * </p>
 * */
@FeignClient(name = "park-system", path = "/smartPark/park-system/wx-oa-mobile-relation", contextId = "WxOaMobileRelationProvider")
public interface WxOaMobileRelationProvider {

    @ApiOperation(value = "内部接口-通过手机号和公众号APPID获取用户openId", tags = "公众号用户openId与手机号关联表")
    @PostMapping("/getOpenIdsByMobilesAndAppId")
    Set<String> getOpenIdsByMobilesAndAppId(@RequestBody @Valid WxOaMobileRelationQuery query);
}
