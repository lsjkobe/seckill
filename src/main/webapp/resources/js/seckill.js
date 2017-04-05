//存放主要交互的逻辑js代码
//javascript模块化
var seckill = {
    //封装秒杀相关ajax
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    handleSeckillKill: function (seckillId, node) {
        //秒杀逻辑
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl:"+killUrl);
                    //绑定一次点击事件，不用.click用。one
                    $('#killBtn').one('click', function () {
                        //执行秒杀请求
                        //1.禁用按钮
                        $(this).addClass('disabled');
                        //2.发送秒杀请求执行秒杀
                        $.post(killUrl,{},function (result) {
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //3.显示秒杀结果
                                node.html('<span class="label label-success">'+stateInfo+'</span>');
                            }
                        });
                    });
                    node.show();
                } else {
                    //未开启
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countDown(seckillId, now, start, end);
                }
            } else {
                console.assert('result:' + result);
            }
        });
    },
    //验证手机号 isNaN,非数字返回true
    validatePhone: function (phone) {
        return phone && phone.length == 11 && !isNaN(phone);
    },
    countDown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        //时间判断
        if (nowTime >= endTime) {
            //秒杀结束
            seckillBox.html('秒杀结束');
        } else if (nowTime < startTime) {
            //秒杀未开始,计时事件绑定
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
                //时间完成后回掉函数
            }).on('finish.countdown', function () {
                //获取秒杀地址，执行秒杀
                seckill.handleSeckillKill(seckillId, seckillBox);
            });
        } else {
            //秒杀开始
            seckill.handleSeckillKill(seckillId, seckillBox);
        }
    },
    detail: {
        init: function (params) {
            //手机验证和登录，计时交互
            //规划我们的交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie("killPhone");
            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                var killPhoneModel = $('#killPhoneModal');
                //绑定phone
                //控制输出
                killPhoneModel.modal({
                    show: true,//显示弹出层
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false//关闭键盘时间
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    console.log('inputPhone=' + inputPhone);
                    if (seckill.validatePhone(inputPhone)) {
                        //电话写入cookie
                        //{expires:7,path:'/seckill'}有效期7天和只在/seckill的路径下才生效
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'})
                        //刷新页面
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
                    }
                });
            }
            //已经登录
            //计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    //时间判断
                    seckill.countDown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result:' + result);
                }
            });
        }
    }
}
