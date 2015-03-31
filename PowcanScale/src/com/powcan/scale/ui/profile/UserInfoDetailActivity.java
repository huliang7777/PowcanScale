package com.powcan.scale.ui.profile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.powcan.scale.R;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.util.SpUtil;

/**
 * 动画参考：http://cyrilmottier.com/2014/05/20/custom-animations-with-fragments/
 * @author Administrator
 */
public class UserInfoDetailActivity extends BaseActivity implements OnClickListener
{
	private ImageView ivEdit;
	private ImageView ivBack;
	private TextView tvUsername;
	private TextView tvGender;
	private TextView tvAge;
	private TextView tvBirthday;
	private TextView tvHeight;
	
	private UserInfo curUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail);
	}

	@Override
	public void onInit() 
	{
		curUser = SpUtil.getInstance(this).getCurrUser();
	}

	@Override
	public void onFindViews() 
	{
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivEdit = (ImageView) findViewById(R.id.iv_edit);
		tvUsername = (TextView) findViewById(R.id.tv_username);
		tvGender = (TextView) findViewById(R.id.tv_gender);
		tvAge = (TextView) findViewById(R.id.tv_age);
		tvBirthday = (TextView) findViewById(R.id.tv_birthday);
		tvHeight = (TextView) findViewById(R.id.tv_height);
	}

	@Override
	public void onInitViewData() 
	{
		
		tvUsername.setText( curUser.getUsername() );
		tvGender.setText( curUser.getGender().equalsIgnoreCase("m") ? "男" : "女" );
		tvAge.setText( String.valueOf( calAge() ) + "岁" );
		tvBirthday.setText( curUser.getBirthday() );
		tvHeight.setText( curUser.getHeight() + "CM" );
	}

	@Override
	public void onBindListener() 
	{
		ivBack.setOnClickListener( this );
		ivEdit.setOnClickListener( this );
	}

	@Override
	public void onClick(View view) 
	{
		switch ( view.getId() ) {
		case R.id.iv_back:
			finish();
			break;

		case R.id.iv_edit:
			Intent intent = new Intent( this, ProfileActivity.class );
			startActivity(intent);
			break;
		}
	}

	@SuppressLint("SimpleDateFormat")
	private int calAge()
	{
		int age = 0;
		String birthday = curUser.getBirthday();
		if ( TextUtils.isEmpty( birthday ) || birthday.equalsIgnoreCase("null") )
		{
			return age;
		}
		
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		Date birth = null;
		try 
		{
			birth = sdf.parse( birthday );
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			return age;
		}
		
		SimpleDateFormat format_y = new SimpleDateFormat( "yyyy" );
		SimpleDateFormat format_M = new SimpleDateFormat( "MM" );
		

		String birth_year = format_y.format( birth );
		String this_year = format_y.format( now );

		String birth_month = format_M.format( birth );
		String this_month = format_M.format( now );

		// 初步，估算
		age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);

		// 如果未到出生月份，则age - 1
		if (this_month.compareTo(birth_month) < 0)
		{
			age -= 1;
		}
		if (age < 0)
		{
			age = 0;
		}
		return age;
	}
}
