package com.gheng.exhibit.service;

import java.io.File;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * 播放语音介绍
 * 
 * @author lixiaoming
 * 
 */
public class PlayVoiceService extends Service {

	private MediaPlayer mediaPlayer = null;
	private boolean isStop = false;// 用于标记音乐是否停止
	private String url = "";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("=====文化年展==onBind");
		return null;
	}

	// 做准备播放音乐的工作
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("=====文化年展==onCreate");
		// if (mediaPlayer == null) {
		// // 创建音乐播放器
		// // mediaPlayer = new MediaPlayer();
		// mediaPlayer = MediaPlayer.create(PlayVoiceService.this,
		// Uri.parse(url));//实例化对象，通过播放本机服务器上的一首音乐
		// preparePlayMusic(url);
		// // 设置音乐播放完毕的监听器
		// // 如果完成发送广播给UI界面
		// mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
		//
		// @Override
		// public void onCompletion(MediaPlayer mp) {
		// // 音乐播放完毕发送广播
		// Intent intent = new Intent();
		// // 设置频道
		// intent.setAction("play");
		// // 设置附加信息
		// Bundle bundle = new Bundle();
		// bundle.putString("info", "播放完毕是否重播");
		// intent.putExtras(bundle);
		// sendBroadcast(intent);
		// }
		// });
		// }
	}

	// 播放音乐的一些准备工作
	public void preparePlayMusic(String voiceUrl) {
		System.out.println("=====文化年展==preparePlayMusic");

		// 音乐的准备工作
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "jishiben.mp3";
		try {
			// 资源重置
			mediaPlayer.reset();
			// 设置该播放器播放的数据源音频文件
			mediaPlayer.setDataSource(path);
			// 准备工作，
			mediaPlayer.prepare();// 播放音乐前，必须执行prepare，表示准备播放音乐
			mediaPlayer.setLooping(false);// 设置是否循环播放

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
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

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("=====文化年展==onStartCommand");
		// 根据intent传入的state的值，判断到底执行：播放，暂停，停止
		int op = intent.getIntExtra("op", 0);
		Log.i("tag", "===onStartCommand,state:" + op);
		url = intent.getStringExtra("voiceurl");
		Log.d("tag", "===========onStartCommand-voiceurl-->" + url);
		if (mediaPlayer == null) {
			// 创建音乐播放器
			// mediaPlayer = new MediaPlayer();
			// preparePlayMusic(url);
			if (!TextUtils.isEmpty(url)) {
				mediaPlayer = MediaPlayer.create(PlayVoiceService.this,
						Uri.parse(url));// 实例化对象，通过播放本机服务器上的一首音乐
				try {
					mediaPlayer.setLooping(false);// 设置不循环播放
					// 设置音乐播放完毕的监听器
					// 如果完成发送广播给UI界面
					mediaPlayer
							.setOnCompletionListener(new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer mp) {
									// 音乐播放完毕发送广播
									Intent intent = new Intent();
									// 设置频道
									intent.setAction("play");
									// 设置附加信息
									Bundle bundle = new Bundle();
									bundle.putString("info", "播放完毕!");
									intent.putExtras(bundle);
									sendBroadcast(intent);
								}
							});
				} catch (Exception e) {
					// url有误发送广播
					Intent intent3 = new Intent();
					intent3.setAction("voiceError");
					sendBroadcast(intent3);
				}
				
			} else {
				// url为空发送广播
				Intent intent2 = new Intent();
				intent2.setAction("empty");
				sendBroadcast(intent2);

			}
		}

		switch (op) {
		case 1:
			// 播放
			play();
			// if (isStop) {
			// // 被停止过了
			// preparePlayMusic(url);
			// }
			// if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
			// mediaPlayer.start();
			// }
			break;

		case 2:
			// 暂停
			try {
				pause();
			} catch (Exception e) {
				// TODO: handle exception
			}

			// if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			// mediaPlayer.pause();
			// }
			break;
		case 3:
			// 停止
			stop();
			// if (mediaPlayer != null) {
			// mediaPlayer.stop();
			// // 执行了停止，会清空之前的资源设置
			// isStop = true;
			// }
			break;
		}

		return Service.START_NOT_STICKY;
	}

	// 停止播放音乐方法
	private void stop() {
		// 当mediaPlayer对象不为空时
		if (mediaPlayer != null) {
			mediaPlayer.seekTo(0);// 设置从头开始
			mediaPlayer.stop();// 停止播放
			try {
				mediaPlayer.prepare();// 预加载音乐
			} catch (IllegalStateException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	// 暂停播放音乐方法
	private void pause() {
		// 当mediaPlayer对象正在播放时并且player对象不为空时
		if (mediaPlayer.isPlaying() && mediaPlayer != null) {
			mediaPlayer.pause();// 暂停播放音乐
		}
	}

	// 播放音乐方法
	private void play() {
		// 当mediaPlayer对象不为空并且player不是正在播放时
		if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
			mediaPlayer.start();// 开始播放音乐
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("=====文化年展==onDestory");
		// stopSelf();//广播自杀
		if (mediaPlayer != null) {
			mediaPlayer.release();// 释放资源
			mediaPlayer = null;
		}

	}

}
