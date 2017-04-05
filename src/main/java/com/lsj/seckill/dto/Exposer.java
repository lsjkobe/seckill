package com.lsj.seckill.dto;

/**
 * 暴露秒杀系统地址DTO
 * Created by Administrator on 2017/4/4.
 */
public class Exposer {
    private boolean exposed;
    //加密
    private String md5;
    private long seckilled;
    //系统当前时间（毫秒）
    private long now;
    //开始时间
    private long start;
    //结束时间
    private long end;
    public Exposer(boolean exposed, String md5, long seckilled){
        this.exposed = exposed;
        this.md5 = md5;
        this.seckilled = seckilled;
    }
    public Exposer(boolean exposed, long seckilled, long now, long start, long end){
        this.exposed = exposed;
        this.seckilled = seckilled;
        this.now = now;
        this.start = start;
        this.end = end;
    }
    public Exposer(boolean exposed, long seckilled){
        this.exposed = exposed;
        this.seckilled = seckilled;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckilled() {
        return seckilled;
    }

    public void setSeckilled(long seckilled) {
        this.seckilled = seckilled;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", seckilled=" + seckilled +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
