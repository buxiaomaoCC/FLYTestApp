package comvoice.example.zhangbin.zxingdemo;

import java.io.IOException;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import comvoice.example.zhangbin.zxingdemo.camera.CameraManager;
import comvoice.example.zhangbin.zxingdemo.decoding.CaptureActivityHandler;
import comvoice.example.zhangbin.zxingdemo.decoding.InactivityTimer;
import comvoice.example.zhangbin.zxingdemo.decoding.LocationUtil;
import comvoice.example.zhangbin.zxingdemo.view.AskDialog;
import comvoice.example.zhangbin.zxingdemo.view.LoadingDialog;
import comvoice.example.zhangbin.zxingdemo.view.ViewfinderView;


public class MainActivity extends Activity implements Callback,OnClickListener {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private TextView txtResult,timerecoder;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private View codeOk;
	private View codeCancel;
	private ProgressBar dialog_progress2;
	int dialogWidth = 0;
	Context context;
	private final int UPLOAD_DATA102 = 102;
	private final int UPLOAD_DATA103 = 103;
	private final int UPLOAD_DATA104 = 104;
	private final int UPLOAD_DATA105 = 105;
	private final int UPLOAD_DATA106 = 106;
	private final int UPLOAD_DATA107 = 107;

	public String selfId = "";
	public String security = "";
	
	Dialog loadingDialog;
	boolean hasGone = false;
	
	MyReceiver myReceiver;
	private String mams_id ="";
	private double jingdu = 0;
	private double weidu = 0;
	private String result;
	JSONArray sendJson;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		context = this;
		setScanWidthHeight();		
//		try{			
//			Context cc = this.createPackageContext("com.myncic.mams", Context.CONTEXT_IGNORE_SECURITY);
//			SharedPreferences preferences = cc.getSharedPreferences("userInfo",Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
//			selfId=preferences.getString("selfId", "");
//			security=preferences.getString("security", "");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		if(selfId.length()==0){
//			 Toast.makeText(MainActivity.this, "请先登陆智能运维平台！", Toast.LENGTH_LONG).show();
//			 finish();
//		}

		
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		dialogWidth = display.getWidth();
				
		//50秒不拍照  自动关闭
		new Thread(new Runnable() {			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(50000);
					if(mams_id.length()==0 && !hasGone){
						shandler.sendEmptyMessageDelayed(7, 0);
						finish();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}).start();
		
		CameraManager.init(getApplication());
		
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		txtResult = (TextView) findViewById(R.id.txtResult);
		timerecoder= (TextView)findViewById(R.id.timerecoder);
		codeOk = (View) findViewById(R.id.codeOk);
		codeCancel = (View) findViewById(R.id.codeCancel);
		dialog_progress2 = (ProgressBar) findViewById(R.id.dialog_progress2);
		codeOk.setOnClickListener(this);
		codeCancel.setOnClickListener(this);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		
		
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.locationonce");
		filter.addAction("android.intent.action.close_mams_main");
		
		registerReceiver(myReceiver, filter);
		
	}
	
	private void setScanWidthHeight(){
		//设置扫描的大小
		DisplayMetrics metrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int widthPixels=metrics.widthPixels;
		int heightPixels=metrics.heightPixels;
		int width=widthPixels<heightPixels?widthPixels:heightPixels;
		if(width<=0)
			width=320;
		CameraManager.MIN_FRAME_WIDTH = (int)(width*3/5); 
		CameraManager.MIN_FRAME_HEIGHT = (int)(width*3/5); 
		CameraManager.MAX_FRAME_WIDTH = (int)(width*3/5);//(int)(width*2/3);    
		CameraManager.MAX_FRAME_HEIGHT = (int)(width*3/5); 		
//		Log.e("distance","CameraManager.MAX_FRAME_WIDTH="+CameraManager.MAX_FRAME_WIDTH+" CameraManager.MAX_FRAME_HEIGHT="+CameraManager.MAX_FRAME_HEIGHT);
		
//		CameraManager.MIN_FRAME_WIDTH = 240; 
//		CameraManager.MIN_FRAME_HEIGHT =240; 
//		CameraManager.MAX_FRAME_WIDTH = 1200;//(int)(width*2/3);    
//		CameraManager.MAX_FRAME_HEIGHT = 675; 	
//		CameraManager.MIN_FRAME_WIDTH = width; 
//		CameraManager.MIN_FRAME_HEIGHT =width; 
//		CameraManager.MAX_FRAME_WIDTH = width;//(int)(width*2/3);    
//		CameraManager.MAX_FRAME_HEIGHT = width; 	
	}
	
	private class MyReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.intent.action.locationonce")) {
				    jingdu = intent.getExtras().getDouble("jingdu",0);
				    weidu = intent.getExtras().getDouble("weidu",0);
				 
				    if(jingdu !=0 && weidu!= 0 ){
				    	new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//发送经纬度
								Looper.prepare();
								
								try {
									sendJson = new JSONArray();
									sendJson.put("get_near_for_lnglat");
									sendJson.put(jingdu+"");
									sendJson.put(weidu+"");
									sendJson.put("");
									
									result = scaleSocket.mySocket(sendJson.toString(),25000);
									try {
										Log.i("scan", "result:" + new JSONArray(result.substring(1,result.length())).toString());
									} catch (Exception e) {
										// TODO: handle exception
									}
									
									if(result.startsWith("+")){
										if(mams_id != null && !mams_id.isEmpty() && mams_id.length() >0){
											Intent intent=new Intent();
											intent.setClass(MainActivity.this, StationChoosePage.class);
											Bundle bun = new Bundle();
											bun.putString("mams_Id", mams_id);
											bun.putString("mams_result", result+"");
											bun.putString("selfId", selfId+"");
											bun.putString("locationJingdu", jingdu+"");
											bun.putString("locationWeidu", weidu+"");
											intent.putExtras(bun);
											startActivity(intent);
										}
									}else if(result.startsWith("*")){
										shandler.sendEmptyMessageDelayed(8, 0);
									}
									
								} catch (Exception e) {
									e.printStackTrace();
								}

								
								shandler.sendEmptyMessageDelayed(6, 0);
								Looper.loop();
							}
						}).start();
				    }else{
				    	shandler.sendEmptyMessageDelayed(6, 0);
				    	final AskDialog askDialog = new AskDialog(context, dialogWidth, "定位失败", false);
				    	
				    	askDialog.getSure().setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								askDialog.getAskDialogDialog().dismiss();
								new LocationUtil(MainActivity.this,0);
							    shandler.sendEmptyMessageDelayed(5, 0);
							}
						});
				    	
				    	askDialog.getCancel().setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								askDialog.getAskDialogDialog().dismiss();
								Intent intent=getIntent();
								intent.setClass(MainActivity.this, FormPage.class);
								Bundle bun = new Bundle();
								bun.putString("mams_Id", mams_id);
								bun.putString("selfId", selfId);
								bun.putString("jingdu", "");
								bun.putString("weidu", "");
								bun.putString("area", "");
								bun.putString("mams_name", "");
								
								intent.putExtras(bun);
								startActivity(intent);
							}
						});
				    }
				    
			}if (intent.getAction().equals("android.intent.action.close_mams_main")) {
				finish();
			}
		}
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {		
		case R.id.codeCancel:
			clickCancel();
			break;
		case R.id.codeOk:
			clickOK();
			break;
		default:
			break;
		}
	}
	private void clickCancel(){
		shandler.removeMessages(UPLOAD_DATA105);
		shandler.removeMessages(UPLOAD_DATA106);
		timerecoder.setVisibility(View.VISIBLE);
		dialog_progress2.setVisibility(View.VISIBLE);
		if(code!=null && !code.isEmpty()){
			code="";
			txtResult.setText("");
			viewfinderView.drawViewfinder();
			inactivityTimer.onActivity();
			handler.restartPreviewAndDecode();
		}else {
			finish();	
		}
	}
	private void clickOK(){
		shandler.removeMessages(UPLOAD_DATA105);
		timerecoder.setVisibility(View.VISIBLE);
		dialog_progress2.setVisibility(View.VISIBLE);
		if(code!=null && !code.isEmpty()){
			if(codetype.equals("QR_CODE")){//二维码
				if (code.startsWith("http://")) {
					Uri myBlogUri = Uri.parse(code); 
					Intent returnIt = new Intent(Intent.ACTION_VIEW, myBlogUri); 
					startActivity(returnIt);
				}else {
					try {
						if(dataJSONArray!=null){
							sendArray=new JSONArray();
							sendArray.put(dataJSONArray.getString(0));
							sendArray.put(dataJSONArray.getString(1));
							sendArray.put(selfId);
							sendArray.put(security);
							qrcode_login();
							
							LoadingDialog ld = new LoadingDialog(MainActivity.this,
									dialogWidth, "数据处理中，请稍后...");
							loadingDialog = ld.getLoadingDialog();
						}else{
							Intent intent=getIntent();
							if (intent!=null) {
								intent.putExtra("code", code+"");
							}
							setResult(200,intent);
							finish();
						}
					} catch (Exception e) {
						e.printStackTrace();					
					}
				}
			}else {
				//一维码
				mams_id = code+"";
				new LocationUtil(MainActivity.this,0);
			    shandler.sendEmptyMessageDelayed(5, 0);
			}
		}else finish();	
		
	}
	int entertime = 0;
	private Handler shandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPLOAD_DATA102:
				Toast.makeText(MainActivity.this,"操作失败，请重试！",Toast.LENGTH_SHORT).show();
				break;
			case UPLOAD_DATA103:
				Toast.makeText(MainActivity.this,"操作成功！",Toast.LENGTH_SHORT).show();				
				break;
			case UPLOAD_DATA104:
				Toast.makeText(MainActivity.this,"操作失败,请尝试重新登录运维平台！",Toast.LENGTH_SHORT).show();				
				break;
			case UPLOAD_DATA105:
				entertime--;
				if(entertime<=0){
					entertime=0;
					timerecoder.setVisibility(View.VISIBLE);
					dialog_progress2.setVisibility(View.VISIBLE);
					clickOK();
				}else {
					timerecoder.setText(entertime+"秒后自动进入");
					timerecoder.setVisibility(View.VISIBLE);
					dialog_progress2.setVisibility(View.VISIBLE);
					shandler.sendEmptyMessageDelayed(UPLOAD_DATA105, 1000);	
				}
				break;
			case UPLOAD_DATA106:
				closetime--;
				if(closetime<=0){
					closetime=0;
					timerecoder.setVisibility(View.VISIBLE);
					dialog_progress2.setVisibility(View.VISIBLE);
					finish();
				}else {
					timerecoder.setText(closetime+"秒后自动退出");
					timerecoder.setVisibility(View.VISIBLE);
					dialog_progress2.setVisibility(View.VISIBLE);
					shandler.sendEmptyMessageDelayed(UPLOAD_DATA106, 1000);	
				}		
				break;
			case 5:
				try {
					WindowManager windowManager = getWindowManager();
					Display display = windowManager.getDefaultDisplay();
					int dialogWidth = display.getWidth();
					LoadingDialog ld = new LoadingDialog(MainActivity.this,
							dialogWidth, "正在定位中，请稍等...");
					loadingDialog = ld.getLoadingDialog();
				} catch (Exception e) {
					e.printStackTrace();
				}				
				break;
				
			case 6:
				if (loadingDialog != null) {
					loadingDialog.dismiss();
				}				
				break;
			case 7:
				Toast.makeText(MainActivity.this,"相机超时，退出",Toast.LENGTH_LONG).show();				
				break;
			case 8:
				Toast.makeText(MainActivity.this,"请检查网络连接",Toast.LENGTH_LONG).show();				
				break;
			default:
				break;
			}
		}
	};
	JSONArray sendArray=null;
	Thread thread=null;
	int closetime=0;
	private void qrcode_login(){
		thread=new Thread(new Runnable() {
			/* (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				try{
					String result=scaleSocket.mySocket(sendArray.toString(), 25000);
					if(result.startsWith("+")){
						shandler.sendEmptyMessage(UPLOAD_DATA103);
						closetime=6;
						shandler.sendEmptyMessageDelayed(UPLOAD_DATA106,1000);
						dataJSONArray=null;
					}else if(result.startsWith("-")){
						shandler.sendEmptyMessage(UPLOAD_DATA104);//从登
						finish();
					}else {
						shandler.sendEmptyMessage(UPLOAD_DATA102);//错误
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					shandler.sendEmptyMessage(6);
				}
			}
		});
		thread.start();
	}
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		try {
			unregisterReceiver(myReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
		hasGone = true;
		
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}
	String codetype="";
	JSONArray dataJSONArray=null;
	public void handleDecode(Result obj, Bitmap barcode) {
		timerecoder.setVisibility(View.VISIBLE);
		dialog_progress2.setVisibility(View.VISIBLE);
		shandler.removeMessages(UPLOAD_DATA106);	
		inactivityTimer.onActivity();
		codetype=obj.getBarcodeFormat().getName();
//		Log.e("tag", "codetype="+codetype);
//		viewfinderView.drawResultBitmap(barcode);
		playBeepSoundAndVibrate();
		code = obj.getText();			
		if(code!=null && !code.isEmpty()){
			if(codetype.equals("QR_CODE")){
				//二维码
				if (code.startsWith("http://")) {
					entertime=6;
				}else{
					try{
						dataJSONArray=new JSONArray(code);
						entertime=1;
					}catch(Exception e){
						dataJSONArray=null;
						entertime=6;
					}
				}
			}
			txtResult.setText(code);
			shandler.sendEmptyMessage(UPLOAD_DATA105);	
		}		
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	private String code;


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			clickCancel();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	

}