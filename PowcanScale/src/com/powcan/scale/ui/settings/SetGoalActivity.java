package com.powcan.scale.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.powcan.scale.R;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.dialog.SelectDataDialog;
import com.powcan.scale.dialog.SelectDataDialog.ItemClickEvent;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.util.SpUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SetGoalActivity extends BaseActivity implements OnClickListener, ItemClickEvent
{
	private static final String TAG = SetGoalActivity.class.getSimpleName();
	
	private ImageView imgBack;
	private TextView tvCurWeight;
	private TextView tvSugCurWeight;
	private TextView tvGoalWeight;
	private TextView tvSugGoalWeight;
	private Button btnSet;
	
	private UserInfo curUser;
	private UserInfoDb dbUserInfo;
	private int curWeight = 0;
	private int goalWeight = 0;
	private List<Integer> list;
	
	private SelectDataDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_goal);
	}

	@Override
	public void onInit() 
	{
		String account = SpUtil.getInstance(this).getAccount();
		dbUserInfo = new UserInfoDb(this);
		curUser = dbUserInfo.getUserInfo( account );
		
		list = new ArrayList<Integer>();
		for( int i = 5; i <= 500; i += 5 )
		{
			list.add( i );
		}
		String strGoalWeight = curUser.getGoalWeight();
		if( TextUtils.isEmpty( strGoalWeight ) )
		{
			strGoalWeight = "0";
		}
		goalWeight = Integer.valueOf( curUser.getGoalWeight() );
	}

	@Override
	public void onFindViews() 
	{
		imgBack = (ImageView) findViewById(R.id.img_back);
		tvCurWeight = (TextView) findViewById(R.id.tv_curweight);
		tvSugCurWeight = (TextView) findViewById(R.id.tv_curweight_suggestion);
		tvGoalWeight = (TextView) findViewById(R.id.tv_goalweight);
		tvSugGoalWeight = (TextView) findViewById(R.id.tv_goalweight_suggestion);
		btnSet = (Button) findViewById(R.id.btn_set);
	}

	@Override
	public void onInitViewData() 
	{
		setData();
	}
	
	private void setData()
	{
		tvCurWeight.setText( "当前体重为" + curWeight + "KG（标准）" );
		tvSugCurWeight.setText( "专家建议:您的标准体重区间为65KG~80KG" );
		tvGoalWeight.setText( "目标体重为" + goalWeight + "KG" );
		tvSugGoalWeight.setText( "距离目标体重还有" + ( curWeight - goalWeight ) + "KG" );
	}

	@Override
	public void onBindListener() 
	{
		imgBack.setOnClickListener( this );
		btnSet.setOnClickListener( this );
	}

	@Override
	public void onClick(View v) 
	{
		switch ( v.getId() ) 
		{
			case R.id.img_back:
				finish();
				break;

			case R.id.btn_set:
				dialog = new SelectDataDialog( this, list, "选择目标体重", goalWeight / 5 - 1, this );
				dialog.show();
				break;
		}
	}

	@Override
	public void onItemClick(int which) 
	{
		Log.d( TAG, "acount : " + list.get(which) );
		
		goalWeight = list.get(which);
		setData();
		
		dbUserInfo.updateGoalWeight( curUser.getAccount() , String.valueOf( goalWeight ) );
		
		dialog.dismiss();
		dialog = null;
	}
}
