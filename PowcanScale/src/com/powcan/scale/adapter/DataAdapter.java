package com.powcan.scale.adapter;

import java.util.List;

import com.powcan.scale.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DataAdapter extends BaseAdapter {

	private List<String> items;
	private LayoutInflater mInflater;

	public DataAdapter(Context context, List<String> list) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.item_account, null);

		TextView tvAccount = (TextView) convertView
				.findViewById(R.id.tv_account);

		String data = items.get(position);
		
		tvAccount.setText( data );

		return convertView;
	}

}
