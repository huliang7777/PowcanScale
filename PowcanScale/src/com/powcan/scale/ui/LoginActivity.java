package com.powcan.scale.ui;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.powcan.scale.MainActivity;
import com.powcan.scale.PowcanScaleApplication;
import com.powcan.scale.R;
import com.powcan.scale.bean.MeasureResult;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.bean.http.LGNRequest;
import com.powcan.scale.bean.http.LGNResponse;
import com.powcan.scale.bean.http.RECRequest;
import com.powcan.scale.db.MeasureResultDb;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.dialog.LoadingDialog;
import com.powcan.scale.net.NetRequest;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.ui.profile.ProfileActivity;
import com.powcan.scale.util.Md5Utils;
import com.powcan.scale.util.SpUtil;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private Button btnCommit;
	private EditText etUsername;
	private EditText etPassword;
	private TextView tvToRegister;
	private TextView tvForgetPassword;

	private LoadingDialog loadingDialog;
	private UserInfoDb dbUserInfo;

	private String account;
	private String from;
	private MeasureResultDb dbMeasureResult;
	private ArrayList<MeasureResult> measureResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public void onInit() {
		// ActionBar actionBar = getSupportActionBar();
		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		// actionBar.setDisplayShowTitleEnabled(true);
		// actionBar.setTitle("登录");

		dbUserInfo = new UserInfoDb(this);
		dbMeasureResult = new MeasureResultDb( this );
	}

	@Override
	public void onFindViews() {
		etUsername = (EditText) findViewById(R.id.et_username);
		etPassword = (EditText) findViewById(R.id.et_password);
		btnCommit = (Button) findViewById(R.id.btn_commit);
		tvToRegister = (TextView) findViewById(R.id.tv_to_register);
		tvForgetPassword = (TextView) findViewById(R.id.tv_forgetPassword);
	}

	@Override
	public void onInitViewData() {
		account = getIntent().getStringExtra("account");
		from = getIntent().getStringExtra("from");
		if (!TextUtils.isEmpty(account) && !account.equals("0")) {
			etUsername.setText(account);
		}
		
		tvToRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		tvToRegister.getPaint().setAntiAlias( true ); // 抗锯齿
	}

	@Override
	public void onBindListener() {
		btnCommit.setOnClickListener(this);
		tvToRegister.setOnClickListener(this);
		tvForgetPassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit:
			// gotoMain();
			if (checkUsername() && checkPassword()) {
				requestLogin();
			}
			break;
		case R.id.tv_to_register:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_forgetPassword:
			intent = new Intent(this, FindPasswordActivity.class);
			startActivity(intent);
			break;
		}
	}

	private boolean checkUsername() {
		String username = getUsername();

		boolean check = false;
		if (TextUtils.isEmpty(username)) {
			showToast("请输入用户名");
		} else if (username.contains(" ")) {
			showToast("用户名不能包含空格");
			// } else if (username.length() != 11) {
			// showToast("手机号码长度不对");
		} else {
			check = true;
		}

		return check;
	}

	public String getUsername() {
		return etUsername.getText().toString();
	}

	private boolean checkPassword() {
		String password = getPassword();

		boolean check = false;
		if (TextUtils.isEmpty(password)) {
			showToast("请输入密码");
		} else if (password.contains(" ")) {
			showToast("密码不能包含空格");
		} else if (password.length() < 6 || password.length() > 30) {
			showToast("密码由6~30位字母，数字和下划线组成");
		} else {
			check = true;
		}

		return check;
	}

	public String getPassword() {
		return etPassword.getText().toString();
	}

	private void requestLogin() {
		final String username = getUsername();
		final String password = getPassword();

		loadingDialog = new LoadingDialog(this, "登录中...");
		loadingDialog.show();
		new AsyncTask<Void, Void, UserInfo>() {

			@Override
			protected UserInfo doInBackground(Void... arg0) {
				UserInfo userInfo = null;
				LGNRequest request = new LGNRequest();
				request.number = username;
				request.pswd = Md5Utils.encryptMD5(password);

				LGNResponse response = NetRequest.getInstance(getActivity())
						.send(request, LGNResponse.class);
				if (response != null && response.RES == 301) {
					userInfo = new UserInfo();
					userInfo.setAccount(String.valueOf(response.ACT));
					userInfo.setPassword(Md5Utils.encryptMD5(password));
					userInfo.setUsername(response.NKN);
					userInfo.setGender(response.GDR);
					userInfo.setBirthday(response.AGE);
					userInfo.setHeight(String.valueOf(response.HET));
					userInfo.setPhone(response.PHN);
					userInfo.setQq(response.QQN);
					userInfo.setEmail(response.EML);
					userInfo.setGoalWeight("0");
				}
				return userInfo;
			}

			@Override
			protected void onPostExecute(UserInfo userInfo) {
				super.onPostExecute(userInfo);
				if (userInfo != null) {
					SpUtil.getInstance(LoginActivity.this).saveCurrUser(
							userInfo);
					dbUserInfo.insertOrUpdateUserInfo(userInfo);
					if (userInfo.getBirthday().equals("0000-00-00")
							|| TextUtils.isEmpty(userInfo.getGender())
							|| userInfo.getHeight().equals("0")) {
						gotoProfile();
					} else {
						gotoMain();
					}
				} else {
					String msg = "登录失败,请检查您的账号或者密码是否正确！";
					showToastShort(msg);
				}
				loadingDialog.dismiss();
			}

		}.execute();

	}

	protected void gotoMain() 
	{
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);

		finish();
	}

	protected void gotoProfile() 
	{
		measureResults = dbMeasureResult.getMeasureResults( getUsername(), "0" );
		int size = measureResults.size();
		for ( int i=0;i<size;i++ )
		{
			final MeasureResult result = measureResults.get( i );
			
			new Timer().schedule( new TimerTask() {
				
				@Override
				public void run() 
				{
					String account = mSpUtil.getAccount();
					
					RECRequest request = new RECRequest();
					request.account = account;
					request.weight = "" + result.getWeight();
					request.fat =  "" + result.getBodyFatRate();
					request.water = "" + result.getWaterContent();
					request.muscle = "0.0";
					request.bone = "0.0";
					request.bmr = "0.0";
					request.sfat = "0.0";
					request.infat = "0.0";
					request.bodyage = "0.0";
					request.amr = "0.0";
					
					LGNResponse response = NetRequest.getInstance(getActivity()).send(request, LGNResponse.class);
					if (response != null && response.RES == 901 )
					{
						Log.d( "HomeFragment", "数据上传成功" );
						dbMeasureResult.updateMeasureResult( result.getId(), 1 );
					}
				}
			}, 100 * i );
		}
		
		Intent intent = new Intent(this, ProfileActivity.class);
		startActivity(intent);

		finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if ( !TextUtils.isEmpty( from ) && from.equals("exit") ) 
			{
				PowcanScaleApplication.getInstance().exit();
			}
		}

		return super.onKeyDown(keyCode, event);
	}
}
