package comvoice.example.zhangbin.zxingdemo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;


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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import comvoice.example.zhangbin.zxingdemo.decoding.LocationUtil;
import comvoice.example.zhangbin.zxingdemo.view.AskDialog;
import comvoice.example.zhangbin.zxingdemo.view.LoadingDialog;

public class StationSearchPage extends Activity {

	private Context context = null;
	private ListView myList;
	private MyListAdapter adapter;
	
	private RelativeLayout return_layout;
	private EditText searchCondition;
	private Button search_btn,goback,search_delete;
	private String mamsId,selfId;
	Dialog loadingDialog;
	String show="";
	String loadingShow="";
	List<StationInfo> stationList;
	private int choose_station = -1;
	JSONArray sendJson;
	String jingdu;
	String weidu;
	MyReceiver myReceiver;
	public long clickTime = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.station_search_page);
		context = this;

		init();
	}
	
	
	

	public void init() {

		
		search_btn = (Button)findViewById(R.id.choose_search);
		return_layout = (RelativeLayout)findViewById(R.id.return_layout);
		searchCondition = (EditText)findViewById(R.id.search_edit);
		myList = (ListView)findViewById(R.id.choose_list);
		goback = (Button)findViewById(R.id.choose_return);
		search_delete = (Button)findViewById(R.id.search_delete);
		
		Intent intent = this.getIntent();
		Bundle bunde = intent.getExtras();
		
		mamsId = bunde.getString("mams_Id");
		selfId = bunde.getString("selfId");
		jingdu = bunde.getString("jingdu");
		weidu = bunde.getString("weidu");
		
		stationList = new ArrayList<StationInfo>();
		
		adapter = new MyListAdapter(context);
		myList.setAdapter(adapter);
	
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.close_searchpage");
		registerReceiver(myReceiver, filter);
		
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
		
		search_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchCondition.setText("");
			}
		});
		
		searchCondition.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				try {
					if (s.toString().length() < 1) {
						search_delete.setVisibility(View.VISIBLE);
					} else {
						search_delete.setVisibility(View.VISIBLE);
					}
			
				} catch (Exception e) {
				}
				
			}
		});
		
		myList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				choose_station = position;
				
				loadingShow = "正在更新当前定位...";
				handler.sendEmptyMessageDelayed(1, 0);
				
				new LocationUtil(StationSearchPage.this,1);
				
			}
		});
		
		search_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Math.abs(System.currentTimeMillis() - clickTime ) > 1000){
					clickTime = System.currentTimeMillis();
				}else{return;}
				
				if(searchCondition.getText().length()==0){
					Toast.makeText(context, "请输入搜索内容", Toast.LENGTH_SHORT).show();
					return;
				}else{
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//发送经纬度
							Looper.prepare();
							loadingShow = "正在搜索中...";
							handler.sendEmptyMessageDelayed(1, 0);
							try {
								sendJson = new JSONArray();
								sendJson.put("get_base_for_name");
								sendJson.put(searchCondition.getText()+"");
								sendJson.put("");
								
								String result = scaleSocket.mySocket(sendJson.toString(),25000);
								try {
									Log.i("scan", "result:" + new JSONArray(result.substring(1,result.length())).toString());
								} catch (Exception e) {
									// TODO: handle exception
								}
								handler.sendEmptyMessageDelayed(2, 0);
								if(result.startsWith("+")){
									try {
										result = result.substring(1,result.length());
										JSONArray resultArray = new JSONArray(result);
										stationList.clear();
										for(int i=0;i<resultArray.length();i++){
											JSONArray array = new JSONArray(resultArray.getString(i));
											stationList.add(new StationInfo(array.getString(0), array.getString(1)));
										}
										
										
										handler.sendEmptyMessageDelayed(4, 0);
										
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}
									
								}else if(result.startsWith("*")){
									show = "搜索失败，请检查网络设置！";
									handler.sendEmptyMessageDelayed(3, 0);
								}else{
									show = "搜索结果为空";
									handler.sendEmptyMessageDelayed(3, 0);
								}
//									
							} catch (Exception e) {
								
							}

					
							Looper.loop();
						}
					}).start();
				}

			}
		});
		
	}
	
	
	class StationInfo{
		String name;
		String area;

		
		
		public StationInfo(String name,  String area
				) {
			super();
			this.name = name;
			
			this.area = area;

		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
		}

		
		
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 1:
				try {
					WindowManager windowManager = getWindowManager();
					Display display = windowManager.getDefaultDisplay();
					int dialogWidth = display.getWidth();
					LoadingDialog ld = new LoadingDialog(StationSearchPage.this,
							dialogWidth, loadingShow);
					loadingDialog = ld.getLoadingDialog();
				} catch (Exception e) {
					e.printStackTrace();
				}				
				break;
			case 2:
				if (loadingDialog != null) {
					loadingDialog.dismiss();
				}		
				break;
			case 3:
				Toast.makeText(StationSearchPage.this,show,Toast.LENGTH_SHORT).show();				
				break;
			case 4:
				adapter.notifyDataSetChanged();				
				break;

			default:
				break;
			}
		}
	};
	
	class MyListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		Context c;
		int currentID = -1;

		public MyListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
			this.c = context;
		}

		@Override
		public int getCount() {
			return stationList.size();
		}

		@Override
		public Object getItem(int position) {
			return stationList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder myHolder;
			if (convertView == null) {
				myHolder = new Holder();
				convertView = inflater.inflate(R.layout.station_search_page_items,
						null);
				myHolder.choose_name_layout = (RelativeLayout) convertView
						.findViewById(R.id.choose_name_layout);
				myHolder.station_name = (TextView) convertView
						.findViewById(R.id.station_name);
				myHolder.station_area = (TextView) convertView
						.findViewById(R.id.station_area);

			} else {
				myHolder = (Holder) convertView.getTag();
			}
			convertView.setTag(myHolder);
			myHolder.station_name.setText(stationList.get(position).getName());
			myHolder.station_area.setText(stationList.get(position).getArea());

			return convertView;
		}

		class Holder {
			RelativeLayout choose_name_layout;
			TextView station_name;
			TextView station_area;
		}

		public void setCurrentID(int currentID) {
			this.currentID = currentID;
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
		try {
			unregisterReceiver(myReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onDestroy();
		
		
	}

	private class MyReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.intent.action.close_searchpage")) {
				   
				   if(intent.getExtras().getString("action").equals("location")){
					   handler.sendEmptyMessageDelayed(2, 0);
					   
					   double locationJingdu = intent.getExtras().getDouble("jingdu",0);
					   double locationWeidu = intent.getExtras().getDouble("weidu",0);
					   
					   
						
						if(locationJingdu!=0 && locationWeidu != 0){
							Intent intent2= new Intent();
						    intent2.setClass(StationSearchPage.this, FormPage.class);
							Bundle bun = new Bundle();
							bun.putString("mams_Id", mamsId);
							bun.putString("selfId", selfId+"");
							StationInfo station = stationList.get(choose_station);
							bun.putString("jingdu", locationJingdu+"");
							bun.putString("weidu", locationWeidu+"");
							bun.putString("area", station.getArea()+"");
							bun.putString("mams_name", station.getName()+"");
							bun.putInt("blind_type", 1);
							intent2.putExtras(bun);
							startActivity(intent2);
						}else{
							WindowManager windowManager = getWindowManager();
							Display display = windowManager.getDefaultDisplay();
							int dialogWidth = display.getWidth();
							
							final AskDialog askDialog = new AskDialog(context, dialogWidth, "定位失败", false);
					    	
							askDialog.setButtonSure("重试定位");
							askDialog.setButtonCancel("取消");
					    	askDialog.getSure().setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									askDialog.getAskDialogDialog().dismiss();
									loadingShow = "定位中...";
									handler.sendEmptyMessageDelayed(1, 0);
									new LocationUtil(StationSearchPage.this,1);
									
								}
							});
					    	
					    	askDialog.getCancel().setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									askDialog.getAskDialogDialog().dismiss();
								}
							});
						}
						
						
				   }else{
					   finish();
				   }
				    
			}
		}
	}
	
}
