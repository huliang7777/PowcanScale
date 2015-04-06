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
     */  
    public static int dip2px(Context context, float dpValue) 
    {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }
    
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
    
    public static String getCurDate()
    {
    	Calendar calendar = Calendar.getInstance();
    	int year = calendar.get( Calendar.YEAR );
    	int month = calendar.get( Calendar.MONTH ) + 1;
    	int day = calendar.get( Calendar.DAY_OF_MONTH );
    	return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }
    
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

	// 获取ApiKey
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

	// 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
	public static boolean hasBind(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		String flag = sp.getString("bind_flag", "");
		if ("ok".equalsIgnoreCase(flag)) {
			return true;
		}
		return false;
	}

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

	public static List<String> getTagsList(String originalText) {
		if (originalText == null || originalText.equals("")) {
			return null;
		}
		List<String> tags = new ArrayList<String>();
		int indexOfComma = originalText.indexOf(',');
		String tag;
		while (indexOfComma != -1) {
			tag = originalText.substring(0, indexOfComma);
			tags.add(tag);

			originalText = originalText.substring(indexOfComma + 1);
			indexOfComma = originalText.indexOf(',');
		}

		tags.add(originalText);
		return tags;
	}

	public static String getLogText(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString("log_text", "");
	}

	public static void setLogText(Context context, String text) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString("log_text", text);
		editor.commit();
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
	
	public static File getOutputFile(Context context) {
		File file = null;
		if (isExternalStorageWritable()) {
			file = getOutputMediaFile(MEDIA_TYPE_IMAGE);
		} else {
			file = getOutputInternalFile(context, MEDIA_TYPE_IMAGE);
		}
		return file;
	}

	private static File getOutputInternalFile(Context context, int mediaType) {
		FileOutputStream openFileOutput = null;
		try {
			String mediaFileName = getMediaFileName(mediaType);
			openFileOutput = context.openFileOutput(mediaFileName, Context.MODE_WORLD_WRITEABLE);
			File fileStreamPath = context.getFileStreamPath(mediaFileName);
			return fileStreamPath;
		} catch (Exception e) {
			e.printStackTrace();
			File fileDir = context.getCacheDir();
			File mediaFile = getMediaFile(mediaType, fileDir.getAbsolutePath());
			return mediaFile;
		} finally {
			close(openFileOutput);
		}
	}
	
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static File getOutputMediaFile(int mediaType) {
		String fileDir = getExternalStoragePublicDirectory("dailysee");
		File mediaFile = getMediaFile(mediaType, fileDir);
		return mediaFile;
	}

	public static File getMediaFile(int mediaType, String fileDir) {
		File mediaFile = new File(fileDir + File.separator + getMediaFileName(mediaType));
		return mediaFile;
	}

	public static String getMediaFileName(int mediaType) {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		if (mediaType == MEDIA_TYPE_IMAGE) {
			return "IMG_" + timeStamp + ".jpg";
		} else if (mediaType == MEDIA_TYPE_VIDEO) {
			return "VID_" + timeStamp + ".mp4";
		}
		throw new RuntimeException("媒体类型错误");
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	private static String getExternalStoragePublicDirectory(String dirName) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), dirName);
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String fileDir = mediaStorageDir.getPath();
		return fileDir;
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
	
	public static String getFileName(String filePath) {
		String fileName = null;
		if (!TextUtils.isEmpty(filePath)) {
			int lastBackslashIndex = filePath.lastIndexOf("/");
			if (lastBackslashIndex >= 0) {
				fileName = filePath.substring(lastBackslashIndex);
			}
		}
		if (!TextUtils.isEmpty(filePath)) {
			fileName = System.currentTimeMillis() + ".jpg";
		}
		return fileName;
	}
	
	public static void hideSoft(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	public static String formatTwoFractionDigits(double d) {
		return formatFractionDigits(d, 2);
	}
	
	public static String formatFractionDigits(double d, int count) {
		NumberFormat nf=NumberFormat.getNumberInstance() ;
		nf.setMaximumFractionDigits(count);
		return nf.format(d);
	}

}
