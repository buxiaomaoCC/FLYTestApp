package comvoice.example.zhangbin.abstractapp;

/**
 * Created by zhangbin on 2018/1/25.
 */

public class MacbookBuilder extends Builder {
    private Computer computer=new Macbook();
    @Override
    public void builderBoard(String board) {
        computer.setBoard(board);
    }

    @Override
    public void builderDisplay(String display) {
        computer.setDisplay(display);
    }

    @Override
    public void builderOS() {
        computer.setmOS();
    }

    @Override
    public Computer create() {
        return computer;
    }

}
