package com.gheng.exhibit.view.support;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;

public class Player implements OnBufferingUpdateListener, OnCompletionListener,
		MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {
	private int videoWidth;
	private int videoHeight;
	public MediaPlayer mediaPlayer;
	private SurfaceHolder surfaceHolder;
	private SeekBar skbProgress;
	private Timer mTimer = new Timer();
	private Context context;

	public Player(Context context,SurfaceView surfaceView, SeekBar skbProgress) {
		this.context = context;
		this.skbProgress = skbProgress;
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mTimer.schedule(mTimerTask, 0, 1000);
	}

	/*******************************************************
	 * 通过定时器和Handler来更新进度条
	 ******************************************************/
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {

			int position = mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();

			if (duration > 0) {
				long pos = skbProgress.getMax() * position / duration;
				skbProgress.setProgress((int) pos);
			}
		};
	};

	// *****************************************************

	public void play() {
		initMediaPlayer();
		mediaPlayer.reset();
		mediaPlayer.start();
	}

	public void playUrl(String videoUrl) {
		initMediaPlayer();
		try {
			mediaPlayer.reset();
			//mediaPlayer.setDataSource()
			mediaPlayer.setDataSource(context,Uri.parse(videoUrl));
//			mediaPlayer.prepare();// prepare之后自动播放
			mediaPlayer.prepareAsync();
			//mediaPlayer.prepareAsync();
			// mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pauseOrStart() {
		initMediaPlayer();
		if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
		}else{
			mediaPlayer.start();
		}
	}

	public void stop() {
		if (mediaPlayer != null) {
			mTimer.cancel();
			mediaPlayer.stop();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		initMediaPlayer();
		Log.e("mediaPlayer", "surface changed");
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		initMediaPlayer();
		Log.e("mediaPlayer", "surface created");
	}
	
	private void initMediaPlayer(){
		try {
			if (mediaPlayer == null) {
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDisplay(surfaceHolder);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.setOnBufferingUpdateListener(this);
				mediaPlayer.setOnPreparedListener(this);
			}
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.e("mediaPlayer", "surface destroyed");
		if (mediaPlayer != null) {
			if(mediaPlayer.isPlaying()){
				mediaPlayer.stop();
				mediaPlayer.release();
			}
			mediaPlayer = null;
		}
	}

	/**  
	 * 通过onPrepared播放  
	 */
	@Override
	public void onPrepared(MediaPlayer arg0) {
		videoWidth = mediaPlayer.getVideoWidth();
		videoHeight = mediaPlayer.getVideoHeight();
		if (videoHeight != 0 && videoWidth != 0) {
			arg0.start();
		}
		Log.e("mediaPlayer", "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		skbProgress.setSecondaryProgress(bufferingProgress);
		int currentProgress = skbProgress.getMax()
				* mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
		Log.e(currentProgress + "% play", bufferingProgress + "% buffer");

	}

}