package comvoice.example.zhangbin.abstractapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Builder builder=new MacbookBuilder();
        Diretor diretor=new Diretor(builder);
        diretor.construct("因特尔主板","Retina 显示器");
        Toast.makeText(this, "Computer Info :"+builder.create().toString(), Toast.LENGTH_SHORT).show();
    }
}
