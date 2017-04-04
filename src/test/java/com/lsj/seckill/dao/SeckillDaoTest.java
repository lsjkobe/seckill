package com.lsj.seckill.dao;

import com.lsj.seckill.bean.Seckill;
import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/4/4.
 * 配置spring与junit整合
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({("classpath*:spring/spring-dao.xml")})
public class SeckillDaoTest {
    @Resource
    public SeckillDao seckillDao;
    @Test
    public void reduceNumber() throws Exception {
        Date killDate = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, killDate);
        System.out.println("------------------------------"+updateCount);
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println("--------------------------------------"+seckill.toString());
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (int i = 0; i < seckills.size(); i++) {
            System.out.println("---------------------------"+seckills.get(i).toString());
        }
    }

}