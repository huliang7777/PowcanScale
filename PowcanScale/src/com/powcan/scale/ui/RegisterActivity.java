package com.powcan.scale.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.powcan.scale.MainActivity;
import com.powcan.scale.R;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.bean.http.BaseResponse;
import com.powcan.scale.bean.http.GATRequest;
import com.powcan.scale.bean.http.GATResponse;
import com.powcan.scale.bean.http.REGRequest;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.dialog.LoadingDialog;
import com.powcan.scale.dialog.SelectAccountDialog;
import com.powcan.scale.dialog.SelectAccountDialog.ItemClickEvent;
import com.powcan.scale.net.NetRequest;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.ui.profile.ProfileActivity;
import com.powcan.scale.util.Md5Utils;
import com.powcan.scale.util.SpUtil;
import com.powcan.scale.util.Utils;

public class RegisterActivity extends BaseActivity implements OnClickListener, ItemClickEvent {

	private static final String TAG = RegisterActivity.class.getSimpleName();
	
	private TextView btnToLogin;
	private Button btnCommit;
	private TextView tvAccount;
	private EditText etPassword;
	private EditText etPhone;
	private EditText etQQ;
	private EditText etEmail;
	
	private List<Integer> listAccount;
	private SelectAccountDialog dialog;
	private LoadingDialog loadingDialog;
	private UserInfoDb dbUserInfo;
	private UserInfo userInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}
	
	@Override
	public void onInit() {
		dbUserInfo = new UserInfoDb( this ); 
	}

	@Override
	public void onFindViews() {
		tvAccount = (TextView) findViewById(R.id.tv_account);
		etPassword = (EditText) findViewById(R.id.et_password);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etQQ = (EditText) findViewById(R.id.et_qq);
		etEmail = (EditText) findViewById(R.id.et_email);
		btnCommit = (Button) findViewById(R.id.btn_register);
		btnToLogin = (TextView) findViewById(R.id.btn_to_login);
	}

	@Override
	public void onInitViewData() 
	{
		this.listAccount = new ArrayList<Integer>();
	}

	@Override
	public void onBindListener() {
		btnCommit.setOnClickListener(this);
		tvAccount.setOnClickListener(this);
		btnToLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			Log.d( TAG, "btn_commit" );
//			if (checkUsername() && checkPassword()) {
//				requestLogin();
				
//			}
			reqRegister();
			break;
		case R.id.tv_account:
			Log.d( TAG, "tv_account" );
			reqRegisterAccount();
			break;
		case R.id.btn_to_login:
			Log.d( TAG, "btn_to_login" );
			Intent intent = new Intent( this, LoginActivity.class );
			startActivity(intent);
			finish();
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
//		} else if (username.length() != 11) {
//			showToast("手机号码长度不对");
		} else {
			check = true;
		}
		
		return check;
	}
	
	public String getUsername() {
		return tvAccount.getText().toString();
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

	protected void gotoProfile() 
	{
		Intent intent = new Intent(this, ProfileActivity.class);
		startActivity(intent);
		
		finish();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onItemClick(int which) 
	{
		Log.d(TAG, "acount : " + listAccount.get(which));
		tvAccount.setText(String.valueOf(listAccount.get(which)));
		dialog.dismiss();
		dialog = null;
	}

	private void reqRegisterAccount()
	{
		if ( listAccount != null && listAccount.size() > 0 )
		{
			dialog = new SelectAccountDialog( RegisterActivity.this, listAccount, RegisterActivity.this );
			dialog.show();
			return;
		}
		loadingDialog = new LoadingDialog( RegisterActivity.this, "加载中..." );
		loadingDialog.show();
		new AsyncTask<Void, Void, List<Integer>>() {

			@Override
			protected List<Integer> doInBackground(Void... arg0) 
			{
				GATRequest gat = new GATRequest(6);
				GATResponse response = (GATResponse) NetRequest.getInstance(getActivity()).send(gat, GATResponse.class);
				if (response != null) {
					listAccount = response.BKH;
					if ( listAccount != null && listAccount.size() > 0) 
					{
						Log.d(TAG, "" + listAccount.get(1));
					}
					return response.BKH;
				}
				
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				List<Integer> list = new ArrayList<Integer>();
//				list.add(2002910);
//				list.add(2007633);
//				list.add(2003910);
//				list.add(2002227);
//				list.add(2001137);
//				list.add(2001260);
//				return list;
				return null;
			}
			
			@Override
			protected void onPostExecute(List<Integer> result)
			{
				super.onPostExecute(result);
				if ( result != null && result.size() > 0) 
				{
					listAccount = result;
					Log.d(TAG, "" + listAccount.get(1));
					dialog = new SelectAccountDialog( RegisterActivity.this, listAccount, RegisterActivity.this );
					dialog.show();
				}
				else
				{
					String msg = "账号加载失败,请重试！";
					showToastShort( msg );
				}
				loadingDialog.dismiss();
			}
			
		}.execute();
	}
	
	private void reqRegister()
	{
		userInfo = new UserInfo();
		userInfo.setAccount( tvAccount.getText().toString() );
		userInfo.setPassword( Md5Utils.encryptMD5( etPassword.getText().toString() ) );
		userInfo.setUsername( "" );
		userInfo.setImei( Utils.getDeviceId(this) );
		userInfo.setBirthday("");
		userInfo.setEmail( etEmail.getText().toString() );
		userInfo.setGender("");
		userInfo.setHeight("");
		userInfo.setPhone( etPhone.getText().toString() );
		userInfo.setQq( etQQ.getText().toString() );
		
		loadingDialog = new LoadingDialog( RegisterActivity.this, "处理中..." );
		loadingDialog.show();
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... arg0) 
			{
				REGRequest reg = new REGRequest();
				reg.account = userInfo.getAccount();
				reg.pswd = userInfo.getPassword();
				reg.imei = userInfo.getImei();
				reg.gender = userInfo.getGender();
				reg.birthday = userInfo.getBirthday();
				reg.height = userInfo.getHeight();
				reg.phone = userInfo.getPhone();
				reg.qq = userInfo.getQq();
				reg.email = userInfo.getEmail();
				
				BaseResponse regResponse = (BaseResponse) NetRequest.getInstance(getActivity()).send(reg, BaseResponse.class);
				if ( regResponse != null && regResponse.RES == 201 ) 
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
					dbUserInfo.insertUserInfo(userInfo);
					SpUtil.getInstance( RegisterActivity.this ).saveCurrUser(userInfo);
					gotoProfile();
				}
				else
				{
					String msg = "注册失败,请重试！";
					showToastShort( msg );
				}
				loadingDialog.dismiss();
			}
			
		}.execute();
	}
}
