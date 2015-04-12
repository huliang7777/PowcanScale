package com.powcan.scale.ui.fragment;


import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.powcan.scale.R;
import com.powcan.scale.bean.MeasureResult;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.db.MeasureResultDb;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.ui.base.BaseFragment;
import com.powcan.scale.util.SpUtil;

public class ReportFragment extends BaseFragment implements OnClickListener 
{
	private int index;
	
	private TextView tvDistanceGoal;
	private TextView tvWeight;
	private TextView tvContinueDay;
	private Button btnShare;
	private Button btnSport;
	private Button btnNutrition;
	private TextView tvContent;
	
	private UserInfo curUser;
	private UserInfoDb dbUserInfo;
	private MeasureResult measureResult;
	private MeasureResultDb dbMeasureResult;
	private ArrayList<MeasureResult> measureResults;
	
	private float curWeight;
	private int goalWeight;
	private int continueDay;
	
	private boolean isSport;

	public ReportFragment() {
		super();
	}

	public static ReportFragment getInstance(int index) {
		ReportFragment fragment = new ReportFragment();
		fragment.setIndex(index);
		return fragment;
	}

	private void setIndex(int index) {
		this.index = index;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report, null);
		return view;
	}

	@Override
	public void onInit() 
	{
		isSport = true;
		continueDay = 0;
		
		dbUserInfo = new UserInfoDb( mContext );
		dbMeasureResult = new MeasureResultDb( mContext );
		
		loadData();
	}
	
	public void loadData()
	{
		String account = mSpUtil.getAccount();
		curUser = dbUserInfo.getUserInfo( account );
		measureResults = dbMeasureResult.getMeasureResults( account );
		measureResult = dbMeasureResult.getLastMeasureResult( account );
		if ( measureResult == null )
		{
			measureResult = new MeasureResult();
		}
		
		goalWeight = Integer.valueOf( curUser.getGoalWeight() );
		curWeight = measureResult.getWeight();
		
		calContinueDay();
	}
	
	private void calContinueDay()
	{
		int size = measureResults.size();
		int curContinueDay = 0;
		MeasureResult measureResult1 = null;
		MeasureResult measureResult2 = null;
		for( int i=0; i<size - 1; i++ )
		{
			measureResult1 = measureResults.get( i );
			measureResult2 = measureResults.get( i + 1 );
			if ( measureResult1.getWeight() < measureResult2.getWeight() )
			{
				curContinueDay ++;
			}
			else
			{
				if ( curContinueDay > continueDay )
				{
					continueDay = curContinueDay;
				}
				curContinueDay = 0;
			}
		}
		if ( size > 0 && curContinueDay > continueDay )
		{
			continueDay = curContinueDay;
		}
	}

	@Override
	public void onFindViews() 
	{
		View v = getView();
		tvDistanceGoal = (TextView) v.findViewById(R.id.tv_distance_goal);
		tvWeight = (TextView) v.findViewById(R.id.tv_weight);
		tvContinueDay = (TextView) v.findViewById(R.id.tv_continue_day);
		btnShare = (Button) v.findViewById(R.id.btn_share);
		btnSport = (Button) v.findViewById(R.id.btn_sport);
		btnNutrition = (Button) v.findViewById(R.id.btn_nutrition);
		tvContent = (TextView) v.findViewById(R.id.tv_content);
	}

	@Override
	public void onInitViewData() 
	{
		tvContent.setText( "运动方案" );
		tvDistanceGoal.setText( Math.abs( curWeight - goalWeight ) + "KG" );
		tvWeight.setText( curWeight + "KG" );
		tvContinueDay.setText( continueDay + "天" );
	}

	@Override
	public void onBindListener() 
	{
		btnShare.setOnClickListener( this );
		btnSport.setOnClickListener( this );
		btnNutrition.setOnClickListener( this );
	}

	@Override
	public void onClick(View v) 
	{
		switch ( v.getId() ) 
		{
		case R.id.btn_sport:
			isSport = true;
			break;
		case R.id.btn_nutrition:
			isSport = false;
			break;
		case R.id.btn_share:
			Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
			intent.setType("text/plain"); // 分享发送的数据类型
			String msg = "脂肪称，特别好，推荐给大家~ \n下载地址：http://www.baidu.com";
			intent.putExtra(Intent.EXTRA_TEXT, msg); // 分享的内容
			mContext.startActivity(Intent.createChooser(intent, "选择分享"));// 目标应用选择对话框的标题
		}
		
		changeContent();
	}
	
	private void changeContent()
	{
		if ( isSport )
		{
			tvContent.setText( "运动方案" );
			btnSport.setBackgroundResource(R.color.radio_color);
			btnSport.setTextColor(mContext.getResources().getColor( R.color.white ));
			btnNutrition.setBackgroundResource(R.drawable.bg_rectangle);
			btnNutrition.setTextColor(mContext.getResources().getColor( R.color.radio_color ));
		}
		else
		{
			tvContent.setText( "饮食与营养" );
			btnSport.setBackgroundResource(R.drawable.bg_rectangle);
			btnSport.setTextColor(mContext.getResources().getColor( R.color.radio_color ));
			btnNutrition.setBackgroundResource(R.color.radio_color);
			btnNutrition.setTextColor(mContext.getResources().getColor( R.color.white ));
		}
	}
	
	@Override
	public void reloadData() 
	{
		super.reloadData();
		if ( mContext == null )
		{
			return;
		}
		loadData();
		tvDistanceGoal.setText( Math.abs( curWeight - goalWeight ) + "KG" );
		tvWeight.setText( curWeight + "KG" );
		tvContinueDay.setText( continueDay + "天" );
	}
}
