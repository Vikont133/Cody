package com.example.cody;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class LockScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {

        // There are was unreadable comment
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            wasScreenOn = false;
            Intent intent11 = new Intent(context, LockScreenActivity.class);
            intent11.addCategory(Intent.ACTION_MAIN);

            intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent11);
        }
    }
}
