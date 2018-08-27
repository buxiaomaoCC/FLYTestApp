package comvoice.example.zhangbin.abstractapp;

/**
 * Created by zhangbin on 2018/1/26.
 */

public class Diretor {
    Builder builder=null;
    public Diretor(Builder builder){
        this.builder=builder;
    }
    /**
     * 构建对象
     */
    public void construct(String board,String display){
        builder.builderBoard(board);
        builder.builderDisplay(display);
        builder.builderOS();
    }
}
