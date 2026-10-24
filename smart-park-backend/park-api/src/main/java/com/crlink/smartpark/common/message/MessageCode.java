package com.crlink.smartpark.common.message;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import static com.crlink.smartpark.common.constants.Constants.DEFAULT_CHARSET;
import static com.crlink.smartpark.common.constants.Constants.GLOBALIZATION_RESOURCE_PREFIX;

public class MessageCode {
    private static final Logger log = LoggerFactory.getLogger(MessageCode.class);
    private static String APP_I18N_CODE = "zh_CN";
    private static Map<String, Object> allDisplayErrorCodes = new ConcurrentHashMap();
    private static Map<Object, Object> displayErrorCodes = new ConcurrentHashMap();
    private static Map<Object, Object> internalErrorMessage = new ConcurrentHashMap();
    private static Map<Object, Object> readableErrorCode = new ConcurrentHashMap();
    private static Map<Object, Object> nonStandardReadableErrorCode = new ConcurrentHashMap();
    private static Map<String, String> cachedLogMessage = new ConcurrentHashMap();

    public MessageCode() {
    }

    public static void init() {
        displayErrorCodes = extractErrorCodes("i18n/infos_zh_CN.properties");
        processReadableErrorCode();
    }

    private static void processReadableErrorCode() {
        nonStandardReadableErrorCode.clear();
        Iterator var0 = readableErrorCode.entrySet().iterator();

        Entry entry;
        String raw;
        while (var0.hasNext()) {
            entry = (Entry) var0.next();
            raw = entry.getValue().toString();

            try {
                String encoded = URLEncoder.encode(raw, DEFAULT_CHARSET);
                if (!raw.equals(encoded)) {
                    nonStandardReadableErrorCode.put(entry.getKey(), raw);
                }
            } catch (UnsupportedEncodingException var5) {
                throw new ResolinkRuntimeException(var5);
            }
        }

        var0 = nonStandardReadableErrorCode.entrySet().iterator();

        while (var0.hasNext()) {
            entry = (Entry) var0.next();
            raw = entry.getValue().toString();

            try {
                readableErrorCode.put(entry.getKey(), URLEncoder.encode(raw, DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException var4) {
                throw new ResolinkRuntimeException(var4);
            }
        }

    }

    public static String logMessage(@PropertyKey(resourceBundle = GLOBALIZATION_RESOURCE_PREFIX) @NotNull String key, Object... params) {
        boolean canNotCache = params != null && params.length > 0;
        String logMessage;
        if (!canNotCache) {
            logMessage = (String) cachedLogMessage.get(key);
            if (logMessage != null) {
                return logMessage;
            }
        }

        logMessage = "log message cannot be retrieved properly";

        try {
            logMessage = searchKeyInAllResourceFile(internalErrorMessage, key, "ERROR MESSAGE IS MISSING", params);
        } catch (Exception var5) {
            log.error(logMessage, var5);
        }

        String result = "{c:" + key + ",m:" + logMessage + "}";
        if (!canNotCache) {
            cachedLogMessage.put(key, result);
        }

        return result;
    }

    public static String displayMessage(@PropertyKey(resourceBundle = GLOBALIZATION_RESOURCE_PREFIX) String key, Object... params) {
        return searchKeyInAllResourceFile(displayErrorCodes, key, "对不起，系统繁忙，请稍候再试", params);
    }

    private static String searchKeyInAllResourceFile(Map<Object, Object> props, String key, String defaultValue, Object... params) {
        if (!props.containsKey(key)) {
            return defaultValue;
        } else {
            Object obj = props.get(key);
            String message = buildMessage(obj, params);
            return StringUtils.isNotBlank(message) ? message : defaultValue;
        }
    }

    private static String buildMessage(Object obj, Object[] params) {
        String message = String.valueOf(obj);
        if (params != null && params.length > 0 && message != null && message.indexOf('{') >= 0) {
            message = MessageFormat.format(message, params);
        }

        if (params != null && params.length > 0 && message != null && message.contains("{0}")) {
            message = MessageFormat.format(message, params);
        }

        return message;
    }


    private static Map<Object, Object> extractErrorCodes(String resourceFilePath) {
        return new HashMap(extractErrorCodes(Thread.currentThread().getContextClassLoader(), resourceFilePath));
    }

    public static Map<Object, Object> extractErrorCodes(ClassLoader classLoader, String resourceFilePath) {
        HashMap props = new HashMap();

        try {
            Properties prop;
            for (Enumeration resources = classLoader.getResources(resourceFilePath); resources.hasMoreElements(); props.putAll(prop)) {
                URL url = (URL) resources.nextElement();
                InputStream in = url.openStream();
                prop = new Properties();
                prop.load(new InputStreamReader(in, DEFAULT_CHARSET));
            }

            return props;
        } catch (IOException var11) {
            log.error("The target resource 【 {} 】 is not available ... ", resourceFilePath);
            return Collections.emptyMap();
        }
    }

    private static Map<Object, Object> extractContextErrorCodes(String resourceFilePath) {
        Map<Object, Object> props = new HashMap();
        props.putAll(extractErrorCodes(Thread.currentThread().getContextClassLoader(), resourceFilePath));
        return props;
    }

    static Message toErrorMessage(@PropertyKey(resourceBundle = GLOBALIZATION_RESOURCE_PREFIX) String key, Object... params) {
        String displayMessage = displayMessage(key, params);
        return Message.of(key, displayMessage);
    }


    public static void setI18n(String i18nCode) {
        APP_I18N_CODE = i18nCode;
        if (allDisplayErrorCodes.keySet().contains(APP_I18N_CODE)) {
            displayErrorCodes = (Map) allDisplayErrorCodes.get(APP_I18N_CODE);
        } else {
            displayErrorCodes = extractContextErrorCodes("i18n/infos_" + APP_I18N_CODE + ".properties");
            allDisplayErrorCodes.put(APP_I18N_CODE, displayErrorCodes);
        }

    }

    public static String getI18nCode() {
        return APP_I18N_CODE;
    }

    static {
        init();
    }
}
