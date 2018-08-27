package comvoice.example.zhangbin.mojidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button bt_show;
    private IndexHorizontalScrollView indexHorizontalScrollView;
    private Today24HourView today24HourView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        indexHorizontalScrollView = (IndexHorizontalScrollView)findViewById(R.id.indexHorizontalScrollView);
        today24HourView = (Today24HourView)findViewById(R.id.today24HourView);
        bt_show=findViewById(R.id.bt_show);
        bt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexHorizontalScrollView.setToday24HourView(today24HourView);
            }
        });

    }
}
