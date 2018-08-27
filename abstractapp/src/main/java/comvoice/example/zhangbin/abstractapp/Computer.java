package comvoice.example.zhangbin.abstractapp;

/**
 * Created by zhangbin on 2018/1/25.
 */

public abstract class Computer {
    protected String mBoard;
    protected String mDisplay;
    protected String mOS;
    protected Computer(){

    }
    //设置主板
    public void setBoard(String mBoard){
        this.mBoard=mBoard;
    }
    //设置显示器
    public void setDisplay(String mDisplay){
        this.mDisplay=mDisplay;
    }
    //设置操作系统
    public abstract void setmOS();

    @Override
    public String toString() {
        return "Computer[mBoard="+mBoard+"]";
    }

}
