
CREATE DATABASE seckkill;

use seckill;

CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` VARCHAR(120) NOT NULL COMMENT '商品名称',
`number` int NOT NULL COMMENT '库存数量',
`create_time` TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`start_time` TIMESTAMP NOT NULL COMMENT '秒杀开启时间',
`end_time` TIMESTAMP NOT NULL COMMENT '秒杀结束使时间',
PRIMARY  KEY(seckill_id),
KEY idx_strat_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT '秒杀库存表';

-- 初始化数据
INSERT INTO seckill(name, number, start_time, end_time)
VALUES
('1000元秒杀iphone6', 100, '2017-4-4 00:00:00', '2017-4-5 00:00:00'),
('500元秒杀ipad2', 200, '2017-4-4 00:00:00', '2017-4-5 00:00:00'),
('300元秒杀小米4', 300, '2017-4-4 00:00:00', '2017-4-5 00:00:00'),
('200元秒杀红米note', 400, '2017-4-4 00:00:00', '2017-4-5 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关的信息
CREATE TABLE success_killed(
`seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
`user_phone` bigint NOT NULL COMMENT '用户手机',
`state` tinyint NOT NULL DEFAULT -1 COMMENT '状态标示:-1:无效 0:成功 1:已付款',
`create_time` TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id, user_phone),/*联合主键 */
KEY idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';
