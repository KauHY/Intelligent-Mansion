package com.crlink.smartpark.system.common;

import cn.hutool.core.collection.CollectionUtil;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.core.utils.RedisUtil;
import com.crlink.smartpark.core.utils.WxOAUtil;
import com.crlink.smartpark.system.service.common.DictionaryService;
import com.crlink.smartpark.system.vo.DictVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @description: 服务初始化操作
 */
@Service
@Slf4j
public class InitOperation {

    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private WxOAUtil wxOAUtil;

    @Resource
    private DictionaryService dictionaryService;

    @Value("${param.wx.timeOut:60000}")
    private Integer timeOut; //请求超时时间：单位毫秒
    @Value("${param.wx.proxyIP:}")
    private String proxyIP;
    @Value("${param.wx.proxyPort:80}")
    private Integer proxyPort;
    @Value("${param.wx.oa.appID:wx84631c09c2b75305}")
    private String appID;
    @Value("${param.wx.oa.appsecret:25a16fdba3416b957f7869d828a72969}")
    private String appsecret;

    public static final String WX_OA_TOKEN_URL_FORMAT = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    public static final Long WX_OA_ACCESS_TOKEN_EXPIRE_SECOND = 7100L;

    /**
     * 缓存所有字典数据
     *
     * @return
     */
    @PostConstruct
    public void cacheAllDictionary() {
        log.info("初始化所有字典数据");
        Map<String, Map<String, DictVO>> typeMap = dictionaryService.initDictionary();
        if (CollectionUtil.isEmpty(typeMap)) {
            return;
        }
        //log.info("所有字典数据：{}", JSONUtil.toJsonStr(typeMap));
        CompletableFuture.runAsync(() -> {
            for (Map.Entry<String, Map<String, DictVO>> entry : typeMap.entrySet()) {
                redisUtil.setObject(Constants.Redis.DICTIONARY_TYPE_PREFIX.getBuildCode(entry.getKey()), entry.getValue());
            }
        });
    }

    @PostConstruct
    public void wxOaInit() {
        wxOAUtil.init();
    }
}
