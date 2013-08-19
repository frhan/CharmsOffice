package com.charmsoffice.mobilestudio.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.charmsoffice.mobilestudio.data.Constants;

public class DoorActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		boolean isRemember = getSharedPreferences(Constants.PREFS_NAME,
				MODE_PRIVATE).getBoolean("prefsRemember", false);
		if (isRemember) {
			
			startActivity(new Intent(this, CharmsActivity.class));
			
			
		} else {
			startActivity(new Intent(this, LoginActivity.class));
		}
		finish();
	}

}
