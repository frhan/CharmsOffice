package com.charmsoffice.mobilestudio.activity;


import android.content.Intent;
import android.content.SharedPreferences.Editor;
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
	
	private TextView userFullName;
	private TextView schoolName;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.charms);
		
		
		Util.writeLogFileIntoExternalStorage(getBaseContext(),Constants.DIR_NAME);
		
		//Constants.USER_FULL_NAME=LoginActivity.mSharedPrefference.getString(LoginActivity.PREFS_USER_FULLNAME, "");
		Constants.USER_FULL_NAME =  getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE).getString(LoginActivity.PREFS_USER_FULLNAME, "");
		Constants.USER_NAME = getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE).getString(LoginActivity.PREFS_USERNAME, "");
		Constants.USER_PASSWORD = getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE).getString(LoginActivity.PREFS_PASSWORD, "");
		Constants.SCHOOL_NAME = getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE).getString(LoginActivity.PREFS_SCHOOL_NAME, "");
		Constants.SCHOOL_CODE = getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE).getString(LoginActivity.PREFS_SCHOOL_CODE, "");
		
		Constants.RECORDING_LIST_URL =  "https://www.charmsoffice.com/charms/SchoolFilesNew/"+Constants.SCHOOL_CODE+"/sturecordings/"; 
		//Constants.RECORDING_LIST_URL =  "https://www.charmsoffice.com/charms/SchoolFilesNew/"+Constants.SCHOOL_CODE+"/sturecordings/"; 
		//Constants.ACCOMPAINMENT_LIST_URL = "https://www.charmsoffice.com/charms/SchoolFilesNew/"+Constants.SCHOOL_CODE+"/public/";
		
		Log.v("CharmsActivity",Constants.RECORDING_LIST_URL);
		
		userFullName = (TextView) findViewById(R.id.tv_username);
		userFullName.setText(Constants.USER_FULL_NAME);
		schoolName = (TextView) findViewById(R.id.tv_school_name);
		schoolName.setText(Constants.SCHOOL_NAME);
		
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

	public void onClickAccount(View view) {
//		Intent intent = new Intent(this, LoginActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		//intent.putExtra("Logout", true);
//		intent.putExtra("EXIT", true);
//		startActivity(intent);
//		finish();
		
		goAccount();
	}

	public void onClickLogout(View view) {

//		LoginActivity.mSharedPrefference = getSharedPreferences(
//				Constants.PREFS_NAME, MODE_PRIVATE);
//
//		Editor editor = LoginActivity.mSharedPrefference.edit();
//		editor.clear();
//		editor.commit();
//
//		// onBackPressed();
//
//		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		//intent.putExtra("Logout", true);
//		intent.putExtra("EXIT", true);
//
//		startActivity(intent);
//		finish();		
		
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