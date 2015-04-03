package com.powcan.scale.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.powcan.scale.R;
import com.powcan.scale.adapter.UserListAdapter;
import com.powcan.scale.bean.UserInfo;
import com.powcan.scale.db.UserInfoDb;
import com.powcan.scale.ui.base.BaseFragment;
import com.powcan.scale.ui.profile.UserInfoDetailActivity;
import com.powcan.scale.ui.settings.SettingsActivity;
import com.powcan.scale.util.SpUtil;

public class LeftFragment extends BaseFragment implements OnClickListener {

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
	private TextView tvUsername;

	private UserInfoDb dbUserInfo;
	private UserInfo curUser;
	private ArrayList<UserInfo> users;
	private UserListAdapter mAdapter;
	
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
//        selectItem(mCurrentSelectedPosition);
        
        curUser = SpUtil.getInstance(mContext).getCurrUser();
		dbUserInfo = new UserInfoDb( mContext ); 
	}

	@Override
	public void onFindViews() {
		View mDrawer = getView();
    	
        mDrawerListView = (ListView) mDrawer.findViewById(R.id.listView);        
        mBtnSettings = mDrawer.findViewById(R.id.btn_settings);
        tvUsername = (TextView) mDrawer.findViewById(R.id.tv_username);
	}

	@Override
	public void onInitViewData() {
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        
        String username = curUser.getUsername();
        if ( TextUtils.isEmpty(username) || username.equalsIgnoreCase("NULL") )
        {
        	username = curUser.getAccount();
        }
        tvUsername.setText(username);
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
		    		Intent intent = new Intent(getActivity(), SettingsActivity.class);
		    		startActivity(intent);
		        }
			}
		});
        
        tvUsername.setOnClickListener( this );
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
        	obj = users.get( position ).getAccount();
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
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	users = dbUserInfo.getUserInfoes();
		mAdapter = new UserListAdapter(mContext, users);
		mDrawerListView.setAdapter( mAdapter );
    }

	@Override
	public void onClick(View view) 
	{
		switch ( view.getId() ) 
		{
			case R.id.tv_username:
				Intent intent = new Intent(mContext, UserInfoDetailActivity.class);
				startActivity(intent);
				break;
		}
	}
}
