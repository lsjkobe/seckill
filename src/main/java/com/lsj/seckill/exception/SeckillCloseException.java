package com.lsj.seckill.exception;

/**
 * 秒杀系统关闭后继续执行异常
 * Created by Administrator on 2017/4/4.
 */
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
