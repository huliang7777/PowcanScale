package com.powcan.scale.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.powcan.scale.R;
import com.powcan.scale.adapter.HomeAdapter;
import com.powcan.scale.bean.Measure;
import com.powcan.scale.bean.MeasureResult;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.db.MeasureResultDb;
import com.powcan.scale.ui.base.BaseFragment;
import com.powcan.scale.util.SpUtil;
import com.powcan.scale.util.Utils;
import com.powcan.scale.widget.ProgressView;

public class HomeFragment extends BaseFragment 
{
	private int index;
	private ListView mListView;
	private HomeAdapter mAdapter;
	private ProgressView weightView;
	private TextView tvSuggest;
	
	private float weight;
	private float bmi;
	private float bodyFatRate;
	private float waterContent;
	private String weightResult;
	private String bmiResult;
	private String bodyFatRateResult;
	private String waterContentResult;

	private float minWeight;
	private float maxWeight;
	private MeasureResult measureResult;
	private MeasureResultDb dbMeasureResult;
	
	private List<Measure> list = new ArrayList<Measure>();
	private UserInfo curUser;
	
	private String[] bmiStandard;
	private String[] bodyFatRateStandard;
	private String[] waterContentStandard;
	private String[] bmiResults;
	private String[] bodyFatRateResults;
	private String[] waterContentResults;
	
	public HomeFragment() {
		super();
	}

	public static HomeFragment getInstance(int index) 
	{
		HomeFragment fragment = new HomeFragment();
		fragment.setIndex(index);
		return fragment;
	}

	private void setIndex(int index) 
	{
		this.index = index;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		return view;
	}

	@Override
	public void onInit() 
	{
		weight = 0;
		curUser = SpUtil.getInstance(mContext).getCurrUser();
		String range = Utils.getWeightRange( Integer.valueOf( curUser.getHeight() ), curUser.getGender(), 0.2f );
		String []ranges = range.split( "-" );
		minWeight = Float.valueOf( ranges[0] );
		maxWeight = Float.valueOf( ranges[1] );
		
		dbMeasureResult = new MeasureResultDb( mContext );
		measureResult = dbMeasureResult.getLastMeasureResult( curUser.getAccount() + "C" );
		if ( measureResult != null )
		{
			weight = measureResult.getWeight();
			bmi = measureResult.getBmi();
			bodyFatRate = measureResult.getBodyFatRate();
			waterContent = measureResult.getWaterContent();
		}
		else
		{
			measureResult = new MeasureResult();
			measureResult.setAccount( curUser.getAccount() + "C" );
			measureResult.setDate( curUser.getAccount() );
			dbMeasureResult.insertMeasureResult(measureResult);
			measureResult = dbMeasureResult.getLastMeasureResult( curUser.getAccount() + "C" );
		}
		
		bmiResults = mContext.getResources().getStringArray( R.array.bmi_result );
		bodyFatRateResults = mContext.getResources().getStringArray( R.array.bodyFatRate_result );
		waterContentResults = mContext.getResources().getStringArray( R.array.waterContent_result );
		
		bmiStandard = mContext.getResources().getStringArray( R.array.bmi_standard );
		
		if( Utils.calAge( curUser.getBirthday() ) <= 30 )
		{
			if ( curUser.getGender().equalsIgnoreCase("M") )
			{
				bodyFatRateStandard = mContext.getResources().getStringArray( R.array.bodyFatRate_male_less_thirty_standard );
			}
			else
			{
				bodyFatRateStandard = mContext.getResources().getStringArray( R.array.bodyFatRate_female_less_thirty_standard );
			}
		}
		else
		{
			if ( curUser.getGender().equalsIgnoreCase("M") )
			{
				bodyFatRateStandard = mContext.getResources().getStringArray( R.array.bodyFatRate_male_more_thirty_standard );
			}
			else
			{
				bodyFatRateStandard = mContext.getResources().getStringArray( R.array.bodyFatRate_female_more_thirty_standard );
			}
		}
		
		if( Utils.calAge( curUser.getBirthday() ) <= 30 )
		{
			if ( curUser.getGender().equalsIgnoreCase("M") )
			{
				waterContentStandard = mContext.getResources().getStringArray( R.array.waterContent_male_less_thirty_standard );
			}
			else
			{
				waterContentStandard = mContext.getResources().getStringArray( R.array.waterContent_female_less_thirty_standard );
			}
		}
		else
		{
			if ( curUser.getGender().equalsIgnoreCase("M") )
			{
				waterContentStandard = mContext.getResources().getStringArray( R.array.waterContent_male_more_thirty_standard );
			}
			else
			{
				waterContentStandard = mContext.getResources().getStringArray( R.array.waterContent_female_more_thirty_standard );
			}
		}
	}

	@Override
	public void onFindViews() {
		View v = getView();

		LayoutInflater inflater = LayoutInflater.from(mContext);
		View header = inflater.inflate(R.layout.item_home_header, null);

		mListView = (ListView) v.findViewById(R.id.list_view);
		mListView.addHeaderView(header);
		weightView = (ProgressView) v.findViewById(R.id.weight_view);
		tvSuggest = (TextView) v.findViewById(R.id.tv_suggest);
	}

	@Override
	public void onInitViewData() 
	{
		mListView.setVisibility(View.VISIBLE);
		suggest();
		if ( list.isEmpty() ) 
		{
			judgeResult();
			list.add(new Measure("体重", weight + "KG", weightResult));
			list.add(new Measure("体脂率", "" + bodyFatRate, bodyFatRateResult));
			list.add(new Measure("水含量", "" + waterContent, waterContentResult));
			list.add(new Measure("BMI", "" + bmi, bmiResult));
			list.add(new Measure("肌肉比例", "0.0", ""));
			list.add(new Measure("身体年龄", "0.0", ""));
			list.add(new Measure("皮下脂肪", "0.0", ""));
			list.add(new Measure("内脏脂肪", "0.0", ""));
			list.add(new Measure("基础代谢(亚)", "0.0", ""));
			list.add(new Measure("基础代谢(欧)", "0.0", ""));
			list.add(new Measure("骨量", "0.0", ""));
		}

		mAdapter = new HomeAdapter(mContext, list);
		mListView.setAdapter(mAdapter);
		
		weightView.setTotalProgress( maxWeight - minWeight );
//		setWeightData( 47.0f );
		weightView.setData( String.valueOf( weight ), weight - minWeight );
	}

	@Override
	public void onBindListener() 
	{

	}

	public void setWeightData( float weight, float bodyFatRate, float waterContent ) 
	{
		this.weight = weight;
		this.bodyFatRate = bodyFatRate;
		this.waterContent = waterContent;
		bmi = weight / ( (Float.valueOf( curUser.getHeight() ) / 100) * (Float.valueOf( curUser.getHeight() ) / 100) );
		bmi = (float)Math.round( bmi * 100 ) / 100;
		measureResult.setAccount( curUser.getAccount() + "C" );
		measureResult.setWeight(weight);
		measureResult.setBodyFatRate(bodyFatRate);
		measureResult.setWaterContent(waterContent);
		measureResult.setBmi( bmi );
		measureResult.setDate( curUser.getAccount() );
		// 更新最新一条数据
		dbMeasureResult.updateMeasureResult( measureResult );
		
		measureResult.setAccount( curUser.getAccount() );
		measureResult.setDate( Utils.getCurDate() );
		
		MeasureResult result = dbMeasureResult.getMeasureResult( curUser.getAccount(), Utils.getCurDate() );
		if ( result == null )
		{
			dbMeasureResult.insertMeasureResult( measureResult );
		}
		else
		{
			result.setWeight( (float)Math.round( ( weight + result.getWeight() ) / 2 * 100 ) / 100  );
			result.setBodyFatRate( (float)Math.round( ( bodyFatRate + result.getBodyFatRate() ) / 2 ) / 100  );
			result.setWaterContent( (float)Math.round( ( waterContent + result.getWaterContent() ) / 2 ) / 100 );
			result.setBmi( bmi );
			dbMeasureResult.updateMeasureResult( result );
		}
		
		suggest();
		judgeResult();
		new Thread( new ProgressRunable() ).start();
		
		list.get(0).data = weight + "KG";
		list.get(0).result = weightResult;
		list.get(1).data = "" + bodyFatRate;
		list.get(1).result = bodyFatRateResult;
		list.get(2).data = "" + waterContent;
		list.get(2).result = waterContentResult;
		list.get(3).data = "" + bmi;
		list.get(3).result = bmiResult;
		mAdapter.notifyDataSetChanged();
	}
	
	class ProgressRunable implements Runnable 
	{
		@Override
		public void run() {
			float progress = weight - minWeight;
			if (progress >= 0) {
				float showProgress = 0.0f;

				int startCount = 0;
				int endCount = Math.round(progress / (maxWeight - minWeight)
						* 100);

				float perProgress = progress / endCount;
				float perWeight = weight / endCount;

				float rWeight = 0.0f;
				while (startCount < endCount) 
				{
					showProgress += perProgress;

					rWeight += perWeight;
					final float showContent = (float) Math.round(rWeight * 100) / 100;

					final float curProgress = showProgress;

					weightView.post(new Runnable() 
					{
						@Override
						public void run() 
						{
							weightView.setData(String.valueOf(showContent), curProgress);
						}
					});
					startCount += 1;
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else
			{
				weightView.setData(String.valueOf(weight), progress );
			}
		}
	}
	
	private void suggest()
	{
		float subWeight = ( maxWeight - minWeight ) / 4;
		if ( weight == 0 )
		{
			tvSuggest.setText( "您还没有测量体重，赶紧测量哦~" );
		}
		else if ( weight < subWeight + minWeight )
		{
			weightResult = "偏瘦";
			tvSuggest.setText( "您的体重太轻了，请多补充营养" );
		}
		else if( weight < subWeight * 2 + minWeight )
		{
			weightResult = "标准";
			tvSuggest.setText( "您的体重正常，请多多保持" );
		}
		else if( weight < subWeight * 3 + minWeight )
		{
			weightResult = "微胖";
			tvSuggest.setText( "您的体重稍微胖了点，请注意饮食，合理安排运动" );
		}
		else
		{
			weightResult = "超标";
			tvSuggest.setText( "您的体重已经超标，请控制饮食，多运动" );
		}
	}
	
	private void judgeResult()
	{
		if ( bmi > 0.0 )
		{
			for( int i=0;i<bmiStandard.length;i++ )
			{
				if ( bmi <= Float.valueOf( bmiStandard[i] ) )
				{
					bmiResult = bmiResults[i];
					break;
				}
			}
		}
		
		if ( bodyFatRate > 0.0 )
		{
			for( int i=0;i<bodyFatRateStandard.length;i++ )
			{
				if ( bodyFatRate <= Float.valueOf( bodyFatRateStandard[i] ) )
				{
					bodyFatRateResult = bodyFatRateResults[i];
					break;
				}
			}
		}
		
		if ( waterContent > 0.0 )
		{
			for( int i=0;i<waterContentStandard.length;i++ )
			{
				if ( waterContent >= Float.valueOf( waterContentStandard[i] ) )
				{
					waterContentResult = waterContentResults[i];
					break;
				}
			}
		}
	}
}
