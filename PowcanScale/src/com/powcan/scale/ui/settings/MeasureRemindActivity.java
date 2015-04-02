package com.powcan.scale.ui.settings;

import java.util.Calendar;
import java.util.Locale;

import com.powcan.scale.R;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.receiver.AlarmMeasure;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.util.SpUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MeasureRemindActivity extends BaseActivity implements OnClickListener
{
	private static final String TAG = MeasureRemindActivity.class.getSimpleName();
	
	private ImageView imgBack;
	private ImageView imgMorningSwitch;
	private ImageView imgNoonSwitch;
	private ImageView imgNightSwitch;
	private View morningSwitch;
	private View noonSwitch;
	private View nightSwitch;
	
	private int morningRemind;
	private int noonRemind;
	private int nightRemind;
	
	private UserInfo curUser;
	private UserInfoDb dbUserInfo;
	
	private static final String MORNING_TIME = "07:30";
	private static final String NOON_TIME = "13:00";
	private static final String NIGHT_TIME = "22:00";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measure_remind);
	}

	@Override
	public void onInit() 
	{
		String account = SpUtil.getInstance(this).getAccount();
		dbUserInfo = new UserInfoDb(this);
		curUser = dbUserInfo.getUserInfo( account );
	}

	@Override
	public void onFindViews() 
	{
		imgBack = (ImageView) findViewById(R.id.img_back);
		imgMorningSwitch = (ImageView) findViewById(R.id.img_morning_switch);
		imgNoonSwitch = (ImageView) findViewById(R.id.img_noon_switch);
		imgNightSwitch = (ImageView) findViewById(R.id.img_night_switch);
		morningSwitch =  findViewById(R.id.rl_morning_switch);
		noonSwitch = findViewById(R.id.rl_noon_switch);
		nightSwitch =  findViewById(R.id.rl_night_switch);
	}

	@Override
	public void onInitViewData() 
	{
		morningRemind = curUser.getMorningRemind();
		noonRemind = curUser.getNoonRemind();
		nightRemind = curUser.getNightRemind();
		
		imgMorningSwitch.setImageResource( morningRemind == 1 ? R.drawable.set_on : R.drawable.set_off );
		imgNoonSwitch.setImageResource( noonRemind == 1 ? R.drawable.set_on : R.drawable.set_off );
		imgNightSwitch.setImageResource( nightRemind == 1 ? R.drawable.set_on : R.drawable.set_off );
	}
	
	@Override
	public void onBindListener() 
	{
		imgBack.setOnClickListener( this );
		morningSwitch.setOnClickListener( this );
		noonSwitch.setOnClickListener( this );
		nightSwitch.setOnClickListener( this );
	}

	@Override
	public void onClick(View v) 
	{
		switch ( v.getId() ) 
		{
			case R.id.img_back:
				finish();
				break;
				
			case R.id.rl_morning_switch:
				imgMorningSwitch.setImageResource( morningRemind == 0 ? R.drawable.set_on : R.drawable.set_off );
				morningRemind = morningRemind == 1 ? 0 : 1;
				alarmMeasure( MORNING_TIME, morningRemind );
				break;
				
			case R.id.rl_noon_switch:
				imgNoonSwitch.setImageResource( noonRemind == 0 ? R.drawable.set_on : R.drawable.set_off );
				noonRemind =  noonRemind == 1 ? 0 : 1;
				alarmMeasure( NOON_TIME, noonRemind );
				break;
				
			case R.id.rl_night_switch:
				imgNightSwitch.setImageResource( nightRemind == 0 ? R.drawable.set_on : R.drawable.set_off );
				nightRemind =  nightRemind == 1 ? 0 : 1;
				alarmMeasure( NIGHT_TIME, nightRemind );
				break;
		}
	}
	
	/**
	 * 提醒测量体重
	 */
	private void alarmMeasure( String time, int isAlarm )
	{
		Log.d( TAG, "alarmMeasure-time : " + time );
		dbUserInfo.updateRemind( curUser.getAccount(), morningRemind, noonRemind, nightRemind );
		
		String []times = time.split(":");
		
		Calendar calendar = Calendar.getInstance( Locale.getDefault() );
	    calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf( times[0] ) );
	    calendar.set(Calendar.MINUTE, Integer.valueOf( times[1] ));
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    if( System.currentTimeMillis() > calendar.getTimeInMillis() )
	    { 
	        calendar.set(Calendar.DAY_OF_YEAR, calendar.get( Calendar.DAY_OF_YEAR) + 1 ); 
	    } 
	    Intent intent = new Intent( "com.powcan.scale.ALARM_MEASURE" );
	    intent.setClass( this, AlarmMeasure.class);
	    PendingIntent sender = PendingIntent.getBroadcast( this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );
	    
	    AlarmManager am = ( AlarmManager )getSystemService( ALARM_SERVICE );
	    
	    if( isAlarm == 1 )
	    {
	    	am.setRepeating( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender );
	    	showToast( time + "测量提醒已经开启" );
	    }
	    else
	    {
	    	am.cancel( sender );
	    	showToast( time + "测量提醒已经取消" );
	    }
	}
}
