package com.io.keiichi.englishmonster;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ScreenLockEnabledActivity extends Activity implements OnClickListener {
    ArrayList<String> engArr;
    ArrayList<String> jpnArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock);

        // Lock解除画面より手前に表示させる
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

//        Button btnRelease = (Button) findViewById(R.id.btn_release);
//        btnRelease.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Activityを終了することでLock解除画面に移る
//                finish();
//            }
//        });

        engArr = new ArrayList<String>();
        jpnArr = new ArrayList<String>();

        MonsterOpenHelper helper = new MonsterOpenHelper(this);

        final SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from monster order by random() limit 1";

        //データベースシャッフル，英語日本語アレイに入れ込み
        for(int maru=0; maru<4; maru++) {
            Cursor c = db.rawQuery(sql, null);
            c.moveToNext();
            engArr.add(c.getString(0));
            jpnArr.add(c.getString(1));
        }

        //TextViewアレイの作成
        List<TextView> kai = new ArrayList<TextView>();
        TextView kai1 = (TextView) findViewById(R.id.kai1);
        TextView kai2 = (TextView) findViewById(R.id.kai2);
        TextView kai3 = (TextView) findViewById(R.id.kai3);
        TextView kai4 = (TextView) findViewById(R.id.kai4);

        kai.add(kai1);
        kai.add(kai2);
        kai.add(kai3);
        kai.add(kai4);

        //0~3までの乱数　これを正解の番号とする　トップに表示する問題の英単語の訳の決定
        Random rnd = new Random();
        int seikai = rnd.nextInt(4);

        //問題をセット
        TextView toi = (TextView) findViewById(R.id.toi);
        toi.setText(engArr.get(seikai));

        //TextViewに設定
        int inin = 0;
        for(TextView tv : kai) {
            tv.setText(jpnArr.get(inin));
            inin++;
        }
    }

    //ここら
    public void onClick(View v){
        if(v.getId()==R.id.toi) seikaiShori(); //正解のとき
        else if(v.getId()==R.id.fin) this.finish(); //終了ぼたん
        else matigaiShori(v.getText().toString()); //その他は不正解
    }

    private void seikaiShori() {
        TextView kaitou = (TextView) findViewById(R.id.kaitou);
        kaitou.setText("正解！ハラショ〜！");
        kaitou.setTextColor(Color.RED);
    }

    private void matigaiShori(String mati) {
        TextView kaitou = (TextView) findViewById(R.id.kaitou);

        //数値のみに変換
        String str = "mati";
        int ret = Integer.parseInt(str.replaceAll("[^0-9]",""));

        kaitou.setText("ざんねん！違います。¥nその単語の意味は以下¥n"+jpnArr.get(ret-1));
        kaitou.setTextColor(Color.BLUE);
    }
}
