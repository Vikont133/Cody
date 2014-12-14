package com.example.cody;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class lockScreenReeiver extends BroadcastReceiver {

        public static boolean wasScreenOn = true;


        @Override
        public void onReceive(Context context, Intent intent) {

                // ���� ����� �������� �� ��������� ��� ��� �����
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                        wasScreenOn = false;
                        Intent intent11 = new Intent(context, MainActivity.class);
                        intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent11);
                }
        }
}
