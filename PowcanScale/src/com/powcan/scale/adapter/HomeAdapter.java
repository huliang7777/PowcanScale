package com.powcan.scale.adapter;

import java.util.List;

import com.powcan.scale.R;
import com.powcan.scale.bean.Measure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HomeAdapter extends BaseAdapter {

	private List<Measure> items;
	private Context context;
	private LayoutInflater mInflater;

	public HomeAdapter(Context context, List<Measure> list) {
		this.context = context;
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
		convertView = mInflater.inflate(R.layout.item_home, null);
		
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
		TextView tvData = (TextView) convertView.findViewById(R.id.tv_data);
		TextView tvResult = (TextView) convertView.findViewById(R.id.tv_result);
		
		Measure measure = items.get(position);
		tvName.setText(measure.name);
		tvData.setText(measure.data);
		
		return convertView;
	}

}
