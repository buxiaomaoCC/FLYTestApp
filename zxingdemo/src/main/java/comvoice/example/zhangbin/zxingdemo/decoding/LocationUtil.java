package comvoice.example.zhangbin.zxingdemo.decoding;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;


public class LocationUtil {
	
	LocationClient locationClient=null;
	MyLocationListenner myListenner=null;
	
	public double locLatitude = 0;
	public double locLongitude = 0;
	public String locAdd = "";
	int type = 0; //0是扫二维码后定位 1是搜索页面定位  2是提交时候定位
	

	Context context;

	
	
	public LocationUtil( Context context,int type) {
		super();
		this.context = context;
		this.type = type;
		startLocation();
	}


	public void startLocation(){
		if(locationClient == null){
			 try {
				 locationClient= new LocationClient(context);
				 
					LocationClientOption lcOption = new LocationClientOption();
					lcOption.setOpenGps(true);
					lcOption.setCoorType("bd09ll");
					lcOption.setPriority(LocationClientOption.GpsFirst);
					lcOption.setProdName("Scan_lbs");
					lcOption.setAddrType("all");
					lcOption.disableCache(true);//禁止使用缓存定位
					lcOption.setOpenGps(true);// 设置是否打开gps，使用gps前提是用户硬件打开gps。默认是不打开gps的。
					lcOption.setScanSpan(100);//1000ms下 一次定位 
					myListenner = new MyLocationListenner();
					locationClient.setLocOption(lcOption);
					locationClient.registerLocationListener(myListenner);
					locationClient.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		 }
	}
	
	public void stopLocation(){
		if (locationClient != null) {
			locationClient.stop();
			locationClient.unRegisterLocationListener(myListenner);
			locationClient = null;
		}
	
	}

	private class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
	
				int locType = location.getLocType(); 
				if(locType != 61 && locType != 161 && locType != 66 && locType != 68 && locType != 65){
					Log.i("scan",
							"loctype is not in [161,61],return!"
									+ location.getLocType() + "......");

				}else{
					locLongitude = location.getLongitude();
					locLatitude = location.getLatitude();
					locAdd = location.hasAddr() ? location.getAddrStr() : "";
				}
				
				Log.i("scan", location.getTime() + "经度" + locLatitude + "维度"
						+ locLongitude + "地址" + locAdd);
				
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putDouble("jingdu", locLongitude);
					bundle.putDouble("weidu", locLatitude);
					
					if(type == 0){
						intent.setAction("android.intent.action.locationonce");
					}else if(type == 1){
						intent.setAction("android.intent.action.close_searchpage");	
						bundle.putString("action","location");
					}else if(type == 2){
						intent.setAction("android.intent.action.form_page_location");
					}
					
				
				
				stopLocation();
				intent.putExtras(bundle);
				context.sendBroadcast(intent);
			
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
		
		}
		
	}

}
