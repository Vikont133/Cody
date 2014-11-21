package com.example.cody;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import java.io.IOException;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.example.cody.algorithm.MainHandler;


public class MainActivity extends Activity {
	private RecordAudioToShortArray record = null;
    public Context me;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setButtonHandlers();
		enableButtons(false);
        me = this;

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
				AppLog.logString("Stop Recording");
				enableButtons(false);
				try {
					record.stopRecording();
                    MainHandler.checkVoice(record.arrayShort);
//                    MainHandler.main(null);
//                    LineGraph lineGraph = new LineGraph();
//                    Intent lineIntent = lineGraph.execute(me, MainHandler.getMelKreps(record.arrayShort));
//                    startActivity(lineIntent);
//                    double[] mel = MainHandler.getMelKreps(record.arrayShort);
//                    for (int i = 0; i < mel.length; i++) {
//                        System.out.println(mel[i] + " ");
//                    }
                } catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			}
		}
	};
}