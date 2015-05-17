package com.powcan.scale.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.powcan.scale.R;
import com.powcan.scale.bean.http.BaseResponse;
import com.powcan.scale.bean.http.UPPRequest;
import com.powcan.scale.dialog.LoadingDialog;
import com.powcan.scale.net.NetRequest;
import com.powcan.scale.ui.base.BaseActivity;
import com.powcan.scale.util.Md5Utils;

/**
 * 设置密码
 * @author Administrator
 *
 */
public class SetPasswordActivity extends BaseActivity implements OnClickListener {

	private Button btnCommit;
//	private EditText etAccount;
	private EditText etPassword;
	private EditText etRePassword;

	private LoadingDialog loadingDialog;
	private String account;

	/**
	 * 创建界面方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setpwd);
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void onInit() 
	{
		account = getIntent().getStringExtra( "account" );
	}

	/**
	 * 查找子view
	 */
	@Override
	public void onFindViews() {
//		etAccount = (EditText) findViewById(R.id.et_account);
		etPassword = (EditText) findViewById(R.id.et_password);
		etRePassword = (EditText) findViewById(R.id.et_repassword);
		btnCommit = (Button) findViewById(R.id.btn_commit);
	}

	@Override
	public void onInitViewData() {
	}

	/**
	 * 绑定事件监听
	 */
	@Override
	public void onBindListener() {
		btnCommit.setOnClickListener(this);
	}

	/**
	 * 点击事件处理方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit:
			if ( check() ) {
				requestFindPwd();
			}
			break;
		}
	}

	/**
	 * 检查用户输入数据合法性
	 * @return
	 */
	private boolean check() 
	{
//		String account = etAccount.getText().toString();
		String password = etPassword.getText().toString();
		String repassword = etRePassword.getText().toString();
		
		Pattern passwordPattern = Pattern.compile("^[0-9a-zA-z]{6,30}$", Pattern.CASE_INSENSITIVE );
		Matcher passwordMatcher = passwordPattern.matcher( password );
		Matcher repasswordMatcher = passwordPattern.matcher( repassword );
		
		boolean check = false;
//		if ( TextUtils.isEmpty( account ) )
//		{
//			showToast("用户账号不可以为空");
//		}
//		else 
		if ( !passwordMatcher.matches() ) 
		{
			showToast("密码必须由6~30位的字符组成！");
		} 
		else if ( !repasswordMatcher.matches() ) 
		{
			showToast("确认密码必须由6~30位的字符组成！");
		}
		else if( !repassword.equals( password ) )
		{
			showToast("两次密码输入不一致！");
		}
		else 
		{
			check = true;
		}
		
		return check;
	}

	/**
	 * 请求后台进行密码设置
	 */
	private void requestFindPwd() 
	{
//		final String account = etAccount.getText().toString();
		final String password = Md5Utils.encryptMD5( etPassword.getText().toString() );
		
		loadingDialog = new LoadingDialog(this, "设置中...");
		loadingDialog.show();
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... v) 
			{
				UPPRequest request = new UPPRequest();
				request.account = account;
				request.password = password;

				BaseResponse response = NetRequest.getInstance(getActivity())
						.send(request, BaseResponse.class);
				if (response != null && response.RES == 701) 
				{
					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute( Boolean isSuc ) {
				super.onPostExecute(isSuc);
				String msg = "";
				if ( isSuc ) 
				{
					msg = "密码设置成功,请登录！";
					finish();
				} 
				else 
				{
					msg = "密码设置失败,请重试！";
				}
				showToastShort(msg);
				loadingDialog.dismiss();
			}

		}.execute();

	}
}
