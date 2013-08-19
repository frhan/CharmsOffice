package com.charmsoffice.mobilestudio.exception;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.charmsoffice.mobilestudio.data.Constants;
import com.charmsoffice.mobilestudio.util.RecordStudio;

public class RecorderExceptionMessage {
	public static void getExceptionMessage(RecordStudio mRecMicToMp3,
			final TextView statusTextView) {
		mRecMicToMp3.setHandle(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case RecordStudio.MSG_REC_STARTED:
					statusTextView.setText(Constants.RECORDING_STARTED);
					break;
				case RecordStudio.MSG_REC_STOPPED:
					statusTextView.setText(Constants.RECORDING_STOPPED);
					break;
				case RecordStudio.MSG_ERROR_GET_MIN_BUFFERSIZE:
					statusTextView.setText(Constants.TERMINAL_SUPPORT_FAILED);
					break;
				case RecordStudio.MSG_ERROR_CREATE_FILE:
					statusTextView.setText(Constants.FILE_GENERATION_FAILED);
					break;
				case RecordStudio.MSG_ERROR_REC_START:
					statusTextView.setText(Constants.RECORDING_STARTED_FAILED);
					break;
				case RecordStudio.MSG_ERROR_AUDIO_RECORD:
					statusTextView.setText(Constants.RECORDING_FAILED);
					break;
				case RecordStudio.MSG_ERROR_AUDIO_ENCODE:
					statusTextView.setText(Constants.FAILED_ENCODE);
					break;
				case RecordStudio.MSG_ERROR_WRITE_FILE:
					statusTextView.setText(Constants.FAILED_WRITE);
					break;
				case RecordStudio.MSG_ERROR_CLOSE_FILE:
					statusTextView.setText(Constants.FILECLOSING_FAILED);
					break;
				default:
					break;
				}
			}
		});
	}
}
