package com.crlink.smartpark.customer.feign;

import com.crlink.smartpark.customer.dto.MeetingReserveDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *
 * </p>
 * */
@FeignClient(name = "park-customer", path = "/smartPark/park-customer/parkManagement/meeting-reserve", contextId = "MeetingReserveProvider")
public interface MeetingReserveProvider {

    /**
     * @param parkOrgId
     * @param nearTwelveMonths
     * @return
     */
    @RequestMapping(value = "/getMeetingReserveBillList", method = RequestMethod.GET)
    List<MeetingReserveDTO> getMeetingReserveBillList(@RequestParam("parkOrgId") String parkOrgId,
                                                      @RequestParam("nearTwelveMonths") Boolean nearTwelveMonths);
}
