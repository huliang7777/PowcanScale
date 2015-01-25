package com.powcan.scale.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class HomeFragment extends Fragment {
	private int index;

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
		Button view = new Button(this.getActivity());
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		view.setText("ViewPager：：：" + index);
		return view;
	}
}
