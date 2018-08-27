package comvoice.example.zhangbin.flysocketclient;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Socket socket = null;
    String buffer = "";
    TextView tv_show;
    Button bt_send,bt_scan;
    EditText et_input;
    String geted1;
    RxPermissions rxPermissions;
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x11) {
                Bundle bundle = msg.getData();
                tv_show.append("server:"+bundle.getString("msg")+"\n");
                startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class), 1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initClick();
    }

    private void initClick() {
        bt_scan.setOnClickListener(this);
        bt_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_scan:
                startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class), 1);
                break;
            case R.id.bt_send:
                geted1 = et_input.getText().toString();
                tv_show.append("client:"+geted1+"\n");
                //启动线程 向服务器发送和接收信息
                new MyThread(geted1).start();
                break;
             default:
                    break;
        }
    }
    private void initView() {
        tv_show = (TextView) findViewById(R.id.tv_show);
        bt_send = (Button) findViewById(R.id.bt_send);
        et_input = (EditText) findViewById(R.id.et_input);
        bt_scan=findViewById(R.id.bt_scan);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
           if(requestCode==1){
                new MyThread(data.getStringExtra("text")).start();
            }
        }
    }
    class MyThread extends Thread {

        public String txt1;

        public MyThread(String str) {
            txt1 = str;
        }

        @Override
        public void run() {
            //定义消息
            Message msg = new Message();
            msg.what = 0x11;
            Bundle bundle = new Bundle();
            bundle.clear();
            try {
                //连接服务器 并设置连接超时为1秒
                socket = new Socket();
                socket.connect(new InetSocketAddress("192.168.1.171", 30000), 1000); //端口号为30000
                //获取输入输出流
                OutputStream ou = socket.getOutputStream();
                BufferedReader bff = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                //读取发来服务器信息
                String line = null;
                buffer="";
                while ((line = bff.readLine()) != null) {
                    buffer = line + buffer;
                }

                //向服务器发送信息
                ou.write(txt1.getBytes("utf-8"));
                ou.flush();
                bundle.putString("msg", buffer.toString());
                msg.setData(bundle);
                //发送消息 修改UI线程中的组件
                myHandler.sendMessage(msg);
                //关闭各种输入输出流
                bff.close();
                ou.close();
                socket.close();

            } catch (SocketTimeoutException aa) {
                //连接超时 在UI界面显示消息
                bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
                msg.setData(bundle);
                //发送消息 修改UI线程中的组件
                myHandler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
