package comvoice.example.zhangbin.startgetimage;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_img;
    private Button bt_copy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initClick();
    }

    private void initClick() {
        bt_img.setOnClickListener(this);
        bt_copy.setOnClickListener(this);
    }

    private void initView() {
        bt_copy=findViewById(R.id.bt_copy);
        bt_img=findViewById(R.id.bt_img);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_copy:

                break;
            case R.id.bt_img:
                try {
                    PackageInfo packageInfo=getPackageManager().getPackageInfo("com.fang.li",0);
                    if(packageInfo!=null){
                        Intent intent=getPackageManager().getLaunchIntentForPackage("com.fang.li");
                        startActivity(intent);
                    }else {
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(this, "请先安装图像获取软件", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


}
