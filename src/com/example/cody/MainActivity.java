package com.example.cody;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private RecordAudioToShortArray record = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setButtonHandlers();
		enableButtons(false);
		
		record = new RecordAudioToShortArray();
	}

	private void setButtonHandlers() {
		((Button) findViewById(R.id.recordbutton)).setOnClickListener(btnClick);
		((Button) findViewById(R.id.stopbutton)).setOnClickListener(btnClick);
	}

	private void enableButton(int id, boolean isEnable) {
		((Button) findViewById(id)).setEnabled(isEnable);
	}

	private void enableButtons(boolean isRecording) {
		enableButton(R.id.recordbutton, !isRecording);
		enableButton(R.id.stopbutton, isRecording);
	}

	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.recordbutton: {
				AppLog.logString("Start Recording");

				enableButtons(true);
				record.startRecording();

				break;
			}
			case R.id.stopbutton: {
				AppLog.logString("Start Recording");

				enableButtons(false);
				try {
					record.stopRecording();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}

			}
		}
	};
}