package com.example.liping.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Map;

import static com.example.liping.myapplication.Util.showError;
/**
 * Created by liping on 12/29/2014.
 */
public class AddNewNumberActivity extends Activity{
    private static final int MAX_NUM_SIZE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            this.getFragmentManager().beginTransaction()
                    .add(R.id.container, new PhoneAddFragment())
                    .commit();
        }

    }
    /**
     * Add phone number and display name
     */
    public static class PhoneAddFragment extends Fragment {

        public PhoneAddFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add_number, container, false);

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            Activity act = this.getActivity();
            SharedPreferences pref = act.getSharedPreferences(this.getString(R.string.app_id), Context.MODE_PRIVATE);
            Util.updateNumberList(pref, act);
        }
    }

    public void resetNewNumberField(View v){
        emptyEditText(R.id.tx_username);
        emptyEditText(R.id.tx_phone);
    }

    private void emptyEditText(int rint) {
        Util.findEditText(this,rint).setText("");
    }

    public void saveNewNumber(View v){
        String username = getEditTextValue(R.id.tx_username);
        String phone = getEditTextValue(R.id.tx_phone);

        UserData userdata = new UserData(phone,username);
        //save
        boolean redirect = saveData(userdata);
        if(redirect){
            Intent intent = new Intent(this,MainActivity.class);
            this.startActivity(intent);
        }
    }

    private String getEditTextValue(int rint) {
        EditText editText = Util.findEditText(this, rint);
        return editText.getText().toString();
    }

    private boolean saveData(UserData data) {
        SharedPreferences sharedPref = this.getSharedPreferences();
        Map<String, ?> phones = this.getAllPhoneNumber(sharedPref);
        int size = phones.size();
        if(size > MAX_NUM_SIZE && !sharedPref.contains(data.getPhone())){
            //show error
            showError(this,getString(R.string.dialog_message));
            return false;
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(data.getPhone(),data.getUsername());
        editor.commit();

        updateNumberList(sharedPref);
        return true;
    }

    private void updateNumberList(SharedPreferences sharedPref) {
        Util.updateNumberList(sharedPref,this);
    }

    public void deletePhone(View v){
        String key = this.getEditTextValue(R.id.tx_phone);
        if(key == null || "".equals(key)){
            return;
        }
        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();

        updateNumberList(pref);
    }

    private SharedPreferences getSharedPreferences(){
        return getSharedPreferences(this.getString(R.string.app_id), Context.MODE_PRIVATE);
    }
    private Map<String, ?> getAllPhoneNumber(SharedPreferences pref) {
        return pref.getAll();
    }

    private class UserData {
        private String phone;
        private String username;

        public UserData(String str_phone, String str_username) {
            this.phone = str_phone;
            this.username = str_username;
        }

        public String getPhone() {
            return phone;
        }

        public String getUsername() {
            return username;
        }
    }
}
