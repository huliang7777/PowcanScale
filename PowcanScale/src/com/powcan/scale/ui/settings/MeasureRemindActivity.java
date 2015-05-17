package com.powcan.scale.ui.settings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.powcan.scale.R;
import com.powcan.scale.adapter.RemindAdapter;
import com.powcan.scale.bean.Remind;
import com.powcan.scale.dialog.SelectStrDataDialog;
import com.powcan.scale.dialog.SelectStrDataDialog.ItemClickEvent;
import com.powcan.scale.dialog.TimeDialog;
import com.powcan.scale.receiver.AlarmMeasure;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.util.SpUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * 测量提醒界面
 * @author Administrator
 *
 */
public class MeasureRemindActivity extends BaseActivity implements OnClickListener, ItemClickEvent
{
	private static final String TAG = MeasureRemindActivity.class.getSimpleName();
	
	private ImageView imgBack;
	private TextView tvAdd;
//	private ImageView imgMorningSwitch;
//	private ImageView imgNoonSwitch;
//	private ImageView imgNightSwitch;
//	private View morningSwitch;
//	private View noonSwitch;
//	private View nightSwitch;
	
	private ListView list;
	private String[] reminds;
	private String[] switches;
	private RemindAdapter adapter;
	private ArrayList<Remind> datas = new ArrayList<Remind>();
	
//	private int morningRemind;
//	private int noonRemind;
//	private int nightRemind;
	
	private static final String MORNING_TIME = "07:30";
	private static final String NOON_TIME = "13:00";
	private static final String NIGHT_TIME = "22:00";
	private static final String[] contents = { "早上测量(建议在早餐前)", "中午测量(建议在午睡前)", "晚上测量(建议在入眠前)", "自定义提醒测量" };
	
	private SelectStrDataDialog operatorDialog;
	private ArrayList<String> operators = new ArrayList<String>();
	private int oprtIndex = -1;
	
	/**
	 * 创建界面方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_measure_remind);
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void onInit() 
	{
		String account = SpUtil.getInstance(this).getAccount();
		
		reminds = SpUtil.getInstance( this ).getRemind( account ).split( "," );
		switches = SpUtil.getInstance( this ).getRemindSwitch( account ).split( "," );
		
		if ( reminds == null || reminds.length < 3 )
		{
			reminds = new String[]{ MORNING_TIME, NOON_TIME, NIGHT_TIME };
			switches = new String[]{ "0", "0", "0" };
			SpUtil.getInstance( this ).setRemind( account, MORNING_TIME + "," + NOON_TIME + "," + NIGHT_TIME );
			SpUtil.getInstance( this ).setRemindSwitch( account, "0,0,0" );
		}
		
		Remind remind = null;
		for ( int i=0;i<reminds.length;i++ )
		{
			remind = new Remind();
			remind.setTime( reminds[ i ] );
			remind.setOn( switches[ i ].equals("1") );
			if ( i > 3 )
			{
				remind.setContent( contents[ 3 ] );
			}
			else
			{
				remind.setContent( contents[ i ] );
			}
			datas.add( remind );
		}
		
		operators.add( "修改" );
		operators.add( "删除" );
	}

	/**
	 * 查找子view
	 */
	@Override
	public void onFindViews() 
	{
		imgBack = (ImageView) findViewById(R.id.img_back);
		list = (ListView) findViewById( R.id.list_view );
		tvAdd =  (TextView) findViewById( R.id.tv_add );
//		imgMorningSwitch = (ImageView) findViewById(R.id.img_morning_switch);
//		imgNoonSwitch = (ImageView) findViewById(R.id.img_noon_switch);
//		imgNightSwitch = (ImageView) findViewById(R.id.img_night_switch);
//		morningSwitch =  findViewById(R.id.rl_morning_switch);
//		noonSwitch = findViewById(R.id.rl_noon_switch);
//		nightSwitch =  findViewById(R.id.rl_night_switch);
	}

	/**
	 * 初始化view界面显示数据
	 */
	@Override
	public void onInitViewData() 
	{
		adapter = new RemindAdapter( this , datas );
		list.setAdapter( adapter );
	}
	
	/**
	 * 绑定事件监听
	 */
	@Override
	public void onBindListener() 
	{
		imgBack.setOnClickListener( this );
		tvAdd.setOnClickListener( this );
		list.setOnItemClickListener( new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) 
			{
				Remind remind = datas.get( position );
				boolean isOn = !remind.isOn();
				alarmMeasure( remind.getTime(), isOn ? 1 : 0, true );
				remind.setOn( !remind.isOn() );
				adapter.notifyDataSetChanged();
				
				String account = SpUtil.getInstance( MeasureRemindActivity.this ).getAccount();
				SpUtil.getInstance( MeasureRemindActivity.this ).setRemind( account, genRemind() );
				SpUtil.getInstance( MeasureRemindActivity.this ).setRemindSwitch( account, genRemindSwitch() );
			}
		});
		list.setOnItemLongClickListener( new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) 
			{
				oprtIndex = position;
				if ( oprtIndex > 2 )
				{
					operatorDialog = new SelectStrDataDialog( MeasureRemindActivity.this, operators, "操作", 0, MeasureRemindActivity.this );
					operatorDialog.show();
				}
//				else
//				{
//					showToast( "系统提醒,不能修改删除" );
//				}
				return true;
			}
		});
	}

	/**
	 * 点击事件处理方法
	 */
	@Override
	public void onClick(View v) 
	{
		switch ( v.getId() ) 
		{
			case R.id.img_back:
				finish();
				break;
				
//			case R.id.rl_morning_switch:
//				imgMorningSwitch.setImageResource( morningRemind == 0 ? R.drawable.set_on : R.drawable.set_off );
//				morningRemind = morningRemind == 1 ? 0 : 1;
//				alarmMeasure( MORNING_TIME, morningRemind );
//				break;
//				
//			case R.id.rl_noon_switch:
//				imgNoonSwitch.setImageResource( noonRemind == 0 ? R.drawable.set_on : R.drawable.set_off );
//				noonRemind =  noonRemind == 1 ? 0 : 1;
//				alarmMeasure( NOON_TIME, noonRemind );
//				break;
//				
//			case R.id.rl_night_switch:
//				imgNightSwitch.setImageResource( nightRemind == 0 ? R.drawable.set_on : R.drawable.set_off );
//				nightRemind =  nightRemind == 1 ? 0 : 1;
//				alarmMeasure( NIGHT_TIME, nightRemind );
//				break;
				
			case R.id.tv_add:
				new TimeDialog( this, new OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
					{
						Remind remind = new Remind();
						remind.setTime( String.format( "%02d", hourOfDay ) + ":" + String.format( "%02d", minute ) );
						remind.setOn( false );
						remind.setContent( contents[ 3 ] );
						datas.add( remind );
						adapter.notifyDataSetChanged();
						
						String account = SpUtil.getInstance( MeasureRemindActivity.this ).getAccount();
						SpUtil.getInstance( MeasureRemindActivity.this ).setRemind( account, genRemind() );
						SpUtil.getInstance( MeasureRemindActivity.this ).setRemindSwitch( account, genRemindSwitch() );
					}
				}, 7, 0, true ).show();
				break;
		}
	}
	
	/**
	 * 提醒测量体重
	 */
	private void alarmMeasure( String time, int isAlarm, boolean isOut )
	{
		Log.d( TAG, "alarmMeasure-time : " + time );
		
		String []times = time.split(":");
		int reqCode = Integer.valueOf( times[0] ) * 100 + Integer.valueOf( times[1] );
		
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
	    PendingIntent sender = PendingIntent.getBroadcast( this, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT );
	    
	    AlarmManager am = ( AlarmManager )getSystemService( ALARM_SERVICE );
	    
	    if( isAlarm == 1 )
	    {
	    	am.setRepeating( AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender );
	    	showToast( time + "测量提醒已经开启" );
	    }
	    else
	    {
	    	am.cancel( sender );
	    	if ( isOut )
	    	{
	    		showToast( time + "测量提醒已经取消" );
	    	}
	    }
	}
	
	/**
	 * 构造提醒数据
	 * @return
	 */
	private String genRemind()
	{
		StringBuffer strBuff = new StringBuffer( MORNING_TIME );
		strBuff.append( "," ).append( NOON_TIME )
		.append( "," ).append( NIGHT_TIME );
		
		int size = datas.size();
		for ( int i=3;i<size;i++ ) 
		{
			strBuff.append( "," ).append( datas.get( i ).getTime() );
		}
		
		return strBuff.toString();
	}
	
	/**
	 * 构造提醒是否开关数据
	 */
	private String genRemindSwitch()
	{
		StringBuffer strBuff = new StringBuffer();
		if ( datas.isEmpty() )
		{
			return "0,0,0";
		}
		int size = datas.size();
		for ( int i=0;i<size;i++ ) 
		{
			if ( i != 0 )
			{ 
				strBuff.append( "," );
			}
			strBuff.append( datas.get( i ).isOn() ? "1" : "0" );
		}
		return strBuff.toString();
	}

	/**
	 * 提醒用户自定义项操作回调
	 */
	@Override
	public void onItemClick( int which ) 
	{
		// 修改时间
		if ( which == 0 )  
		{
			if ( oprtIndex > 2 )
			{
				final Remind remind = datas.get( oprtIndex );
				String[] time = remind.getTime().split( ":" );
				new TimeDialog( this, new OnTimeSetListener() {
					
					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
					{
						if ( remind.isOn() )
						{
							alarmMeasure( remind.getTime(), 0, false );
						}
						remind.setTime( String.format( "%02d", hourOfDay ) + ":" + String.format( "%02d", minute ) );
						remind.setOn( false );
						adapter.notifyDataSetChanged();
						
						String account = SpUtil.getInstance( MeasureRemindActivity.this ).getAccount();
						SpUtil.getInstance( MeasureRemindActivity.this ).setRemind( account, genRemind() );
						SpUtil.getInstance( MeasureRemindActivity.this ).setRemindSwitch( account, genRemindSwitch() );
					}
				}, Integer.valueOf( time[ 0 ] ), Integer.valueOf( time[ 1 ] ), true ).show();
			}
		}
		// 删除提醒
		else if ( which == 1 )
		{
			if ( oprtIndex > 2 )
			{
				Remind remind = datas.remove( oprtIndex );
				adapter.notifyDataSetChanged();
				String account = SpUtil.getInstance( MeasureRemindActivity.this ).getAccount();
				SpUtil.getInstance( MeasureRemindActivity.this ).setRemind( account, genRemind() );
				SpUtil.getInstance( MeasureRemindActivity.this ).setRemindSwitch( account, genRemindSwitch() );
				
				alarmMeasure( remind.getTime(), 0, false ); 
			}
		}
		operatorDialog.dismiss();
		operatorDialog = null;
	}
}
