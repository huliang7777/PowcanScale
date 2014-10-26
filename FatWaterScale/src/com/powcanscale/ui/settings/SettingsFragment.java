package com.powcanscale.ui.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.powcanscale.R;
import com.umeng.fb.FeedbackAgent;


/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static SettingsFragment newInstance(int sectionNumber) {
		SettingsFragment fragment = new SettingsFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public SettingsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final FeedbackAgent agent = new FeedbackAgent(getActivity());
		agent.sync();
		
		View rootView = inflater.inflate(R.layout.pager_fragment_main, container, false);
		TextView textView = (TextView) rootView.findViewById(R.id.section_label);
		textView.setText("Setting" + Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			    agent.startFeedbackActivity();
			}
		});
		return rootView;
	}
}