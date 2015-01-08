package com.example.liping.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(intent.getAction().toString());
         if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
             context.startActivity(new Intent(context, MainActivity.class));
         }
    }
}