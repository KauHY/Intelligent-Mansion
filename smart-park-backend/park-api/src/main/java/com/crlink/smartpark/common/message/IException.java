package com.crlink.smartpark.common.message;


public interface IException {
    IMessage getFirstErrorMessage();

    IMessage getErrorMessage();
}
