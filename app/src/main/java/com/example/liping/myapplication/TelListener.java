package com.example.liping.myapplication;

import android.content.Context;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by liping on 12/30/2014.
 */
public class TelListener extends PhoneStateListener {
    private final Context ctx;

    public TelListener(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:/* 无任何状态 */
                        System.out.printf("IDEL");
                        setSpeakMode(true);
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:/* 接起电话 */
                        System.out.println("OFFHOOK");
                        setSpeakMode(true);
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:/* 电话进来 */
                        System.out.println("RINGING");
                        setSpeakMode(true);
                        break;
                }


    }

    private void setSpeakMode(boolean open){
        AudioManager audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        audioManager.setMode(AudioManager.MODE_IN_CALL);

        if(!audioManager.isSpeakerphoneOn() && open) {
            audioManager.setSpeakerphoneOn(true);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),AudioManager.STREAM_VOICE_CALL);
        }else if(audioManager.isSpeakerphoneOn() && !open){
            audioManager.setSpeakerphoneOn(false);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,currVolume, AudioManager.STREAM_VOICE_CALL);
        }
    }
}
