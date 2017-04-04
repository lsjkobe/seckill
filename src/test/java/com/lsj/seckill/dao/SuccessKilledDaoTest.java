package com.lsj.seckill.dao;

import com.lsj.seckill.bean.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/4/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({("classpath*:spring/spring-dao.xml")})
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception {
        Long id = 1000L;
        Long phone = 13726226699L;
        int insertCount = successKilledDao.insertSuccessKilled(id,phone);
        System.out.println("---------------------"+insertCount);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        Long id = 1000L;
        Long phone = 13726226699L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println("---------------------"+successKilled.toString());
    }

}