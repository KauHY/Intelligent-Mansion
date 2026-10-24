package com.crlink.smartpark.common.message;


public class MessageHelper {

    private static final Message message = Message.code("10001", new Object[0]);

    public MessageHelper() {
    }

    static Message defaultOne() {
        return message;
    }
}
