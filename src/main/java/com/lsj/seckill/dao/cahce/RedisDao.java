package com.lsj.seckill.dao.cahce;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.lsj.seckill.bean.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Leo.li on 2017/4/6.
 */
public class RedisDao {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        this.jedisPool = new JedisPool(ip,port);
    }
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
    public Seckill getSeckill(long seckillId){
        //Redis逻辑操作
        //JedisPoll相当于数据库连接池，Jedis相当于connect
        try {
            try (Jedis jedis = jedisPool.getResource()) {
                String key = "seckill:" + seckillId;
                //并没有实现内部序列化操作
                //get->byte[]->反序列化->Object
                //采用自定义序列化
                //protostuff:需要获取的是一个pojo
                byte[] bytes = jedis.get(key.getBytes());
                //缓存获取到
                if (bytes != null) {
                    //空对象
                    Seckill seckill = schema.newMessage();
                    //Protostuff通过对象的schema把反序列化之后的对象加到空对象seckill中
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    //seckill被反序列化
                    return seckill;
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public String putSeckill(Seckill seckill){
        //set Object(seckill)->序列化->byte[]
        try {
            try (Jedis jedis = jedisPool.getResource()) {
                String key = "seckill:" + seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout = 60 * 60; // 1小时
                return jedis.setex(key.getBytes(), timeout, bytes);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }

        return  null;
    }
}
