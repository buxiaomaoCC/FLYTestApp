package comvoice.example.zhangbin.rongclouddemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import comvoice.example.zhangbin.rongclouddemo.R;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class MainActivity extends AppCompatActivity {
    String Token="hyiNmici5sMj5d0jmLBLUzD/7qqHET2TT4DpQOs/9D9gt+kSwyjW37EUm7vKCUfixnHpDSGr9/FH0eUQIeGJIA==";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initConnect();
    }

    private void initConnect() {
        RongIM.connect(Token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取Token
            }

            @Override
            //连接成功
            public void onSuccess(String s) {
                Log.e("MainActivity","success--"+s);//用户ID
                Intent intent=new Intent(MainActivity.this,ConversationActivity.class);
                intent.putExtra("id",s);
                startActivity(intent);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("MainActivity","error--"+errorCode);
            }
        });
    }
}
