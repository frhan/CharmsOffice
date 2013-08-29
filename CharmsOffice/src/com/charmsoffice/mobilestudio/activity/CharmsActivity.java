package com.charmsoffice.mobilestudio.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.charmsoffice.mobilestudio.R;
import com.charmsoffice.mobilestudio.data.Constants;
import com.charmsoffice.mobilestudio.util.Util;

public class CharmsActivity extends MainActivity {
	
	private TextView txtUserFullName;
	private TextView txtSchoolName;
	String userFullName;
	String schoolName;
	public SharedPreferences mSharedPrefference;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.charms);
		
		
		Util.writeLogFileIntoExternalStorage(getBaseContext(),Constants.DIR_NAME);
		mSharedPrefference = getApplicationContext().getSharedPreferences(Constants.PREFS_NAME,
				MODE_PRIVATE);
		//Constants.USER_FULL_NAME=LoginActivity.mSharedPrefference.getString(LoginActivity.PREFS_USER_FULLNAME, "");
		Constants.USER_FULL_NAME =  mSharedPrefference.getString(LoginActivity.PREFS_USER_FULLNAME, Constants.USER_FULL_NAME);		
		Constants.USER_NAME = mSharedPrefference.getString(LoginActivity.PREFS_USERNAME, Constants.USER_NAME);
		Constants.USER_PASSWORD = mSharedPrefference.getString(LoginActivity.PREFS_PASSWORD, Constants.USER_PASSWORD);
		Constants.SCHOOL_NAME = mSharedPrefference.getString(LoginActivity.PREFS_SCHOOL_NAME,Constants.SCHOOL_NAME);
		Constants.SCHOOL_CODE = mSharedPrefference.getString(LoginActivity.PREFS_SCHOOL_CODE, Constants.SCHOOL_CODE);
		
		Constants.RECORDING_LIST_URL =  "https://www.charmsoffice.com/charms/SchoolFilesNew/"+Constants.SCHOOL_CODE+"/sturecordings/"; 
		//Constants.RECORDING_LIST_URL =  "https://www.charmsoffice.com/charms/SchoolFilesNew/"+Constants.SCHOOL_CODE+"/sturecordings/"; 
		//Constants.ACCOMPAINMENT_LIST_URL = "https://www.charmsoffice.com/charms/SchoolFilesNew/"+Constants.SCHOOL_CODE+"/public/";
		
		Log.v("CharmsActivity",Constants.RECORDING_LIST_URL);
		Log.v("charmsactivit",Constants.USER_FULL_NAME);
		Log.v("CharmsActivity",Constants.SCHOOL_CODE);
		
		txtUserFullName = (TextView) findViewById(R.id.tv_username);
		txtUserFullName.setText(Constants.USER_FULL_NAME);
		txtSchoolName = (TextView) findViewById(R.id.tv_school_name);
		txtSchoolName.setText(Constants.SCHOOL_NAME);
		
	}

	public void onClickRecordingStudio(View view) {
		Intent goRecordingStudioActivity = new Intent(CharmsActivity.this,
				RecordingStudioActivity.class);
		startActivity(goRecordingStudioActivity);
		/*finish();*/

	}

	public void onClickEverythingElse(View view) {
		Intent goRecordingStudioActivity = new Intent(CharmsActivity.this,
				CharmsOfficeWebViewActivity.class);
		startActivity(goRecordingStudioActivity);
		/*finish();*/

	}

	public void onClickAccount(View view) 
	{		
		goAccount();
	}

	public void onClickLogout(View view) 
	{
		logOut();

	}
	
	@Override
	public void onBackPressed() {
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction("com.package.ACTION_LOGOUT");
		sendBroadcast(broadcastIntent);
		
		finish();
	}
	


}