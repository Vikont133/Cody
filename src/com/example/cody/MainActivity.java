package com.example.cody;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    private static final int RECORDER_SAMPLERATE = 16000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;
    private int bufferSize = 2048;
    private Thread recordingThread = null;
    private boolean isRecording = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);;

        AppLog.logString("start message");

        setButtonHandlers();
        enableButtons(false);
    }

    private void setButtonHandlers() {
        findViewById(R.id.recordbutton).setOnClickListener(btnClick);
        findViewById(R.id.stopbutton).setOnClickListener(btnClick);
    }

    private void enableButton(int id,boolean isEnable){
        findViewById(id).setEnabled(isEnable);
    }

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.recordbutton,!isRecording);
        enableButton(R.id.stopbutton,isRecording);
    }

    private void startRecording(){
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize);

        int i = recorder.getState();
        if(i==1)
            recorder.startRecording();

        isRecording = true;

        recordingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                readBuffer();
            }
        },"AudioRecorder Thread");

        recordingThread.start();
    }

    private void readBuffer(){
        short data[] = new short[bufferSize];

        while(isRecording){
            System.out.println(recorder.read(data, 0, bufferSize));
        }
    }

    private void stopRecording(){
        if(null != recorder){
            isRecording = false;

            int i = recorder.getState();
            if(i==1) {
                recorder.stop();
            }
            recorder.release();

            recorder = null;
            recordingThread = null;
        }
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.recordbutton:{
                    AppLog.logString("Start Recording");

                    enableButtons(true);
                    startRecording();

                    break;
                }
                case R.id.stopbutton:{
                    AppLog.logString("Stop Recording");

                    enableButtons(false);
                    stopRecording();

                    break;
                }
            }
        }
    };
}