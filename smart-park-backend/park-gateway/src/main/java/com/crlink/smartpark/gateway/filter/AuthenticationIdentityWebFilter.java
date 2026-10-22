package com.crlink.smartpark.gateway.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.JwtBaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.crlink.smartpark.common.constants.Constants.*;
import static com.crlink.smartpark.common.constants.Constants.Redis.*;

/**
 * <p>
 * 权限过滤器
 * </p>
 * */
@Slf4j
@Component
public class AuthenticationIdentityWebFilter implements GlobalFilter, Ordered {

    @Value("#{'${param.filter.white_uri}'.split(',')}")
    private List<String> whiteUriList;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder mutate = request.mutate();
        String currentUrl = request.getPath().toString();
        //白名单过滤
        String token = request.getHeaders().getFirst(Constants.HEADER_AUTH);
        if (checkWhiteUri(currentUrl) && StringUtils.isBlank(token)) {
            return chain.filter(exchange.mutate().request(mutate.build()).build());
        }
        if (StringUtils.isBlank(token) || token.length() < TOKEN_PREFIX_INDEX + 1) {
            return makeAuthenticationResponse(exchange.getResponse(), ResponseResult.errorMessage("40009"));
        }
        try {
            //校验token
            Map<String, String> userMap = JwtBaseUtil.validateToken(token);
            String tokenId = userMap.get(Constants.TOKEN_ID);
            Long expire = stringRedisTemplate.getExpire(USER_ACCESS_TOKEN_PREFIX.getBuildCode(tokenId), TimeUnit.MINUTES);
            if (expire > Constants.TIME_SECOND) {
                //向请求头添加信息
                addHeaderInfo(request, token, userMap);
                //重置token过期时间
                resetTokenExpireTime(expire, tokenId);
                String userMapStr = JSONUtil.toJsonStr(userMap);
                if (log.isDebugEnabled()) {
                    log.debug("用户请求头信息：{}", userMapStr);
                }
                mutate.header(Constants.HEADER_USER_INFO, userMapStr);

            } else {
                return makeAuthenticationResponse(exchange.getResponse(), ResponseResult.errorMessage("40009"));
            }
        } catch (Exception e) {
            return makeAuthenticationResponse(exchange.getResponse(), ResponseResult.errorMessage("40009"));
        }
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private void resetTokenExpireTime(Long expire, String tokenId) {
        if (expire < Constants.OVERTIME_HALF) {
            CompletableFuture.runAsync(() -> {
                stringRedisTemplate.expire(USER_ACCESS_TOKEN_PREFIX.getBuildCode(tokenId), Constants.OVERTIME, TimeUnit.MINUTES);
                stringRedisTemplate.expire(USER_INFO_PREFIX.getBuildCode(tokenId), Constants.OVERTIME, TimeUnit.MINUTES);
                stringRedisTemplate.expire(WX_USER_INFO_PREFIX.getBuildCode(tokenId), Constants.OVERTIME, TimeUnit.MINUTES);
                stringRedisTemplate.expire(USER_BUSINESS_INFO_PREFIX.getBuildCode(tokenId), Constants.OVERTIME, TimeUnit.MINUTES);
            });
        }
    }


    private boolean checkWhiteUri(String currentUrl) {
        boolean isWhiteUri = false;
        if (CollectionUtil.isNotEmpty(whiteUriList)) {
            if (whiteUriList.contains(currentUrl)) {
                isWhiteUri = true;
            } else {
                AntPathMatcher antPathMatcher = new AntPathMatcher();
                for (String uri : whiteUriList) {
                    if (antPathMatcher.match(uri, currentUrl)) {
                        isWhiteUri = true;
                        break;
                    }
                }
            }
        }
        return isWhiteUri;
    }

    /**
     * 封装登录过期响应实体
     *
     * @param response 响应实体对象
     * @return 响应实体
     */
    private Mono<Void> makeAuthenticationResponse(ServerHttpResponse response, ResponseResult responseResult) {
        byte[] bits = JSONUtil.toJsonStr(responseResult).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.getHeaders().add(Constants.JSON_TYPE_RESPONSE_HEADER_N, Constants.JSON_TYPE_RESPONSE_HEADER_V);
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 增加请求头信息,方便跨服务传输
     *
     * @param userMap
     */
    private void addHeaderInfo(ServerHttpRequest request, String token, Map<String, String> userMap) {
        userMap.put(EMPLOYEE_ID, request.getHeaders().getFirst(EMPLOYEE_ID));
        userMap.put(HEADER_SOURCE, Character.toString(token.charAt(TOKEN_PREFIX_INDEX)));
        userMap.put(COMPANY_ID, request.getHeaders().getFirst(COMPANY_ID));
    }


}
