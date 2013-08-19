package com.charmsoffice.mobilestudio.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.Html;

public class CharmOfficeApp {
	
	public static CharmOfficeApp instance;

	public static CharmOfficeApp getInstance() 
	{
		if (instance == null) 
		{
			instance = new CharmOfficeApp();
		}
		return instance;
	}

	public static void destroyInstance() 
	{
		instance = null;
	}
	
	public void openErrorDialog(String err_msg, String title, Context context)
	{
		err_msg = Html.fromHtml(err_msg).toString();
		final AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(title);
		alert.setMessage(err_msg);
		alert.setCancelable(true);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				
			}
		});

		alert.show();
	}
	
	 public void openInternetSettingsActivity(final Context context)
		{
			final AlertDialog.Builder alert = new AlertDialog.Builder(context);
			alert.setTitle("Internet Problem");
			alert.setMessage("No internet connection. Please connect to a network first.");
			alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				}
			});

			alert.show();
		}

}
