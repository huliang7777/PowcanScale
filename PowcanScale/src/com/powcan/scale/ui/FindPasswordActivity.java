package com.powcan.scale.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.powcan.scale.R;
import com.powcan.scale.bean.http.BaseResponse;
import com.powcan.scale.bean.http.FBPRequest;
import com.powcan.scale.dialog.LoadingDialog;
import com.powcan.scale.dialog.SelectStrDataDialog;
import com.powcan.scale.dialog.SelectStrDataDialog.ItemClickEvent;
import com.powcan.scale.net.NetRequest;
import com.powcan.scale.ui.base.BaseActivity;

/**
 * 找回密码界面
 * @author Administrator
 *
 */
public class FindPasswordActivity extends BaseActivity implements OnClickListener, ItemClickEvent {

	private ImageView ivBack;
	private Button btnCommit;
	private EditText etAccount;
	private EditText etNumber;
	private TextView tvSelect;
	private List<String> list;

	private LoadingDialog loadingDialog;
	private SelectStrDataDialog dialog;
	private int type;
	private String[] hints = { "请输入手机", "请输入QQ", "请输入邮箱" };
	private String[] types = { "phone", "qq", "email" };

	/**
	 * 创建界面方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpwd);
	}

	/**
	 * 初始化数据
	 */
	@Override
	public void onInit() {
		list = new ArrayList<String>();
		list.add("手机");
		list.add("QQ");
		list.add("邮箱");
		
		type = 0;
	}

	/**
	 * 查找子view
	 */
	@Override
	public void onFindViews() {
		ivBack = (ImageView) findViewById(R.id.iv_back);
		etAccount = (EditText) findViewById(R.id.et_account);
		etNumber = (EditText) findViewById(R.id.et_number);
		btnCommit = (Button) findViewById(R.id.btn_commit);
		tvSelect = (TextView) findViewById(R.id.tv_select);
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
		ivBack.setOnClickListener(this);
		tvSelect.setOnClickListener(this);
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
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_select:
			dialog = new SelectStrDataDialog( this, list, "账号类型", 0, this );
			dialog.show();
			break;
		}
	}

	/**
	 * 检查用户输入是否合法	 
	 * */
	private boolean check() 
	{
		String account = etAccount.getText().toString();
		String number = etNumber.getText().toString();
		
		Pattern phonePattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", Pattern.CASE_INSENSITIVE );
		Matcher phoneMatcher = phonePattern.matcher( number );
		
		Pattern qqPattern = Pattern.compile("^[0-9]{6,12}$", Pattern.CASE_INSENSITIVE );
		Matcher qqMatcher = qqPattern.matcher( number );
		
		Pattern emailPattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", Pattern.CASE_INSENSITIVE );
		Matcher emailMatcher = emailPattern.matcher( number );
		
		boolean check = false;
		if ( TextUtils.isEmpty( account ) )
		{
			showToast("账户不可以为空");
		}
		else if ( type == 0 && !phoneMatcher.matches() ) 
		{
			showToast("手机号码输入有误！");
		} 
		else if ( type == 1 && !qqMatcher.matches() ) 
		{
			showToast("qq输入有误！");
		} 
		else if ( type == 2 && !emailMatcher.matches() ) 
		{
			showToast("邮箱输入有误！");
		}
		else 
		{
			check = true;
		}
		
		return check;
	}

	/**
	 * 请求后台找回密码
	 */
	private void requestFindPwd() 
	{
		final String account = etAccount.getText().toString();
		final String number = etNumber.getText().toString();
		
		loadingDialog = new LoadingDialog(this, "处理中...");
		loadingDialog.show();
		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... arg0) 
			{
				FBPRequest request = new FBPRequest();
				request.account = account;
				request.number = number;
				request.type = types[ type ];

				BaseResponse response = NetRequest.getInstance(getActivity())
						.send(request, BaseResponse.class);
				if (response != null && response.RES == 601) {
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
					msg = "密码找回成功,请输入新密码！";
					Intent intent = new Intent( FindPasswordActivity.this, SetPasswordActivity.class );
					intent.putExtra( "account", account );
					startActivity( intent );
					finish();
				} 
				else 
				{
					msg = "密码找回失败,请检查您的用户id和账号是否正确！";
				}
				showToastShort(msg);
				loadingDialog.dismiss();
			}

		}.execute();

	}

	/**
	 * 选择帐号类型回调方法
	 */
	@Override
	public void onItemClick( int which ) 
	{
		type = which;
		
		tvSelect.setText( list.get( which ) );
		
		etNumber.setHint( hints[ which ] );
		
		dialog.dismiss();
		dialog = null;
	}
}
