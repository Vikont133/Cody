package com.example.cody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.example.cody.algorithm.MainHandler;


public class MainActivity extends Activity {
	private RecordAudioToShortArray record = null;

    private boolean isRecordingFragment = false;

    private ListView mainListView;

    private ArrayAdapter<String> mainMenuAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        initMainLayout();

		try {
            // There are was unreadable comment
			startService(new Intent(this, MyService.class));
            // There are was unreadable comment
			StateListener phoneStateListener = new StateListener();
			phoneStateListener.setButtonHandlers1();
			phoneStateListener.enableButtons1(true);
            // There are was unreadable comment
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            // There are was unreadable comment
			telephonyManager.listen(phoneStateListener,
					PhoneStateListener.LISTEN_CALL_STATE);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	class StateListener extends PhoneStateListener {

		private View.OnClickListener btnClick1 = new View.OnClickListener() {
			public void onClick(View v) {
			}
		};

		private void setButtonHandlers1() {

		}

		private void enableButtons1(boolean isRecording) {

		}
	}

	private void setButtonHandlers() {
		mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0: { // Add user
                        initialAddLayout();
                        break;
                    }

                    case 1: { // User list
                        initUserListLayout();
                        break;
                    }
                }

            }
        });
	}

    @Override
    public void onBackPressed() {
        initMainLayout();
    }
	private void enableButton(int id, boolean isEnable) {
		((Button) findViewById(id)).setEnabled(isEnable);
	}

	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
                case 1: {
                    setContentView(R.layout.add_user);
                    initialAddLayout();
                    break;
                }
                case 2: {
                    setContentView(R.layout.check_user);
                    initialCheckLayout();
                    break;
                }
            }
		}
	};

    private void initialCheckLayout(){
        /*findViewById(R.id.startbutton).setOnClickListener(checkViewListener);
        findViewById(R.id.startbutton).setEnabled(true);
        findViewById(R.id.stopbutton).setOnClickListener(checkViewListener);
        findViewById(R.id.stopbutton).setEnabled(false);
        findViewById(R.id.backbutton).setOnClickListener(checkViewListener);
        findViewById(R.id.backbutton).setEnabled(true);*/
    }

    private void initMainLayout() {
        setContentView(R.layout.activity_main);
        mainListView = (ListView) findViewById(R.id.main_list_view);
        mainMenuAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.main_menu_items));
        mainListView.setAdapter(mainMenuAdapter);

        setButtonHandlers();
    }

    private void initialAddLayout(){
        setContentView(R.layout.add_user);
        findViewById(R.id.startbutton).setOnClickListener(addViewListener);
        findViewById(R.id.newUserName).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                ((EditText) v).setText("");
            }
        });
    }

    private void initUserListLayout() {
        setContentView(R.layout.user_list_layout);
        final ListView userListView = (ListView) findViewById(R.id.user_list);
        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                MainHandler.getUserList());
        userListView.setAdapter(userAdapter);

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String userToDelete = userListView.getItemAtPosition(position).toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.are_you_sure_to_delete_user)
                        .setTitle(R.string.delete_user);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainHandler.removeUser(userToDelete);
                        initUserListLayout();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private View.OnClickListener addViewListener = new View.OnClickListener() {
        private int n = 0;
        private List<double[]> arr = new ArrayList<double[]>();

        @Override
        public void onClick(View v) {
            EditText usernameEdit = (EditText) findViewById(R.id.newUserName);
            String username = usernameEdit.getText().toString();
            if(username.length() < 1) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        getResources().getText(R.string.user_already_exist), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if(MainHandler.isUserExist(username)) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        getResources().getText(R.string.user_already_exist), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            usernameEdit.setEnabled(false);

            if (!isRecordingFragment) {
                record = new RecordAudioToShortArray();
                record.startRecording();
                isRecordingFragment = true;
                ((Button) findViewById(R.id.startbutton)).setText(R.string.stop_record);
            } else {
                try {
                    record.stopRecording();
                    arr.add(MainHandler.getMelKreps(record.arrayShort));
                    click(++n);
                    ((Button) findViewById(R.id.startbutton)).setText(R.string.start_record);
                    if (n == 10) {
                        initMainLayout();

                        Toast toast = Toast.makeText(getApplicationContext(),
                                getResources().getText(R.string.user_added), Toast.LENGTH_SHORT);
                        toast.show();

                        MainHandler.addUser(username,
                                arr);
                        arr = new ArrayList<double[]>();

                        n = 0;

                        return;
                    }

                    isRecordingFragment = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
            /*switch (v.getId()) {
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
            }*/
        }
    };
}