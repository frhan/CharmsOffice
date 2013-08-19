package com.charmsoffice.mobilestudio.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.Window;
import android.view.WindowManager;

import com.charmsoffice.mobilestudio.R;
import com.charmsoffice.mobilestudio.data.Constants;
import com.charmsoffice.mobilestudio.util.AssignmentList;

public class RecordingStudioActivity extends MainActivity {

	AssignmentList assignmentListObj;
	private TextView userFullName;
	private TextView schoolName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.recordingstudio);

		userFullName = (TextView) findViewById(R.id.tv_username);
		userFullName.setText(Constants.USER_FULL_NAME);
		

		schoolName = (TextView) findViewById(R.id.tv_record_studio_school_name);
		schoolName.setText(Constants.SCHOOL_NAME);
		
	}

	public void onClickAssignmentButton(View view) {
		startActivity(new Intent(RecordingStudioActivity.this, AssignmentListActivity.class));
		/*finish();*/
	}

	public void onClickAccompanimentButton(View view) {

		startActivity(new Intent(RecordingStudioActivity.this, AccompanimentListActivity.class));
		/* finish();*/

	}

	public void onClickRecordMeButton(View view) 
	{
		Intent intent = new Intent(RecordingStudioActivity.this, PlayerActivity.class);
		intent.putExtra("RecordMe", "Record");
		intent.putExtra("name", "Studio");
		startActivity(intent);
		/* finish();*/
	}

	public void onClickMyRecordingListButton(View view) {
		// Toast.makeText(RecordingStudioActivity.this,
		// "This part will be implemented.", Toast.LENGTH_LONG).show();

		// Intent goAccompanimentListIntent = new
		// Intent(RecordingStudioActivity.this, SongsListActivity.class);
		// goAccompanimentListIntent.putExtra(Constants.LIST_INTENT_PUT_EXTRA,
		// Constants.ACCOMPAINMENT_LIST_URL);
		// goAccompanimentListIntent.putExtra(Constants.TAG_PUT_EXTRA,
		// Constants.TAG_ACCOMPS);
		// startActivity(goAccompanimentListIntent);

		Intent intent = new Intent(RecordingStudioActivity.this, RecordListActivity.class);
		intent.putExtra("RecordMe", "Record");
		//intent.putExtra("name", "StudioSty");
		startActivity(intent);

		/*finish();*/

	}


	public void onClickLogout(View view) {
		//	 LoginActivity.mSharedPrefference = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
		//         
		//         Editor editor = LoginActivity.mSharedPrefference.edit();
		//         editor.clear();
		//         editor.commit();
		//        // onBackPressed();
		//         
		//         Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		//         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//         intent.putExtra("Logout", true);
		//         startActivity(intent);
		//         finish();
		logOut();

	}

	public void onClickCharms(View view) {
		/* Intent goRecordingStudioActivity = new Intent(RecordingStudioActivity.this, CharmsActivity.class);
	        startActivity(goRecordingStudioActivity);
	        finish();*/
		finish();

	}

	/* @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		 Intent goRecordingStudioActivity = new Intent(RecordingStudioActivity.this, CharmsActivity.class);
	        startActivity(goRecordingStudioActivity);
	        finish();
	}*/
}