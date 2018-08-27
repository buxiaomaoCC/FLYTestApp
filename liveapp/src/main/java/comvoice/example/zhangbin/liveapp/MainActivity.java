package comvoice.example.zhangbin.liveapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lv_show;
    private List<String> mDataList;
    private List<String>mUrlLsit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initClick();
    }

    private void initClick() {
        lv_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,LiveActivity.class);
                intent.putExtra("url",mUrlLsit.get(i));
                intent.putExtra("title",mDataList.get(i));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        mDataList=new ArrayList<>();
        mDataList.add("Newstv搏击");
        mDataList.add("Newstv动画王国");
        mDataList.add("Newstv动作电影");
        mDataList.add("Newstv古装剧场");
        mDataList.add("Newstv海外剧场");
        mDataList.add("Newstv家庭剧场");
        mDataList.add("Newstv金牌综艺");
        mDataList.add("Newstv惊悚悬疑");
        mDataList.add("Newstv精品电影");
        mDataList.add("Newstv军旅剧场");
        mDataList.add("Newstv爱情喜剧");
        mUrlLsit=new ArrayList<>();
        mUrlLsit.add("http://223.110.245.134/PLTV/3/224/3221226656/index.m3u8");
        mUrlLsit.add("http://183.207.249.15/PLTV/3/224/3221225555/index.m3u8");
        mUrlLsit.add("http://183.207.249.10/PLTV/3/224/3221225529/index.m3u8");
        mUrlLsit.add("http://183.207.249.14/PLTV/3/224/3221225527/index.m3u8");
        mUrlLsit.add("http://183.207.249.16/PLTV/3/224/3221225547/index.m3u8");
        mUrlLsit.add("http://183.207.249.14/PLTV/3/224/3221225549/index.m3u8");
        mUrlLsit.add("http://183.207.249.16/PLTV/3/224/3221225559/index.m3u8");
        mUrlLsit.add("http://183.207.249.7/PLTV/3/224/3221225561/index.m3u8");
        mUrlLsit.add("http://183.207.249.14/PLTV/3/224/3221225567/index.m3u8");
        mUrlLsit.add("http://183.207.249.15/PLTV/3/224/3221225531/index.m3u8");
        mUrlLsit.add("http://183.207.249.12/PLTV/3/224/3221225525/index.m3u8");

        lv_show.setAdapter(new LiveAdapter(MainActivity.this,mDataList));
    }

    private void initView() {
        lv_show=findViewById(R.id.lv_show);
    }

}
