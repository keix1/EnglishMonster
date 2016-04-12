package com.io.keiichi.englishmonster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AtoZFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // fragment再生成抑止
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        int i=0;
        CharSequence[] ada = new CharSequence[26];
        for(char a='A'; a <='Z'; a++ ) {
            // リスト表示用のアラートダイアログ
            ada[i]= String.valueOf(a);
            i++;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(ada, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //ListFragmentへ飛ばしてAtoZの単語表示
                ListFragment newFragment = new ListFragment();
                newFragment = ListFragment.newInstance(which);
                newFragment.show(getFragmentManager(), "contact_us");

            }
        });
        return builder.create();
    }
}