package com.powcan.scale.adapter;

import java.util.List;

import com.powcan.scale.bean.Measure;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class HomeAdapter extends BaseAdapter {

	private List<Measure> items;
	private Context context;

	public HomeAdapter(Context context, List<Measure> list) {
		this.context = context;
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
		return null;
	}

}
