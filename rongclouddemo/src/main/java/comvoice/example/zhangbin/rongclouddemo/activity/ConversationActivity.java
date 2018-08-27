package comvoice.example.zhangbin.rongclouddemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import comvoice.example.zhangbin.rongclouddemo.R;
import io.rong.imkit.RongIM;

public class ConversationActivity extends AppCompatActivity  {
    private static final String TAG=ConversationActivity.class.getSimpleName();
    private TextView tv_receive;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
//        tv_receive=findViewById(R.id.tv_receive);
        try{
            RongIM.getInstance().startPrivateChat(this,id,"");
        }catch (Exception e){
            Log.e("异常",e.getMessage().toString());
        }

//        OpenTalk();
    }
//    private void OpenTalk(){
//        if(RongIM.getInstance()!=null){
//            RongIM.getInstance().startPrivateChat(this,"1","cc");
//        }
//    }
}
