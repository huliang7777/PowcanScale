package com.powcan.scale.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore.MediaColumns;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * 工具类
 * @author Administrator
 *
 */
public class Utils {
	public static final String TAG = "PushDemoActivity";
	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_CONTENT = "content";
	public static final String RESPONSE_ERRCODE = "errcode";
	protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
	public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	protected static final String EXTRA_ACCESS_TOKEN = "access_token";
	public static final String EXTRA_MESSAGE = "message";

	public static String logStringCache = "";

	public static final int MEDIA_TYPE_IMAGE = 1;

	public static final int MEDIA_TYPE_VIDEO = 2;
	
	public static final int[][] quarterMonths = { {1,2,3}, {4,5,6}, {7,8,9}, {10,11,12} };
	
	 /**
	  * 根据手机的分辨率从 dp 的单位 转成为 px(像素)   
	  * @param context 界面上下文
	  * @param dpValue dp单位数据
	  * @return 像素单位数据
	  */
    public static int dip2px(Context context, float dpValue) 
    {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }
    
    /**
     * 保留小数点几位小数
     * @param d 数据
     * @param count 几位小数
     * @return 数据
     */
    public static String formatFractionDigits( float d, int count) 
    {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(count);
		return nf.format( d );
	}
    
    /**
     * 根据生日计算年龄
     * @param birthday 生日
     * @return 年龄
     */
    @SuppressLint("SimpleDateFormat")
	public static int calAge( String birthday )
	{
		int age = 0;
		if ( TextUtils.isEmpty( birthday ) || birthday.equalsIgnoreCase("null") || birthday.equals("0000-00-00") )
		{
			return age;
		}
		
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		Date birth = null;
		try 
		{
			birth = sdf.parse( birthday );
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			return age;
		}
		
		SimpleDateFormat format_y = new SimpleDateFormat( "yyyy" );
		SimpleDateFormat format_M = new SimpleDateFormat( "MM" );
		

		String birth_year = format_y.format( birth );
		String this_year = format_y.format( now );

		String birth_month = format_M.format( birth );
		String this_month = format_M.format( now );

		// 初步，估算
		age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);

		// 如果未到出生月份，则age - 1
		if (this_month.compareTo(birth_month) < 0)
		{
			age -= 1;
		}
		if (age < 0)
		{
			age = 0;
		}
		return age;
	}
    
    /**
     * 获得当前日期
     * @return 日期
     */
    public static String getCurDate()
    {
    	Calendar calendar = Calendar.getInstance();
    	int year = calendar.get( Calendar.YEAR );
    	int month = calendar.get( Calendar.MONTH ) + 1;
    	int day = calendar.get( Calendar.DAY_OF_MONTH );
    	return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }
    
    /**
     * 获得当前日期时间
     * @return 日期时间
     */
    public static String getCurDateTime()
    {
    	Calendar calendar = Calendar.getInstance();
    	int year = calendar.get( Calendar.YEAR );
    	int month = calendar.get( Calendar.MONTH ) + 1;
    	int day = calendar.get( Calendar.DAY_OF_MONTH );
    	int hour = calendar.get( Calendar.HOUR_OF_DAY );
    	int minute = calendar.get( Calendar.MINUTE );
    	int second = calendar.get( Calendar.SECOND );
    	return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) 
    		+ " " + String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second) ;
    }
    
    /**
     * 根据身高和年龄获取体重的范围
     * @param height 身高
     * @param gender 年龄
     * @param percent 范围的上下比例
     * @return 体重范围数据
     */
    public static String getWeightRange( int height, String gender, float percent )
    {
    	// 健康体重计算方式
     	// 标准体重=身高(m)×身高(m)×标准系数（女性20，男性22）
 		// 标准体重正负10﹪为正常体重
    	int ratio = 22;
    	float minWeight = 0;
    	float maxWeight = 0;
    	
    	if ( gender.equalsIgnoreCase( "F" ) )
    	{
    		ratio = 20;
    	}
    	float weight = (float)height / 100 * (float)height / 100 * ratio; 
		minWeight = (float)( Math.round(weight * ( 1.0 - percent ) * 100) / 100 );
		maxWeight = (float)( Math.round(weight * ( 1.0 + percent ) * 100) / 100 ) ; 
    	
		Log.d(TAG, minWeight + "-" + maxWeight);
    	return minWeight + "-" + maxWeight;
    }
    
    /**
     * 获得当前周的所有日期
     * @return 当前周的日期始末
     */
    public static String[] getCurWeekDate()
    {
    	String [] dates = new String[ 7 ];
    	Calendar calendar = Calendar.getInstance();
        // 今天是一周中的第几天
        int dayOfWeek = calendar.get( Calendar.DAY_OF_WEEK );
 
        if ( calendar.getFirstDayOfWeek() == Calendar.SUNDAY ) 
        {
        	calendar.add( Calendar.DAY_OF_MONTH, 1 );
        }
        // 计算一周开始的日期
        calendar.add( Calendar.DAY_OF_MONTH, -dayOfWeek );
        
        int year = calendar.get( Calendar.YEAR );
    	int month = calendar.get( Calendar.MONTH ) + 1;
    	int day = calendar.get( Calendar.DAY_OF_MONTH );
    	dates[0] = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
        
    	for ( int i=1; i<7; i++ )
    	{
    		// 计算一周结束的日期
            calendar.add( Calendar.DAY_OF_MONTH, 1 );
            
            year = calendar.get( Calendar.YEAR );
        	month = calendar.get( Calendar.MONTH ) + 1;
        	day = calendar.get( Calendar.DAY_OF_MONTH );
        	dates[i] = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    	}
        return dates;
    }
    
    /**
     * 获得当前月的信息
     * @return 当前月的信息
     */
    public static int[] getCurMonthDate()
    {
    	Calendar calendar = Calendar.getInstance();
    	int year = calendar.get( Calendar.YEAR );
    	int month = calendar.get( Calendar.MONTH ) + 1;
    	int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    	
    	calendar.set(Calendar.DAY_OF_MONTH, 1);
    	int dayOfWeek = calendar.get( Calendar.DAY_OF_WEEK);
    	
    	return new int[]{ year, month, endDay, dayOfWeek };
    }
    
    /**
     * 获得当前月季度的信息
     * @param m 月
     * @return 季度信息
     */
    public static int[] getQuarterDate( int m )
    {
    	int[] dates = new int[7];
    	Calendar calendar = Calendar.getInstance();
    	int year = calendar.get( Calendar.YEAR );
    	int month = m;
    	if ( month == -1 )
    	{
    		month = calendar.get( Calendar.MONTH );
    	}
    	int[] quarterMonth = quarterMonths[ month / 3 ];
    	
    	for(int i=0;i<quarterMonth.length;i++)
    	{
    		calendar.set( Calendar.MONTH, quarterMonth[i] - 1 );
        	int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        	dates[ i * 2 ] = quarterMonth[i];
        	dates[ i * 2 + 1 ] = endDay;
    	}
    	dates[ 6 ] = year;
    	return dates;
    }
    
    /**
     * 获得年的信息
     * @return 年信息
     */
    public static int[] getYearDate()
    {
    	int[] dates = new int[25];
    	Calendar calendar = Calendar.getInstance();
    	int year = calendar.get( Calendar.YEAR );
    	for(int i=0;i<12;i++)
    	{
    		calendar.set( Calendar.MONTH, i );
        	int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        	dates[ i * 2 ] = i + 1;
        	dates[ i * 2 + 1 ] = endDay;
    	}
    	dates[ 24 ] = year;

    	return dates;
    }

	/**
	 * 获取ApiKey
	 * @param context
	 * @param metaKey
	 * @return
	 */
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	/**
	 * 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
	 * @param context
	 * @return
	 */
	public static boolean hasBind(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		String flag = sp.getString("bind_flag", "");
		if ("ok".equalsIgnoreCase(flag)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 设置绑定
	 * @param context
	 * @param flag
	 */
	public static void setBind(Context context, boolean flag) {
		String flagStr = "not";
		if (flag) {
			flagStr = "ok";
		}
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString("bind_flag", flagStr);
		editor.commit();
	}

	/**
	 * 获取手机的imei.
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei;
	}

	/**
	 * 判断网络是否可用.
	 * 
	 * @param context
	 *            上下文
	 * @return true代表网络可用，false代表网络不可用.
	 */
	public static boolean isNetworkValid(Context context) {
		boolean result = false;
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			result = false;
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null) {
				result = false;
			} else {
				if (info.isAvailable()) {
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * 显示（时间是短暂的）土司消息
	 * 
	 * @param context
	 *            上下文
	 * @param msg
	 *            消息内容
	 */
	public static void showMsg(Context context, String msg) {
		if (context != null && msg != null) {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 外部存储是否可写(也可读)，true代表可写，false代表不可写.
	 * 
	 * @return
	 */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
	
	

	/**
	 * 根据uri获取图片的真实路径.
	 * 
	 * @param activity
	 *            活动对象
	 * @param uri
	 *            uri
	 * @return 图片的真实路径
	 */
	public static String getPath(Activity activity, Uri uri) {
		String path=uri.getPath();
		try{
			String[] proj = { MediaColumns.DATA };
			// 好像是android多媒体数据库的封装接口，具体的看Android文档
			Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
			// 按我个人理解 这个是获得用户选择的图片的索引值
			int columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			// 将光标移至开头 ，这个很重要，不小心很容易引起越界
			cursor.moveToFirst();
			// 最后根据索引值获取图片路径
			path = cursor.getString(columnIndex);
		}catch(Exception e){
			//Uri是一个文件类型而不是一个多媒体类型，也就是说已file结尾而不是content结尾
		}
		return path;
	}

	/**
	 * 安全执行runnable的UI事件.
	 * 
	 * @param activity
	 * @param runnable
	 */
	public static void runOnUiThreadSafety(Activity activity,
			final Runnable runnable) {
		if (activity == null) {
			return;
		}
		if (runnable == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					runnable.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 关闭处理中的对话框.
	 * 
	 * @param progressDialog
	 *            处理对话框
	 */
	public static void clossDialog(Dialog dialog) {
		if (dialog != null && dialog.isShowing() && dialog.getWindow() != null) {
			dialog.dismiss();
		}
	}
	
	/**
	 * 保留2位小数
	 * @param d
	 * @return
	 */
	public static String formatTwoFractionDigits(double d) {
		return formatFractionDigits((float)d, 2);
	}
}
