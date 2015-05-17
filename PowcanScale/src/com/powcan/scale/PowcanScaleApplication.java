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

/**
 * 全局的运行类
 * @author Administrator
 *
 */
public class PowcanScaleApplication extends Application 
{
	private final static String TAG = "PowcanScaleApplication";
	private static PowcanScaleApplication instance = null;
	
	private List<Activity> activityList = new LinkedList<Activity>();
	
	/**
	 * 获得该对象的实例
	 * @return 全局类型实例Application
	 */
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
	
	/**
	 * 添加Activity到容器中
	 * @param activity 界面activity
	 */
	public void addActivity(Activity activity) 
	{
		activityList.add(activity);
	}

	/**
	 * 删除Activity到容器中
	 * @param activity 界面activity
	 */
	public void removeActivity(Activity activity) 
	{
		activityList.remove(activity);
	}

	/**
	 * 退出并关闭所有界面
	 */
	public void exit() 
	{
		clear();
		System.exit(0);
	}
	
	/**
	 * 关闭所有界面
	 * 遍历所有Activity并finish
	 */
	public void clear()
	{
		for (Activity activity : activityList) 
		{
			activity.finish();
		}
	}
}
