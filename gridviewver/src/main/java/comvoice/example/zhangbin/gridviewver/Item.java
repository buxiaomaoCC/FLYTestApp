package comvoice.example.zhangbin.gridviewver;

public class Item {
    private static Item item;
    private int width;
    public Item(){

    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public static Item getItem(){
        if(item==null){
            synchronized (Item.class){
                if(item==null){
                    item=new Item();
                }
            }
        }
        return item;
    }
}
