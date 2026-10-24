package com.crlink.smartpark.common.message;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.PropertyKey;

import static com.crlink.smartpark.common.constants.Constants.GLOBALIZATION_RESOURCE_PREFIX;

@Getter
public class ResolinkRuntimeException extends RuntimeException implements IException {
    private static final long serialVersionUID = -3300742783344720503L;
    private Message realMessage = null;

    public ResolinkRuntimeException(Throwable th) {
        super(th);
        if (th instanceof IException) {
            IMessage errorMessage = ((IException) th).getErrorMessage();
            if (null != errorMessage) {
                this.realMessage = Message.of(errorMessage.getCode(), errorMessage.getDisplayText());
            }
        }

        if (null == this.realMessage) {
            this.realMessage = Message.defaultError();
        }

    }

    public ResolinkRuntimeException(@PropertyKey(resourceBundle = GLOBALIZATION_RESOURCE_PREFIX) String key, Object... params) {
        this.realMessage = Message.code(key, params);
    }

    public ResolinkRuntimeException(Integer returnCode, @PropertyKey(resourceBundle = GLOBALIZATION_RESOURCE_PREFIX) String key, Object... params) {
        this.realMessage = Message.code(key, params);
        this.realMessage.setCode(String.valueOf(returnCode));
    }

    public ResolinkRuntimeException(Message message) {
        this.realMessage = message;
    }

    public ResolinkRuntimeException(Throwable th, Message message) {
        super(th);
        this.realMessage = message;
    }

    @Override
    public Message getFirstErrorMessage() {
        return this.getErrorMessage();
    }

    @Override
    public Message getErrorMessage() {
        return null != this.realMessage ? this.realMessage : Message.defaultError();
    }

    @Override
    public String getMessage() {
        Message message = this.getErrorMessage();
        String error;
        if (null != message) {
            error = message.toString();
            if (null != error) {
                return error;
            }
        }

        error = super.getMessage();
        return StringUtils.isNotEmpty(error) ? error : Message.defaultError().toString();
    }
}

