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
		selected = 0;
		account = SpUtil.getInstance( mContext ).getAccount();
		
		dbMeasureResult = new MeasureResultDb( mContext );
	}
	

	@Override
	public void onFindViews() 
	{
		View v = getView();
		
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
		btnWeek.setOnClickListener( this );
		btnMonth.setOnClickListener( this );
		btnQuarter.setOnClickListener( this );
		btnYear.setOnClickListener( this );
		
		btnWeek.callOnClick();
	}

	@Override
	public void onClick(View v) 
	{
		switch ( v.getId() ) 
		{
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
		
		HashMap<String, String> map = dbMeasureResult.getMeasureResults( account, dates[ 0 ], dates[6] );

		// 处理数据
		ArrayList<MeasureResult> measureResults = new ArrayList<MeasureResult>();
		if ( !map.isEmpty() )
		{
			MeasureResult measureResult = null;
			for( int i=0; i< dates.length; i++ )
			{
				measureResult = new MeasureResult();
				measureResult.setAccount(account);
				
				String weight = map.get( dates[i] );
				if ( weight != null )
				{
					measureResult.setWeight( Integer.valueOf( weight ) );
					measureResult.setDate( dates[i].substring(5) );
				}
				else
				{
					measureResult.setWeight( 0.0f );
					measureResult.setDate( dates[i].substring(5) );
				}
				measureResults.add( measureResult );
			}
			showChart( measureResults );
		}
		
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
    	
		HashMap<String, String> map = dbMeasureResult.getMeasureResults( account, firstDate, lastDate );
		// 处理数据
		ArrayList<MeasureResult> measureResults = new ArrayList<MeasureResult>();
		if ( !map.isEmpty() )
		{
			MeasureResult measureResult = null;
			String date = null;
			int week = dayOfWeek;
			float totalWeight = 0;
			int count = 0;
			int weekCount = 1;
			for( int i=1; i<=endDay; i++ )
			{
				date = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", i);
				
				String weight = map.get( date );
				if ( weight != null )
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
			showChart( measureResults );
		}
	}
	
	private void dealQuarterData()
	{
		int []quarterDates = Utils.getQuarterDate( -1 );
		int year = quarterDates[ 6 ];
    	String firstDate = year + "-" + String.format("%02d", quarterDates[0]) + "-01";
    	String lastDate = year + "-" + String.format("%02d", quarterDates[4]) + "-" + String.format("%02d", quarterDates[5]);
		
    	tvDesc.setText( firstDate + "~" + lastDate );
    	
		HashMap<String, String> map = dbMeasureResult.getMeasureResults( account, firstDate, lastDate );
		// 处理数据
		ArrayList<MeasureResult> measureResults = new ArrayList<MeasureResult>();
		if ( !map.isEmpty() )
		{
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
					
					String weight = map.get( date );
					if ( weight != null )
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
			showChart( measureResults );
		}
		
	}
	
	private void dealYearData()
	{
		int []quarterDates = Utils.getYearDate();
		int year = quarterDates[ 24 ];
    	String firstDate = year + "-" + String.format("%02d", quarterDates[0]) + "-01";
    	String lastDate = year + "-" + String.format("%02d", quarterDates[22]) + "-" + String.format("%02d", quarterDates[23]);
		
    	tvDesc.setText( firstDate + "~" + lastDate );
    	
		HashMap<String, String> map = dbMeasureResult.getMeasureResults( account, firstDate, lastDate );
		// 处理数据
		ArrayList<MeasureResult> measureResults = new ArrayList<MeasureResult>();
		if ( !map.isEmpty() )
		{
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
					
					String weight = map.get( date );
					if ( weight != null )
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
			showChart( measureResults );
		}
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
}
