package com.charmsoffice.mobilestudio.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.charmsoffice.mobilestudio.R;
import com.charmsoffice.mobilestudio.data.CommunicationLayer;
import com.charmsoffice.mobilestudio.data.Constants;
import com.charmsoffice.mobilestudio.util.AssignmentList;

public class AssignmentListActivity extends MainActivity {
	private ListView lstAssignment;
	private ArrayList<AssignmentList> arrayList;
	private AssignmentList assignmentListObj;
	Boolean isSDPresent = android.os.Environment.getExternalStorageState()
			.equals(android.os.Environment.MEDIA_MOUNTED);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.assignment);

		lstAssignment = (ListView) findViewById(R.id.listView1);

		arrayList = new ArrayList<AssignmentList>();

		fetchAllData();

		lstAssignment.setAdapter(new EfficientAdapter(getApplicationContext(),
				arrayList));
		lstAssignment.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				Intent goPlayerIntent = new Intent(AssignmentListActivity.this,
						PlayerActivity.class);
				//arrayList.get(position).getAssignmentAccomp();
				//Log.v("assignment Activitytesting", "_____"+arrayList.get(position).getAssignmentAccomp());


				//Log.v("assignment Activity", "++++++++===="+Constants.SONG_NAME_PUT_EXTRA);

				//goPlayerIntent.putExtra(Constants.FILE_DES_PUT_EXTRA, arrayList.get(position).getAssignmentName());
				Constants.ASSIGNMENT_NOTE=arrayList.get(position).getAssignmentMessage();
				Log.v("assignment Activitytesting", "Constants.ASSIGNMENT_NOTE=_____"+Constants.ASSIGNMENT_NOTE);
				Constants.FILE_DES_PUT_EXTRA=arrayList.get(position).getAssignmentName();
				Log.v("assignment Activitytesting", "Constants.FILE_DES_PUT_EXTRA_____"+Constants.FILE_DES_PUT_EXTRA);
				String url = Constants.ACCOMPAINMENT_LIST_URL+Constants.SCHOOL_CODE+"/public/";
				goPlayerIntent.putExtra(Constants.LIST_URL_PUT_EXTRA,
						url);

				goPlayerIntent.putExtra(Constants.SONG_NAME_PUT_EXTRA, arrayList.get(position).getAssignmentAccomp());
				Log.v("assignment Activitytesting", "SONG_NAME_PUT_EXTRA_____"+arrayList.get(position).getAssignmentAccomp());
				goPlayerIntent.putExtra("name","Assignments");
				if (isSDPresent) {
					startActivity(goPlayerIntent);
					/*finish();*/
				} else {
					Toast.makeText(AssignmentListActivity.this,
							"Sdcard not available.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}


	/*	@Override
	public void onBackPressed() {
		 Intent goRecordingStudioActivity = new Intent(AssignmentListActivity.this, RecordingStudioActivity.class);
	        startActivity(goRecordingStudioActivity);
	        finish();

	}*/


	public void onClickLogout(View view) {

		LoginActivity.mSharedPrefference = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);

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

	public void onClickRecordingStudio(View view) {
		/* Intent goRecordingStudioActivity = new Intent(AssignmentListActivity.this, RecordingStudioActivity.class);
	        startActivity(goRecordingStudioActivity);
	        finish();*/
		onBackPressed();

	}

	private void fetchAllData() {
		try {
			arrayList.removeAll(arrayList);
			String assignmentList = CommunicationLayer
					.getAssignmentList(Constants.USER_NAME,
							Constants.USER_PASSWORD, "assignments");

			JSONObject jsonAssignment = new JSONObject(assignmentList);
			JSONArray jArray = jsonAssignment.getJSONArray("assignments");

			for (int i = 0; i < jArray.length(); i++) {
				assignmentListObj = new AssignmentList();
				JSONObject json_data = jArray.getJSONObject(i);
				assignmentListObj.setAssignmentName(json_data
						.getString("assignment"));
				assignmentListObj.setAssignmentDate(json_data
						.getString("duedate"));
				assignmentListObj.setAssignmentAccomp(json_data
						.getString("accomp"));
				assignmentListObj.setAssignmentMessage(json_data
						.getString("notes"));
				arrayList.add(assignmentListObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class EfficientAdapter extends BaseAdapter {

		private static ArrayList<AssignmentList> searchArrayList;
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context,
				ArrayList<AssignmentList> results) {
			searchArrayList = results;
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return searchArrayList.size();
		}

		public Object getItem(int position) {
			return searchArrayList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.assignment_row, null);

				holder = new ViewHolder();
				holder.assignmentName = (TextView) convertView
						.findViewById(R.id.textView1);
				holder.assignmentDate = (TextView) convertView
						.findViewById(R.id.textView2);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.assignmentName.setText(searchArrayList.get(position)
					.getAssignmentName());
			holder.assignmentDate.setText(searchArrayList.get(position)
					.getAssignmentDate());

			return convertView;
		}

		static class ViewHolder {
			TextView assignmentName, assignmentDate;
		}

	}

}
