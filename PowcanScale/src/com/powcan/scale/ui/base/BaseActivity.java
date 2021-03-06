package com.powcan.scale.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.powcan.scale.PowcanScaleApplication;
import com.powcan.scale.R;
import com.powcan.scale.util.SpUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 基础activity类
 * @author Administrator
 *
 */
public abstract class BaseActivity extends FragmentActivity {

	protected boolean mSlideFinish = false;

	protected int mDownX = 0;

	protected int finishAnimId = 0;

	protected Intent mIntent;

	protected boolean isStartActivity = false;

	protected Toast mShortToast = null;

	protected Toast mLongToast = null;
	
	public SpUtil mSpUtil;

	/**
	 * 界面创建回调方法
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mSpUtil = SpUtil.getInstance(this);
		
		PowcanScaleApplication.getInstance().addActivity(this);
	}
	
	/**
	 * 设置界面类型回调方法
	 */
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);

		// 调用顺序
		onInit();
		onFindViews();
		onInitViewData();
		onBindListener();
	}

	/**
	 * 设置返回监听
	 */
	public OnClickListener mBackClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title) {
		TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		if (tvTitle != null) {
			tvTitle.setText(title);
		}
	}

	/**
	 * 初始化 
	 */
	public abstract void onInit();

	/**
	 * 查找控件
	 */
	public abstract void onFindViews();

	/**
	 * 初始化控件内容 
	 */
	public abstract void onInitViewData();

	/**
	 * 注册控件事件
	 */
	public abstract void onBindListener();

	/**
	 * 打开滑动退出此Activity的功能
	 * @param isOpen
	 */
	public void setSlide2Finish(boolean isOpen) {
		mSlideFinish = isOpen;
	}
	
	/**
	 * 触摸时间回调方法
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean res = super.dispatchTouchEvent(ev);
		// YLog.i(this, "dispatchTouchEvent="+ev.getAction()+" "+res);
		if (mSlideFinish) {
			int action = ev.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				mDownX = (int) ev.getRawX();
				break;
			case MotionEvent.ACTION_UP:
				Window window = this.getWindow();
				DisplayMetrics dm = new DisplayMetrics();
				window.getWindowManager().getDefaultDisplay().getMetrics(dm);
				if (Math.abs((ev.getRawX() - mDownX)) > (dm.widthPixels / 3)) {
					this.finish();
				}
				break;
			}
		}
		return res;
	}

	/**
	 * 结束此activity时动画 从左边移到右边 
	 * 
	 */
	public void finishWithLeftAnim() {
		finishAnimId = R.anim.from_left_out;
	}

	/**
	 * 结束此activity时动画 从上往下移动
	 */
	public void finishWithDownAnim() {
		finishAnimId = R.anim.from_up_out;
	}

	/**
	 * Toast 短时间显示
	 * @param message
	 */
	public void showToastShort(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	public void showToastShort(int id) {
		Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast 长时间显示
	 * @param message
	 */
	public void showToastLong(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * Toast
	 * @param str资源ID
	 */
	public void showToast(int strResID) {
		String msgStr = this.getResources().getString(strResID);
		showToastLong(msgStr);
	}

	/**
	 * 显示提示
	 * 
	 * @param message
	 */
	public void showToast(String message) {
		showToastLong(message);
	}

	// jiang add
	protected Dialog progressDialog;

	protected Activity getActivity() {
		return this;
	}

	/**
	 * 在主线程中更新界面
	 * @param runnable
	 */
	protected void runOnUiThreadSafety(final Runnable runnable) {
		Activity activity = getActivity();
		if (activity == null) {
			return;
		}
		if (runnable == null) {
			return;
		}
		try {
			activity.runOnUiThread(runnable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭进度弹出框
	 */
	protected void toCloseProgressMsg() {
		runOnUiThreadSafety(new Runnable() {
			@Override
			public void run() {
				closeProgressDialog();
			}
		});
	}

	/**
	 * 获得进度弹出框
	 * @param msg
	 * @return
	 */
	protected Dialog getProgressDialog(String msg) {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setMessage(msg);
		return dialog;
	}

	/**
	 * 关闭进度弹出框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing() && progressDialog.getWindow() != null) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 更新进度弹出框数据
	 * @param msg
	 */
	protected void toShowProgressMsg(final String msg) {
		runOnUiThreadSafety(new Runnable() {
			@Override
			public void run() {
				if (progressDialog != null && progressDialog.isShowing()) {
					if (progressDialog instanceof ProgressDialog) {
						((ProgressDialog) progressDialog).setMessage(msg);
					} else {
						try {
							progressDialog.getClass().getMethod("setMessage", String.class).invoke(progressDialog, msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					progressDialog = getProgressDialog(msg);
					progressDialog.show();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		isStartActivity = false;
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void finish() {
		super.finish();
		if (finishAnimId != 0) {
			overridePendingTransition(R.anim.none, finishAnimId);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PowcanScaleApplication.getInstance().removeActivity(this);
	}

}
