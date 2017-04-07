package com.lsj.seckill.service.impl;

import com.lsj.seckill.bean.Seckill;
import com.lsj.seckill.bean.SuccessKilled;
import com.lsj.seckill.dao.SeckillDao;
import com.lsj.seckill.dao.SuccessKilledDao;
import com.lsj.seckill.dao.cahce.RedisDao;
import com.lsj.seckill.dto.Exposer;
import com.lsj.seckill.dto.SeckillExecution;
import com.lsj.seckill.enums.SeckillStatEnum;
import com.lsj.seckill.exception.RepeatKillException;
import com.lsj.seckill.exception.SeckillCloseException;
import com.lsj.seckill.exception.SeckillException;
import com.lsj.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/4.
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillDao seckillDao;

    @Resource
    private SuccessKilledDao successKilledDao;

    @Resource
    public RedisDao redisDao;

    //生成md5的混淆
    private final String slat = "fjhfkajsflkGISJDFjkigjkjgfkJKfPOUHMuiioat849584@#$%^";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exposerSeckillUrl(long seckillId) {
        //优化点：缓存优化:超时的基础上维护一致性
        //1访问redis（需要开启redisw服务）
        //解决当一个热门商品被大量访问时所造成的访问数据库开销
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            seckill = this.getById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                redisDao.putSeckill(seckill);
            }
        }
//        Seckill seckill = this.getById(seckillId);
//        if(seckill == null){
//            return new Exposer(false, seckillId);
//        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId); //TODD
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    /**
     * 事务只有运行期的异常才会rock back
     * 使用注解控制事务方法的优点
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其它网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3。不是所有的方法都需要事务，如只有一条数据擦修改操作式，只读操作不需要事务管理
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        Date nowTime = new Date();
        //减库存
        try {
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                //减库存（因为update会有行级锁，所以把update放到insert之后）
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    //没有记录，秒杀结束
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);

                }
            }


        } catch (SeckillCloseException | RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            //所以编译期异常转化成运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
//        return null;
    }

    @Override
    public SeckillExecution executeSeckillByProcedure(long seckillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        Date killDate = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killDate);
        map.put("result", null);
        try {
            seckillDao.killByProcedure(map);
            //获取result
            int result = MapUtils.getInteger(map, "result", -2);
            if (result == 1) {
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,successKilled);
            }else{
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }
}
