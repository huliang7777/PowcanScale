package com.powcan.scale.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.powcan.scale.R;
import com.powcan.scale.adapter.HomeAdapter;
import com.powcan.scale.ui.base.BaseFragment;

public class ReportFragment extends BaseFragment {
	private int index;
	private ListView mListView;
	private HomeAdapter mAdapter;

	public ReportFragment() {
		super();
	}

	public static ReportFragment getInstance(int index) {
		ReportFragment fragment = new ReportFragment();
		fragment.setIndex(index);
		return fragment;
	}

	private void setIndex(int index) {
		this.index = index;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_report, null);
		return view;
	}

	@Override
	public void onInit() {
		
	}

	@Override
	public void onFindViews() {
		View v = getView();
		
	}

	@Override
	public void onInitViewData() {
	}

	@Override
	public void onBindListener() {
		
	}
}
