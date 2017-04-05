package com.lsj.seckill.exception;

/**
 * 重复秒杀异常（运行期异常）继承RuntimeException
 * Created by Administrator on 2017/4/4.
 */
public class RepeatKillException extends SeckillException {
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
