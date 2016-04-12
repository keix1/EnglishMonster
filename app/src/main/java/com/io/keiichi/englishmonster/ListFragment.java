package com.io.keiichi.englishmonster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends DialogFragment {

    String[] ada;
    static List<String> dataList = new ArrayList<String>();

    static ArrayAdapter<String> adapter;
    String tangoData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // fragment再生成抑止
        setRetainInstance(true);
    }

    public static ListFragment newInstance(int towh) {
        ListFragment lsFrag = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("which",towh);
        lsFrag.setArguments(bundle);

        return lsFrag;
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int which = getArguments().getInt("which");

        int i=0;
        ada = new String[26];
        for(char a='A'; a <='Z'; a++ ) {
            // リスト表示用のアラートダイアログ
            ada[i]= String.valueOf(a);
            i++;
        }


        MonsterOpenHelper helper = new MonsterOpenHelper(getActivity());
        final SQLiteDatabase db = helper.getWritableDatabase();

        String selectStr = "english LIKE ? || '%' ";
        String ss = ada[which];
        String[] selectArg = { ss };//s.toString() };

        Cursor cur = db.query("tango", null, selectStr, selectArg, null, null, null);

        while(cur.moveToNext()) {
            tangoData = String.format("%s = %s",cur.getString(0),cur.getString(1));
            dataList.add(0,tangoData);
        }
        String[] stAr = (String[])dataList.toArray(new String[0]);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(stAr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {



            }

        });

        return builder.create();


    }

    @Override
    public void onPause(){
        super.onPause();
        dataList.clear();

    }



}