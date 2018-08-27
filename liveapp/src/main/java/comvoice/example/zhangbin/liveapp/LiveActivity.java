package comvoice.example.zhangbin.liveapp;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

public class LiveActivity extends AppCompatActivity {
    private String url,title;
    private RelativeLayout rl_loding,rl_top_layout ,activity_live,rl_buttom_layout;
    private ProgressBar pb_loding;
    private TextView tv_loding,tv_title,tv_time;
    private ImageView iv_return,iv_play;
    private io.vov.vitamio.widget.VideoView videoView;
    private static final int RETRY_TIMES=5;
    private int mCount=0;//计数器
    private final long AUTO_HIDE_TIME=5000;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rl_top_layout.setVisibility(View.GONE);
            rl_buttom_layout.setVisibility(View.GONE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        url=getIntent().getStringExtra("url");
        title=getIntent().getStringExtra("title");
        initView();
        initTime();
        initClick();
        initPlayer();
    }

    private void initPlayer() {
        Vitamio.isInitialized(LiveActivity.this);
        videoView.setVideoURI(Uri.parse(url));
        videoView.setOnPreparedListener(new io.vov.vitamio.MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(io.vov.vitamio.MediaPlayer mp) {
                videoView.start();
            }
        });
       videoView.setOnErrorListener(new io.vov.vitamio.MediaPlayer.OnErrorListener() {
           @Override
           public boolean onError(io.vov.vitamio.MediaPlayer mp, int what, int extra) {
               if(mCount>RETRY_TIMES){
                   new AlertDialog.Builder(LiveActivity.this).setMessage("播放出错")
                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                       }
                   }).setCancelable(false).show();
               }else {
                   videoView.stopPlayback();
                   videoView.setVideoURI(Uri.parse(url));
                   mCount++;
               }
               return false;
           }
       });
       videoView.setOnInfoListener(new io.vov.vitamio.MediaPlayer.OnInfoListener() {
           @Override
           public boolean onInfo(io.vov.vitamio.MediaPlayer mp, int what, int extra) {
               switch (what){
                   case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                       rl_loding.setVisibility(View.VISIBLE);
                       break;
                   case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                       rl_loding.setVisibility(View.GONE);
                       break;
                   default:
                       break;
               }
               return false;
           }
       });
    }

    private void initClick() {
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoView.isPlaying()){
                    videoView.stopPlayback();
                }else {
                    videoView.setVideoURI(Uri.parse(url));
                    videoView.setOnPreparedListener(new io.vov.vitamio.MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(io.vov.vitamio.MediaPlayer mp) {
                            videoView.start();
                        }
                    });
                    iv_play.setImageResource(R.drawable.play);
                }
            }
        });
        activity_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //顶部布局为显示时，则隐藏
                if(rl_buttom_layout.getVisibility()==View.VISIBLE||rl_top_layout.getVisibility()==View.VISIBLE){
                    rl_top_layout.setVisibility(View.GONE);
                    rl_buttom_layout.setVisibility(View.GONE);
                    return;
                }
                if(videoView.isPlaying()){//视频正在播放
                    iv_play.setImageResource(R.drawable.pause);
                }else {
                    iv_play.setImageResource(R.drawable.play);
                }
                //隐藏时，则显示
                rl_buttom_layout.setVisibility(View.VISIBLE);
                rl_top_layout.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           Thread.sleep(5000);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                       Message message=handler.obtainMessage();
                       handler.sendMessage(message);
                   }
               }).start();
            }
        });
    }

    private void initTime() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date =new Date(System.currentTimeMillis());
        tv_time.setText(simpleDateFormat.format(date));
    }

    private void initView() {
        videoView=findViewById(R.id.videoView);
        rl_loding=findViewById(R.id.rl_loding);
        pb_loding=findViewById(R.id.pb_loding);
        tv_loding=findViewById(R.id.tv_loding);
        tv_title=findViewById(R.id.tv_title);
        iv_return=findViewById(R.id.iv_return);
        tv_time=findViewById(R.id.tv_time);
        rl_top_layout=findViewById(R.id.rl_top_layout);
        activity_live=findViewById(R.id.activity_live);
        rl_buttom_layout=findViewById(R.id.rl_buttom_layout);
        iv_play=findViewById(R.id.iv_play);
//        rl_buttom_layout.setVisibility(View.VISIBLE);
//        rl_top_layout.setVisibility(View.VISIBLE);
        tv_title.setText(title);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mCount=0;
        if(videoView!=null){
            videoView.stopPlayback();
        }
    }
}
