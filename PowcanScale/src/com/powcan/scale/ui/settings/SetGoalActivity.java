package com.powcan.scale.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.powcan.scale.R;
import com.powcan.scale.bean.MeasureResult;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.db.MeasureResultDb;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.dialog.SelectDataDialog;
import com.powcan.scale.dialog.SelectDataDialog.ItemClickEvent;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.ui.profile.ProfileActivity;
import com.powcan.scale.util.SpUtil;
import com.powcan.scale.util.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 设置目标体重界面
 * @author Administrator
 *
 */
public class SetGoalActivity extends BaseActivity implements OnClickListener, ItemClickEvent
{
	private static final String TAG = SetGoalActivity.class.getSimpleName();
	
	private ImageView imgBack;
	private TextView tvCurWeight;
	private TextView tvSugCurWeight;
	private TextView tvGoalWeight;
	private TextView tvSugGoalWeight;
	private Button btnSet;
	private Button btnUser;
	
	private UserInfo curUser;
	private UserInfoDb dbUserInfo;
	private MeasureResult measureResult;
	private MeasureResultDb dbMeasureResult;
	
	private float curWeight;
	private int goalWeight;
	private List<Integer> list;
	
	private SelectDataDialog dialog;
	
	/**
	 * 创建界面方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_goal);
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void onInit() 
	{
		String account = SpUtil.getInstance(this).getAccount();
		dbUserInfo = new UserInfoDb(this);
		curUser = dbUserInfo.getUserInfo( account );
		dbMeasureResult = new MeasureResultDb( this );
		measureResult = dbMeasureResult.getLastMeasureResult( account );
		if ( measureResult == null )
		{
			measureResult = new MeasureResult();
		}
		
		String range = Utils.getWeightRange( Integer.valueOf( curUser.getHeight() ), curUser.getGender(), 0.2f );
		String []ranges = range.split( "-" );
		list = new ArrayList<Integer>();
		int start = Math.round( Float.valueOf( ranges[ 0 ] ) );
		int end = Math.round( Float.valueOf( ranges[ 1 ] ) );
		for( int i = start; i <= end; i += 1 )
		{
			list.add( i );
		}
		String strGoalWeight = curUser.getGoalWeight();
		if( TextUtils.isEmpty( strGoalWeight ) )
		{
			strGoalWeight = "0";
		}
		goalWeight = Integer.valueOf( curUser.getGoalWeight() );
		curWeight = measureResult.getWeight();
	}

	/**
	 * 查找子view
	 */
	@Override
	public void onFindViews() 
	{
		imgBack = (ImageView) findViewById(R.id.img_back);
		tvCurWeight = (TextView) findViewById(R.id.tv_curweight);
		tvSugCurWeight = (TextView) findViewById(R.id.tv_curweight_suggestion);
		tvGoalWeight = (TextView) findViewById(R.id.tv_goalweight);
		tvSugGoalWeight = (TextView) findViewById(R.id.tv_goalweight_suggestion);
		btnSet = (Button) findViewById(R.id.btn_set);
		btnUser = (Button) findViewById( R.id.btn_user );
	}

	/**
	 * 初始化view界面显示数据
	 */
	@Override
	public void onInitViewData() 
	{
		setData();
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		String account = SpUtil.getInstance(this).getAccount();
		curUser = dbUserInfo.getUserInfo( account );
		setData();
	}
	
	/**
	 * 将数据显示在界面上
	 */
	private void setData()
	{
		String range = Utils.getWeightRange( Integer.valueOf( curUser.getHeight() ), curUser.getGender(), 0.1f );
		String []ranges = range.split( "-" );
		tvCurWeight.setText( "当前体重为" + curWeight + "KG（标准）" );
		tvSugCurWeight.setText( "专家建议:您的标准体重区间为" + ranges[0] + "KG~" + ranges[1] + "KG" );
		tvGoalWeight.setText( "目标体重为" + goalWeight + "KG" );
		tvSugGoalWeight.setText( "距离目标体重还有" + Utils.formatTwoFractionDigits( Math.abs( curWeight - goalWeight ) ) + "KG" );
	}

	/**
	 * 绑定事件监听
	 */
	@Override
	public void onBindListener() 
	{
		imgBack.setOnClickListener( this );
		btnSet.setOnClickListener( this );
		btnUser.setOnClickListener( this );
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

			case R.id.btn_set:
				dialog = new SelectDataDialog( this, list, "选择目标体重", goalWeight / 5 - 1, this );
				dialog.show();
				break;
			case R.id.btn_user:
				Intent intent = new Intent( this, ProfileActivity.class );
				intent.putExtra( "from", "UserInfoDetail" );
				startActivity(intent);
				break;
		}
	}

	/**
	 * 体重选中回调方法
	 */
	@Override
	public void onItemClick(int which) 
	{
		Log.d( TAG, "acount : " + list.get(which) );
		
		goalWeight = list.get(which);
		setData();
		
		// 将目标体重更新到用户信息中
		dbUserInfo.updateGoalWeight( curUser.getAccount() , String.valueOf( goalWeight ) );
		curUser.setGoalWeight( String.valueOf( goalWeight ) );
		SpUtil.getInstance(SetGoalActivity.this ).saveCurrUser( curUser );
		
		dialog.dismiss();
		dialog = null;
	}
}
