package com.charmsoffice.mobilestudio.activity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import android.R.bool;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.charmsoffice.mobilestudio.R;
import com.charmsoffice.mobilestudio.data.BarLevelDrawable;
import com.charmsoffice.mobilestudio.data.CommunicationLayer;
import com.charmsoffice.mobilestudio.data.Constants;
import com.charmsoffice.mobilestudio.data.MicrophoneInputListener;
import com.charmsoffice.mobilestudio.exception.RecorderExceptionMessage;
import com.charmsoffice.mobilestudio.util.RecordStudio;
import com.charmsoffice.mobilestudio.util.Util;

public class PlayerActivity extends MainActivity implements OnClickListener,MicrophoneInputListener {
	private MediaPlayer mPlayer = null;
	private ImageView playButton = null, imgViewTrash, imgViewUpload;
	private ImageView stopButton = null;
	private ImageView recordButton;
	private String currentFile;
	private boolean isStarted = false;
	private static String mNewMergedFileName = null;
	private RecordStudio mRecordStudio = new RecordStudio(Environment.getExternalStorageDirectory() + "/merged.mp3",
			8000,this); 
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	ProgressDialog mProgressDialog;
	ProgressBar pb;
	Dialog dialog;
	int downloadedSize = 0;
	int totalSize = 0;
	TextView cur_val;
	public static String dwnload_file_path = "";

	static int serverResponseCode;
	private boolean isRecord = false;

	public static String sub = "";
	// public static String songName = "";

	private boolean isCheck = false;
	private boolean isVoiceRecorded = false;
	private boolean isCurrentSongRecorded = false;
	private int secondsLeft = 0;
	private TextView userFullName;
	private TextView schoolName;
	private TextView recordTime;
	private TextView txtRecord;
	private Button btnBack;
	private ToggleButton tbAutoLock;
	PowerManager powerManager;
	WakeLock wakeLock;
	private  SharedPreferences mSharedPrefference;
	private static final String PREFS_AUTO_LOCK = "auto_lock";
	BarLevelDrawable mBarLevel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);


		setContentView(R.layout.layout_player);

		userFullName = (TextView) findViewById(R.id.tv_username);
		userFullName.setText(Constants.USER_FULL_NAME);
		
		schoolName = (TextView) findViewById(R.id.tv_player_school_name);
		schoolName.setText(Constants.SCHOOL_NAME);

		playButton = (ImageView) findViewById(R.id.play);
		stopButton = (ImageView) findViewById(R.id.stop);
		recordTime = (TextView) findViewById(R.id.txt_record_time);
		recordButton = (ImageView)findViewById(R.id.record);
		imgViewTrash = (ImageView) findViewById(R.id.iv_trash);
		imgViewTrash.setClickable(false);
		txtRecord = (TextView) findViewById(R.id.txt_record);
		btnBack = (Button) findViewById(R.id.btn_back);
		tbAutoLock = (ToggleButton) findViewById(R.id.tb_auto_lock);
		tbAutoLock.setOnClickListener(this);

		imgViewUpload = (ImageView) findViewById(R.id.iv_upload);
		imgViewUpload.setClickable(false);

		mBarLevel = (BarLevelDrawable) findViewById(R.id.bar_level_drawable_view);

		imgViewTrash.setAlpha(90);
		((ImageView) findViewById(R.id.iv_upload)).setImageResource(R.drawable.upload2);

		mPlayer = new MediaPlayer();
		TextView fileDescription = (TextView) findViewById(R.id.selectedfile);
		TextView fileComment = (TextView) findViewById(R.id.tv_comment);
		TextView fileCommentRecordingList = (TextView) findViewById(R.id.tv_comment_recordlist);
		RecorderExceptionMessage.getExceptionMessage(mRecordStudio, (TextView) findViewById(R.id.StatusTextView));

		powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
		mSharedPrefference = getSharedPreferences(Constants.PREFS_NAME,
				MODE_PRIVATE);

		if(getIntent().getStringExtra("name") != null)
		{
			String name = getIntent().getStringExtra("name");			
			Log.i("PlayerActivity", name);
			btnBack.setText(name);			
		}

		if (getIntent().getStringExtra("RecordMe") != null) {
			isCheck = true;
			playButton.setImageResource(R.drawable.play_disabled);
			playButton.setClickable(false);
			fileDescription.setText("Record below and upload to your account");
			txtRecord.setVisibility(View.VISIBLE);

		} else {
			isCheck = false;
			sub = getIntent().getStringExtra(Constants.SONG_NAME_PUT_EXTRA);
			// songName=
			// getIntent().getStringExtra(Constants.FILE_DES_PUT_EXTRA);

			Log.v("player Activity", "++++++++====" + Constants.FILE_DES_PUT_EXTRA);
			/*
			 * if (sub.contains(" ")) {
			 * 
			 * }
			 */
			if (sub.contains(".mp3") || sub.contains(" ")) {
				sub = sub.replace(".mp3", "");
				sub = sub.replace(" ", "%20");
				Log.v("sub-----", "subtesting====" + sub);
			}

			else {

			}
			Log.v("dwnload_file_path-----", "dwnload_file_path====" + Constants.LIST_URL_PUT_EXTRA);

			mNewMergedFileName = sub;
			Log.v("mNewMergedFileName-----", "mNewMergedFileName====" + mNewMergedFileName);

			//"https://www.charmsoffice.com/charms/SchoolFilesNew/charltrain/sturecordings/
			if (getIntent().getStringExtra(Constants.LIST_URL_PUT_EXTRA).equalsIgnoreCase(Constants.RECORDING_LIST_URL)) 
			{
				dwnload_file_path = getIntent().getStringExtra(Constants.LIST_URL_PUT_EXTRA) + Constants.USER_PASSWORD
						+ "/" + sub + ".mp3";

				Log.v("dwnload_file_path11 -----", "dwnload_file_path11 ====" + dwnload_file_path);

			} else {
				dwnload_file_path = getIntent().getStringExtra(Constants.LIST_URL_PUT_EXTRA)

						+ sub + ".mp3";
				Log.v("dwnload_file_path22 -----", "dwnload_file_path22 ====" + dwnload_file_path);
			}

			// Log.v("sub-----", "subtesting===="+sub);

			Constants.DOWNLOAD_FILE_PATH = dwnload_file_path;

			Log.v("Constants.DOWNLOAD_FILE_PATH -----", "Constants.DOWNLOAD_FILE_PATH ===="
					+ Constants.DOWNLOAD_FILE_PATH);
			fileDescription.setText(Constants.FILE_DES_PUT_EXTRA);

			if (!Constants.RECORDINGLIST_GRADING_NOTE.equals("") || !Constants.RECORDINGLIST_GRADING_NOTE.equals(" ")) {
				fileCommentRecordingList.setText(Constants.RECORDINGLIST_GRADING_NOTE);
				Constants.RECORDINGLIST_GRADING_NOTE = "";
			}
			if(!Constants.ASSIGNMENT_NOTE.equals("")||!Constants.ASSIGNMENT_NOTE.equals(" ")){
				fileComment.setText(Constants.ASSIGNMENT_NOTE);
				Constants.ASSIGNMENT_NOTE="";
			}


			mProgressDialog = new ProgressDialog(PlayerActivity.this);
			mProgressDialog.setMessage("Downloading...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setCancelable(false);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			DownloadFile downloadFile = new DownloadFile();
			downloadFile.execute(dwnload_file_path);		

		}
	}
	@Override
	protected void onStop() {
		super.onStop();
		if(wakeLock.isHeld())
		{
			wakeLock.release();
		}

	}

	@Override
	protected void onResume() {

		super.onResume();

		recordTime.setVisibility(View.GONE);

		boolean isAutoLock = getAutoLock();
		tbAutoLock.setChecked(isAutoLock);
		setAutoLock(isAutoLock);
	}
	private Timer timer = null;
	int count = 0;

	private void startTimer()
	{
		recordTime.setVisibility(View.VISIBLE);
		if(timer == null)			
		{
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() 
			{
				@Override
				public void run() 
				{					
					timerMethod();
				}
			}, 0,1000);
		}
	}
	private void timerMethod()
	{
		this.runOnUiThread(Timer_Tick);		
	}

	private void stopTimer()
	{
		if(timer != null)
		{
			timer.cancel();
			timer = null;
			count = 0;
		}
	}

	private Runnable Timer_Tick = new Runnable() 
	{

		public void run()
		{
			count++;
			int seconds = count;
			int minutes = seconds / 60;
			seconds     = seconds % 60;
			recordTime.setText("Recording: "+String.format("%02d:%02d", minutes, seconds));
		}
	};

	public void onClickLogout(View view) {

		//
		//		LoginActivity.mSharedPrefference = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
		//
		//		Editor editor = LoginActivity.mSharedPrefference.edit();
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
		/*		Intent goRecordingStudioActivity = new Intent(PlayerActivity.this, RecordingStudioActivity.class);
		startActivity(goRecordingStudioActivity);
		finish();*/
		onBackPressed();


	}


	public void onClickRewindButton(View view) {
		if (mPlayer.isPlaying()) {
			/*Log.w("value is ", mPlayer.getCurrentPosition() + "");
            int currentPosition = mPlayer.getCurrentPosition();
            if (currentPosition - 3000 > 0) {
                mPlayer.seekTo(currentPosition - 3000);
            } else {
                mPlayer.seekTo(0);
            }*/
			mPlayer.seekTo(0);
		}

	}

	public void onClickStopButton(View view) {

		if (mRecordStudio.isRecording()) {
			mRecordStudio.stop();
			stopTimer();
			recordButton.setEnabled(true);
			// writeNameMergedFile();
		}
		stopPlay();
		if(isVoiceRecorded || isCurrentSongRecorded){

			imgViewTrash.setAlpha(250);
			((ImageView) findViewById(R.id.back)).setImageResource(R.drawable.rewind_disabled);
			((ImageView) findViewById(R.id.iv_upload)).setImageResource(R.drawable.upload);
			imgViewTrash.setClickable(true);
			imgViewUpload.setClickable(true);
		}


		if (isCheck) {
			playButton.setClickable(true);
			// mPlayer.getDuration();
		}

	}

	public void onClickPlayButton(View view) {
		if (mPlayer.isPlaying()) {
			mPlayer.pause();
			playButton.setImageResource(R.drawable.play_ready);
		} else {
			if (isStarted) {
				mPlayer.start();
				playButton.setImageResource(R.drawable.pause_ready);
			} else {
				if (isCheck) {
					String createdFileDir = Environment.getExternalStorageDirectory() + "/merged.mp3";
					((ImageView) findViewById(R.id.back)).setImageResource(R.drawable.rewind_ready);
					Log.e("File path is", "_________" + createdFileDir);
					startPlay(createdFileDir);
				} else {

					Log.v("playercurrentFile==", " ======" + currentFile);


					startPlay(currentFile);
					((ImageView) findViewById(R.id.back)).setImageResource(R.drawable.rewind_ready);

					if(isCurrentSongRecorded){
						String createdFileDir = Environment.getExternalStorageDirectory() + "/merged.mp3";
						((ImageView) findViewById(R.id.back)).setImageResource(R.drawable.rewind_ready);
						Log.e("File path is", "_________" + createdFileDir);
						startPlay(createdFileDir);
					}
				}
			}
		}
		mPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				playButton.setImageResource(R.drawable.play_ready);
				((ImageView) findViewById(R.id.back)).setImageResource(R.drawable.rewind_disabled);
			}
		});
	}

	public void onClickRecordButton(View view) {

		if (getIntent().getStringExtra("RecordMe") != null) {
			getcountdownBeforeRecording();
			isRecord = true;
			isVoiceRecorded = true;
		} else {
			if (mPlayer.isPlaying()) {
				mRecordStudio.start();
				startTimer();
				recordButton.setEnabled(false);
				isCurrentSongRecorded = true;
			} else {
				getcountdownBeforeRecording();
				isCurrentSongRecorded = true;
				Handler handler = new Handler(); 
				handler.postDelayed(new Runnable() { 
					public void run() {    
						startPlay(currentFile);
					} 
				}, 5000);

				// startPlay(currentFile);



			}
		}
	}

	private void getcountdownBeforeRecording() {
		new CountDownTimer(5500, 1000) {
			public void onTick(long ms) {

				if (Math.round((float) ms / 1000) != secondsLeft) {
					secondsLeft = Math.round((long) ms / 1000);
					((TextView) findViewById(R.id.StatusTextView))
					.setText("Recording Start in " + secondsLeft
							+ getUnitofRecording());
				}
			}

			public void onFinish() {
				mRecordStudio.start();
				startTimer();
				recordButton.setEnabled(false);

			}
		}.start();
	}

	protected String getUnitofRecording() {
		if (secondsLeft < 2) {
			return " Second";
		} else {
			return " Seconds";
		}
	}

	public void onClickTrashButton(View view) {

		Log.e("VoiceRecorded is", "_________" + isVoiceRecorded);
		if(isCurrentSongRecorded){
			stopPlay();
			isCurrentSongRecorded=false;
			((ImageView) findViewById(R.id.iv_upload)).setImageResource(R.drawable.upload2);
			imgViewTrash.setAlpha(90);
			playButton.setImageResource(R.drawable.play_ready);

			imgViewTrash.setClickable(false);
			imgViewUpload.setClickable(false);
			String createdFileDir = Environment.getExternalStorageDirectory() + "/"+sub+ ".mp3";
			((ImageView) findViewById(R.id.back)).setImageResource(R.drawable.rewind_disabled);
			Log.e("File path is", "_________" + createdFileDir);
			//startPlay(createdFileDir);
		}

		if(isVoiceRecorded){

			isVoiceRecorded =false;

			((ImageView) findViewById(R.id.iv_upload)).setImageResource(R.drawable.upload2);
			imgViewTrash.setAlpha(90);
			playButton.setImageResource(R.drawable.play_disabled);
			((ImageView) findViewById(R.id.back)).setImageResource(R.drawable.rewind_disabled);

			imgViewTrash.setClickable(false);
			imgViewUpload.setClickable(false);
			playButton.setClickable(false); 
		}


		if (isCheck) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/" + mNewMergedFileName + ".mp3");
			file.delete();
		} else {
			/*File file = new File(Environment.getExternalStorageDirectory()+ "/" + sub + ".mp3");
			file.delete();*/

			File fileMerge = new File(Environment.getExternalStorageDirectory()
					+ "/" + "merged.mp3");
			fileMerge.delete();
		}

		imgViewTrash.setClickable(false);
		imgViewUpload.setClickable(false);
		// mPlayer.reset();
	}

	private void startPlay(String file) {
		mPlayer.stop();
		mPlayer.reset();

		try {
			mPlayer.setDataSource(file);
			mPlayer.prepare();
			mPlayer.start();
			playButton.setImageResource(R.drawable.pause_ready);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		isStarted = true;
	}

	private void stopPlay() {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.reset();
		}

		((ImageView) findViewById(R.id.back)).setImageResource(R.drawable.rewind_disabled);
		playButton.setImageResource(R.drawable.play_ready);
		stopButton.setImageResource(R.drawable.stop_ready);

		isStarted = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}

		/*	if(wakeLock.isHeld())
			wakeLock.release();*/
	}

	private void writeNameMergedFile() {
		if (isCheck) {
			imgViewTrash.setClickable(true);
			imgViewUpload.setClickable(true);
			LayoutInflater li = LayoutInflater.from(PlayerActivity.this);
			final View promptsView = li.inflate(R.layout.layout_merged_file_name, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlayerActivity.this);
			alertDialogBuilder.setTitle(getString(R.string.alert_write_name_title))
			.setMessage(getString(R.string.alert_write_name_message)).setView(promptsView).setCancelable(false)
			.setPositiveButton(getString(R.string.alert_ok), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					mNewMergedFileName = ((EditText) promptsView.findViewById(R.id.editTextDialogUserInput))
							.getText().toString();
					if (mNewMergedFileName.contains(" ")) {
						//mNewMergedFileName = mNewMergedFileName.replace(".mp3", "");
						mNewMergedFileName = mNewMergedFileName.replace(" ", "%20");
						Log.v("mNewMergedFileName-----", "mNewMergedFileName====" + mNewMergedFileName);
					}

					String createdFileDir = Environment.getExternalStorageDirectory() + "/";
					File from = new File(createdFileDir, "merged.mp3");
					File to = new File(createdFileDir, mNewMergedFileName + Constants.FILE_FORMAT);
					from.renameTo(to);


					imgViewUpload.setBackgroundResource(R.drawable.upload2);
					imgViewTrash.setAlpha(90);
					imgViewTrash.setClickable(false);
					imgViewUpload.setClickable(false);

					getUploadDataTask uploadDataTask = new getUploadDataTask();
					dlog = ProgressDialog.show(PlayerActivity.this, "Uploading", "Please wait...", true, false);

					uploadDataTask.execute();
				}
			})

			.setNegativeButton(getString(R.string.alert_cancel), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();






		} else {
			mNewMergedFileName = sub;
			String createdFileDir = Environment.getExternalStorageDirectory() + "/";
			File from = new File(createdFileDir, "merged.mp3");
			File to = new File(createdFileDir, mNewMergedFileName + Constants.FILE_FORMAT);
			from.renameTo(to);
			imgViewTrash.setClickable(true);
			imgViewUpload.setClickable(true);
		}


		/*        Handler handler = new Handler(); 
	    handler.postDelayed(new Runnable() { 
	         public void run() {    
	        	 //startPlay(currentFile);
	         } 
	    }, 3000);
   	 AlertDialog.Builder alert = new AlertDialog.Builder(this);
	 alert .setTitle("Uploaded ");
		alert.setMessage("Your file has been uploaded.");
		alert.setCancelable(false);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				//startActivity(new Intent(PlayerActivity.this, PlayerActivity.class));

			}
		});

		alert.show();*/

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mPlayer != null) {
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;
		}
		if (mRecordStudio.isRecording()) {
			mRecordStudio.stop();
			stopTimer();
			recordButton.setEnabled(true);
			// writeNameMergedFile();
		}
		
		if(wakeLock.isHeld())
			wakeLock.release();
	}

	public void onClickUploadButton(View view) {
		isVoiceRecorded =false;
		isCurrentSongRecorded=false;

		Log.v("sub-----", "isRecord testing===="+isRecord);
		if(isRecord){
			writeNameMergedFile();
			isRecord = false;
		}

		else
		{
			((ImageView) findViewById(R.id.iv_upload)).setImageResource(R.drawable.upload2);
			imgViewTrash.setAlpha(90);


			try {
				String checkSongName = "";
				if (isCheck) {
					checkSongName = CommunicationLayer.getFileNameCheck(
							Constants.USER_NAME, Constants.USER_PASSWORD, "check",
							mNewMergedFileName + ".mp3");
				} else {

					mNewMergedFileName = Constants.FILE_DES_PUT_EXTRA;
					if (mNewMergedFileName.contains(" ")) {
						//mNewMergedFileName = mNewMergedFileName.replace(".mp3", "");
						mNewMergedFileName = mNewMergedFileName.replace(" ", "%20");
						Log.v("mNewMergedFileName-----", "mNewMergedFileName====" + mNewMergedFileName);
					}
					String createdFileDir = Environment.getExternalStorageDirectory() + "/";
					File from = new File(createdFileDir, "merged.mp3");
					File to = new File(createdFileDir, mNewMergedFileName + Constants.FILE_FORMAT);
					from.renameTo(to);

					imgViewTrash.setClickable(true);
					imgViewUpload.setClickable(true);


					checkSongName = CommunicationLayer.getFileNameCheck(
							Constants.USER_NAME, Constants.USER_PASSWORD, "check",
							mNewMergedFileName + ".mp3");
				}
				JSONObject songNameList = new JSONObject(checkSongName);
				String songMsg = songNameList.getString("message");
				String songResult = songNameList.getString("result");
				if (songMsg.equalsIgnoreCase("entry already exists")
						|| songResult.equalsIgnoreCase("error")) {
					//Toast.makeText(PlayerActivity.this, "entry already exists",Toast.LENGTH_LONG).show();
					AlertDialog.Builder alert = new AlertDialog.Builder(this);
					alert.setMessage("There seems to be a file already uploaded.");
					alert.setCancelable(false);
					alert.setTitle("Not Uploaded");
					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int whichButton) 
						{
							//startActivity(new Intent(PlayerActivity.this, PlayerActivity.class));
							//dialog.cancel();
						}
					});

					alert.show();
				} else {
					String path = Environment.getExternalStorageDirectory() + "/"
							+ mNewMergedFileName + ".mp3";
					getUploadDataTask uploadDataTask = new getUploadDataTask();
					dlog = ProgressDialog.show(this, "Uploading", "Please wait...",true, false);

					uploadDataTask.execute();
					imgViewTrash.setClickable(false);
					imgViewUpload.setClickable(false);


					/*	   Handler handler = new Handler(); 
   		    handler.postDelayed(new Runnable() { 
   		         public void run() {    
   		        	 //startPlay(currentFile);
   		         } 
   		    }, 3000);


   		 AlertDialog.Builder alert = new AlertDialog.Builder(this);
   		     alert.setTitle("Uploaded ");
  			alert.setMessage("Your file has been uploaded.");
  			alert.setCancelable(false);
  			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
  			{
  				public void onClick(DialogInterface dialog, int whichButton) 
  				{
  					//startActivity(new Intent(PlayerActivity.this, PlayerActivity.class));

  				}
  			});

  			alert.show();*/




				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	ProgressDialog dlog;

	public class getUploadDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				String path = Environment.getExternalStorageDirectory() + "/" + mNewMergedFileName + ".mp3";
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"https://www.charmsoffice.com/charms/mobileStudio/mobileStudioUpload.asp?username="
								+ Constants.USER_NAME + "&sid=" + Constants.USER_PASSWORD + "&action=save&filename="
								+ mNewMergedFileName + ".mp3");

				MultipartEntity reqEntry = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

				FileBody bin = new FileBody(new File(path));
				reqEntry.addPart("filename", bin);
				httppost.setEntity(reqEntry);
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity resEntity = response.getEntity();

				InputStream is = resEntity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

				StringBuilder sb = new StringBuilder();
				String line = null;

				while ((line = reader.readLine()) != null) {
					sb.append(line);
					Log.e("respose", "____________" + line);
				}

				is.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (dlog.isShowing())
				dlog.dismiss();

			Util.showOkDialog(PlayerActivity.this, "Uploaded", "Your file has been uploaded");

		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}

	public static boolean deleteDirectory(File path) {
		// TODO Auto-generated method stub
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	private class DownloadFile extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... sUrl) {
			try {
				URL url = new URL(sUrl[0]);
				URLConnection connection = url.openConnection();
				connection.connect();
				// this will be useful so that you can show a typical 0-100%
				// progress bar
				int fileLength = connection.getContentLength();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + sub
						+ ".mp3");

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			mProgressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			currentFile = Environment.getExternalStorageDirectory() + "/" + sub + ".mp3";
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onBackPressed() {		
		if (isCheck) {
			File file = new File(Environment.getExternalStorageDirectory() + "/" + mNewMergedFileName + ".mp3");
			file.delete();
		} else {
			File file = new File(Environment.getExternalStorageDirectory() + "/" + sub + ".mp3");
			file.delete();

			File fileMerge = new File(Environment.getExternalStorageDirectory() + "/" + "merged.mp3");
			fileMerge.delete();
		}
		finish();
		//super.onBackPressed();

		/*Intent goRecordingStudioActivity = new Intent(PlayerActivity.this, RecordingStudioActivity.class);
		startActivity(goRecordingStudioActivity);
		finish();*/

	}

	@Override
	public void onClick(View v) {
		if(v == tbAutoLock)
		{
			setAutoLock(tbAutoLock.isChecked());	
			saveToSharedPrefs(tbAutoLock.isChecked());
		}

	}

	private void setAutoLock(boolean isLock)
	{
		if(isLock)
		{			
			if(wakeLock.isHeld())
				wakeLock.release();		

		}else 
		{
			wakeLock.acquire();
		}		
		Log.i("Paayer", "Auto -Lock: "+isLock);
	}

	private volatile boolean mDrawing;
	private volatile int mDrawingCollided;
	double mRmsSmoothed; 
	double mAlpha = 0.7;//0.9 
	double mGain = 2500.0 / Math.pow(10.0, 90.0 / 20.0);
	double mOffsetdB = 10;
	
	@Override
	public void processAudioFrame(short[] audioFrame) 
	{
		if (!mDrawing) {
			mDrawing = true;
			// Compute the RMS value. (Note that this does not remove DC).
			double rms = 0;
			for (int i = 0; i < audioFrame.length; i++) {
				rms += audioFrame[i]*audioFrame[i];
			}
			rms = Math.sqrt(rms/audioFrame.length);
			mRmsSmoothed = mRmsSmoothed * mAlpha + (1 - mAlpha) * rms;
			final double rmsdB = 20.0 * Math.log10(mGain * mRmsSmoothed);

			mBarLevel.post(new Runnable() {
				@Override
				public void run() {
					// The bar has an input range of [0.0 ; 1.0] and 10 segments.
					// Each LED corresponds to 6 dB.
					mBarLevel.setLevel((mOffsetdB + rmsdB) / 60);

					//  DecimalFormat df = new DecimalFormat("##");
					//mdBTextView.setText(df.format(20 + rmsdB));

					//DecimalFormat df_fraction = new DecimalFormat("#");
					//int one_decimal = (int) (Math.round(Math.abs(rmsdB * 10))) % 10;
					//mdBFractionTextView.setText(Integer.toString(one_decimal));
					mDrawing = false;
				}
			});
		}else 
		{
			mDrawingCollided++;
			Log.v("Player Activity", "Level bar update collision, i.e. update took longer " +
					"than 20ms. Collision count" + Double.toString(mDrawingCollided));
		}

	}

	private boolean getAutoLock()
	{
		boolean isRemember = mSharedPrefference.getBoolean(PREFS_AUTO_LOCK,
				false);
		return isRemember;
	}

	private void saveToSharedPrefs(boolean value)
	{

		Editor editor = mSharedPrefference.edit();		
		editor.putBoolean(PREFS_AUTO_LOCK, value);
		editor.commit();
	}
}