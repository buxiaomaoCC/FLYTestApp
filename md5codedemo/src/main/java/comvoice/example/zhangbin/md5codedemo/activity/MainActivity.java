package comvoice.example.zhangbin.md5codedemo.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import comvoice.example.zhangbin.md5codedemo.utils.AESUtils;
import comvoice.example.zhangbin.md5codedemo.utils.AESUtils2;
import comvoice.example.zhangbin.md5codedemo.utils.AESUtils3;
import comvoice.example.zhangbin.md5codedemo.utils.MD5Utils;
import comvoice.example.zhangbin.md5codedemo.R;
import comvoice.example.zhangbin.md5codedemo.utils.RSAUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_send,bt_public_se,bt_private_jm,bt_aes_jiami,bt_aes_jiemi;
    private EditText et_input;
    private TextView tv_show;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    KeyPair key;
    String secretKey;
    String encryStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initClick();
    }

    private void initClick() {
        bt_send.setOnClickListener(this);
        bt_public_se.setOnClickListener(this);
        bt_private_jm.setOnClickListener(this);
        bt_aes_jiemi.setOnClickListener(this);
        bt_aes_jiami.setOnClickListener(this);
    }

    private void initView() {
        bt_aes_jiami=findViewById(R.id.bt_aes_jiami);
        bt_aes_jiemi=findViewById(R.id.bt_aes_jiemi);
        bt_send=findViewById(R.id.bt_send);
        et_input=findViewById(R.id.et_imput);
        tv_show=findViewById(R.id.tv_show);
        bt_public_se=findViewById(R.id.bt_public_se);
        bt_private_jm=findViewById(R.id.bt_private_jm);
        //rsa加密生成公钥密钥
        key=RSAUtils.generateRSAKeyPair(2048);
        publicKey=key.getPublic();
        privateKey=key.getPrivate();
        //生成一个动态key
        secretKey= AESUtils.generateKey();
        Log.e("aes",secretKey);
        AESUtils3.encrypt("哈哈哈哈哈哈哈哈哈哈哈哈哈","123456");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        String data=et_input.getText().toString().trim();

        switch (view.getId()){
            case R.id.bt_public_se://rsa公钥加密
                String r= RSAUtils.encryptDataByPublicKey(data.getBytes(),publicKey);
                tv_show.setText(r);
                break;
            case R.id.bt_send://md5加密
               String result= new MD5Utils().getMD5Code(data);
                tv_show.setText(result);
                break;
            case R.id.bt_private_jm://rsa私钥解密
                String p=RSAUtils.decryptToStrByPrivate(tv_show.getText().toString(),privateKey);
                tv_show.setText(p);
                break;
            case R.id.bt_aes_jiami://aes加密
//                long start=System.currentTimeMillis();
//                encryStr=AESUtils.encrypt(secretKey,data);
//                long end =System.currentTimeMillis();
//                tv_show.setText(encryStr);
                String a=AESUtils3.encrypt(data,"123456");
                tv_show.setText(a);
                break;
            case R.id.bt_aes_jiemi://aes解密
//                String decryStr=AESUtils.decrypt(secretKey,encryStr);
//                tv_show.setText(decryStr);
                String b=AESUtils3.decrypt(tv_show.getText().toString(),"123456");
                tv_show.setText(b);
                break;
        }
    }
}
