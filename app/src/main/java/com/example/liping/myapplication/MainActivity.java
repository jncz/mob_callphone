package com.example.liping.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import java.util.Map;
import java.util.Set;
import java.util.Stack;


public class MainActivity extends Activity {
    private static final int btn_w = 300;
    private static final int btn_h = 300;

    private int btn_size_h = 1;//horizontal
    private int btn_size_v = 1;//vertical
    private int screen_w;
    private int screen_h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("on create");
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        screen_w = dm.widthPixels;
        screen_h = dm.heightPixels;

        getBtnNumber(screen_w,screen_h);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            this.getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    private int getBtnNumber(int w, int h) {
        btn_size_h = (int) Math.floor(w / btn_w);
        btn_size_v = (int) Math.floor(h / btn_h);

        return (int) (btn_size_h * btn_size_v);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setSpeakerphoneOn(false);

        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        TelListener listener = new TelListener(this) ;
        telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        System.out.println("post create");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("save state");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        System.out.println("post resume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("on stop");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.out.println("low mem");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("on destroy");
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        System.out.println("user leave hint");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("pause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("on Restart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("on Start");

        applyBtnLayout();

        System.out.println("start done");
    }

    private void applyBtnLayout() {
        GridLayout layout = (GridLayout) this.findViewById(R.id.GridLayout);
        Stack<Button> btns = createBtns();

        boolean empty = btns.empty();
        if(empty){
           return;
        }
        int size = btns.size();
        for (int i=0;i<size;i++) {
            Button btn = btns.pop();
            GridLayout.Spec row = GridLayout.spec(i, 1);
            GridLayout.Spec col = GridLayout.spec(0, 1);
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, col);
            gridLayoutParam.width = (int) (screen_w * 0.9);
            gridLayoutParam.height = (int) (screen_h * 0.9 / size);

            layout.addView(btn, gridLayoutParam);
        }
    }

    private Stack<Button> createBtns() {
        SharedPreferences pref = this.getSharedPreferences(this.getString(R.string.app_id), Context.MODE_PRIVATE);
        Map<String, ?> all = pref.getAll();

        Set<String> keys = all.keySet();

        Stack<Button> stack = new Stack<Button>();
        for (String key:keys){
            Button btn = createBtn(key, (String) all.get(key));
            stack.push(btn);
        }
        return stack;
    }

    private Button createBtn(final String key, String s) {
        Button btn = new Button(this);
        btn.setText(s);
        btn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("clicked,call number " + key);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + key.trim()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                am.setMode(AudioManager.MODE_IN_CALL);
//                am.setSpeakerphoneOn(true);
//                am.setMicrophoneMute(true);

                startActivity(intent);
//                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                am.setMode(AudioManager.MODE_IN_CALL);
//                am.setSpeakerphoneOn(true);
//                am.setMicrophoneMute(true);

//                System.out.println("on: "+am.isSpeakerphoneOn());
            }
        });
        return btn;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("on Restore");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openSetting();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSetting() {
        Intent intent = new Intent(this,AddNewNumberActivity.class);

        this.startActivity(intent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


}
