package comvoice.example.zhangbin.socketclientdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_ip,et_data;
    private OutputStream outputStream=null;
    private Socket socket=null;
    private String ip;
    private String data;
    private Button bt_connect,bt_send;
    private boolean socketStatus=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String encoding = System.getProperty("file.encoding");
        Log.e("编码",encoding);
        initView();
        initClick();
    }

    private void initClick() {
        bt_connect.setOnClickListener(this);
        bt_send.setOnClickListener(this);
    }

    private void initView() {
        et_ip=findViewById(R.id.et_ip);
        et_data=findViewById(R.id.et_data);
        bt_connect=findViewById(R.id.bt_connect);
        bt_send=findViewById(R.id.bt_send);
    }
    private void connect(){
        ip=et_ip.getText().toString();
        if(ip==null){
            Toast.makeText(this, "请输入IP地址", Toast.LENGTH_SHORT).show();
        }
        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                if(!socketStatus){
                    try {
                        socket=new Socket(ip,8000);
                        if(socket==null){
                            return;
                        }else {
                            socketStatus=true;
                        }
                        outputStream=socket.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    private void send(){
        data=et_data.getText().toString();
        if(data==null){
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
        }else {
            data=data+"\0";
        }
        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                if(socketStatus){
                    try {
//                       OutputStreamWriter outputStreamWriter=new OutputStreamWriter(socket.getOutputStream(),"gb18030");
//                        BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
//                        PrintWriter out=new PrintWriter(new OutputStreamWriter(outputStream,"GB18030"));
//                        outputStream.write(bufferedWriter.toString().getBytes());
                        outputStream.write(data.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    //当客户端界面返回时，关闭相应的socket资源

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_connect:
                connect();
                break;
            case R.id.bt_send:
                send();
                break;
        }
    }
}
