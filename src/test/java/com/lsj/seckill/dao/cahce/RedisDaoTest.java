package com.lsj.seckill.dao.cahce;

import com.lsj.seckill.bean.Seckill;
import com.lsj.seckill.dao.SeckillDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Leo.li on 2017/4/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({("classpath*:spring/spring-dao.xml")})
public class RedisDaoTest {
    @Resource
    private RedisDao redisDao;

    @Resource
    private SeckillDao seckillDao;

    @Test
    public void getSeckill() throws Exception {
        long id = 1000L;
        Seckill seckill = redisDao.getSeckill(id);
        if (seckill == null) {
            seckill = seckillDao.queryById(id);
            if(seckill != null){
                String result = redisDao.putSeckill(seckill);
                System.out.println(result);
                Seckill seckill1 = redisDao.getSeckill(seckill.getSeckillId());
                System.out.println(seckill1);
            }

        }
    }


}