package com.imooc.seckill.redis;

public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds;
    private String prefix;

    public BasePrefix(int expireSeconds, String prefix){

    }
    public BasePrefix(String prefix){
        //this(0,prefix);//默认使用0，不会过期
        this.expireSeconds=0;
        this.prefix=prefix;
    }
    //默认0代表永不过期
    public int expireSeconds(){return expireSeconds;}

    //前缀为类名:+prefix
    public String getPrefix(){
        String className=getClass().getSimpleName();
        return className+":"+prefix;
    }


}
