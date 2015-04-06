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
	private float minWeight;
	private float maxWeight;
	private MeasureResult measureResult;
	private MeasureResultDb dbMeasureResult;
	
	private List<Measure> list = new ArrayList<Measure>();
	private UserInfo curUser;
	
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
		measureResult = dbMeasureResult.getLastMeasureResult( curUser.getAccount() );
		if ( measureResult != null )
		{
			weight = (int) measureResult.getWeight();
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

		if ( list.isEmpty() ) 
		{
//			list.add(new Measure("体重", ""));
			list.add(new Measure("BMI", ""));
			list.add(new Measure("体脂率", ""));
			list.add(new Measure("肌肉比例", ""));
			list.add(new Measure("身体年龄", ""));
			list.add(new Measure("皮下脂肪", ""));
			list.add(new Measure("内脏脂肪", ""));
			list.add(new Measure("基础代谢(亚)", ""));
			list.add(new Measure("基础代谢(欧)", ""));
			list.add(new Measure("骨量", ""));
			list.add(new Measure("水含量", ""));
		}

		mAdapter = new HomeAdapter(mContext, list);
		mListView.setAdapter(mAdapter);
		
		weightView.setTotalProgress( maxWeight - minWeight );
		weightView.setData( String.valueOf( weight ), weight - minWeight );
		suggest();
	}

	@Override
	public void onBindListener() {

	}

	public void setWeightData(float weight) 
	{
//		list.get(0).data = weight + "KG";
//		mAdapter.notifyDataSetChanged();
		this.weight = weight;
		suggest();
		new Thread( new ProgressRunable() ).start();
	}
	
	class ProgressRunable implements Runnable 
	{
		@Override
		public void run() {
			float progress = weight - minWeight;
			float showProgress = 0.0f;
			
			int startCount = 0;
			int endCount = Math.round( ( weight - minWeight ) / ( maxWeight - minWeight ) * 100 ) ;
			
			float perProgress = progress / endCount;
			float perWeight = weight / endCount;
			
			float rWeight = 0.0f;
			while ( startCount < endCount ) 
			{
				showProgress += perProgress;
				
				rWeight += perWeight;
				final float showContent = (float)Math.round( rWeight * 10 ) / 10;
				
				final float curProgress = showProgress;
				
				weightView.post(new Runnable() {
					
					@Override
					public void run() {
						weightView.setData( String.valueOf( showContent ), curProgress );
					}
				});
				
				startCount += 1;
				try 
				{
					Thread.sleep(100);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
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
			tvSuggest.setText( "您的体重太轻了，请多补充营养" );
		}
		else if( weight < subWeight * 2 + minWeight )
		{
			tvSuggest.setText( "您的体重正常，请多多保持" );
		}
		else if( weight < subWeight * 3 + minWeight )
		{
			tvSuggest.setText( "您的体重稍微胖了点，请注意饮食，合理安排运动" );
		}
		else
		{
			tvSuggest.setText( "您的体重已经超标，请控制饮食，多运动" );
		}
	}
}
