package com.charmsoffice.mobilestudio.activity;

import com.charmsoffice.mobilestudio.data.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class MainActivity extends Activity 
{
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = context;
	}
	
	protected void goAccount()
	{
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
		//intent.putExtra("Logout", true);
		intent.putExtra("EXIT", true);
		startActivity(intent);
		finish();
		
	}
	
	protected void logOut()
	{
		SharedPreferences sf = getSharedPreferences(
				Constants.PREFS_NAME, MODE_PRIVATE);

		Editor editor = sf.edit();
		editor.clear();
		editor.commit();

		// onBackPressed();

		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//intent.putExtra("Logout", true);
		intent.putExtra("EXIT", true);	

		startActivity(intent);
		finish();	
	}

}
