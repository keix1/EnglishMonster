package com.io.keiichi.englishmonster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class SelectFragment extends DialogFragment {


    String worded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // fragment再生成抑止
        setRetainInstance(true);
    }

    public static SelectFragment newInstance(String word) {
        SelectFragment selFrag = new SelectFragment();
        Bundle bundle = new Bundle();
        bundle.putString("word",word);
        selFrag.setArguments(bundle);

        return selFrag;
    }



    private void jumpSearch(String word) {
        Uri uri = Uri.parse("http://ejje.weblio.jp/content/" + word);
        Intent i = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(i);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String word = getArguments().getString("word");

//        worded = getArguments().getString("words");
        String[] stAr = {"Search","modify","delete"};


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(stAr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        jumpSearch(word);
                        Toast.makeText(getActivity(),word+"を検索中",Toast.LENGTH_SHORT).show();

                        break;
                    case 1:
                        Toast.makeText(getActivity(),"未実装機能",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"未実装機能",Toast.LENGTH_SHORT).show();
                        break;

                }



            }

        });

        return builder.create();


    }

    @Override
    public void onPause(){
        super.onPause();


    }



}