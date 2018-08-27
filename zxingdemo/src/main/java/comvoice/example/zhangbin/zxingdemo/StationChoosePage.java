package comvoice.example.zhangbin.zxingdemo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class StationChoosePage extends Activity {

	private Context context = null;
	private ListView myList;
	private MyListAdapter adapter;
	
	private RelativeLayout return_layout,choose_name_layout;
	private String result ,mamsId,selfId;
	private Button goback;
	MyReceiver myReceiver;
	List<StationInfo> stationList;
	private int choose_station = -2;
	private String locationJingdu;
	private String locationWeidu;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.station_choose_page);
		context = this;

		init();
	}
	
	
	

	public void init() {
		
		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.close_mams_list");
		registerReceiver(myReceiver, filter);
		
		return_layout = (RelativeLayout)findViewById(R.id.return_layout);
		choose_name_layout = (RelativeLayout)findViewById(R.id.choose_name_layout);
		myList = (ListView)findViewById(R.id.choose_list);
		goback = (Button)findViewById(R.id.choose_return);
		
		Intent intent = this.getIntent();
		Bundle bunde = intent.getExtras();
		
		mamsId = bunde.getString("mams_Id");
		result  = bunde.getString("mams_result");
		selfId = bunde.getString("selfId");
		locationJingdu = bunde.getString("locationJingdu");
		locationWeidu = bunde.getString("locationWeidu");
		
		stationList = new ArrayList<StationInfo>();
		result = result.substring(1,result.length());
		
		try {
			JSONArray totalArray = new JSONArray(result);
			for(int i=0;i<totalArray.length();i++){
				JSONArray array = new JSONArray(totalArray.getString(i));
				stationList.add(new StationInfo(array.getString(0), array.getString(1), array.getDouble(2), array.getDouble(3), array.getString(4))); 
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		adapter = new MyListAdapter(context);
		myList.setAdapter(adapter);
		
		choose_name_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				adapter.setCurrentID(-1);
				choose_station = -1;
				Intent intent=getIntent();
				Bundle bun = new Bundle();
				bun.putString("mams_Id", mamsId);
				bun.putString("selfId", selfId);
				bun.putString("jingdu", locationJingdu);
				bun.putString("weidu", locationWeidu);
				intent.setClass(StationChoosePage.this, StationSearchPage.class);
				
				intent.putExtras(bun);
				startActivity(intent);
			}
		});
		
		
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
		
		myList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				choose_station = position;
				
				Intent intent=new Intent();
				
				Bundle bun = new Bundle();
				bun.putString("mams_Id", mamsId);
				bun.putString("selfId", selfId);
				intent.setClass(StationChoosePage.this, FormPage.class);
				StationInfo station = stationList.get(choose_station);
				bun.putString("jingdu", station.getJingdu()+"");
				bun.putString("weidu", station.getWeidu()+"");
				bun.putString("area", station.getArea()+"");
				bun.putString("mams_name", station.getName()+"");
				bun.putInt("blind_type", 0);
				intent.putExtras(bun);
				startActivity(intent);
				
			}
		});
		
	}
	
	
	class StationInfo{
		String name;
		String distance;
		String area;
		double jingdu;
		double weidu;
		
		
		public StationInfo(String name,  String area,
				double jingdu, double weidu,String distance) {
			super();
			this.name = name;
			this.distance = distance;
			this.area = area;
			this.jingdu = jingdu;
			this.weidu = weidu;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDistance() {
			return distance;
		}
		public void setDistance(String distance) {
			this.distance = distance;
		}
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
		}
		public double getJingdu() {
			return jingdu;
		}
		public void setJingdu(double jingdu) {
			this.jingdu = jingdu;
		}
		public double getWeidu() {
			return weidu;
		}
		public void setWeidu(double weidu) {
			this.weidu = weidu;
		}
		
		
	}
	
	
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
				convertView = inflater.inflate(R.layout.station_choose_page_items,
						null);
				myHolder.choose_name_layout = (RelativeLayout) convertView
						.findViewById(R.id.choose_name_layout);
				myHolder.station_name = (TextView) convertView
						.findViewById(R.id.station_name);
				myHolder.station_distance = (TextView) convertView
						.findViewById(R.id.station_distance);
				myHolder.station_area = (TextView) convertView
						.findViewById(R.id.station_area);
	

			} else {
				myHolder = (Holder) convertView.getTag();
			}
			convertView.setTag(myHolder);
			myHolder.station_name.setText(stationList.get(position).getName());
			myHolder.station_distance.setText("距离"+stationList.get(position).getDistance()+"米");
			myHolder.station_area.setText(stationList.get(position).getArea());
			

			return convertView;
		}

		class Holder {
			RelativeLayout choose_name_layout;
			TextView station_name;
			TextView station_area;
			TextView station_distance;			
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
		
		super.onDestroy();
		try {
			unregisterReceiver(myReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	private class MyReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.intent.action.close_mams_list")) {
				   
				    finish();
				    
			}
		}
	}

}
