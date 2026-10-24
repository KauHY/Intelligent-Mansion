package com.crlink.smartpark.common.message;

import lombok.Data;
import org.jetbrains.annotations.PropertyKey;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.crlink.smartpark.common.constants.Constants.GLOBALIZATION_RESOURCE_PREFIX;

@SuppressWarnings("AlibabaConstantFieldShouldBeUpperCase")
@Data
public class Message implements Serializable, IMessage {
    private static final long serialVersionUID = 3017472277626865964L;
    public static final Map<String, Message> cachedMessages = new ConcurrentHashMap(500);
    private String code = null;
    private String displayText = null;

    private Message(String code, String displayMessage) {
        this.code = code;
        this.displayText = displayMessage;
    }

    public static Message defaultError() {
        return MessageHelper.defaultOne();
    }

    public static Message code(@PropertyKey(resourceBundle = GLOBALIZATION_RESOURCE_PREFIX) String key, Object... params) {
        if (params != null && params.length > 0) {
            return MessageCode.toErrorMessage(key, params);
        } else {
            Message message = (Message) cachedMessages.get(key);
            if (message == null) {
                message = MessageCode.toErrorMessage(key, params);
                cachedMessages.put(key, message);
            }

            return of(message.getCode(), message.getDisplayText());
        }
    }

    public static Message of(String code, String displayText) {
        Message m = new Message();
        m.code = code;
        m.displayText = displayText;
        return m;
    }


    public static void clean() {
        cachedMessages.clear();
    }

    public Message() {
    }

    @Override
    public String getCode() {
        return this.code;
    }


}
