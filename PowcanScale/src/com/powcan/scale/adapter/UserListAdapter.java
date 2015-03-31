package com.powcan.scale.adapter;

import java.util.List;

import com.powcan.scale.R;
import com.powcan.scale.bean.UserInfo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UserListAdapter extends BaseAdapter {

	private List<UserInfo> items;
	private LayoutInflater mInflater;

	public UserListAdapter(Context context, List<UserInfo> list) {
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
		convertView = mInflater.inflate(R.layout.item_user, null);

		TextView tvName = (TextView) convertView
				.findViewById(R.id.tv_name);
		ImageView ivImage = (ImageView) convertView
				.findViewById(R.id.iv_image);

		UserInfo user = items.get(position);
		String username = user.getUsername();
		if (TextUtils.isEmpty(username) || username.equalsIgnoreCase("NULL")) 
		{
			username = user.getAccount();
		}
		tvName.setText( username );
		
		if( user.getGender().equalsIgnoreCase("f") )
		{
			ivImage.setImageResource(R.drawable.icon_female);
		}
		return convertView;
	}

}
