package comvoice.example.zhangbin.gridviewver;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    LayoutInflater inflater;
    ArrayList<AppInfo> listInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView)findViewById(R.id.gridView1);
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflater = LayoutInflater.from(this);
        listInfo = new ArrayList<AppInfo>();
        for(int i=0;i<12;i++){
            AppInfo appInfo = new AppInfo();
            appInfo.rid = R.drawable.c1;
            appInfo.title = "001";
            listInfo.add(appInfo);
        }
        int size = listInfo.size();
        int length = 100;
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length)*(density) )/2;
        Item.getItem().setWidth(dm.widthPixels);
        int itemWidth = (dm.widthPixels-80)/5;
//        int itemWidth = (int) (length * density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(6); // 设置列数量=列表集合数
        //gridView.setAdapter(new MyAdapter());
        gridView.setAdapter(new MyGridAdapter(this,listInfo));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "顺序"+i, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
