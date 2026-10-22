package com.crlink.smartpark.visitor.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.core.utils.DateUtils;
import com.crlink.smartpark.visitor.entity.Car;
import com.crlink.smartpark.visitor.service.CarService;
import com.crlink.smartpark.visitor.service.impl.CarServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class CarSchedule{

    @Autowired
    private CarService carService;

    public static final int pageSize = 1_000;

    //0 0 1 * * ?每天凌晨一点执行一次
    @Scheduled(cron = "0 0 1 * * ?")
    /**
     * 每天凌晨一点更新车辆授权状态
     * 0-未生效；1-生效中；2-已失效
     */
    public void updateAuthStatus(){
        LambdaQueryWrapper<Car> queryWrapper = Wrappers.lambdaQuery();
        int count = carService.count(queryWrapper);
        int times = count / pageSize + 1;
        int i = 1;
        queryWrapper.select(Car::getId, Car::getAuthStartTime, Car::getAuthEndTime);
        while (i <= times){
            PageHelper.startPage(i, pageSize);
            List<Car> list = carService.getBaseMapper().selectList(queryWrapper);
            list.forEach(e -> e.setStatus(CarServiceImpl.betweenDateStartAndEnd(e.getAuthStartTime(), e.getAuthEndTime())));
            carService.saveOrUpdateBatch(list);
            i++;
        }
    }
}
