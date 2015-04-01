package com.powcan.scale;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class PowcanScaleApplication extends Application 
{
	private final static String TAG = "PowcanScaleApplication";
	private static PowcanScaleApplication instance = null;
	
	private List<Activity> activityList = new LinkedList<Activity>();
	
	public static PowcanScaleApplication getInstance()
	{
		return instance;
	}
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		
		Log.i(TAG, "onCreate");
		
		instance = this;
	}
	
	// 添加Activity到容器中
	public void addActivity(Activity activity) 
	{
		activityList.add(activity);
	}

	// 添加Activity到容器中
	public void removeActivity(Activity activity) 
	{
		activityList.remove(activity);
	}

	// 遍历所有Activity并finish
	public void exit() 
	{
		for (Activity activity : activityList) 
		{
			activity.finish();
		}
		System.exit(0);
	}
}
