package com.gheng.exhibit.view.noused;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;

import com.gheng.exhibit.utils.AppTools;
import com.gheng.exhibit.view.BaseActivity;
import com.gheng.exhibit.view.support.Player;
import com.gheng.exhibit.widget.TitleBar;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.smartdot.wenbo.huiyi.R;

public class VideoPlayerActivity extends BaseActivity {
	
	@ViewInject(R.id.surfaceView)
	private SurfaceView surfaceView;  
	
	@ViewInject(R.id.btnPause)
    private Button btnPause;
	
	@ViewInject(R.id.btnPlayUrl)
	private Button btnPlayUrl;
	
	@ViewInject(R.id.btnStop)
	private Button btnStop;  
	
	@ViewInject(R.id.skbProgress)
    private SeekBar skbProgress;  
	
    private Player player;  
    
    private int pchi;
    
    @ViewInject(R.id.titleBar)
    private TitleBar titleBar;
  
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_video);  
    }  
    
    @Override
    protected void init() {
    	pchi = getIntent().getIntExtra("pchi", 0);
    	btnPlayUrl.setOnClickListener(new ClickEvent());  
    	btnPause.setOnClickListener(new ClickEvent());  
        btnStop.setOnClickListener(new ClickEvent());  
        
        btnPlayUrl.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);
        btnPause.setVisibility(View.GONE);
        
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());  
        player = new Player(this,surfaceView, skbProgress);  
        
        titleBar.setOnClickListener(new TitleBar.OnClickListener() {
			@Override
			public void clickRightImage() {
				
			}
			
			@Override
			public void clickLeftImage() {
				finish();
			}
		});
        
        String url= AppTools.imageChange(getIntent().getStringExtra("videourl"));
        player.playUrl(url);
    }
  
    class ClickEvent implements OnClickListener {  
  
        @Override  
        public void onClick(View arg0) {  
            if (arg0 == btnPause) {  
                player.pauseOrStart();  
            } else if (arg0 == btnPlayUrl) {  
               String url= AppTools.imageChange(getIntent().getStringExtra("videourl"));
               player.playUrl(url);
            } else if (arg0 == btnStop) {  
                player.stop();  
            }  
  
        }  
    }  
  
    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {  
        int progress;  
        @Override  
        public void onProgressChanged(SeekBar seekBar, int progress,  
                boolean fromUser) {  
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()  
            this.progress = progress * player.mediaPlayer.getDuration()  
                    / seekBar.getMax();  
        }  
        @Override  
        public void onStartTrackingTouch(SeekBar seekBar) {  
        }  
        @Override  
        public void onStopTrackingTouch(SeekBar seekBar) {  
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字  
            player.mediaPlayer.seekTo(progress);  
        }  
    }

	@Override
	protected void setI18nValue() {
		
	}  
}