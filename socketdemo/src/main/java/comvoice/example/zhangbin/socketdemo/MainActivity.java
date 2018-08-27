package comvoice.example.zhangbin.socketdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.GetChars;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.InflaterInputStream;

public class MainActivity extends AppCompatActivity {
    private TextView tv_show,tv_receiver;
    private ServerSocket serverSocket=null;
    StringBuffer stringBuffer=new StringBuffer();
    private InputStream inputStream;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    tv_show.setText(msg.obj.toString());
                    break;
                case 2:
                    tv_show.setText(msg.obj.toString());
                    stringBuffer.setLength(0);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String encoding = System.getProperty("file.encoding");
        Log.e("编码",encoding);
        initView();
        recerveData();
    }

    private void initView() {
        tv_show=findViewById(R.id.tv_show);
        tv_receiver=findViewById(R.id.tv_receiver);
    }
    //服务器端接收数据
    public void recerveData(){
        Thread thread=new Thread(){
            @Override
            public void run() {
                super.run();
                //端口号
                try {
                    serverSocket=new ServerSocket(8000);
                    GetIpAddress.getLocalIpAddress(serverSocket);
                    Message message=handler.obtainMessage();
                    message.what=1;
                    message.obj="IP:"+GetIpAddress.getIP() + " PORT: " + GetIpAddress.getPort();
                    handler.sendMessage(message);
                    while ((true)){
                        Socket socket=null;
                        socket=serverSocket.accept();
                        inputStream=socket.getInputStream();
                        new ServerThread(socket,inputStream).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    class ServerThread extends Thread{

        private Socket socket;
        private InputStream   inputStream;
        private StringBuffer stringBuffer = MainActivity.this.stringBuffer;
        InputStreamReader inputStreamReader=null;

        public ServerThread(Socket socket,InputStream inputStream){
            this.socket = socket;
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            int len;
            byte[] bytes = new byte[20];
            boolean isString = false;

            try {
                inputStreamReader=new InputStreamReader(socket.getInputStream(),"gbk");
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                //在这里需要明白一下什么时候其会等于 -1，其在输入流关闭时才会等于 -1，
                //并不是数据读完了，再去读才会等于-1，数据读完了，最结果也就是读不到数据为0而已；
                while ((len = inputStream.read(bytes)) != -1) {
                    for(int i=0; i<len; i++){
                        if(bytes[i] != '\0'){
                            stringBuffer.append((char)bytes[i]);
                        }else {
                            isString = true;
                            break;
                        }
                    }
                    if (isString) {
                        Message message_2 = handler.obtainMessage();
                        message_2.what = 2;
                        message_2.obj = len;
                        handler.sendMessage(message_2);
                        isString = false;
                    }
                }
                } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //当这个异常发生时，说明客户端那边的连接已经断开

        }
    }


    /*当按返回键时，关闭相应的socket资源*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
