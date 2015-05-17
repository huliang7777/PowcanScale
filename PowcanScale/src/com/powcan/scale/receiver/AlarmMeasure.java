package com.powcan.scale.receiver;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.powcan.scale.MainActivity;
import com.powcan.scale.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Vibrator;

/**
 * 测量提醒广播接收类
 * @author Administrator
 *
 */
public class AlarmMeasure extends BroadcastReceiver 
{
	private static final String ACTION = "com.powcan.scale.ALARM_MEASURE";
	
	/**
	 * 提醒处理，发送通知
	 */
	@Override
	public void onReceive( Context context, Intent intent ) 
	{
		if( ACTION.equals( intent.getAction() ) )
		{
			//定义NotificationManager
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
			// 定义通知栏展现的内容信息
			int icon = R.drawable.ic_launcher;
			CharSequence tickerText = "测量提醒";
			long when = System.currentTimeMillis();
			Notification notification = new Notification(icon, tickerText, when);

			// 定义下拉通知栏时要展现的内容信息
			CharSequence contentTitle = "测量提醒";
			CharSequence contentText = "到点了啦，可以测量体重了....";
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			
			Intent notificationIntent = new Intent( context, MainActivity.class );
			PendingIntent contentIntent = PendingIntent.getActivity( context, 0,
					notificationIntent, 0);
			notification.setLatestEventInfo(context, contentTitle, contentText,
					contentIntent);
			// 用mNotificationManager的notify方法通知用户生成标题栏消息通知
			mNotificationManager.notify(1, notification);
			
//			final MediaPlayer mMediaPlayer = MediaPlayer.create( context, 
//					RingtoneManager.getActualDefaultRingtoneUri( context, RingtoneManager.TYPE_RINGTONE) );
//			
//			
//			if ( mMediaPlayer != null )
//			{
//				notification.defaults = Notification.DEFAULT_LIGHTS;
//				mMediaPlayer.setVolume( 1.0f, 1.0f );
//				mMediaPlayer.setLooping( false );
//				mMediaPlayer.start();
//				
//				new Timer().schedule( new TimerTask() {
//					
//					@Override
//					public void run() {
//						if ( mMediaPlayer.isPlaying() )
//						{
//							mMediaPlayer.pause();
//						}
//						mMediaPlayer.stop();
//						mMediaPlayer.release();
//					}
//				}, 5000 );
//			}
//			else
//			{
//				notification.defaults = Notification.DEFAULT_SOUND;
//			}
			
			AssetManager assetManager = context.getAssets();
			AssetFileDescriptor fileDescriptor;
			try {
				fileDescriptor = assetManager.openFd("measure.mp3");
				final MediaPlayer mMediaPlayer = new MediaPlayer();
				mMediaPlayer.setVolume( 1.0f, 1.0f );
				mMediaPlayer.setLooping( false );
				mMediaPlayer.setDataSource( fileDescriptor.getFileDescriptor(),
	                    fileDescriptor.getStartOffset(),
	                    fileDescriptor.getLength() );
				
				mMediaPlayer.prepare();
				mMediaPlayer.start();
				
				
				new Timer().schedule( new TimerTask() {
				
					@Override
					public void run() {
						if ( mMediaPlayer.isPlaying() )
						{
							mMediaPlayer.pause();
						}
						mMediaPlayer.stop();
						mMediaPlayer.release();
					}
				}, 5000 );
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(5000);
		}  
	}

}
