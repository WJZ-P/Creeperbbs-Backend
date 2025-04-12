package me.wjz.creeperhub.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SnowFlake {
    /**
     * 分布式ID生成器（雪花算法改进版）
     * 结构：符号位(1) + 时间戳(41) + 数据中心ID(5) + 机器ID(5) + 序列号(12)=64位
     * 支持时钟回拨检测和有限等待
     */
    private static final long EPOCH = 1735660800000L; // 2025-01-01 00:00:00
    private static final long SEQUENCE_BITS = 12L;//序列位
    private static final long WORKER_BITS = 5L;//机器ID位
    private static final long DATA_CENTER_BITS = 5L;//数据中心ID位

    // 各部分的左移位数
    private final static long WORKER_SHIFT = SEQUENCE_BITS;
    private final static long DATA_CENTER_SHIFT = SEQUENCE_BITS + WORKER_BITS;
    private final static long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_BITS + DATA_CENTER_BITS;
    // 时钟回拨容忍阈值（5ms）

    @Value("${app.snowflake.max-backward-ms}")
    private long maxBackwardMs = 5;
    @Value("${app.snowflake.datacenter-id}")
    private long dataCenterId;  // 数据中心ID
    @Value("${app.snowflake.worker-id}")
    private long workerId;      // 机器ID
    private long sequence = 0L;       // 序列号
    private long lastTimestamp = -1L; // 上次生成时间戳

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();//当前时间戳
        // 时钟回拨检测
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset <= maxBackwardMs) {
                try {
                    // 在容忍阈值内等待
                    wait(offset << 1);
                    timestamp = System.currentTimeMillis();
                    if (timestamp < lastTimestamp) {
                        throw new RuntimeException("时钟回拨异常，拒绝生成ID");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("时钟回拨等待中断", e);
                }
            } else {
                throw new RuntimeException(
                        String.format("时钟回拨过大（%dms），拒绝生成ID", offset));
            }
        }

        //同一毫秒内序列递增
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & (1 << SEQUENCE_BITS) - 1;
            if(sequence==0){
                //说明当前毫秒的数据已经用完了，要等待下一毫秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        else sequence=0L;

        lastTimestamp = timestamp;
        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (dataCenterId << DATA_CENTER_SHIFT)
                | (workerId << WORKER_SHIFT)
                | sequence;
    }

    /**
     * 等待到下一毫秒
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
