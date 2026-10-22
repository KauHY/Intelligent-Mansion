package com.crlink.smartpark.core;

import cn.hutool.db.sql.SqlFormatter;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.SystemClock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})})
public class PerformanceInterceptor implements Interceptor {
    /**
     * SQL 执行最大时长，超过自动停止运行，有助于发现问题。
     */
    private long maxTime = 0;
    /**
     * SQL 是否格式化
     */
    private boolean format = false;
    /**
     * 是否写入日志文件<br>
     * true 写入日志文件，不阻断程序执行！<br>
     * 超过设定的最大执行时长异常提示！
     */
    private boolean writeInLog = false;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Statement statement;
        Object firstArg = invocation.getArgs()[0];
        if (Proxy.isProxyClass(firstArg.getClass())) {
            statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
        } else {
            statement = (Statement) firstArg;
        }
        MetaObject stmtMetaObj = SystemMetaObject.forObject(statement);
        try {
            statement = (Statement) stmtMetaObj.getValue("stmt.statement");
        } catch (Exception e) {
            //log.error("error:", e);
            // do nothing
        }
        if (stmtMetaObj.hasGetter("delegate")) {//Hikari
            try {
                statement = (Statement) stmtMetaObj.getValue("delegate");
            } catch (Exception e) {

            }
        }

        String originalSql = null;
        if (originalSql == null) {
            originalSql = statement.toString();
        }
        originalSql = originalSql.replaceAll("[\\s]+", " ");
        int index = indexOfSqlStart(originalSql);
        if (index > 0) {
            originalSql = originalSql.substring(index);
        }

        // 计算执行 SQL 耗时
        long start = SystemClock.now();
        Object result = invocation.proceed();
        long timing = SystemClock.now() - start;

        // 格式化 SQL 打印执行结果
        Object target = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(target);
        MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        StringBuilder formatSql = new StringBuilder();
        formatSql.append(" Time：").append(timing);
        formatSql.append(" ms - ID：").append(ms.getId());
        formatSql.append("\n Execute SQL：").append(sqlFormat(originalSql, format)).append("\n");
        if (this.isWriteInLog()) {
            if (this.getMaxTime() >= 1 && timing > this.getMaxTime()) {
                log.error(formatSql.toString());
            } else {
                log.debug(formatSql.toString());
            }
        } else {
            System.err.println(formatSql);
            if (this.getMaxTime() >= 1 && timing > this.getMaxTime()) {
                throw new RuntimeException(" The SQL execution time is too large, please optimize ! ");
            }
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties prop) {
        String maxTime = prop.getProperty("maxTime");
        String format = prop.getProperty("format");
        if (StringUtils.isNotEmpty(maxTime)) {
            this.maxTime = Long.parseLong(maxTime);
        }
        if (StringUtils.isNotEmpty(format)) {
            this.format = Boolean.valueOf(format);
        }
    }

    public long getMaxTime() {
        return maxTime;
    }

    public PerformanceInterceptor setMaxTime(long maxTime) {
        this.maxTime = maxTime;
        return this;
    }

    public boolean isFormat() {
        return format;
    }

    public PerformanceInterceptor setFormat(boolean format) {
        this.format = format;
        return this;
    }

    public boolean isWriteInLog() {
        return writeInLog;
    }

    public PerformanceInterceptor setWriteInLog(boolean writeInLog) {
        this.writeInLog = writeInLog;
        return this;
    }

    public Method getMethodRegular(Class<?> clazz, String methodName) {
        if (Object.class.equals(clazz)) {
            return null;
        }
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return getMethodRegular(clazz.getSuperclass(), methodName);
    }

    /**
     * 获取sql语句开头部分
     *
     * @param sql
     * @return
     */
    private int indexOfSqlStart(String sql) {
        String upperCaseSql = sql.toUpperCase();
        Set<Integer> set = new HashSet<>();
        set.add(upperCaseSql.indexOf("SELECT "));
        set.add(upperCaseSql.indexOf("UPDATE "));
        set.add(upperCaseSql.indexOf("INSERT "));
        set.add(upperCaseSql.indexOf("DELETE "));
        set.remove(-1);
        if (CollectionUtils.isEmpty(set)) {
            return -1;
        }
        List<Integer> list = new ArrayList<>(set);
        Collections.sort(list, Integer::compareTo);
        return list.get(0);
    }

    /**
     * 格式sql
     *
     * @param boundSql
     * @param format
     * @return
     */
    public static String sqlFormat(String boundSql, boolean format) {
        if (format) {
            try {
                return SqlFormatter.format(boundSql);
            } catch (Exception ignored) {
            }
        }
        return boundSql;
    }
}
