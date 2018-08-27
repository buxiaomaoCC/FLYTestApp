package comvoice.example.zhangbin.zxingdemo.view;



import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import comvoice.example.zhangbin.zxingdemo.R;

public class AskDialog {
	private LayoutInflater factory;
	Dialog askDialog;
	int dialogWidth;
	String showText;
	Context context;
	Button sure;
	Button cancel;
	TextView title;
	boolean cancelable;
	TextView content;
	LinearLayout layout;
	
	
	public AskDialog(Context context,int dialogWidth, String showText,boolean cancelable) {
		super();
		this.context = context;
		this.dialogWidth = dialogWidth;
		this.showText = showText;
		this.cancelable =cancelable;
		show();
	}
	
	
	public void show(){
		try {
			factory = LayoutInflater.from(context);
			View loadingView = factory.inflate(R.layout.dialoglayout4ask, null);
			askDialog = new Dialog(context, R.style.loading_dialog);
			askDialog.setCancelable(cancelable);
			if(cancelable){
				askDialog.setCanceledOnTouchOutside(true);
			}
			askDialog.setContentView(loadingView, new LinearLayout.LayoutParams(
					(int)(dialogWidth*0.9),
					LinearLayout.LayoutParams.FILL_PARENT));
			layout = (LinearLayout)loadingView.findViewById(R.id.dialogcontent_linview);
			title = (TextView)loadingView.findViewById(R.id.dialogtitle);
			content = (TextView)loadingView.findViewById(R.id.dialogcontent);
			content.setText(showText);
			
			sure = (Button)loadingView.findViewById(R.id.dialogsure);
			cancel = (Button)loadingView.findViewById(R.id.dialogcancel);
			askDialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	public Dialog getAskDialogDialog() {
		return askDialog;
	}


	public void setAskDialogDialog(Dialog askDialog) {
		this.askDialog = askDialog;
	}


	public int getDialogWidth() {
		return dialogWidth;
	}


	public void setDialogWidth(int dialogWidth) {
		this.dialogWidth = dialogWidth;
	}


	public Button getSure() {
		return sure;
	}


	public void setSure(Button sure) {
		this.sure = sure;
	}


	public Button getCancel() {
		return cancel;
	}
	
	public void setButtonSure(String text){
		sure.setText(text);
	}

	public void setButtonCancel(String text){
		cancel.setText(text);
	}
	
	public void setCancel(Button cancel) {
		this.cancel = cancel;
	}


	public void setTitle(String str) {
		title.setText(str+"");
	}


	public LinearLayout getLayout() {
		return layout;
	}


	public void setLayout(LinearLayout layout) {
		this.layout = layout;
	}


	public TextView getContent() {
		return content;
	}


	public void setContent(TextView content) {
		this.content = content;
	}
	
	
	
}
