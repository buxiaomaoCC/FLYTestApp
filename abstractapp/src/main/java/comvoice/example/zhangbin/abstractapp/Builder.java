package comvoice.example.zhangbin.abstractapp;

/**
 * Created by zhangbin on 2018/1/25.
 */

public abstract class Builder {
    //设置主机
    public abstract void builderBoard(String board);
    //设置操作系统
    public abstract void builderDisplay(String display);
    //设置操作系统
    public abstract void builderOS();
    //创建Computer
    public abstract Computer create();
}
