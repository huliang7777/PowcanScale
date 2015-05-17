package com.powcan.scale.dialog;

import android.app.TimePickerDialog;
import android.content.Context;

/**
 * 重载系统时间选择窗口
 * 解决两次执行onTimeSet问题
 * @author Administrator
 *
 */
public class TimeDialog extends TimePickerDialog {
	@Override
	protected void onStop() {
		// super.onStop();
	}

	public TimeDialog(Context context, OnTimeSetListener callBack,
			int hourOfDay, int minute, boolean is24HourView) {
		super(context, callBack, hourOfDay, minute, is24HourView);
	}
}
