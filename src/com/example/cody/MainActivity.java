package com.example.cody;

import android.content.Context;

import java.io.IOException;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.cody.algorithm.MainHandler;


public class MainActivity extends Activity {
	private RecordAudioToShortArray record = null;
    public Context me;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setButtonHandlers();
        me = this;
	}

	private void setButtonHandlers() {
		findViewById(R.id.addlayoutbutton).setOnClickListener(btnClick);
		findViewById(R.id.checkloyaoutbutton).setOnClickListener(btnClick);
	}

	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
                case R.id.addlayoutbutton: {
                    setContentView(R.layout.add_user);
                    findViewById(R.id.startbutton).setOnClickListener(addViewListener);
                    findViewById(R.id.stopbutton).setOnClickListener(addViewListener);
                    break;
                }
                case R.id.checkloyaoutbutton: {
                    setContentView(R.layout.check_user);
                    findViewById(R.id.startbutton1).setOnClickListener(checkViewListener);
                    findViewById(R.id.stopbutton1).setOnClickListener(checkViewListener);
                    findViewById(R.id.backbutton).setOnClickListener(checkViewListener);
                    break;
                }
            }
		}
	};

    private View.OnClickListener addViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startbutton: {
                    record = new RecordAudioToShortArray();
                    record.startRecording();
                    break;
                }
                case R.id.stopbutton: {
                    try {
                        record.stopRecording();
                        String name = ((EditText) findViewById(R.id.editText)).getText().toString();
                        MainHandler.addUser(name, record.arrayShort);

                        setContentView(R.layout.activity_main);
                        findViewById(R.id.addlayoutbutton).setOnClickListener(btnClick);
                        findViewById(R.id.checkloyaoutbutton).setOnClickListener(btnClick);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private View.OnClickListener checkViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startbutton1: {
                    record = new RecordAudioToShortArray();
                    record.startRecording();
                    break;
                }
                case R.id.backbutton: {
                    setContentView(R.layout.activity_main);
                    findViewById(R.id.addlayoutbutton).setOnClickListener(btnClick);
                    findViewById(R.id.checkloyaoutbutton).setOnClickListener(btnClick);
                    break;
                }
                case R.id.stopbutton1: {
                    try {
                        record.stopRecording();
                        String name = MainHandler.checkVoice(record.arrayShort);
                        ((TextView) findViewById(R.id.textView)).setText(name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
}