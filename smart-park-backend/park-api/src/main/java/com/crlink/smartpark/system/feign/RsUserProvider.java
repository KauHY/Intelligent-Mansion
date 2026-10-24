package com.crlink.smartpark.system.feign;

import com.crlink.smartpark.system.vo.RsUserListVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@FeignClient(name = "park-system", path = "/smartPark/park-system/rs-auth", contextId = "RsUserProvider")
public interface RsUserProvider {

    @GetMapping("/userList")
    List<RsUserListVO> userListBy(@RequestParam("realName") String realName);
}
