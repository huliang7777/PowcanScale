package com.powcan.scale.receiver;

import com.powcan.scale.MainActivity;
import com.powcan.scale.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmMeasure extends BroadcastReceiver 
{
	private static final String ACTION = "com.powcan.scale.ALARM_MEASURE";
	
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
		}  
	}

}
