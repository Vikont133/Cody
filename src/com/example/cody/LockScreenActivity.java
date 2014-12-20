package com.example.cody;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cody.algorithm.MainHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class LockScreenActivity extends Activity {
    private RecordAudioToShortArray record = null;

    private ImageButton checkUserButton;
    private TextView hintText;

    StateListener sl;
    private static int click = 0;
    View mDecorView;
    boolean currentFocus;
    boolean isPaused;
    Handler collapseNotificationHandler;

    private void initLayout() {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set background same with system
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        wallpaperDrawable.setAlpha(70);

        setContentView(R.layout.lock_screen);
        ImageView background = (ImageView) getWindow()
                .getDecorView().findViewById(R.id.lock_screen_background);
        background.setImageDrawable(wallpaperDrawable);

        checkUserButton = (ImageButton) findViewById(R.id.checkUserButton);
        hintText = (TextView) findViewById(R.id.hint_view);

        checkUserButton.setOnClickListener(onCheckUserButtonClick);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLayout();

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

    @Override
    public void onBackPressed() {
        // Dummy
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

    private View.OnClickListener onCheckUserButtonClick = new View.OnClickListener() {
        private void startRecord() {
            record = new RecordAudioToShortArray();
            record.startRecording();
        }

        private void stopRecord() {
            try {
                record.stopRecording();
                String name = MainHandler.checkVoice(record.arrayShort);
                if(name == null) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getResources().getText(R.string.unknown_user), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                finish();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            if (v.isFocused()) {
                v.setFocusable(false);
                v.setFocusableInTouchMode(false);

                hintText.setText(R.string.greeting_string);

                stopRecord();

            } else {
                v.setFocusable(false);
                v.setFocusableInTouchMode(true);
                v.requestFocus();

                hintText.setText(R.string.record_in_action);

                startRecord();
            }
        }
    };

    public void onWindowFocusChanged(boolean hasFocus) {

        currentFocus = hasFocus;

        if (!hasFocus) {

            // Method that handles loss of window focus
            collapseNow();
        }
    }

    public void collapseNow() {

        // Initialize 'collapseNotificationHandler'
        if (collapseNotificationHandler == null) {
            collapseNotificationHandler = new Handler();
        }

        // If window focus has been lost && activity is not in a paused state
        // Its a valid check because showing of notification panel
        // steals the focus from current activity's window, but does not
        // 'pause' the activity
        if (!currentFocus && !isPaused) {

            // Post a Runnable with some delay - currently set to 300 ms
            collapseNotificationHandler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    // Use reflection to trigger a method from 'StatusBarManager'

                    Object statusBarService = getSystemService("statusbar");
                    Class<?> statusBarManager = null;

                    try {
                        statusBarManager = Class.forName("android.app.StatusBarManager");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    Method collapseStatusBar = null;

                    try {

                        // Prior to API 17, the method to call is 'collapse()'
                        // API 17 onwards, the method to call is `collapsePanels()`

                        if (Build.VERSION.SDK_INT > 16) {
                            collapseStatusBar = statusBarManager .getMethod("collapsePanels");
                        } else {
                            collapseStatusBar = statusBarManager .getMethod("collapse");
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    collapseStatusBar.setAccessible(true);

                    try {
                        collapseStatusBar.invoke(statusBarService);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    // Check if the window focus has been returned
                    // If it hasn't been returned, post this Runnable again
                    // Currently, the delay is 100 ms. You can change this
                    // value to suit your needs.
                    if (!currentFocus && !isPaused) {
                        collapseNotificationHandler.postDelayed(this, 100L);
                    }

                }
            }, 50L);
        }
    }
}