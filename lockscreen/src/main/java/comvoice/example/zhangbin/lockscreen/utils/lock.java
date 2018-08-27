package comvoice.example.zhangbin.lockscreen.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhangbin on 2018/2/6.
 */

public class lock {
    private static SharedPreferences sharedPreferences;
    private static String identifying;

    /**
     * 设置是否使用手势密码
     */
    public static void putScreen(Context context,String key,int value){
        if(sharedPreferences==null){
            sharedPreferences=context.getSharedPreferences(identifying,Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putInt(key,value).commit();
    }
    /**
     * 得到储存的状态码，1为使用手势密码，0为不使用
     */
    public static int getScreen(Context context,String key,int defValue){
        if(sharedPreferences==null){
            sharedPreferences=context.getSharedPreferences(identifying,Context.MODE_PRIVATE);
        }
        return sharedPreferences.getInt(key,defValue);
    }


}
