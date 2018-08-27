package comvoice.example.zhangbin.zxingdemo;

import java.io.File;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class LauncherActivity extends Activity {
	private String selfId, security;
	private int port;
	private String ipadd;
	private Context context;
	private String updataUrl="http://218.201.212.6/im_client/yunwei.apk";
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		context=this;
		ImageView butPaipai = (ImageView) findViewById(R.id.img_paipai);
		butPaipai.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(Environment.getExternalStorageDirectory().getPath()+ "/img");
				if (!file.exists())
					file.mkdirs();
				File imageFile = new File(Environment.getExternalStorageDirectory().getPath()+ "/img/tempphoto1.jpg");
				Uri imageFileUri = Uri.fromFile(imageFile);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						imageFileUri);

				Bundle bundle=new Bundle();
				intent.putExtra("savepath",Environment.getExternalStorageDirectory().getPath()+ "/img/tempphoto.jpg");
				intent.putExtras(bundle);
				startActivityForResult(intent, 58);

			}
		});
		
//		ImageView butPaipaiqr = (ImageView) findViewById(R.id.img_paipaiqr);
//		butPaipaiqr.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(LauncherActivity.this, MainActivity.class);
//				startActivityForResult(intent,56);
//			}
//		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == 58 ){
		}
		
		if(requestCode==0){
			
		}
		Log.e("tag","requestCode="+requestCode + " resultCode="+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
	}
}