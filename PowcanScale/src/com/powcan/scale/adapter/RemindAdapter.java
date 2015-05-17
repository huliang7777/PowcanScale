package com.powcan.scale.adapter;

import java.util.List;

import com.powcan.scale.R;
import com.powcan.scale.bean.Remind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 测量提醒界面适配类
 * @author Administrator
 *
 */
public class RemindAdapter extends BaseAdapter {

	private List<Remind> items;
	private LayoutInflater mInflater;

	public RemindAdapter(Context context, List<Remind> list) {
		this.mInflater = LayoutInflater.from(context);
		this.items = list;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获得并构造帐号界面view
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.item_remind, null);

		TextView tvTime = (TextView) convertView
				.findViewById(R.id.tv_time);
		TextView tvContent = (TextView) convertView
				.findViewById(R.id.tv_content);
		ImageView imgSwitch = (ImageView) convertView
				.findViewById(R.id.img_switch);

		Remind remind = items.get(position);
		
		tvTime.setText( remind.getTime() );
		tvContent.setText( remind.getContent() );
		imgSwitch.setImageResource( remind.isOn() ? R.drawable.set_on : R.drawable.set_off );

		return convertView;
	}

}
