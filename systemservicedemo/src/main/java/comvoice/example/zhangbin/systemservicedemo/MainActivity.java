package comvoice.example.zhangbin.systemservicedemo;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_netTest,bt_wifiTest,bt_volumeTest,bt_packageTest,bt_cutTest;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater= (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.activity_main,null);
        setContentView(view);
        initView();
        initClick();
    }

    private void initClick() {
        bt_netTest.setOnClickListener(this);
        bt_wifiTest.setOnClickListener(this);
        bt_volumeTest.setOnClickListener(this);
        bt_packageTest.setOnClickListener(this);
        bt_cutTest.setOnClickListener(this);
    }

    private void initView() {
        context=MainActivity.this;
        bt_netTest=findViewById(R.id.bt_netTest);
        bt_wifiTest=findViewById(R.id.bt_wifiTest);
        bt_volumeTest=findViewById(R.id.bt_volumeTest);
        bt_packageTest=findViewById(R.id.bt_packageTest);
        bt_cutTest=findViewById(R.id.bt_cutTest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_netTest:
                boolean net=netTest();
                Toast.makeText(context, ""+net, Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_wifiTest:
                dianliangTest();
                break;
            case R.id.bt_volumeTest:
                volumMax();
                break;
            case R.id.bt_packageTest:
                packAge();
                break;
            case R.id.bt_cutTest:
                cut();
                break;
            default:
                break;
        }
    }
    /**
     * 剪切功能
     */
    private void cut(){
        ClipboardManager clipboardManager= (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData;
        String text="hello world";
        clipData=ClipData.newPlainText("text",text);
        clipboardManager.setPrimaryClip(clipData);

        ClipData clipData1=clipboardManager.getPrimaryClip();
        ClipData.Item item=clipData1.getItemAt(0);
        String test=item.getText().toString();
        Toast.makeText(context, "粘贴数据为："+test, Toast.LENGTH_SHORT).show();
    }
    /**
     * 获取当前包名
     */
    private void packAge(){
       ActivityManager activityManager= (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
       String packAge=activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
        Toast.makeText(context, "当前包名："+packAge, Toast.LENGTH_SHORT).show();
    }

    /**
     * 得到系统音量值
     */
    private void volumMax(){
        if(context!=null){
            AudioManager audioManager= (AudioManager) context.getSystemService(AUDIO_SERVICE);
            int max=audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);//最大音量值
            int now=audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);//当前音量值
            if(now>=1){
                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,now-1,1);//减小音量
            }
            Toast.makeText(context, "系统最大音量值为："+max+",当前音量值为："+now, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断网络服务是否开启
     * @return
     */
    private boolean netTest(){
        if(context!=null){
            ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    /**
     * 开启或关闭wifi
     */
    private void dianliangTest(){
        if(context!=null){
          WifiManager wifiManager= (WifiManager) context.getSystemService(WIFI_SERVICE);
          if(wifiManager.isWifiEnabled()){
              wifiManager.setWifiEnabled(false);
              Toast.makeText(context, "wifi已经关闭", Toast.LENGTH_SHORT).show();
          }else {
              wifiManager.setWifiEnabled(true);
              Toast.makeText(context, "wifi已经打开", Toast.LENGTH_SHORT).show();
          }
        }
    }
}
