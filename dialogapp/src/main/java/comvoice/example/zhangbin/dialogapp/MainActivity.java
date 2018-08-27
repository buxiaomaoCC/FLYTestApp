package comvoice.example.zhangbin.dialogapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener{
    private CustomDialog.Builder builder;
    private CustomDialog customDialog;
    private Button bt_two,bt_one;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        builder=new CustomDialog.Builder(MainActivity.this);
        bt_one=findViewById(R.id.bt_one);
        bt_two=findViewById(R.id.bt_two);
        init();

    }

    private void init() {
        bt_two.setOnClickListener(this);
        bt_one.setOnClickListener(this);
    }

    private void showSingleButtonDialog(String alerDialog, String btnDialog, View.OnClickListener onClickListener){
        customDialog=builder.setMessage(alerDialog)
                .setSingleButton(btnDialog,onClickListener)
                .createSingleButtonDialog();
        customDialog.show();
    }
    private void showTwoButtonDialog(String alerDialog, String confirmText, String cancelText, View.OnClickListener clickListener, View.OnClickListener onClickListener){
        customDialog=builder.setMessage(alerDialog)
                .setPositiviteButton(confirmText,onClickListener)
                .setNEgativeButton(cancelText,onClickListener)
                .createTwoButtonDialog();
        customDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_one:
                showSingleButtonDialog("这是单选对话框的内容！", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        customDialog.dismiss();
                        //这里写自定义处理XXX
                    }
                });
                break;
            case R.id.bt_two:
                showTwoButtonDialog("这是双选对话框的内容！", null, null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                        //这里写自定义处理XXX
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                        //这里写自定义处理XXX
                    }
                });
                break;
        }
    }
}
