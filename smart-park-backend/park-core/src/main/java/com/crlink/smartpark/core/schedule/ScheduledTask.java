package com.crlink.smartpark.core.schedule;

import java.util.concurrent.ScheduledFuture;

public final class ScheduledTask {
    //private String taskId;

    public volatile ScheduledFuture<?> future;

    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
