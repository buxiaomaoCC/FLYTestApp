package comvoice.example.zhangbin.rongclouddemo.application;

import android.app.Application;

import io.rong.imkit.RongIM;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //项目已运行，就初始化融云SDK
        RongIM.init(this);
    }
}
