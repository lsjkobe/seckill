package com.lsj.seckill.exception;

/**
 *
 * Created by Administrator on 2017/4/4.
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
