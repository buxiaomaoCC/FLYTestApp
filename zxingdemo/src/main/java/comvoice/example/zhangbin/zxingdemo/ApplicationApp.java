package comvoice.example.zhangbin.zxingdemo;


import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class ApplicationApp extends Application {
	private List<Activity> list=new LinkedList<Activity>();
	public static ApplicationApp app;
//	public static SharedPreferences preferences;
//	public Object obj;
	public boolean falgs;
	public  boolean update; //是否只在wifi下开启更新
	public boolean openLock;//是否打开应用锁
	public static String ipAdd="218.201.212.6";
	public static int port=1234;
	
	
	
	public void addActivity(Activity activity){
		list.add(activity);
	}
	public void removeActivity(Activity activity){
		list.remove(activity);
	}
	public void exit(){
		for(Activity activity:list){
			activity.finish();
		}
		System.exit(0);;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		app=this;
		
		
		
//		preferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
		// 注册crashHandler全局异常捕获
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}
	public boolean isUpdate() {
		return update;
	}
	public void setUpdate(boolean update) {
		this.update = update;
	}
	public boolean isOpenLock() {
		return openLock;
	}
	public void setOpenLock(boolean openLock) {
		this.openLock = openLock;
	}
	
}
