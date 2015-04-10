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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.powcan.scale.MainActivity;
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
	private EditText etUsername;
	private EditText etGender;
	private EditText etBirthday;
	private EditText etHeight;
	private TextView tvSelect;
	
	private String gender;
	private String birthday;
	private String height;
	
	private LoadingDialog loadingDialog;
	private SelectGenderDialog genderDialog;
	private SelectHeightDialog heightDialog;
	private UserInfoDb dbUserInfo;
	private UserInfo userInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
	}

	@Override
	public void onInit() 
	{
		userInfo = SpUtil.getInstance( this ).getCurrUser();
		dbUserInfo = new UserInfoDb( this ); 
		
		gender = userInfo.getGender();
		birthday = userInfo.getBirthday();
		height = userInfo.getHeight();
	}

	@Override
	public void onFindViews() 
	{
		btnSave = (Button) findViewById(R.id.btn_save);
		etUsername = (EditText) findViewById(R.id.et_username);
		etGender = (EditText) findViewById(R.id.et_gender);
		etBirthday = (EditText) findViewById(R.id.et_birthday);
		etHeight = (EditText) findViewById(R.id.et_height);
		tvSelect = (TextView) findViewById(R.id.tv_select);
	}

	@Override
	public void onInitViewData() 
	{
		etUsername.setText( userInfo.getUsername() );
		etGender.setText( gender );
		etBirthday.setText( birthday );
		if( !TextUtils.isEmpty( height ) && !height.equals( "0" ) )
		{
			etHeight.setText( height + "CM" );
		}
		
	}

	@Override
	public void onBindListener() {
		btnSave.setOnClickListener( this );
		etGender.setOnClickListener( this );
		tvSelect.setOnClickListener( this );
		etHeight.setOnClickListener( this );
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
		case R.id.tv_select:
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
		}
	}

	private void reqSaveUserInfo() 
	{
		String username = etUsername.getText().toString();
		String birthday = etBirthday.getText().toString();
		
		Pattern birthdayPattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))$", Pattern.CASE_INSENSITIVE );
		Matcher birthdayMatcher = birthdayPattern.matcher( birthday );
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
		
		userInfo.setUsername( username );
		userInfo.setGender( gender );
		userInfo.setBirthday( birthday );
		userInfo.setHeight( height );
		
		
		loadingDialog = new LoadingDialog( this, "保存中..." );
		loadingDialog.show();
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... arg0) 
			{
				UTURequest utu = new UTURequest();
				utu.account = userInfo.getAccount();
				utu.nickname = userInfo.getUsername();
				utu.gender = userInfo.getGender();
				utu.birthday = userInfo.getBirthday();
				utu.height = userInfo.getHeight();
//				utu.phone = "";
//				utu.qq = "";
//				utu.email = "";
				
				BaseResponse regResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(utu, BaseResponse.class);
				if (regResponse != null && ( regResponse.RES >= 401 || regResponse.RES <= 404 ) ) 
				{
					return true;
				}
				
				return false;
			}
			
			@Override
			protected void onPostExecute(Boolean result)
			{
				super.onPostExecute(result);
				if ( result ) 
				{
					dbUserInfo.updateUserInfo( userInfo );
					SpUtil.getInstance( ProfileActivity.this ).saveCurrUser(userInfo);
					
					showToast("资料保存成功");
					
					gotoMain();
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
		etGender.setText( gender);
		genderDialog.dismiss();
	}

	@Override
	public void onItemClick(int iHeight) 
	{
		height = String.valueOf( iHeight );
		etHeight.setText( height + "CM" );
		heightDialog.dismiss();
	}
}
