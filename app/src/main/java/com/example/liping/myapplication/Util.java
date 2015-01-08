package com.example.liping.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liping on 12/31/2014.
 */
public class Util {
    public static void updateNumberList(SharedPreferences pref,final Activity act){
        Map<String, ?> phones = pref.getAll();
        ListView view = (ListView) act.findViewById(R.id.num_listView);

        Set entries = phones.entrySet();
        Iterator it = entries.iterator();
        List<String> texts = new ArrayList<String>();
        while(it.hasNext()){
            Map.Entry obj = (Map.Entry) it.next();
            String key = (String) obj.getKey();
            String value = (String) obj.getValue();

            String text = key+" : "+value;
            texts.add(text);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,android.R.layout.simple_list_item_1, texts.toArray(new String[0]));

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String value = ((TextView)v).getText().toString();
                String[] values = value.split(":");
                if(values.length < 2){
                    showError(act,act.getString(R.string.edit_phone_error));
                    return;
                }
                String k = values[0].trim();
                String va = values[1].trim();

                EditText phone =  findEditText(act,R.id.tx_phone);
                EditText name  =  findEditText(act,R.id.tx_username);
                phone.setText(k);
                name.setText(va);
            }
        });
        view.setAdapter(adapter);
    }

    public static void showError(Context ctx,String msg) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(ctx, msg, duration);
        toast.show();
    }

    public static EditText findEditText(Activity act,int rint){
        return (EditText) act.findViewById(rint);
    }
}
