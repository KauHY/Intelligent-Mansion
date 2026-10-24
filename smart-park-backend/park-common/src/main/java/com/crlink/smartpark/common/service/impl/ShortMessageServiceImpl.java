package com.crlink.smartpark.common.service.impl;

import cn.hutool.core.math.MathUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.ShortMessageConstant;
import com.crlink.smartpark.common.constants.SmsConstant;
import com.crlink.smartpark.common.entity.DiskFile;
import com.crlink.smartpark.common.mapper.DiskFileMapper;
import com.crlink.smartpark.common.model.ShortMessageReqDTO;
import com.crlink.smartpark.common.service.DiskFileService;
import com.crlink.smartpark.common.service.IOssService;
import com.crlink.smartpark.common.service.ShortMessageService;
import com.crlink.smartpark.core.utils.MyFileUtils;
import com.crlink.smartpark.core.utils.RedisUtil;
import com.crlink.smartpark.core.utils.SendSmsUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.DOT;

/**
 * <p>
 * 上传文件 服务实现类
 * </p>
 * */
@Service
public class ShortMessageServiceImpl implements ShortMessageService {

    @Resource
    private RedisUtil redisUtil;
    @Override
    public Boolean getSmsCode(ShortMessageReqDTO reqDTO) {
        ShortMessageConstant.TemplateEnum templateEnum = ShortMessageConstant.TemplateEnum.getTemplateByType(reqDTO.getType());
        String code = RandomUtil.randomNumbers(templateEnum.getLength());

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("code", code);
        boolean flag = SendSmsUtil.sendSms(templateEnum.getSignName(), templateEnum.getTemplateCode(), reqDTO.getMobile(),
                JSONUtil.toJsonStr(jsonObject));
        if (flag) {
            String smsCodeKey = templateEnum.buildRedisCode(reqDTO.getMobile());
            redisUtil.setObject(smsCodeKey, code, templateEnum.getValidMinute(), templateEnum.getTimeUnit());
        }
        return flag;
    }

}
