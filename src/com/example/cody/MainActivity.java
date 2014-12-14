package com.example.cody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.example.cody.algorithm.MainHandler;


public class MainActivity extends Activity {
	private RecordAudioToShortArray record = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setButtonHandlers();
		
		try {
			// инициализация рисивера
			startService(new Intent(this, MyService.class));
			// стартуем отслеживание состояния телефона
			StateListener phoneStateListener = new StateListener();
			phoneStateListener.setButtonHandlers1();
			phoneStateListener.enableButtons1(true);
			// узнаем все сервисы которые есть
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			// слушаем когда телефон уходит в сон и включаем нашего блокировщика
			telephonyManager.listen(phoneStateListener,
					PhoneStateListener.LISTEN_CALL_STATE);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	class StateListener extends PhoneStateListener {

		private View.OnClickListener btnClick1 = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.unlock: {
					AppLog.logString("Unlock");
					finish();
				}
					break;
				}
			}
		};
		
		private void setButtonHandlers1() {
			((Button) findViewById(R.id.unlock)).setOnClickListener(btnClick1);
		}

		private void enableButtons1(boolean isRecording) {
			enableButton(R.id.unlock, isRecording);
		}
	}

	private void setButtonHandlers() {
		findViewById(R.id.addlayoutbutton).setOnClickListener(btnClick);
		findViewById(R.id.checkloyaoutbutton).setOnClickListener(btnClick);
	}
	
	private void enableButton(int id, boolean isEnable) {
		((Button) findViewById(id)).setEnabled(isEnable);
	}

	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
                case R.id.addlayoutbutton: {
                    setContentView(R.layout.add_user);
                    initialAddLayout();
                    break;
                }
                case R.id.checkloyaoutbutton: {
                    setContentView(R.layout.check_user);
                    initialCheckLayout();
                    break;
                }
            }
		}
	};

    private void initialCheckLayout(){
        findViewById(R.id.startbutton).setOnClickListener(checkViewListener);
        findViewById(R.id.startbutton).setEnabled(true);
        findViewById(R.id.stopbutton).setOnClickListener(checkViewListener);
        findViewById(R.id.stopbutton).setEnabled(false);
        findViewById(R.id.backbutton).setOnClickListener(checkViewListener);
        findViewById(R.id.backbutton).setEnabled(true);
    }

    private void initialAddLayout(){
        findViewById(R.id.startbutton).setOnClickListener(addViewListener);
        findViewById(R.id.startbutton).setEnabled(false);
        findViewById(R.id.stopbutton).setOnClickListener(addViewListener);
        findViewById(R.id.stopbutton).setEnabled(false);
        findViewById(R.id.confirmbutton).setOnClickListener(addViewListener);
        findViewById(R.id.confirmbutton).setEnabled(true);
    }

    private View.OnClickListener addViewListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startbutton: {
                    findViewById(R.id.startbutton).setEnabled(false);
                    record = new RecordAudioToShortArray();
                    record.startRecording();
                    findViewById(R.id.stopbutton).setEnabled(true);
                    break;
                }
                case R.id.stopbutton: {
                    try {
                        findViewById(R.id.stopbutton).setEnabled(false);
                        record.stopRecording();
                        arr.add(MainHandler.getMelKreps(record.arrayShort));
                        click(++n);
                        if (n == 10) {
                            setContentView(R.layout.activity_main);
                            setButtonHandlers();
                            MainHandler.addUser(name, arr);
                            arr = new ArrayList<double[]>();n = 0;
                            return;
                        }
                        findViewById(R.id.startbutton).setEnabled(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case R.id.confirmbutton: {
                    name = ((EditText) findViewById(R.id.editText)).getText().toString();
                    findViewById(R.id.stopbutton).setEnabled(false);
                    findViewById(R.id.startbutton).setEnabled(true);
                    findViewById(R.id.confirmbutton).setEnabled(false);
                    findViewById(R.id.editText).setEnabled(false);
                }
            }
        }

        private int n = 0;
        private List<double[]> arr = new ArrayList<double[]>();
        private String name;
    };

    private void click(int n){
        switch (n){
            case 1:  {((RadioButton)findViewById(R.id.radioButton1)).setChecked(true); break;}
            case 2:  {((RadioButton)findViewById(R.id.radioButton2)).setChecked(true); break;}
            case 3:  {((RadioButton)findViewById(R.id.radioButton3)).setChecked(true); break;}
            case 4:  {((RadioButton)findViewById(R.id.radioButton4)).setChecked(true); break;}
            case 5:  {((RadioButton)findViewById(R.id.radioButton5)).setChecked(true); break;}
            case 6:  {((RadioButton)findViewById(R.id.radioButton6)).setChecked(true); break;}
            case 7:  {((RadioButton)findViewById(R.id.radioButton7)).setChecked(true); break;}
            case 8:  {((RadioButton)findViewById(R.id.radioButton8)).setChecked(true); break;}
            case 9:  {((RadioButton)findViewById(R.id.radioButton9)).setChecked(true); break;}
            case 10: {((RadioButton)findViewById(R.id.radioButton10)).setChecked(true); break;}
        }
    }

    private View.OnClickListener checkViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startbutton: {
                    record = new RecordAudioToShortArray();
                    record.startRecording();
                    findViewById(R.id.startbutton).setEnabled(false);
                    findViewById(R.id.stopbutton).setEnabled(true);
                    break;
                }
                case R.id.backbutton: {
                    setContentView(R.layout.activity_main);
                    setButtonHandlers();
                    break;
                }
                case R.id.stopbutton: {
                    try {
                        record.stopRecording();
                        String name = MainHandler.checkVoice(record.arrayShort);
                        ((TextView) findViewById(R.id.textView)).setText(name);
                        findViewById(R.id.startbutton).setEnabled(true);
                        findViewById(R.id.stopbutton).setEnabled(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
}