package com.powcanscale.adapter;

import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.powcanscale.R;
import com.powcanscale.ui.PagerPlaceholderFragment;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

	private Context context;

	public SectionsPagerAdapter(Context context, FragmentManager fm, String user) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = PagerPlaceholderFragment.newInstance(position + 1);
			break;
		case 1:
			fragment = PagerPlaceholderFragment.newInstance(position + 1);
			break;
		case 2:
			fragment = PagerPlaceholderFragment.newInstance(position + 1);
			break;
		default:
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return context.getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return context.getString(R.string.title_section2).toUpperCase(l);
		case 2:
			return context.getString(R.string.title_section3).toUpperCase(l);
		}
		return null;
	}
}