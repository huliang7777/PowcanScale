package com.powcan.scale.ui.profile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.powcan.scale.MainActivity;
import com.powcan.scale.PowcanScaleApplication;
import com.powcan.scale.R;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.bean.http.BaseResponse;
import com.powcan.scale.bean.http.UTURequest;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.dialog.LoadingDialog;
import com.powcan.scale.dialog.SelectGenderDialog;
import com.powcan.scale.dialog.SelectHeightDialog;
import com.powcan.scale.dialog.SelectGenderDialog.GenderSelectEvent;
import com.powcan.scale.dialog.SelectHeightDialog.ItemClickEvent;
import com.powcan.scale.net.NetRequest;
import com.powcan.scale.ui.RegisterActivity;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.util.SpUtil;
import com.powcan.scale.util.Utils;

/**
 * 动画参考：http://cyrilmottier.com/2014/05/20/custom-animations-with-fragments/
 * @author Administrator
 */
public class ProfileActivity extends BaseActivity implements OnClickListener, GenderSelectEvent, ItemClickEvent
{
	private static final String TAG = RegisterActivity.class.getSimpleName();

	private Button btnSave;
	private ImageView imgBack;
	private EditText etUsername;
	private EditText etGender;
	private EditText etBirthday;
	private EditText etHeight;
	private EditText etPhone;
	private EditText etQQ;
	private EditText etEmail;
	private ImageView imgSelect;
	private View llPhone;
	private View llQq;
	private View llEmail;
	
	private String gender;
	private String birthday;
	private String height;
	private String phone;
	private String qq;
	private String email;
	
	private LoadingDialog loadingDialog;
	private SelectGenderDialog genderDialog;
	private SelectHeightDialog heightDialog;
	private UserInfoDb dbUserInfo;
	private UserInfo userInfo;
	
	private String from;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
	}

	@Override
	public void onInit() 
	{
		from = getIntent().getStringExtra( "from" );
		userInfo = SpUtil.getInstance( this ).getCurrUser();
		dbUserInfo = new UserInfoDb( this ); 
		
		gender = userInfo.getGender();
		birthday = userInfo.getBirthday();
		height = userInfo.getHeight();
		phone = userInfo.getPhone();
		qq = userInfo.getQq();
		email = userInfo.getEmail();
		
		if ( TextUtils.isEmpty( phone ) || phone.equalsIgnoreCase( "NULL" ) )
		{
			phone = "";
		}
		if ( TextUtils.isEmpty( qq ) || qq.equals( "0" ) )
		{
			qq = "";
		}
		if ( TextUtils.isEmpty( email ) || email.equalsIgnoreCase( "NULL" ) )
		{
			email = "";
		}
		
		if ( TextUtils.isEmpty( from ) )
		{
			from = "";
		}
	}

	@Override
	public void onFindViews() 
	{
		btnSave = (Button) findViewById(R.id.btn_save);
		etUsername = (EditText) findViewById(R.id.et_username);
		etGender = (EditText) findViewById(R.id.et_gender);
		etBirthday = (EditText) findViewById(R.id.et_birthday);
		etHeight = (EditText) findViewById(R.id.et_height);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etQQ = (EditText) findViewById(R.id.et_qq);
		etEmail = (EditText) findViewById(R.id.et_email);
		llPhone = findViewById(R.id.ll_phone);
		llQq = findViewById(R.id.ll_qq);
		llEmail = findViewById(R.id.ll_email);
		imgSelect = (ImageView) findViewById(R.id.img_select);
		imgBack = (ImageView) findViewById(R.id.img_back);
	}

	@Override
	public void onInitViewData() 
	{
		etUsername.setText( userInfo.getUsername() );
		String gstr = "";
		if ( gender.equalsIgnoreCase("M") )
		{
			gstr = "男";
		}
		else if ( gender.equalsIgnoreCase("F") )
		{
			gstr = "女";
		}
		etGender.setText( gstr );
		etBirthday.setText( birthday );
		if( !TextUtils.isEmpty( height ) && !height.equals( "0" ) )
		{
			etHeight.setText( height + "CM" );
		}
		
		if ( from.equals( "UserInfoDetail" ) )
		{
			llPhone.setVisibility( View.VISIBLE );
			llQq.setVisibility( View.VISIBLE );
			llEmail.setVisibility( View.VISIBLE );
			etPhone.setText( phone );
			etQQ.setText( qq );
			etEmail.setText( email );
		}
		else
		{
			llPhone.setVisibility( View.GONE );
			llQq.setVisibility( View.GONE );
			llEmail.setVisibility( View.GONE );
		}
	}

	@Override
	public void onBindListener() {
		btnSave.setOnClickListener( this );
		etGender.setOnClickListener( this );
		imgSelect.setOnClickListener( this );
		etHeight.setOnClickListener( this );
		imgBack.setOnClickListener( this );
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
			Log.d( TAG, "btn_commit" );
			reqSaveUserInfo();			
			break;
		case R.id.et_gender:
			genderDialog = new SelectGenderDialog( this, gender, this );
			genderDialog.show();
			break;
		case R.id.img_select:
			birthday = etBirthday.getText().toString();
			if ( TextUtils.isEmpty(birthday) || birthday.equals("0000-00-00") )
			{
				birthday = Utils.getCurDate();
			}
			String []datas = birthday.split("-");
			int year = Integer.valueOf( datas[0] );
			int month = Integer.valueOf( datas[1] );
			int day = Integer.valueOf( datas[2] );
			
			new DatePickerDialog( this, new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker picker, int year, int month, int day) 
				{
					birthday = year + "-" + ( month + 1 ) + "-" + day;
					etBirthday.setText( birthday );
				}
			}, year, month - 1, day).show();
			break;
		case R.id.et_height:
			heightDialog = new SelectHeightDialog( this, height, gender, this );
			heightDialog.show();
			break;
		case R.id.img_back:
			if ( !from.equals( "UserInfoDetail" ) )
			{
				PowcanScaleApplication.getInstance().exit();
			}
			else
			{
				finish();
			}
			break;
		}
	}

	private void reqSaveUserInfo() 
	{
		String username = etUsername.getText().toString();
		String birthday = etBirthday.getText().toString();
		String tempPhone = etPhone.getText().toString();
		String tempQq = etQQ.getText().toString();
		String tempEmail = etEmail.getText().toString();
		
		Pattern birthdayPattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))$", Pattern.CASE_INSENSITIVE );
		Matcher birthdayMatcher = birthdayPattern.matcher( birthday );
		
		Pattern phonePattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", Pattern.CASE_INSENSITIVE );
		Matcher phoneMatcher = phonePattern.matcher( tempPhone );
		
		Pattern qqPattern = Pattern.compile("^[0-9]{6,12}$", Pattern.CASE_INSENSITIVE );
		Matcher qqMatcher = qqPattern.matcher( tempQq );
		
		Pattern emailPattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", Pattern.CASE_INSENSITIVE );
		Matcher emailMatcher = emailPattern.matcher( tempEmail );
		
		if (TextUtils.isEmpty(username) || username.length() < 2 || username.length() >= 30)
		{
			showToast("用户必须由2~30位的字符组成！");
			return;
		}
		else if ( TextUtils.isEmpty( gender ) )
		{
			showToast("请选择性别！");
			return;
		}
		else if ( TextUtils.isEmpty( birthday ) )
		{
			showToast("请输入出生年月日！");
			return;
		}
		else if ( !birthdayMatcher.matches() )
		{
			showToast("出生年月日输入有误！");
			return;
		}
		else if ( TextUtils.isEmpty( height ) || height.equals( "0" ) )
		{
			showToast("请选择您的身高！");
			return;
		}
		if ( from.equals( "UserInfoDetail" ) )
		{
			if ( !TextUtils.isEmpty( phone ) && !phoneMatcher.matches() ) 
			{
				showToast("手机号码输入有误！");
				return;
			} 
			else if ( !TextUtils.isEmpty( qq ) && !qqMatcher.matches() ) 
			{
				showToast("qq输入有误！");
				return;
			} 
			else if ( !TextUtils.isEmpty( email ) && !emailMatcher.matches() ) 
			{
				showToast("邮箱输入有误！");
				return;
			}
		}
		
		
		userInfo.setUsername( username );
		userInfo.setGender( gender );
		userInfo.setBirthday( birthday );
		userInfo.setHeight( height );
		if ( from.equals( "UserInfoDetail" ) )
		{
			userInfo.setPhone( tempPhone );
			userInfo.setQq( tempQq );
			userInfo.setEmail( tempEmail );
		}
		
		loadingDialog = new LoadingDialog( this, "保存中..." );
		loadingDialog.show();
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected Integer doInBackground(Void... arg0) 
			{
				UTURequest utu = new UTURequest();
				utu.account = userInfo.getAccount();
				utu.nickname = userInfo.getUsername();
				utu.gender = userInfo.getGender();
				utu.birthday = userInfo.getBirthday();
				utu.height = userInfo.getHeight();
				if ( from.equals( "UserInfoDetail" ) )
				{
					utu.phone = userInfo.getPhone();
					utu.qq = userInfo.getQq();
					utu.email = userInfo.getEmail();
				}
				
				BaseResponse regResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(utu, BaseResponse.class);
				if (regResponse != null ) 
				{
					return regResponse.RES;
				}
				
				return 0;
			}
			
			@Override
			protected void onPostExecute(Integer result)
			{
				super.onPostExecute(result);
				if ( result == 401 ) 
				{
					dbUserInfo.updateUserInfo( userInfo );
					SpUtil.getInstance( ProfileActivity.this ).saveCurrUser(userInfo);
					
					showToast("资料保存成功");
					if ( !from.equals( "UserInfoDetail" ) )
					{
						gotoMain();
					}
				}
				else if ( result == 402 )
				{
					userInfo.setPhone( phone );
					dbUserInfo.updateUserInfo( userInfo );
					SpUtil.getInstance( ProfileActivity.this ).saveCurrUser(userInfo);
					showToast("更新资料部分成功，手机号码重复");
				}
				else if ( result == 403 )
				{
					userInfo.setPhone( qq );
					dbUserInfo.updateUserInfo( userInfo );
					SpUtil.getInstance( ProfileActivity.this ).saveCurrUser(userInfo);
					showToast("更新资料部分成功，QQ重复");
				}
				else if ( result == 404 )
				{
					userInfo.setPhone( email );
					dbUserInfo.updateUserInfo( userInfo );
					SpUtil.getInstance( ProfileActivity.this ).saveCurrUser(userInfo);
					showToast("更新资料部分成功，邮箱重复");
				}
				else
				{
					String msg = "资料保存失败,请重试！";
					showToastShort( msg );
				}
				loadingDialog.dismiss();
			}
			
		}.execute();
	}

	protected void gotoMain() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		
		finish();
	}

	@Override
	public void onGenderSelect(int which) 
	{
		if ( which == 1 )
		{
			gender = "M";
		}
		else if ( which == 2 )
		{
			gender = "F";
		}
		etGender.setText( gender.equals("M") ? "男" : "女" );
		genderDialog.dismiss();
	}

	@Override
	public void onItemClick(int iHeight) 
	{
		height = String.valueOf( iHeight );
		etHeight.setText( height + "CM" );
		heightDialog.dismiss();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if ( !from.equals( "UserInfoDetail" ) )
			{
				PowcanScaleApplication.getInstance().exit();
			}
		}

		return super.onKeyDown(keyCode, event);
	}
}
