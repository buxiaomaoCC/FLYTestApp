package comvoice.example.zhangbin.zxingdemo;

import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import comvoice.example.zhangbin.zxingdemo.decoding.LocationUtil;
import comvoice.example.zhangbin.zxingdemo.view.AskDialog;
import comvoice.example.zhangbin.zxingdemo.view.LoadingDialog;

public class FormPage extends Activity {

	private Context context = null;
	
	
	private Button submit,goback;
	private RelativeLayout return_layout;
	private TextView mams_jingdu,mams_weidu;
	private TextView mams_id,mams_name,mams_area;
	

	MyReceiver myReceiver;
	private String mamsId = "";
	private String selfId;

	private String jingdu;
	private String weidu;
	private String area;
	private String station_name;
	String show="";
	public long clickTime = 0;
	Dialog loadingDialog;
	int blindType = 0;//0是默认基站  1是自定义基站
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.form_page);
		context = this;
		getId();
		addListener();
		init();
	}
	
	public void init() {

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		Intent intent = this.getIntent();
		Bundle bunde = intent.getExtras();
		
		mamsId = bunde.getString("mams_Id");
		selfId = bunde.getString("selfId");
		jingdu = bunde.getString("jingdu");
		weidu = bunde.getString("weidu");
		area = bunde.getString("area");
		station_name = bunde.getString("mams_name");
		blindType = bunde.getInt("blind_type");
		
		mams_id.setText(""+mamsId);
		mams_name.setText(""+station_name);
		mams_jingdu.setText(""+jingdu);
		mams_weidu.setText(""+weidu);
		mams_area.setText(""+area);
		
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.form_page_location");
		registerReceiver(myReceiver, filter);
		
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {

				try {
					WindowManager windowManager = getWindowManager();
					Display display = windowManager.getDefaultDisplay();
					int dialogWidth = display.getWidth();
					LoadingDialog ld = new LoadingDialog(context,
							dialogWidth, "正在提交数据，请稍等...");
					loadingDialog = ld.getLoadingDialog();
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
			}
			case 2: {

				if (loadingDialog != null) {
					loadingDialog.dismiss();
				}
				break;
			}
			
			case 3:{
				Toast.makeText(context, show, Toast.LENGTH_SHORT).show();
				break;
			}

			case 4 :{
				
				WindowManager windowManager = getWindowManager();
				Display display = windowManager.getDefaultDisplay();
				int dialogWidth = display.getWidth();
				final AskDialog askDialog = new AskDialog(context, dialogWidth, "油机基站绑定成功！", false);
				
				askDialog.setButtonSure("继续绑定");
				askDialog.setButtonCancel("退出");
				
				
				askDialog.getSure().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					askDialog.getAskDialogDialog().dismiss();
					Intent intent = new Intent();
					intent.setAction("android.intent.action.close_mams_list");
					context.sendBroadcast(intent);
					
					Intent intent3 = new Intent();
					intent3.setAction("android.intent.action.close_searchpage");
					Bundle bundle = new Bundle();
					bundle.putString("action", "finish");
					intent3.putExtras(bundle);
					context.sendBroadcast(intent3);
						finish();
					}
				});
				
				askDialog.getCancel().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						askDialog.getAskDialogDialog().dismiss();
						Intent intent = new Intent();
						intent.setAction("android.intent.action.close_mams_list");
						context.sendBroadcast(intent);
						
						Intent intent2 = new Intent();
						intent2.setAction("android.intent.action.close_mams_main");
						context.sendBroadcast(intent2);
						
						Intent intent3 = new Intent();
						intent3.setAction("android.intent.action.close_searchpage");
						Bundle bundle = new Bundle();
						bundle.putString("action", "finish");
						intent3.putExtras(bundle);
						context.sendBroadcast(intent3);
						finish();
						
						 
					}
				});
				
				break;
			}
			default:
				break;
			}
		}
	};
	
	public void getId(){
		
		submit = (Button)findViewById(R.id.send_mams);
		return_layout = (RelativeLayout)findViewById(R.id.return_layout);
		
		mams_id = (TextView)findViewById(R.id.mams_id);
		mams_name = (TextView)findViewById(R.id.mams_name);
		mams_jingdu = (TextView)findViewById(R.id.mams_jingdu);
		mams_weidu = (TextView)findViewById(R.id.mams_weidu);
		mams_area = (TextView)findViewById(R.id.mams_area);
		goback = (Button)findViewById(R.id.choose_return);
	}
	
	public void addListener(){
		return_layout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		goback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				finish();
			}
		});
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(Math.abs(System.currentTimeMillis() - clickTime ) > 1000){
					clickTime = System.currentTimeMillis();
				}else{return;}
//				if(mams_name.getText().length()<1){
//					Toast.makeText(context, "请填写基站名", Toast.LENGTH_SHORT).show();
//					return;
//				}else if(mams_jingdu.getText().length()<1){
//					Toast.makeText(context, "请填写经度", Toast.LENGTH_SHORT).show();
//					return;
//				}else if(mams_weidu.getText().length()<1){
//					Toast.makeText(context, "请填写维度", Toast.LENGTH_SHORT).show();
//					return;
//				}else if(mams_area.getText().length()<1){
//					Toast.makeText(context, "请填写所属区域", Toast.LENGTH_SHORT).show();
//					return;
//				}
				
				handler.sendEmptyMessageDelayed(1, 0);
				if(blindType == 0){
					uploadData(jingdu,weidu);
				}else{
					new LocationUtil(FormPage.this,2);
				}
				
				
			}
		});
		
	
	}

	public void uploadData(final String locationJingdu,final String locationWeidu ){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				
				
				try {
					JSONArray sendJson = new JSONArray();
					
					sendJson.put("binding_wifi_state");
					JSONObject json = new JSONObject();
					json.put("basename", station_name);
					json.put("area", area);
					json.put("lng", ""+locationJingdu);
					json.put("lat", ""+locationWeidu);
					json.put("deviceid", ""+mamsId);
					sendJson.put(java.net.URLEncoder.encode(json.toString(), "UTF-8"));
					
					sendJson.put(selfId);
					sendJson.put("");	
					String result = scaleSocket.mySocket(sendJson.toString(),25000);
					
					handler.sendEmptyMessageDelayed(2, 0);
					
					if(result.startsWith("+")){
						
						handler.sendEmptyMessageDelayed(4, 0);
						
					}else{
						show = "绑定失败，请重试！";
						handler.sendEmptyMessageDelayed(3, 0);
					}
				} catch (Exception e) {
					// TODO: handle exception
					handler.sendEmptyMessageDelayed(2, 0);
					show = "绑定失败，请重试！";
					handler.sendEmptyMessageDelayed(3, 0);
					e.printStackTrace();
				}
				
				Looper.loop();
			}
		}).start();
	}
	
	private class MyReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.intent.action.form_page_location")) {
				 	   
			   final double locationJingdu = intent.getExtras().getDouble("jingdu",0);
			   final double locationWeidu = intent.getExtras().getDouble("weidu",0);
		   
				if(locationJingdu!=0 && locationWeidu != 0){
					uploadData(locationJingdu+"",locationWeidu+"");
				}else{
					handler.sendEmptyMessageDelayed(2, 0);
					show = "绑定失败，请重试！";
					handler.sendEmptyMessageDelayed(3, 0);
				}
			
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return false;
	}

	protected void onDestroy() {
		
		super.onDestroy();
		try {
			unregisterReceiver(myReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
