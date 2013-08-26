package com.charmsoffice.mobilestudio.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.TextView.OnEditorActionListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.charmsoffice.mobilestudio.R;
import com.charmsoffice.mobilestudio.data.CommunicationLayer;
import com.charmsoffice.mobilestudio.data.Constants;
import com.charmsoffice.mobilestudio.networkoperation.JSONParser;
import com.charmsoffice.mobilestudio.util.CharmOfficeApp;
import com.charmsoffice.mobilestudio.util.InternetConnectivity;

public class LoginActivity extends Activity {

	EditText userName, userPassword;
	Button logIn;
	public static final String PREFS_USERNAME = "prefsUsername";
	public static final String PREFS_PASSWORD = "prefsPassword";
	public String PREFS_REMEMBER = "prefsRemember";
	public static final String PREFS_USER_FULLNAME = "prefsUserfullname";

	public static final String PREFS_SCHOOL_NAME = "prefsschoolname";
	public static final String PREFS_SCHOOL_CODE = "prefsschoolcode";

	public final String PREFS_FIRST_TIME_LOGIN = "prefsRemember";
	public static SharedPreferences mSharedPrefference;

	private ToggleButton rememberBtn;

	// public static boolean isRemember;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


		setContentView(R.layout.login);


		userName = (EditText) findViewById(R.id.schoolcode);

		userPassword = (EditText) findViewById(R.id.studentid);
		mSharedPrefference = getSharedPreferences(Constants.PREFS_NAME,
				MODE_PRIVATE);

		String usernameName = mSharedPrefference.getString(PREFS_USERNAME, "");
		String upassWord = mSharedPrefference.getString(PREFS_PASSWORD, "");
		Constants.USER_FULL_NAME=mSharedPrefference.getString(PREFS_USER_FULLNAME, "");
		Log.v("", "msg   "+Constants.USER_FULL_NAME);

		boolean isRemember = mSharedPrefference.getBoolean(PREFS_REMEMBER,
				false);

		rememberBtn = (ToggleButton) findViewById(R.id.rememberBtn);

		if (getIntent().getBooleanExtra("Logout", false)) {
			finish();
		}

		if (isRemember) {
			userName.setText(usernameName);
			userPassword.setText(upassWord);
			rememberBtn.setChecked(isRemember);
		} else {
			userName.setText("");
			userPassword.setText("");
			rememberBtn.setChecked(isRemember);
		}

		rememberBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				rememberLoginInfo();
			}
		});
		userPassword.setOnEditorActionListener(new OnEditorActionListener() 
		{			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_GO) {
					performDone();
					return true;
				}
				return false;		

			}
		});
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.package.ACTION_LOGOUT");
		registerReceiver(receiver, intentFilter);


	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
		
	}
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("onReceive","Logout in progress");
			//At this point you should start the login activity and finish this one
			finish();
		}
	};
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		data.getStringExtra("someValue");
		userName.setText(mSharedPrefference.getString(PREFS_USERNAME, ""));
		userPassword.setText(mSharedPrefference.getString(PREFS_PASSWORD, ""));
		rememberBtn.setChecked(true);
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void performDone()
	{

		if (!InternetConnectivity.isConnectedToInternet(this)) {
			CharmOfficeApp.getInstance().openInternetSettingsActivity(this);
		}

		else {
			try {
				if (userName.getText().toString().trim().length() > 0
						&& userPassword.getText().toString().trim().length() > 0) 
				{
					//String userSchoolName = userName.getText().toString().trim().replace(" ", "%20d");
					String respose = CommunicationLayer.getConfirmation(
							userName.getText().toString().trim(), userPassword
							.getText().toString(), "logon");
					JSONObject json = new JSONObject(respose);

					/*	String str = json.getString("info");
					JSONObject jsonFirstLastName = new 	JSONObject(str);

					Constants.USER_FIRST_NAME = jsonFirstLastName.getString("firstName");
					Log.v("Constants.USER_FIRST_NAME", Constants.USER_FIRST_NAME);

					Constants.USER_LAST_NAME = jsonFirstLastName.getString("lastName");
					Log.v("Constants.USER_LAST_NAME ",Constants.USER_LAST_NAME );

					Constants.USER_FULL_NAME=Constants.USER_FIRST_NAME+" "+Constants.USER_LAST_NAME ;*/




					Log.v("result name  is :", "____" + json.getString("info"));

					//Log.v("result name  is :", "____" + jArray.getString(1));

					Log.v("result is :", "____" + json.getString("result"));

					if (json.getString("result").equalsIgnoreCase("success")) {
						Editor editor = mSharedPrefference.edit();


						String str = json.getString("info");
						JSONObject jsonFirstLastName = new 	JSONObject(str);

						Constants.USER_FIRST_NAME = jsonFirstLastName.getString("firstName");
						Log.v("Constants.USER_FIRST_NAME", Constants.USER_FIRST_NAME);

						Constants.USER_LAST_NAME = jsonFirstLastName.getString("lastName");
						Log.v("Constants.USER_LAST_NAME ",Constants.USER_LAST_NAME );

						Constants.USER_FULL_NAME=Constants.USER_FIRST_NAME+" "+Constants.USER_LAST_NAME ;
						editor.putString(PREFS_USER_FULLNAME, Constants.USER_FULL_NAME);
						
						Constants.SCHOOL_NAME = jsonFirstLastName.getString("school");
						editor.putString(PREFS_SCHOOL_NAME, Constants.SCHOOL_NAME);	
						
						Constants.SCHOOL_CODE = jsonFirstLastName.getString("schcode");
						editor.putString(PREFS_SCHOOL_CODE, Constants.SCHOOL_CODE);	



						Constants.USER_NAME = userName.getText().toString()
								.trim();
						Constants.USER_PASSWORD = userPassword.getText()
								.toString().trim();

						mSharedPrefference = getSharedPreferences(
								Constants.PREFS_NAME, MODE_PRIVATE);

						editor.putString(PREFS_USERNAME, Constants.USER_NAME);
						editor.putString(PREFS_PASSWORD,
								Constants.USER_PASSWORD);
						editor.commit();
						openSuccessDialog("Successfully Logged in!");

					}

					else {
						CharmOfficeApp
						.getInstance()
						.openErrorDialog(
								"Either the school code or the password was incorrect",
								"Account", this);
					}
				}

				else {
					CharmOfficeApp.getInstance().openErrorDialog(
							"Please enter a username or SID to login", " ",
							this);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void onClickDone(View view) 
	{
		performDone();

	}

	/*
	 * public void onClickLogout(View view) { SharedPreferences pref =
	 * getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
	 * 
	 * //SharedPreferences prefs; // here you get your prefrences by either of
	 * // two methods Editor editor = pref.edit(); editor.clear();
	 * editor.commit(); // onBackPressed(); finish();
	 * 
	 * }
	 */

	public void openSuccessDialog(String success_msg) {
		success_msg = Html.fromHtml(success_msg).toString();
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage(success_msg);
		alert.setCancelable(false);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				startActivity(new Intent(LoginActivity.this,
						CharmsActivity.class));
				//finish();
			}
		});

		alert.show();

	}

	private void rememberLoginInfo() {
		mSharedPrefference = getSharedPreferences(Constants.PREFS_NAME,
				MODE_PRIVATE);
		Editor editor = mSharedPrefference.edit();
		if (rememberBtn.isChecked()) {
			editor.putBoolean(PREFS_REMEMBER, true);
			editor.commit();
		} else {
			editor.putBoolean(PREFS_REMEMBER, false);
			editor.commit();
		}
	}

	/*	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		mSharedPrefference = getSharedPreferences(Constants.PREFS_NAME,
				MODE_PRIVATE);

		Editor editor = mSharedPrefference.edit();
		editor.clear();
		editor.commit();

		// SharedPreferences prefs; // here you get your prefrences by either of

		finish();

		// System.exit(0);

	}*/

}