package com.powcan.scale.adapter;

import java.util.List;

import com.powcan.scale.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 帐号界面适配类
 * @author Administrator
 *
 */
public class AccountAdapter extends BaseAdapter {

	private List<Integer> items;
	private LayoutInflater mInflater;

	public AccountAdapter(Context context, List<Integer> list) {
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
		convertView = mInflater.inflate(R.layout.item_account, null);

		TextView tvAccount = (TextView) convertView
				.findViewById(R.id.tv_account);

		Integer account = items.get(position);
		
		tvAccount.setText( account == 0 ? "" : String.valueOf(account));

		return convertView;
	}

}
