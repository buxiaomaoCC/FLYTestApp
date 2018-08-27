package comvoice.example.zhangbin.singleton.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import comvoice.example.zhangbin.singleton.R;
import comvoice.example.zhangbin.singleton.Singleton.Enum;
import comvoice.example.zhangbin.singleton.Singleton.Singlenton;

public class MainActivity extends AppCompatActivity {
    private Button bt_singleton;
    private TextView tv_show;
    Singlenton singlenton=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bt_singleton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_show.setText(Enum.INSTANCE.getE());
            }
        });
    }

    private void initView() {
        bt_singleton=findViewById(R.id.bt_singleton);
        tv_show=findViewById(R.id.tv_show);
//        singlenton=new Singlenton();
//        singlenton.setName("忙忙忙，忙出什么所以然");
        Enum.INSTANCE.e="我是枚举单例模式";

    }
}
