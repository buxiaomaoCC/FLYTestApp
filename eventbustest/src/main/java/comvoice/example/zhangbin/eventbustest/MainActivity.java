package comvoice.example.zhangbin.eventbustest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    private Button bt_skip;
    private TextView tv_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        tv_show.setText("我是原始数据");
        EventBus.getDefault().register(this);
        initClick();
    }
    private void initClick() {
        bt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(intent);
            }
        });
    }
    private void initView() {
        bt_skip=findViewById(R.id.bt_skip);
        tv_show=findViewById(R.id.tv_show);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handMessage(EventBean eventBean){
        //逻辑处理写在这里
        tv_show.setText(eventBean.getName());
    }
}
