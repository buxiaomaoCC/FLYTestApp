package comvoice.example.zhangbin.httpget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_test,bt_addgirl;
    OkHttpClient okHttpClient;
    private ImageView iv,iv1,iv2;
    String msg;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initClick();
    }

    private void initClick() {
        bt_test.setOnClickListener(this);
        bt_addgirl.setOnClickListener(this);
    }

    private void initView() {
        iv=findViewById(R.id.iv);
        tv=findViewById(R.id.tv);
        iv1=findViewById(R.id.iv1);
        iv2=findViewById(R.id.iv2);
        bt_test=findViewById(R.id.bt_test);
        bt_addgirl=findViewById(R.id.bt_addgirl);
        okHttpClient=new OkHttpClient();
    }
    //查询全部
    private void initOkhttp(){
        Request request=new Request.Builder().url("http://192.168.2.122:8080/girls/girls").build();
        okHttpClient.newCall(request).enqueue(new MyClent());
    }
    //添加 一个
    private void initAdd(){
        Request request=new Request.Builder().url("http://192.168.2.122:8080/girls/addGirls{李四,26}").build();
        okHttpClient.newCall(request).enqueue(new MyClent());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_test:
                initOkhttp();
                break;
            case R.id.bt_addgirl:
                initAdd();
                break;
        }
    }

    class MyClent implements Callback{

        @Override
        public void onFailure(Call call, IOException e) {
            Toast.makeText(MainActivity.this, "访问失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            msg=response.body().string();
//            final Bitmap bitmap=getURLimage(msg);
//            Glide.with(MainActivity.this).load(msg).into(iv2);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                    Log.e("返回值",msg+"");
                    tv.setText(""+msg);
//                    iv.setImageBitmap(bitmap);
                }
            });
        }
    }
    private Bitmap getURLimage(String url){
        Bitmap bitmap=null;
        try {
            URL myurl=new URL(url);
            //获得连接
            HttpURLConnection conn= (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setUseCaches(false);//设置是否缓存
            conn.connect();
            InputStream inputStream=conn.getInputStream();
            bitmap= BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
