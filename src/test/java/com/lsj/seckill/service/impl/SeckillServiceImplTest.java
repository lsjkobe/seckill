package com.lsj.seckill.service.impl;

import com.lsj.seckill.bean.Seckill;
import com.lsj.seckill.dao.SeckillDao;
import com.lsj.seckill.dto.Exposer;
import com.lsj.seckill.dto.SeckillExecution;
import com.lsj.seckill.exception.RepeatKillException;
import com.lsj.seckill.exception.SeckillCloseException;
import com.lsj.seckill.exception.SeckillException;
import com.lsj.seckill.service.SeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/4/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring/spring-*.xml")
public class SeckillServiceImplTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() throws Exception {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void exposerSeckillUrl() throws Exception {
        long id = 1001L;
        Exposer exposer = seckillService.exposerSeckillUrl(id);
        logger.info("exposer={}",exposer);
        if(exposer.isExposed()){
            executeSeckill(exposer.getMd5());
        }
    }

    public void executeSeckill(String md5) throws Exception {
        long id = 1001L;
        long phone = 13726226697L;

        SeckillExecution seckillExecution = null;
        try {
            seckillExecution = seckillService.executeSeckill(id,phone, md5);
        } catch (RepeatKillException | SeckillCloseException e) {
            logger.error(e.getMessage());
        }
        logger.info("seckillExecution={}", seckillExecution);
    }

}