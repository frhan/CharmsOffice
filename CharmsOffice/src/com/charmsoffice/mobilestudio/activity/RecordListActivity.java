package com.charmsoffice.mobilestudio.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.view.Window;
import android.view.WindowManager;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData.Item;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.charmsoffice.mobilestudio.R;
import com.charmsoffice.mobilestudio.R.string;
import com.charmsoffice.mobilestudio.data.CommunicationLayer;
import com.charmsoffice.mobilestudio.data.Constants;
import com.charmsoffice.mobilestudio.util.AssignmentList;
import com.charmsoffice.mobilestudio.util.ExpandAnimation;
import com.charmsoffice.mobilestudio.util.ExpandCollapseAnimation;


public class RecordListActivity extends MainActivity {
	private ArrayList<AssignmentList> arrayList;
	private AssignmentList assignmentListObj;
	public static String deleteFileName = "";

	ArrayAdapter<Item> listAdapter;
	ListView list;
	boolean expanded = false;
	private View previousView = null;
	private boolean isExpanded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.record_list);
		arrayList = new ArrayList<AssignmentList>();
		list = (ListView) findViewById(R.id.udiniList);
		fetchAllData();
		final CustomListAdapter listAdapter = new CustomListAdapter(this,
				R.layout.list_items, arrayList);
		listAdapter.notifyDataSetChanged();
		Button b = new Button(this);
		b.setText("HelloButton");
		b.setVisibility(View.INVISIBLE);
		list.addFooterView(b);
		list.setAdapter(listAdapter);
		list.invalidate();


		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {


				View toolbar = view.findViewById(R.id.toolbar);
				// View toolbar2 = view.findViewById(R.id.toolbar2);
/*
				if(previousView != null && toolbar != previousView)
				{
					if((Boolean)previousView.getTag())
					{	
						ExpandAnimation expandAni = new ExpandAnimation(previousView, 500);
						toolbar.startAnimation(expandAni);
						previousView.setTag(false);
					}
				}

*/				ExpandAnimation expandAni = new ExpandAnimation(toolbar, 500);
				toolbar.startAnimation(expandAni);
				

			}

		});

	}
	/*
	@Override
	public void onBackPressed() {
		Intent goRecordingStudioActivity = new Intent(RecordListActivity.this, RecordingStudioActivity.class);
		startActivity(goRecordingStudioActivity);
		finish();
	}*/


	public void onClickLogout(View view) {

//		LoginActivity.mSharedPrefference = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
//
//		Editor editor = LoginActivity.mSharedPrefference.edit();
//
//		editor.clear();
//		editor.commit();
//		// onBackPressed();
//
//		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent.putExtra("Logout", true);
//		startActivity(intent);
//		finish();
		logOut();

	}

	public void onClickRecordingStudio(View view) {
		/*Intent goRecordingStudioActivity = new Intent(RecordListActivity.this, RecordingStudioActivity.class);
		startActivity(goRecordingStudioActivity);
		finish();*/
		onBackPressed();

	}


	private void fetchAllData() {
		try {
			arrayList.removeAll(arrayList);
			String assignmentList = CommunicationLayer.getAssignmentList(
					Constants.USER_NAME, Constants.USER_PASSWORD, "recordings");

			JSONObject jsonAssignment = new JSONObject(assignmentList);
			JSONArray jArray = jsonAssignment.getJSONArray("recordings");

			for (int i = 0; i < jArray.length(); i++) {
				assignmentListObj = new AssignmentList();
				JSONObject json_data = jArray.getJSONObject(i);
				assignmentListObj.setAssignmentName(json_data
						.getString("description"));
				assignmentListObj.setAssignmentDate(json_data
						.getString("filename"));
				assignmentListObj.setRecordingListScore(json_data
						.getString("score"));
				assignmentListObj.setRecordingListMessage(json_data
						.getString("notes"));
				arrayList.add(assignmentListObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class CustomListAdapter extends ArrayAdapter {
		private ArrayList<AssignmentList> searchArrayList;
		private Context mContext;

		public CustomListAdapter(Context context, int textViewResourceId,
				ArrayList<AssignmentList> arrayList) {
			super(context, textViewResourceId, arrayList);
			this.searchArrayList = arrayList;
			this.mContext = context;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			TextView gradeTV = null;
			TextView notesTV = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.list_items,
						null);
			}
			Log.w("Record list", searchArrayList + "__________"
					+ searchArrayList.get(position).getAssignmentDate());
			((TextView) convertView.findViewById(R.id.title))
			.setText(searchArrayList.get(position).getAssignmentName());

			ImageView playBtn = (ImageView) convertView
					.findViewById(R.id.iv_record_play);
			ImageView deleteBtn = (ImageView) convertView
					.findViewById(R.id.iv_record_delete);
			if (!searchArrayList.get(position).getRecordingListScore()
					.equalsIgnoreCase("0")) {
				((ImageView) convertView.findViewById(R.id.iv_scoreBase))
				.setImageResource(R.drawable.greencheck);

				deleteBtn.setVisibility(View.GONE);
				gradeTV = (TextView) convertView.findViewById(R.id.grade);
				gradeTV.setVisibility(View.VISIBLE);
				gradeTV
				.setText("Grade : "
						+ searchArrayList.get(position)
						.getRecordingListScore());
				notesTV = (TextView) convertView.findViewById(R.id.notes);
				notesTV.setVisibility(View.VISIBLE);
				notesTV.setText(""
						+ searchArrayList.get(position)
						.getRecordingListMessage());

			} else {

				deleteBtn.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								RecordListActivity.this);

						// set title
						alertDialogBuilder
						.setTitle("CharmsOffice Recorded Item");

						// set dialog message
						// alertDialogBuilder.setMessage("Do you want to delete "+
						// searchArrayList.get(position).getAssignmentDate() +
						// "?").setCancelable(false)
						alertDialogBuilder.setMessage(
								"Do you want to delete "
										+ searchArrayList.get(position)
										.getAssignmentDate())
										.setCancelable(false)

										.setPositiveButton("Yes",
												new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {

												HttpClient httpclient = new DefaultHttpClient();
												HttpResponse response = null;
												try {
													deleteFileName = searchArrayList
															.get(position)
															.getAssignmentDate();

													if (deleteFileName
															.contains(".mp3")
															|| deleteFileName
															.contains(" ")) {
														// deleteFileName =
														// deleteFileName.replace(".mp3",
														// "");
														deleteFileName = deleteFileName
																.replace(" ",
																		"%20");
														Log
														.v(
																"sub-----",
																"subtesting===="
																		+ deleteFileName);
													}

													response = httpclient
															.execute(new HttpGet(
																	"https://www.charmsoffice.com/charms/mobileStudio/mobileStudioUpload.asp?username="
																			+ Constants.USER_NAME
																			+ "&sid="
																			+ Constants.USER_PASSWORD
																			+ "&action=delete&filename="
																			+ deleteFileName));
													Log
													.w(
															"Record list",
															searchArrayList
															+ "__________"
															+ deleteFileName);

												} catch (ClientProtocolException e) {
													e.printStackTrace();
												} catch (IOException e) {
													e.printStackTrace();
												}
												StatusLine statusLine = response
														.getStatusLine();
												if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
													ByteArrayOutputStream out = new ByteArrayOutputStream();
													try {
														response.getEntity()
														.writeTo(out);
													} catch (IOException e) {
														e.printStackTrace();
													}
													try {
														out.close();
													} catch (IOException e) {
														e.printStackTrace();
													}
													String responseString = out
															.toString();
												} else {
													try {
														response.getEntity()
														.getContent()
														.close();
													} catch (IllegalStateException e) {
														e.printStackTrace();
													} catch (IOException e) {
														e.printStackTrace();
													}
													try {
														throw new IOException(
																statusLine
																.getReasonPhrase());
													} catch (IOException e) {
														e.printStackTrace();
													}
												}
												// setNotifyOnChange(true);
												arrayList.remove(position);

												/* Exception hapening */
												// listAdapter.notifyDataSetChanged();
												// list.setAdapter(listAdapter);
												/* Exception hapening */

												notifyDataSetChanged();
												list.refreshDrawableState();

												list.invalidate();
												// RecordListActivity.this.finish();
											}
										}).setNegativeButton("No",
												new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// if this button is clicked,
												// just
												// close
												// the dialog box and do nothing
												dialog.cancel();
											}
										});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();

						// show it
						alertDialog.show();

					}
				});
			}

			playBtn.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent goPlayerIntent = new Intent(mContext,
							PlayerActivity.class);
					goPlayerIntent.putExtra(Constants.SONG_NAME_PUT_EXTRA,
							searchArrayList.get(position).getAssignmentDate());

					Constants.FILE_DES_PUT_EXTRA=searchArrayList.get(position).getAssignmentName();
					Constants.RECORDINGLIST_GRADING_NOTE=searchArrayList.get(position).getRecordingListMessage();
					// Constants.RECORDING_LIST_URL =
					// Constants.RECORDING_LIST_URL;
					goPlayerIntent.putExtra(Constants.LIST_URL_PUT_EXTRA,
							Constants.RECORDING_LIST_URL);
					goPlayerIntent.putExtra("name", "My Rec");

					mContext.startActivity(goPlayerIntent);
					/*finish();*/
				}
			});
			View toolbar = convertView.findViewById(R.id.toolbar);
			((LinearLayout.LayoutParams) toolbar.getLayoutParams()).bottomMargin = -65;
			toolbar.setVisibility(View.GONE);

			return convertView;
		}
	}
}
