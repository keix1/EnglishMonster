package com.io.keiichi.englishmonster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class DiaHyoujiFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


//        LayoutInflater i
//                = (LayoutInflater) getActivity()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View content = i.inflate(R.layout.diahyouji_layout, null);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme);

        builder.setTitle("モンスターの育て方");
        builder.setMessage("ENG:\n英単語を入力してね！\n\n"+
                "JAP:\n日本語の意味を入力してね！\n\n" +
                "きみのモンスターは英単語の数が多いほどたくさん成長するよ！\n\n"+
                "登録した英単語を長押しすると削除できるよ！\n\n"+
                "赤いボタンかエンターキーで入力できるよ！\n\n"+
                "日本語も英語も入力すると自動的に登録した英単語を検索するよ！\n" +
                "\nAtoZ:\nAからZまでのアルファベットを選択すると登録した英単語が表示される\n\n"+
                "Today's Words:\n今日食べさせた英単語の数\n\n"+
                "Week's Words:\n日曜日から今日まで食べさせた英単語数\n\n"+
                "Total's Words:\n今日まで食べさせた全部の英単語の数\n\n");
//        builder.setView(content);


        return builder.create();
    }
//
//    @Override
//    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b)
//    {
//        View content = i.inflate(R.layout.diahyouji_layout, null);
//        return content;
//    }
}