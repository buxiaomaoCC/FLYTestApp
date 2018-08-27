package comvoice.example.zhangbin.singleton.Singleton;

/**
 * Created by zhangbin on 2018/1/19.
 *
 */

public class Singlenton {
    private static Singlenton singlenton;
    private String name;
    public Singlenton(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /*
    * 单例模式-懒汉模式
    * 优点：只有使用时才会被实例化
    * 缺点：每次调用getSinglenton都会进行同步，浪费资源
     */
    public static synchronized Singlenton getSinglenton(){
        if(singlenton==null){
            singlenton=new Singlenton();
        }
        return singlenton;
    }

    /*
    * 双重校验锁（使用较多）
    * 优点：资源利用率高，第一次执行getInstance时单例对象才会被实例化，效率高
    * 缺点：第一次加载时反应稍慢
     */
    public static Singlenton DoubleSinglenton(){
        if(singlenton==null){
            synchronized (Singlenton.class){
                if(singlenton==null){
                    singlenton=new Singlenton();
                }
            }
        }
        return singlenton;
    }
    /**
     * 静态内部类
     * 优点：确保线程安全，保证单例对象的唯一性，延迟了单例的实例化
     */
    public static Singlenton InnerSinglenton(){
        return SingleyonHolder.sInstance;
    }
    private static class SingleyonHolder{
        private static final Singlenton sInstance=new Singlenton();
    }
    /**
     * 枚举单例
     *
     */
}
