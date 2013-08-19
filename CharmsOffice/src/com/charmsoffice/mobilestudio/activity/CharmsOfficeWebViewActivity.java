package com.charmsoffice.mobilestudio.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.Window;
import android.view.WindowManager;

import com.charmsoffice.mobilestudio.R;
import com.charmsoffice.mobilestudio.data.Constants;

public class CharmsOfficeWebViewActivity extends Activity {
	WebView wb;

	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if(url.equals("http://www.charmsoffice.com/parstu") || 
					url.equals("https://www.charmsoffice.com/parstu" ))
			{
				
				//Toast.makeText(CharmsOfficeWebViewActivity.this, "From href", Toast.LENGTH_SHORT).show();
				onBackPressed();
				return false;
			}
			return false;
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	        // Remove notification bar
	        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_web_view_with_html);
		wb = (WebView) findViewById(R.id.webView);
		wb.getSettings().setJavaScriptEnabled(true);
		wb.getSettings().setLoadWithOverviewMode(true);
		wb.getSettings().setUseWideViewPort(true);
		wb.getSettings().setBuiltInZoomControls(true);
		wb.getSettings().setPluginsEnabled(true);
		wb.setWebViewClient(new HelloWebViewClient());
		wb.loadUrl("http://www.charmsoffice.com/parstu/login.asp?s="+Constants.USER_NAME+"&p="+Constants.USER_PASSWORD);
	}
	

//	@Override
//	public void onBackPressed() 
//	{
//		 Intent goRecordingStudioActivity = new Intent(CharmsOfficeWebViewActivity.this, CharmsActivity.class);
//	        startActivity(goRecordingStudioActivity);
//	        finish();
//	}
}