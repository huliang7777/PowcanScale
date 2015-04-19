package com.powcan.scale.ui.fragment;


import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.powcan.scale.R;
import com.powcan.scale.bean.MeasureResult;
import com.powcan.scale.bean.chart.ChartData;
import com.powcan.scale.db.MeasureResultDb;
import com.powcan.scale.ui.base.BaseFragment;
import com.powcan.scale.util.SpUtil;
import com.powcan.scale.util.Utils;
import com.powcan.scale.widget.chart.FancyChart;

public class ChartFragment extends BaseFragment implements OnClickListener 
{
	private int index;
	
	private Button btnDay;
	private Button btnWeek;
	private Button btnMonth;
	private Button btnQuarter;
	private Button btnYear;
	private FancyChart chartView;
	private TextView tvDesc;
	
	private MeasureResultDb dbMeasureResult;
	private String account;
	
	private int selected;
	
	public ChartFragment() {
		super();
	}

	public static ChartFragment getInstance(int index) {
		ChartFragment fragment = new ChartFragment();
		fragment.setIndex(index);
		return fragment;
	}

	private void setIndex(int index) {
		this.index = index;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chart, null);
		return view;
	}

	@Override
	public void onInit() 
	{
		selected = -1;
		account = SpUtil.getInstance( mContext ).getAccount();
		
		dbMeasureResult = new MeasureResultDb( mContext );
	}
	

	@Override
	public void onFindViews() 
	{
		View v = getView();
		
		btnDay = (Button) v.findViewById(R.id.btn_day);
		btnWeek = (Button) v.findViewById(R.id.btn_week);
		btnMonth = (Button) v.findViewById(R.id.btn_month);
		btnQuarter = (Button) v.findViewById(R.id.btn_quarter);
		btnYear = (Button) v.findViewById(R.id.btn_year);
		chartView = (FancyChart) v.findViewById(R.id.chart_view);
		tvDesc = (TextView) v.findViewById(R.id.tv_desc);
	}

	@Override
	public void onInitViewData() 
	{
	}

	@Override
	public void onBindListener() 
	{
		btnDay.setOnClickListener( this );
		btnWeek.setOnClickListener( this );
		btnMonth.setOnClickListener( this );
		btnQuarter.setOnClickListener( this );
		btnYear.setOnClickListener( this );
		
		btnDay.callOnClick();
	}

	@Override
	public void onClick(View v) 
	{
		switch ( v.getId() ) 
		{
			case R.id.btn_day:
				if ( selected == 0 )
				{
					return;
				}
				selected = 0;
				resetButton();
				btnDay.setBackgroundResource(R.color.radio_color);
				btnDay.setTextColor(mContext.getResources().getColor( R.color.white ));
				
			break;
			case R.id.btn_week:
				if ( selected == 1 )
				{
					return;
				}
				selected = 1;
				resetButton();
				btnWeek.setBackgroundResource(R.color.radio_color);
				btnWeek.setTextColor(mContext.getResources().getColor( R.color.white ));
				
				break;
			case R.id.btn_month:
				if ( selected == 2 )
				{
					return;
				}
				selected = 2;
				resetButton();
				btnMonth.setBackgroundResource(R.color.radio_color);
				btnMonth.setTextColor(mContext.getResources().getColor( R.color.white ));
				break;
			case R.id.btn_quarter:
				if ( selected == 3 )
				{
					return;
				}
				selected = 3;
				resetButton();
				btnQuarter.setBackgroundResource(R.color.radio_color);
				btnQuarter.setTextColor(mContext.getResources().getColor( R.color.white ));
				break;
			case R.id.btn_year:
				if ( selected == 4 )
				{
					return;
				}
				selected = 4;
				resetButton();
				btnYear.setBackgroundResource(R.color.radio_color);
				btnYear.setTextColor(mContext.getResources().getColor( R.color.white ));
				break;
		}
		changeContent();
	}
	
	private void resetButton()
	{
		btnDay.setBackgroundResource(R.drawable.bg_rectangle);
		btnDay.setTextColor(mContext.getResources().getColor( R.color.radio_color ));
		btnWeek.setBackgroundResource(R.drawable.bg_rectangle);
		btnWeek.setTextColor(mContext.getResources().getColor( R.color.radio_color ));
		btnMonth.setBackgroundResource(R.drawable.bg_rectangle);
		btnMonth.setTextColor(mContext.getResources().getColor( R.color.radio_color ));
		btnQuarter.setBackgroundResource(R.drawable.bg_rectangle);
		btnQuarter.setTextColor(mContext.getResources().getColor( R.color.radio_color ));
		btnYear.setBackgroundResource(R.drawable.bg_rectangle);
		btnYear.setTextColor(mContext.getResources().getColor( R.color.radio_color ));
	}
	
	private void changeContent()
	{
		chartView.clearValues();
		switch ( selected ) 
		{
			case 0:
				dealDayData();
			break;
			case 1:
				dealWeekData();
				break;
			case 2:
				dealMonthData();
				break;
			case 3:
				dealQuarterData();
				break;
			case 4:
				dealYearData();
				break;
		}
	}
	
	private void dealDayData()
	{
		ArrayList<MeasureResult> results = dbMeasureResult.getMeasureResults( account, 5 );
		
		// 处理数据
		ArrayList<MeasureResult> measureResults = new ArrayList<MeasureResult>();
//		if ( !results.isEmpty() )
//		{
			int length = 5;
			int size = results.size();
			tvDesc.setText( "最近" + length + "次测量" );
			
			MeasureResult measureResult = null;
			for( int i=0; i< 5; i++ )
			{
				measureResult = new MeasureResult();
				measureResult.setAccount(account);
				
				if ( i < size )
				{
					Float weight = results.get( i ).getWeight();
					measureResult.setWeight( weight );
					measureResult.setDate( String.valueOf( i + 1 ) );
				}
				else
				{
					measureResult.setWeight( 0.0f );
					measureResult.setDate( String.valueOf( i + 1 ) );
				}
				measureResults.add( measureResult );
			}
			
			chartView.setVisibility(View.VISIBLE);
			showChart( measureResults );
//		}
//		else
//		{
//			tvDesc.setText( "" );
//			chartView.setVisibility(View.GONE);
//		}
		
	}
	
	private void dealWeekData()
	{
		String []dates = Utils.getCurWeekDate();
		
//		MeasureResult measureResult = null;
//		for( int i=0; i< dates.length; i++ )
//		{
//			measureResult = new MeasureResult();
//			measureResult.setAccount(account);
//			
//			measureResult.setWeight( (int)( Math.random() * 100 / 10 ) + 40 );
//			measureResult.setDate( dates[i] );
//
//			dbMeasureResult.insertMeasureResult( measureResult );
//		}
		tvDesc.setText( dates[ 0 ] + "~" + dates[6] );
		
		HashMap<String, Float> map = dbMeasureResult.getMeasureResults( account, dates[ 0 ], dates[6] );

		// 处理数据
		ArrayList<MeasureResult> measureResults = new ArrayList<MeasureResult>();
//		if ( !map.isEmpty() )
//		{
			MeasureResult measureResult = null;
			for( int i=0; i< dates.length; i++ )
			{
				measureResult = new MeasureResult();
				measureResult.setAccount(account);
				
				Float weight = map.get( dates[i] );
				if ( weight != null && weight != 0.0f )
				{
					measureResult.setWeight( weight );
					measureResult.setDate( dates[i].substring(5) );
				}
				else
				{
					measureResult.setWeight( 0.0f );
					measureResult.setDate( dates[i].substring(5) );
				}
				measureResults.add( measureResult );
			}
			chartView.setVisibility(View.VISIBLE);
			showChart( measureResults );
//		}
//		else
//		{
//			chartView.setVisibility(View.GONE);
//		}
		
	}
	
	private void dealMonthData()
	{
		int []monthDays = Utils.getCurMonthDate();
		int year = monthDays[ 0 ];
    	int month = monthDays[ 1 ];
    	int endDay = monthDays[ 2 ];
    	int dayOfWeek = monthDays[ 3 ];
    	
    	String firstDate = year + "-" + String.format("%02d", month) + "-01";
    	String lastDate = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", endDay);
		
    	tvDesc.setText( firstDate + "~" + lastDate );
    	
		HashMap<String, Float> map = dbMeasureResult.getMeasureResults( account, firstDate, lastDate );
		// 处理数据
		ArrayList<MeasureResult> measureResults = new ArrayList<MeasureResult>();
//		if ( !map.isEmpty() )
//		{
			MeasureResult measureResult = null;
			String date = null;
			int week = dayOfWeek;
			float totalWeight = 0;
			int count = 0;
			int weekCount = 1;
			for( int i=1; i<=endDay; i++ )
			{
				date = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", i);
				
				Float weight = map.get( date );
				if ( weight != null && weight != 0.0f )
				{
					totalWeight += Float.valueOf( weight );
					++ count;
				}
				
				if ( week == 7 || i == endDay )
				{
					measureResult = new MeasureResult();
					measureResult.setAccount(account);
					measureResult.setWeight( Math.round( totalWeight / count ) );
					measureResult.setDate( "第" + weekCount + "周" );
					measureResults.add( measureResult );
					count = 0;
					week = 0;
					totalWeight = 0;
					weekCount ++ ;
				}
				++week;
			}
			chartView.setVisibility(View.VISIBLE);
			showChart( measureResults );
//		}
//		else
//		{
//			chartView.setVisibility(View.GONE);
//		}
	}
	
	private void dealQuarterData()
	{
		int []quarterDates = Utils.getQuarterDate( -1 );
		int year = quarterDates[ 6 ];
    	String firstDate = year + "-" + String.format("%02d", quarterDates[0]) + "-01";
    	String lastDate = year + "-" + String.format("%02d", quarterDates[4]) + "-" + String.format("%02d", quarterDates[5]);
		
    	tvDesc.setText( firstDate + "~" + lastDate );
    	
		HashMap<String, Float> map = dbMeasureResult.getMeasureResults( account, firstDate, lastDate );
		// 处理数据
		ArrayList<MeasureResult> measureResults = new ArrayList<MeasureResult>();
//		if ( !map.isEmpty() )
//		{
			MeasureResult measureResult = null;
			String date = null;
			
			for( int i=0;i<3;i++)
			{
				float totalWeight = 0;
				int count = 0;
				int month = quarterDates[ i * 2];
				int endDay = quarterDates[ i * 2 + 1];
				for( int j=1; j<=endDay; j++ )
				{
					date = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", j);
					
					Float weight = map.get( date );
					if ( weight != null && weight != 0.0f )
					{
						totalWeight += Float.valueOf( weight );
						++ count;
					}
				}
				measureResult = new MeasureResult();
				measureResult.setAccount(account);
				measureResult.setWeight(Math.round(totalWeight / count));
				measureResult.setDate( month + "月" );
				measureResults.add(measureResult);
			}
			chartView.setVisibility(View.VISIBLE);
			showChart( measureResults );
//		}
//		else
//		{
//			chartView.setVisibility(View.GONE);
//		}
		
	}
	
	private void dealYearData()
	{
		int []quarterDates = Utils.getYearDate();
		int year = quarterDates[ 24 ];
    	String firstDate = year + "-" + String.format("%02d", quarterDates[0]) + "-01";
    	String lastDate = year + "-" + String.format("%02d", quarterDates[22]) + "-" + String.format("%02d", quarterDates[23]);
		
    	tvDesc.setText( firstDate + "~" + lastDate );
    	
		HashMap<String, Float> map = dbMeasureResult.getMeasureResults( account, firstDate, lastDate );
		// 处理数据
		ArrayList<MeasureResult> measureResults = new ArrayList<MeasureResult>();
//		if ( !map.isEmpty() )
//		{
			MeasureResult measureResult = null;
			String date = null;
			float totalWeight = 0;
			int count = 0;
			for( int i=0;i<12;i++)
			{
				int month = quarterDates[ i * 2];
				int endDay = quarterDates[ i * 2 + 1];
				for( int j=1; j<=endDay; j++ )
				{
					date = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", j);
					
					Float weight = map.get( date );
					if ( weight != null && weight != 0.0f )
					{
						totalWeight += Float.valueOf( weight );
						++ count;
					}
				}
				
				if( i % 3 == 2 )
				{
					measureResult = new MeasureResult();
					measureResult.setAccount(account);
					measureResult.setWeight(Math.round(totalWeight / count));
					measureResult.setDate( ( i / 3 + 1 ) + "季度" );
					measureResults.add(measureResult);
					totalWeight = 0;
					count = 0;
				}
			}
			chartView.setVisibility(View.VISIBLE);
			showChart( measureResults );
//		}
//		else
//		{
//			chartView.setVisibility(View.GONE);
//		}
	}
	
	private void showChart( ArrayList<MeasureResult> measureResults )
	{
		ChartData data = new ChartData(ChartData.LINE_COLOR_GREEN);
		int size = measureResults.size();
		MeasureResult mMeasureResult = null;
		for(int i=0; i<size; i++) 
		{
			mMeasureResult = measureResults.get(i);
			data.addPoint( i, (int)mMeasureResult.getWeight() );
			data.addXValue( i, mMeasureResult.getDate() );
		}
		chartView.addData(data);
		chartView.postInvalidate();
	}
	
	@Override
	public void reloadData()
	{
		super.reloadData();
		if ( mContext == null )
		{
			return;
		}
		account = SpUtil.getInstance( mContext ).getAccount();
		changeContent();
	}
}
