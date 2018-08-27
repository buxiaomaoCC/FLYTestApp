package comvoice.example.zhangbin.lockscreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import comvoice.example.zhangbin.lockscreen.utils.Const;
import comvoice.example.zhangbin.lockscreen.utils.lock;

public class Main2Activity extends AppCompatActivity {
    private TextView tv_setting;
    //再按一次退出程序
    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        final int screenLock=lock.getScreen(Main2Activity.this,"sreen",-1);
        Log.e("temp1",Const.temp+"");
        if(Const.temp==0){
            Log.e("temp2",screenLock+"");
            if(screenLock==1){
                Intent intent=new Intent(Main2Activity.this,SecondActivity.class);
                startActivity(intent);
            }
        }
        initClick();
    }

    private void initClick() {
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Main2Activity.this);
                builder.setTitle("密码设置");
                builder.setMessage("是否开启手势密码?");
                builder.setPositiveButton("不开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lock.putScreen(Main2Activity.this,"sreen",0);
                    }
                });
                builder.setNegativeButton("开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       Intent intent=new Intent(Main2Activity.this,MainActivity.class);
                       startActivity(intent);
                    }
                });
                builder.show();
            }
        });
    }

    private void initView() {
        tv_setting=findViewById(R.id.tv_setting);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 1000) {
                Toast.makeText(this,getString(R.string.setting_quit), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                Const.temp=0;
                finish();
            }
            return true;

        }
        return super.onKeyDown(keyCode, event);

    }
}
