package com.powcan.scale.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.powcan.scale.R;
import com.powcan.scale.adapter.HomeAdapter;
import com.powcan.scale.bean.Measure;
import com.powcan.scale.ui.base.BaseFragment;

public class HomeFragment extends BaseFragment {
	private int index;
	private ListView mListView;
	private HomeAdapter mAdapter;

	public HomeFragment() {
		super();
	}

	public static HomeFragment getInstance(int index) {
		HomeFragment fragment = new HomeFragment();
		fragment.setIndex(index);
		return fragment;
	}

	private void setIndex(int index) {
		this.index = index;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		return view;
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onFindViews() {
		View v = getView();
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View header = inflater.inflate(R.layout.item_home_header, null);
		
		mListView = (ListView) v.findViewById(R.id.list_view);
		mListView.addHeaderView(header);
	}

	@Override
	public void onInitViewData() {
		mListView.setVisibility(View.VISIBLE);
		
		List<Measure> list = new ArrayList<Measure>();
		list.add(new Measure("体重", "75KG"));
		list.add(new Measure("BMI", "75KG"));
		list.add(new Measure("体脂率", "75KG"));
		list.add(new Measure("肌肉比例", "75KG"));
		list.add(new Measure("身体年龄", "75KG"));
		list.add(new Measure("皮下脂肪", "75KG"));
		list.add(new Measure("内脏脂肪", "75KG"));
		list.add(new Measure("基础代谢(亚)", "75KG"));
		list.add(new Measure("基础代谢(欧)", "75KG"));
		list.add(new Measure("骨量", "75KG"));
		list.add(new Measure("水含量", "75KG"));
		
		mAdapter = new HomeAdapter(mContext, list);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onBindListener() {
		
	}
}
