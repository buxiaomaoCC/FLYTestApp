package comvoice.example.zhangbin.parcelable;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Parcel parcel;
    private EditText et_name,et_password;
    private CheckBox cb;
    private String INFO="test";
    private Button bt_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        et_name=findViewById(R.id.et_name);
        et_password=findViewById(R.id.et_password);
        cb=findViewById(R.id.cb);
        bt_test=findViewById(R.id.bt_test);
        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=et_name.getText().toString().trim();
                String password=et_password.getText().toString().trim();
                if(name!=null&&!name.equals("")&&!password.equals("")&&password!=null){
                    initsave(name,password);
                }else {
                    Toast.makeText(MainActivity.this, "账户信息不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getmes();
    }
    private void initsave(String name,String password){
        SharedPreferences sharedPreferences= getSharedPreferences(INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        if(cb.isChecked()){
        editor.putString("name",name);
        editor.putString("password",password);
        editor.putBoolean("checkbox",cb.isChecked());
            Toast.makeText(this, "信息保存成功", Toast.LENGTH_SHORT).show();
        }else{
            editor.putString("name","");
            editor.putString("password","");
            editor.putBoolean("checkbox",false);
        }
        editor.commit();

    }
    private void getmes(){
        SharedPreferences sharedPreferences= getSharedPreferences(INFO, Context.MODE_PRIVATE);
        String name=sharedPreferences.getString("name","");
        String password=sharedPreferences.getString("password","");
        Boolean checkbox=sharedPreferences.getBoolean("checkbox",cb.isChecked());

        et_name.setText(name);
        et_password.setText(password);
        cb.setChecked(checkbox);
    }
}
