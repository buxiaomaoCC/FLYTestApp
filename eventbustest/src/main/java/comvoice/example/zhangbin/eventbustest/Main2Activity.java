package comvoice.example.zhangbin.eventbustest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;


public class Main2Activity extends AppCompatActivity {
    private Button bt_return;
    private TextView tv_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        initClick();
    }

    private void initClick() {
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new EventBean("我是返回的数据"));
                finish();
            }
        });
    }

    private void initView() {
        bt_return=findViewById(R.id.bt_return);
        tv_return=findViewById(R.id.tv_return);
    }
}
