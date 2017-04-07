package com.lsj.seckill.dao;

import com.lsj.seckill.bean.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/4.
 */
public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId")long seckillId, @Param("killTime") Date killTime);

    /**
     *根据id查询秒杀记录
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offet
     * @param limit
     * @return
     */
    //Parameter 'offet' not found. Available parameters are [arg0, limit, param1, param2]错误用@Param解决
    List<Seckill> queryAll(@Param("offet")int offet,@Param("limit") int limit);

    /**
     * 使用存储过程执行秒杀
     * @param paramMap
     */
    void killByProcedure(Map<String, Object> paramMap);
}
