package com.powcan.scale.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.powcan.scale.R;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.dialog.PerfectDataRemindDialog;
import com.powcan.scale.dialog.PerfectDataRemindDialog.PerfectDataEvent;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.util.SpUtil;
import com.powcan.scale.util.Utils;

/**
 * 用户信息详情界面
 * @author Administrator
 *
 */
public class UserInfoDetailActivity extends BaseActivity implements OnClickListener, PerfectDataEvent
{
	private TextView ivEdit;
	private ImageView ivBack;
	private TextView tvUsername;
	private TextView tvGender;
	private TextView tvAge;
	private TextView tvBirthday;
	private TextView tvHeight;
	private TextView tvPhone;
	private TextView tvQQ;
	private TextView tvEmail;

	private String account;
	private UserInfo curUser;
	private UserInfoDb dbUserInfo;
	private boolean isShowSetting;
	
	private PerfectDataRemindDialog dialog;
	private boolean isRemind;
	
	/**
	 * 创建界面方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail);
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void onInit() 
	{
		dbUserInfo = new UserInfoDb( this );
		account = getIntent().getStringExtra("account");
		loadData();
	}
	
	/**
	 * 加载数据
	 */
	private void loadData()
	{
		String acc = SpUtil.getInstance(this).getAccount();
		if ( TextUtils.isEmpty( account ) )
		{
			curUser = SpUtil.getInstance(this).getCurrUser();
		}
		else
		{
			curUser = dbUserInfo.getUserInfo( account );
		}
		
		isShowSetting = acc.equals( curUser.getAccount() );
		
		isRemind = SpUtil.getInstance( this ).getPerfectDataRemind( account );
	}

	/**
	 * 查找子view
	 */
	@Override
	public void onFindViews() 
	{
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivEdit = (TextView) findViewById(R.id.iv_edit);
		tvUsername = (TextView) findViewById(R.id.tv_username);
		tvGender = (TextView) findViewById(R.id.tv_gender);
		tvAge = (TextView) findViewById(R.id.tv_age);
		tvBirthday = (TextView) findViewById(R.id.tv_birthday);
		tvHeight = (TextView) findViewById(R.id.tv_height);
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		tvQQ = (TextView) findViewById(R.id.tv_qq);
		tvEmail = (TextView) findViewById(R.id.tv_email);
	}

	/**
	 * 初始化view界面显示数据
	 */
	@Override
	public void onInitViewData() 
	{
		String phone = curUser.getPhone();
		String qq = curUser.getQq();
		String email = curUser.getEmail();
		
		if ( isRemind && ( TextUtils.isEmpty( phone ) 
				|| TextUtils.isEmpty( qq ) || TextUtils.isEmpty( email ) )
				|| phone.equalsIgnoreCase( "NULL" )
				|| qq.equals( "0" )
				|| email.equalsIgnoreCase( "NULL" ))
		{
			dialog = new PerfectDataRemindDialog( this, this );
			dialog.show();
		}
	}

	/**
	 * 绑定事件监听
	 */
	@Override
	public void onBindListener() 
	{
		ivBack.setOnClickListener( this );
		ivEdit.setOnClickListener( this );
	}

	/**
	 * 点击事件处理方法
	 */
	@Override
	public void onClick(View view) 
	{
		switch ( view.getId() ) {
		case R.id.iv_back:
			finish();
			break;

		case R.id.iv_edit:
			Intent intent = new Intent( this, ProfileActivity.class );
			intent.putExtra( "from", "UserInfoDetail" );
			startActivity(intent);
			break;
		}
	}
	
	/**
	 * 用户信息完善回调接口方法
	 */
	@Override
	public void onButtonSelect( int which, boolean remind ) 
	{
		if ( which == 1 )
		{
			Intent intent = new Intent( this, ProfileActivity.class );
			intent.putExtra( "from", "UserInfoDetail" );
			startActivity(intent);
		}

		SpUtil.getInstance( this ).setPerfectDataRemind( account, !remind );
		dialog.dismiss();
		dialog = null;
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		loadData();
		
		tvUsername.setText( curUser.getUsername() );
		tvGender.setText( curUser.getGender().equalsIgnoreCase("m") ? "男" : "女" );
		tvAge.setText( String.valueOf( Utils.calAge( curUser.getBirthday() ) ) + "岁" );
		tvBirthday.setText( curUser.getBirthday() );
		tvHeight.setText( curUser.getHeight() + "CM" );
		String phone = curUser.getPhone();
		String qq = curUser.getQq();
		String email = curUser.getEmail();
		if ( TextUtils.isEmpty( phone ) || phone.equalsIgnoreCase( "NULL" ) )
		{
			tvPhone.setVisibility(View.GONE);
		}
		else
		{
			tvPhone.setVisibility(View.VISIBLE);
			tvPhone.setText( phone );
		}
		if ( TextUtils.isEmpty( qq ) || qq.equals( "0" ) )
		{
			tvQQ.setVisibility(View.GONE);
		}
		else
		{
			tvQQ.setVisibility(View.VISIBLE);
			tvQQ.setText( qq );
		}
		if ( TextUtils.isEmpty( email ) || email.equalsIgnoreCase( "NULL" ) )
		{
			tvEmail.setVisibility(View.GONE);
		}
		else
		{
			tvEmail.setVisibility(View.VISIBLE);
			tvEmail.setText( email );
		}
		
		if ( isShowSetting )
		{
			ivEdit.setVisibility( View.VISIBLE );
		}
		else
		{
			ivEdit.setVisibility( View.GONE );
		}
	}
}
