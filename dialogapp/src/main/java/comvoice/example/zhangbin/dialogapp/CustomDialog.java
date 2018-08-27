package comvoice.example.zhangbin.dialogapp;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zhangbin on 2018/1/29.
 */

public class CustomDialog extends Dialog {
    public CustomDialog(@NonNull Context context) {
        super(context);
    }
    public CustomDialog(Context context,int theme){
        super(context,theme);

    }
    public static class Builder{
        private String message;
        private View contentView;
        private String positiveButtonText;
        private String negativeButtonText;
        private String singleButtonText;
        private View.OnClickListener positaveButtonClickListener;
        private View.OnClickListener negativeButtonClickListener;
        private View.OnClickListener singleButtonClickListrner;
        private View layout;
        private CustomDialog dialog;
        public Builder(Context context){
            //这里传入自定义的style,直接影响此dialog的显示效果，style具体实现见style.xml
            dialog=new CustomDialog(context,R.style.Dialog);
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout=inflater.inflate(R.layout.dialog_layout,null);
            dialog.addContentView(layout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        public Builder setMessage(String message){
            this.message=message;
            return this;
        }
        public Builder setContentView(View view){
            this.contentView=view;
            return this;
        }
        public Builder setPositiviteButton(String positiveButtonText,View.OnClickListener listener){
            this.positiveButtonText=positiveButtonText;
            this.positaveButtonClickListener=listener;
            return this;
        }
        public Builder setNEgativeButton(String negativeButtonText,View.OnClickListener listener){
            this.negativeButtonText=negativeButtonText;
            this.positaveButtonClickListener=listener;
            return this;
        }
        public Builder setSingleButton(String singleButtonText,View.OnClickListener listener){
            this.singleButtonText=singleButtonText;
            this.positaveButtonClickListener=listener;
            return this;
        }
        /**
         * 创建单按钮对话框
         */
        public CustomDialog createSingleButtonDialog(){
            showSingleButton();
            layout.findViewById(R.id.singleButton).setOnClickListener(singleButtonClickListrner);
            //如果传入的按钮文字为空，则使用默认的“返回”
            if(singleButtonText!=null){
                ((Button)layout.findViewById(R.id.singleButton)).setText(singleButtonText);
            }else {
                ((Button)layout.findViewById(R.id.singleButton)).setText("返回");
            }
            create();
            return dialog;
        }

        /**
         * 创建双按钮对话框
         */
        public CustomDialog createTwoButtonDialog(){
            showTwoButton();
            layout.findViewById(R.id.positiveButton).setOnClickListener(positaveButtonClickListener);
            layout.findViewById(R.id.negativeButton).setOnClickListener(negativeButtonClickListener);
            //如果传入的按钮文字为空，则默认使用“是”和“否”
            if(positiveButtonText!=null){
                ((Button)layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);

            }else {
                ((Button)layout.findViewById(R.id.positiveButton)).setText("是");
            }
            if(negativeButtonText!=null){
                ((Button)layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
            }else {
                ((Button)layout.findViewById(R.id.negativeButton)).setText("否");
            }
            create();
            return dialog;

        }

        /**
         * 单按钮对话框和双按钮对话框的公共部分在这里设置
         */
        public void create(){
            if(message!=null){
                ((TextView)layout.findViewById(R.id.message)).setText(message);
            }else if(contentView!=null){
                ((LinearLayout)layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout)layout.findViewById(R.id.content)).addView(contentView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            dialog.setContentView(layout);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
        }
        private void showTwoButton() {
            layout.findViewById(R.id.singleButtonLayout).setVisibility(View.GONE);
            layout.findViewById(R.id.twoButtonLayout).setVisibility(View.VISIBLE);
        }
        /**
         * 显示单按钮布局，隐藏双按钮
         */
        private void showSingleButton(){
            layout.findViewById(R.id.singleButtonLayout).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.twoButtonLayout).setVisibility(View.GONE);
        }
    }
}
