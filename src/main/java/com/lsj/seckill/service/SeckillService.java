package com.lsj.seckill.service;

import com.lsj.seckill.bean.Seckill;
import com.lsj.seckill.dto.Exposer;
import com.lsj.seckill.dto.SeckillExecution;
import com.lsj.seckill.exception.RepeatKillException;
import com.lsj.seckill.exception.SeckillCloseException;
import com.lsj.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口:站在使用者角度设计
 * 三个方面:方法定义粒度，参数，返回类型
 * Created by Administrator on 2017/4/4.
 */
public interface SeckillService {
    /**
     * 查询所有秒杀商品
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 根据id查询秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启输出秒杀接口地址
     * 否者输出系统时间和秒杀时间
     * @param seckillId
     * @return
     */
    Exposer exposerSeckillUrl(long seckillId);

    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException;
}
