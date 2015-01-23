package com.powcan.scale.ui.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.luckymonkey.ui.base.BaseFragment;
import com.powcan.scale.R;

public class LeftFragment extends BaseFragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    
    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;
    
    private ListView mDrawerListView;

    private int mCurrentSelectedPosition = 0;
    private boolean mUserLearnedDrawer;

	private View mBtnSettings;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }
		return inflater.inflate(R.layout.fragment_navigation_drawer, null);
	}

	@Override
	public void onInit() {
		// Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
	}

	@Override
	public void onFindViews() {
		View mDrawer = getView();
    	
        mDrawerListView = (ListView) mDrawer.findViewById(R.id.listView);        
        mBtnSettings = mDrawer.findViewById(R.id.btn_settings);
	}

	@Override
	public void onInitViewData() {
        mDrawerListView.setAdapter(new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new String[]{
                        getString(R.string.title_section1),
                        getString(R.string.title_section2),
                        getString(R.string.title_section3),
                }));
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
	}

	@Override
	public void onBindListener() {
		mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mBtnSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
		        if (mCallbacks != null) {
		            mCallbacks.onNavigationDrawerItemSelected(R.id.btn_settings, null);
		        }
			}
		});
	}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        
        Object obj = null;
        if (mDrawerListView != null) {
        	ListAdapter mAdapter = mDrawerListView.getAdapter();
        	if (mAdapter != null) {
        		obj = mAdapter.getItem(position);
        	}
            mDrawerListView.setItemChecked(position, true);
        }
        closeDrawer();
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position, obj);
        }
    }

    private void closeDrawer() {
        if (!mUserLearnedDrawer) {
            // The user manually opened the drawer; store this flag to prevent auto-showing
            // the navigation drawer automatically in the future.
            mUserLearnedDrawer = true;
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(getActivity());
            sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
        }
	}

	/**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position, Object obj);
    }
}
