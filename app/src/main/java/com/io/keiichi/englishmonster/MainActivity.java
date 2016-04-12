package com.io.keiichi.englishmonster;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,View.OnClickListener, TextWatcher {



    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;



//----Monster---
    ListView listView;
    Button saveButton;
    Button engBtn,jpBtn;

    HashMap<Integer,Integer> exMap;
    private int localLevel;
    private int localExp;
    private int nowLevel;
    private int nowExp;
    private int nextExp;
    private int toNextExp;
    private Handler mHandler = new Handler();
    private Runnable updates;
    private Runnable updateTarget;
    static List<String> dataList = new ArrayList<String>();
    static List<Character> diewList = new ArrayList<Character>();
    static ArrayAdapter<String> adapter;
    String tangoData;
    String db_file;
    final String ENG_PATH = "/storage/sdcard0/ENGLISH_MONSTERS";
    //    TextView todayEx = (TextView) findViewByd(id.todayEX);
    //    TextView untilEx = (TextView) findViewById(id.nowEX);
    //int todayEX;
    //int nowEX;
    static boolean first=true;

    String inputtingEng;

    //フローティング用
    //    private FrameLayout frameLayout01;
    private FrameLayout frameLayout01;
    private ImageView target;
//    private Button trash;

    private int targetLocalX;
    private int targetLocalY;

    private int screenX;
    private int screenY;
    //---------
//----Monster---


    private void listReload(Cursor c) {
        adapter.clear();
        listView.setAdapter(adapter);

        while (c.moveToNext()) {
            tangoData = String.format("%s = %s", c.getString(0), c.getString(1));
            dataList.add(0, tangoData);
            adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    dataList);
            listView.setAdapter(adapter);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        //データベース取得
        MonsterOpenHelper helper = new MonsterOpenHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();
       
//        //経験値管理用配列作成
//        double ex=60.0;
//        exMap = new HashMap<Integer,Integer>();
//        for(int i=1; i<991; i++) {
//            exMap.put(i,(int)ex);
//            //           System.out.println("["+i+"] = "+ (int)ex + ";");
//            ex=ex+ex*0.006;
//        }

        //経験値管理用配列作成
        double ex=0;
        exMap = new HashMap<Integer,Integer>();
        exMap.put(1,0);
        for(int i=2; i<300; i++) {
            ex=ex+i*10;
            exMap.put(i,(int)ex);
            //           System.out.println("["+i+"] = "+ (int)ex + ";");

        }



        Cursor cu = db.query("tango", new String[] {"SUM(LENGTH(english))"}, null, null, null, null, null);
        cu.moveToNext();
        nowExp = cu.getInt(0);



//        db.execSQL("UPDATE monster SET exp = ? WHERE name = Monster);",new Object[]{nowExp});
//        int exOr = db.update(String table, ContentValues values, String whereClause, String[] whereArgs)
//        update()メソッド：テーブルにデータを更新します。
//        戻り値は正常に更新でき場合が更新したレコード数が戻り、失敗した場合は-1が戻ります。
//        引数tableにはテーブル名を指定します。
//        引数valuesには更新レコードの列名と値がマッピングされたContentValuesインスタンスを設定します。
//        引数whereClauseには更新対象レコードを検索するための条件を指定します。
//        引数whereArgsにはwhereClauseにパラメータ（？で指定）が含まれる場合に置き変わる値を指定します。不要な場合はnullを指定します。

        //nowExpのアップデート，それをデータベースに入れてやる
        ContentValues cValues = new ContentValues();
        String whereClause = "name = 'Monster'";
        String[] whereArgs = {"Monster"};
        cValues.put("exp",nowExp);//nowExp);

        try {
            int exOr = db.update("monster", cValues, whereClause, null);
        } catch(Exception e) {
//            db.execSQL("INSERT INTO monster(name,level,exp,zokusei) VALUES('Monster',1,0,'nothing');");
            Log.d("insert","update失敗");
        }


        //localLevelの設定
        Cursor locur = db.query("monster",new String[] {"level","exp","nextexp"},null,null,null,null,null);
        locur.moveToNext();
        nowLevel = locur.getInt(0);
        //nextExp = locur.getInt(2);
        //localExp = locur.getInt(1);

//無駄な処理，，データベースからレベルもってきて処理させる，ここは消す
//        if(exMap.containskey(nowExp) {
//            localLevel = exMap.get(nowExp);
//        } else {
//            while(!exMap.containsKey(nowExp)) {
//                nowExp =
//               if( exMap.containsKey(nowExp)) {
//
//            }
//        }

  //      int mokuhyou = 500;
           //Toeic準拠レベルの場合は目標までのExpをnextExpとする
//        int now= nowLevel;
//        while(nowExp > exMap.get(nowLevel)) {
//            nextExp = exMap.get(now);
//            now = now +1;
//            }

//１レベルずつの場合はこちらを使う
//            nextExp = exMap.get(nowLevel+1);

//        int now =0;
//        while(nowExp > exMap.get(nowLevel+now)) {
//            nextExp = exMap.get(nowLevel+now);
//            now=now+10;
//        }
//        nextExp = exMap.get(nowLevel+now);
        //toNextExp = nextExp - nowExp;


//        makeToasty("現在のレベルは"+nowLevel+"です．TOEIC"+mokuhyou+"点まであと"+toNextExp+"EXP必要");


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        File dir = new File(ENG_PATH);
        if(!dir.exists()){
            boolean result = dir.mkdirs();
            if(result){
                System.out.println("Success");
            }
            else{
                System.out.println("not Success...");
            }
        }




//----Monster------
        //フローティングキャラクターのための定義
        frameLayout01 = (FrameLayout)findViewById(R.id.FrameLayout01);
        target = (ImageView)findViewById(R.id.ImageView01);
//        trash = (Button)findViewById(R.id.trash);
//        trash.setOnClickListener(this);
        //-------------------
        findViews();
        setListeners();
        setAdapters();
        final EditText engField = (EditText)findViewById(R.id.english);
        final EditText jpField = (EditText)findViewById(R.id.japanese);
        engField.addTextChangedListener(this);
        jpField.addTextChangedListener(this);

       //db.execSQL("ALTER TABLE tango ADD COLUMN date[text];");
        db_file = db.getPath();
        Cursor cur = db.query("tango",new String[] {"english","japanese"},null,null,null,null,null);

        while(cur.moveToNext()) {
            tangoData = String.format("%s = %s",cur.getString(0),cur.getString(1));
            dataList.add(0,tangoData);
            adapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    dataList);
            listView.setAdapter(adapter);
        }
        //        cur.close();
        //        db.close();
        //
        updates = new Runnable() {
            public void run() {
                engField.requestFocus();
            }
        };

        jpField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //EnterKeyが押されたかを判定
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_ENTER) {

                    //                    //ソフトキーボードを閉じる
                    //                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    //                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //検索処理的な何か

                    //                    if(!engField.getText().toString().equals("") && !jpField.getText().toString().equals("")) addItem();
                    //                    else {
                    //                        makeToasty("空白は食べれない");
                    //                        //Toast.makeText(this,"空白は食べれない", Toast.LENGTH_SHORT).show();
                    //                    }
                    checkItems();
                    mHandler.postDelayed(updates, 101);

                    return true;
                }


                return false;
            }
        });


//データベース取得




        //項目削除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MonsterOpenHelper helperlist = new MonsterOpenHelper(getApplicationContext());
                final SQLiteDatabase dblist = helperlist.getWritableDatabase();

                ListView list = (ListView) parent;
                String item = (String) list.getItemAtPosition(position);
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) list.getAdapter();
                int eq = item.indexOf("=");
                String subEn = item.substring(0,eq).trim();
                String subJp = item.substring(eq+1,item.length()).trim();
//                String[] itemSp = item.split(" ",0);
                // 項目を削除
                adapter.remove(item);




//                String selectStr = "english LIKE ? || '%' ";// OR japanese LIKE ''%'|| ? ||'%'' )";

//makeToastyLong(subEn);
//                makeToastyLong(subJp);

//                Cursor cucu = dblist.query("tango", new String[]{"english","japanese"},"english = ? AND japanese = ?", new String[]{/*subEn,subJp*/itemSp[0],itemSp[2]},null,null,null);
                Cursor cucu = dblist.query("tango", new String[]{"english"},"english = ? AND japanese = ?", new String[]{subEn,subJp},null,null,null);

                cucu.moveToNext();
                String delTango = cucu.getString(0);
                exDown(delTango.length());
//                dblist.delete("tango", "english = ? AND japanese = ?", new String[]{itemSp[0],itemSp[2]});
                dblist.delete("tango", "english = ? AND japanese = ?", new String[]{subEn,subJp});
                // db.execSQL("DELETE FROM tango WHERE english = "+itemSp[0].toString()+";");
                //ここに処理を書く


                dblist.close();

                exUpdate();

                return true;
            }
        });

        //項目削除
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                MonsterOpenHelper helperlist = new MonsterOpenHelper(getApplicationContext());
                                                final SQLiteDatabase dblist = helperlist.getWritableDatabase();

                                                ListView list = (ListView) parent;
                                                String item = (String) list.getItemAtPosition(position);
                                                ArrayAdapter<String> adapter = (ArrayAdapter<String>) list.getAdapter();
                                                int eq = item.indexOf("=");
                                                String subEn = item.substring(0,eq).trim();
                                                String subJp = item.substring(eq+1,item.length()).trim();
                                                SelectFragment selectFragment = new SelectFragment();
//                                                makeToasty(subEn);
                                                selectFragment = SelectFragment.newInstance(subEn);
                                                selectFragment.show(getFragmentManager(), "contact_us");

                                            }
                                        });


        target.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //                          int x = (int) event.getRawX();
                //                          int y = (int) event.getRawY();

                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(10);

                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                      //  Log.i("childcount", String.valueOf(frameLayout01.getChildCount()));
                        targetLocalX = target.getLeft();
                        targetLocalY = target.getTop();

                        screenX = x;
                        screenY = y;

                        break;

                    case MotionEvent.ACTION_MOVE:
                       // Log.i("framecount", String.valueOf(frameLayout01.getChildCount()));

                        int diffX = screenX - x;
                        int diffY = screenY - y;

                        targetLocalX -= diffX;
                        targetLocalY -= diffY;

                        target.layout(targetLocalX,
                                targetLocalY,
                                targetLocalX + target.getWidth(),
                                targetLocalY + target.getHeight());

                        screenX = x;
                        screenY = y;

                        break;

                    case MotionEvent.ACTION_UP:
                        //Log.i("childcount", String.valueOf(frameLayout01.getChildCount()));
//                        int trashLeft = trash.getLeft() + trash.getWidth() / 2;
//                        int trashTop = trash.getTop() + trash.getHeight() / 2;
                        int targetRight = target.getLeft() + target.getWidth();
                        int targetBottom = target.getTop() + target.getHeight();

//                        if (targetRight > trashLeft && targetBottom > trashTop) {
//                           frameLayout01.removeView(target);
//                        }

                        Log.i("target", "xx=" + String.valueOf(targetLocalX) + ", yy=" + String.valueOf(targetLocalY));
                        break;
                }
                return true;
            }
        });


        
        engField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // EditTextのフォーカスが外れた場合
                if (hasFocus) {
                    //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    //imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    
                    // ソフトキーボードを表示にする
                    imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                    
                }
                else {
                    //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    //imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    
                    // ソフトキーボードを非表示にする
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);

                }
            }
        });

        jpField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // EditTextフォーカスしたとき
                if (hasFocus) {
                    //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    //imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    
                    // ソフトキーボードを表示にする
                    imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
                    
                }
                else {
                    //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    //imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    
                    // ソフトキーボードを非表示にする
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);


                }
            }
        
        });

//        jpField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // EditTextのフォーカスが外れた場合
//                if (hasFocus == false) {
//                    // ソフトキーボードを非表示にする
//                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//            }
//        });

        db.close();

        //経験値 単語数 更新処理
        exUpdate();
//----Monster------

    }

    //レベルダウン 経験値
    private void exDown(int tangosu) {



        if(nowExp-tangosu >= 0) {
            nowExp = nowExp - tangosu;


            //レベルダウン
            if (nowExp < exMap.get(nowLevel)) {//nowExp > nextExp) {
                //nextExp = exMap.get(nowLevel + 1);
                if (nowLevel != 1) {
                   while (nowExp < exMap.get(nowLevel)) {
                        nowLevel = nowLevel - 1;
                   }
                }
                //            次のレベルの経験値を算出
                nextExp = exMap.get(nowLevel + 1);
                //次のレベル”まで”の経験値を算出
                toNextExp = nextExp - nowExp;

                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(100);


                makeToasty("レベルダウン…");

                makeToastyLong("現在のレベルは" + nowLevel + "です\n次のレベルまで，あと" + toNextExp + "EX必要です");
//        db.execSQL("UPDATE monster SET exp = ? WHERE name = Monster);",new Object[]{nowExp});
//        int exOr = db.update(String table, ContentValues values, String whereClause, String[] whereArgs)
//        update()メソッド：テーブルにデータを更新します。
//        戻り値は正常に更新でき場合が更新したレコード数が戻り、失敗した場合は-1が戻ります。
//        引数tableにはテーブル名を指定します。
//        引数valuesには更新レコードの列名と値がマッピングされたContentValuesインスタンスを設定します。
//        引数whereClauseには更新対象レコードを検索するための条件を指定します。
//        引数whereArgsにはwhereClauseにパラメータ（？で指定）が含まれる場合に置き変わる値を指定します。不要な場合はnullを指定します。

                //nowExpのアップデート，それをlocalExpに入れてやる
                ContentValues cValues = new ContentValues();
                String whereClause = "name = 'Monster'";
                String[] whereArgs = {"Monster"};
                cValues.put("exp", nowExp);//nowExp);
                cValues.put("level", nowLevel);


                MonsterOpenHelper helper = new MonsterOpenHelper(this);
                final SQLiteDatabase db = helper.getWritableDatabase();

                try {
                    int exOr = db.update("monster", cValues, whereClause, null);
                } catch (Exception e) {

                }


            }
        } else {
            nowExp = 0;
        }

    }

    //----EngField読み取り
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,int after) {
        //操作前のEtidTextの状態を取得する
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //操作中のEtidTextの状態を取得する

    }

    @Override
    public void afterTextChanged(Editable s) {
        //操作後のEtidTextの状態を取得する

    /*--- 取得例（EditTextの更新内容をTextViewに反映） ---*/
        final EditText engField = (EditText)findViewById(R.id.english);
        final EditText jpField = (EditText)findViewById(R.id.japanese);
 

        MonsterOpenHelper helper = new MonsterOpenHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();
        //        Cursor cur = db.query("tango",new String[] {"english","japanese"},null,null,null,null,null);

        if(jpField.getText().toString().equals("")) {
            String selectStr = "english LIKE ? || '%' ";// OR japanese LIKE ''%'|| ? ||'%'' )";
            String ss = s.toString();
            String[] selectArg = { ss };//s.toString() };

            Cursor c = db.query("tango", null, selectStr, selectArg, null, null, null);

            listReload(c);
        }

        else if(engField.getText().toString().equals("")) {
            String selectStr = "japanese LIKE ? || '%' ";// OR japanese LIKE ''%'|| ? ||'%'' )";
            String ss = s.toString();
            String[] selectArg = { ss };//s.toString() };

            Cursor c = db.query("tango", null, selectStr, selectArg, null, null, null);

            listReload(c);

        }

       else if(!engField.getText().toString().equals("") && !jpField.getText().toString().equals("")) {
        //            MonsterOpenHelper helper = new MonsterOpenHelper(this);
//            final SQLiteDatabase db = helper.getWritableDatabase();
            //db.execSQL("ALTER TABLE tango ADD COLUMN date[text];");
            Cursor cur = db.query("tango",new String[] {"english","japanese"},null,null,null,null,null);
            listReload(cur);
        }


        db.close();
    /*--- 取得例 ---*/

    }
//----EngField読み取り




//----Monster---]


    public void makeToasty(String st) {
        Toast.makeText(this,st, Toast.LENGTH_SHORT).show();
    }
    public void makeToastyLong(String st) {
        Toast.makeText(this,st, Toast.LENGTH_LONG).show();
    }

    protected void findViews(){
        listView = (ListView)findViewById(R.id.listView1);
        saveButton = (Button)findViewById(R.id.saveButton);
        engBtn = (Button)findViewById(R.id.engSearch);
        jpBtn = (Button)findViewById(R.id.jpSearch);
    }

    protected void setListeners(){
        saveButton.setOnClickListener(this);
        engBtn.setOnClickListener(this);
        jpBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //       int childCount = frameLayout01.getChildCount();
        //       Log.i("childcount",String.valueOf(frameLayout01.getChildCount()));

        //        if(childCount == 1) {
        //        frameLayout01.addView(target);
        //       }

        switch(v.getId()){
            case R.id.saveButton:
                checkItems();
                break;

            case R.id.engSearch:
                final EditText engField = (EditText)findViewById(R.id.english);
                jumpSearch(engField.getText().toString());
                makeToasty(engField.getText().toString()+"で検索中");
                break;

            case R.id.jpSearch:
                final EditText jpField = (EditText)findViewById(R.id.japanese);
                jumpSearch(jpField.getText().toString());
                makeToasty(jpField.getText().toString()+"で検索中");
                break;
        }


    }

    private void jumpSearch(String word) {
       Uri uri = Uri.parse("http://ejje.weblio.jp/content/" + word);
       Intent i = new Intent(Intent.ACTION_VIEW,uri);
       startActivity(i);
    }

    public void checkItems() {
        MonsterOpenHelper helper = new MonsterOpenHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();

        final EditText engField = (EditText)findViewById(R.id.english);
        final EditText jpField = (EditText)findViewById(R.id.japanese);
        boolean bl = true;
        if(!engField.getText().toString().equals("") && !jpField.getText().toString().equals("")) {
            Cursor cur2;

                cur2 = db.query("tango", new String[]{"english", "japanese"}, null, null, null, null, null);

            while(cur2.moveToNext()) {
                if(engField.getText().toString().equals(cur2.getString(0)) && jpField.getText().toString().equals(cur2.getString(1))) {
                    makeToasty("もう食べたよそれは〜");
                    bl = false;
                }
            }

            if(bl==true) addItem();


        }
        else {
            Toast.makeText(this, "空白は食べれない", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    protected void setAdapters(){
        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dataList);
        listView.setAdapter(adapter);
    }

    protected void addItem(){
        final EditText engField = (EditText)findViewById(R.id.english);
        final EditText jpField = (EditText)findViewById(R.id.japanese);

        MonsterOpenHelper helper = new MonsterOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();


        //現在時刻の取得
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        // HH:mm:ss");
        String strSysDate = sdfDate.format(calendar.getTime());
        String strSysTime = sdfTime.format(calendar.getTime());

        //        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd", Locale.JAPANESE);
        //        Date date = new Date();
        //        String string = sdf.format(date);

        inputtingEng = engField.getText().toString();
        ContentValues insertTango = new ContentValues();
        insertTango.put("english",engField.getText().toString().trim());
        insertTango.put("japanese", jpField.getText().toString().trim());
        insertTango.put("date",strSysDate);
        insertTango.put("time",strSysTime);
try {
        long id = db.insert("tango", null, insertTango);
}finally {
    db.close();
}
        dataList.add(0, engField.getText().toString() + " = " + jpField.getText().toString());
        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dataList);
        listView.setAdapter(adapter);
        engField.getEditableText().clear();
        jpField.getEditableText().clear();
        engField.requestFocus();


        exUpdate();
        Toast.makeText(this, "もぐもぐ", Toast.LENGTH_SHORT).show();
        //        adapter.add(engField.getText().toString() + " = " + jpField.getText().toString());

    }

// アップデート 経験値更新
    protected void exUpdate() {

        MonsterOpenHelper helper = new MonsterOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        //        int year = calendar.get(Calendar.YEAR);
        //        int month = calendar.get(Calendar.MONTH) + 1;
        //        int day = calendar.get(Calendar.DATE);
        //
        //        cal = Calendar.getInstance();
        //        cal.set(Calendar.DAY_OF_WEEK_IN_MONTH,1);
        //        cal.set(Calendar.DAY_OF_WEEK,7);
        //        int thisSat = cal.getTime().format("dd");


        ///        if(thisSat <= 07) {
        /////            Cursor cus = db.rawQuery("SELECT * FROM tango WHERE date BETWEEN " + year + "-" + month + "-01 ||  AND " + year + "-" + month + "-07 || '%';");
        ///
        ///            final TextView weekTango = (TextView)findViewById(R.id.weekCountWords);
        ///            long tangoWeekCount = DatabaseUtils.queryNumEntries(db, "tango","date BETWEEN ? AND ?",new String[]{ year + "-" + month + "-"+ day + " 00:00:00", year + "-" + month + "-07 23:59:59"  });
        ///            weekTango.setText(String.valueOf(tangoWeekCount));
        ///        } else {
        ///
        



        //とりあえずは単語数のみアップデート
        final TextView nowTango = (TextView)findViewById(R.id.nowCountWords);
        long tangoCount = DatabaseUtils.queryNumEntries(db, "tango");
        nowTango.setText(String.valueOf(tangoCount));

        //先週の日曜日からの単語数アップデート
        Calendar calendarW = Calendar.getInstance(); //現在の日にち
        Calendar calendarW0 = Calendar.getInstance(); //先週の日曜日の日にち

        int ago = calendarW0.get(Calendar.DAY_OF_WEEK)-1;
        //        Toast.makeText(this,String.valueOf(ago),Toast.LENGTH_LONG).show();

        SimpleDateFormat sdfW = new SimpleDateFormat("yyyy-MM-dd");
        String strSysDateW = sdfW.format(calendarW.getTime());
        //strSysDateW =  "'" + strSysDateW + "'";


        calendarW0.add(Calendar.DAY_OF_YEAR,-ago);
        SimpleDateFormat sdfW0 = new SimpleDateFormat("yyyy-MM-dd");
        String strSysDateW0 = sdfW0.format(calendarW0.getTime());
        //strSysDateW0 =  "'" + strSysDateW0 + " 00:00:00'";

        //       strSysDateW0 = "2015-05-07";
        //     strSysDateW =  "2015-05-08";

        final TextView weekTango = (TextView)findViewById(R.id.weekCountWords);
        long tangoWeekCount = DatabaseUtils.queryNumEntries(db, "tango", "date BETWEEN ? AND ?", new String[]{strSysDateW0, strSysDateW});
        weekTango.setText(String.valueOf(tangoWeekCount));


        //今日の分の単語数のアップデート
        Calendar calendarDay = Calendar.getInstance();
        SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
        String strSysDateDay = sdfDay.format(calendarDay.getTime());

        final TextView todayTango = (TextView)findViewById(R.id.todayCountWords);
        long tangoTodayCount = DatabaseUtils.queryNumEntries(db, "tango","date LIKE ?",new String[]{ strSysDateDay });
        todayTango.setText(String.valueOf(tangoTodayCount));


        //改修途中
        mNavigationDrawerFragment.countingFrag((int)tangoTodayCount,(int)tangoWeekCount,(int)tangoCount);


        // レベル管理
        Cursor cu = db.query("tango", new String[] {"SUM(LENGTH(english))"}, null, null, null, null, null);
        cu.moveToNext();
        nowExp = cu.getInt(0);


 //       exMap.get(nowLevel);

        nextExp=exMap.get(nowLevel+1);
        toNextExp = nextExp - nowExp;
        //レベル更新
        if(toNextExp<=0) {//nowExp > nextExp) {
            //nextExp = exMap.get(nowLevel + 1);
            int nowExpTemp = nowExp;
           while (nowExpTemp >= exMap.get(nowLevel+1)) {
//            if(exMap.get(nowLevel+1)-exMap.get(nowLevel) < inputtingEng.length())
//                toNextExp - inputtingEng.length();

               nowLevel = nowLevel + 1;
               //nowExpTemp = nowExpTemp - exMap.get(nowLevel);

            }


            //            次のレベルの経験値を算出
            nextExp = exMap.get(nowLevel+1);
            //次のレベル”まで”の経験値を算出
            toNextExp = nextExp - nowExp;

            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(1000);

            makeToasty("レベルアップ！");

            makeToastyLong("現在のレベルは" + nowLevel + "です\n次のレベルまで，あと" + toNextExp + "EX必要です");
//        db.execSQL("UPDATE monster SET exp = ? WHERE name = Monster);",new Object[]{nowExp});
//        int exOr = db.update(String table, ContentValues values, String whereClause, String[] whereArgs)
//        update()メソッド：テーブルにデータを更新します。
//        戻り値は正常に更新でき場合が更新したレコード数が戻り、失敗した場合は-1が戻ります。
//        引数tableにはテーブル名を指定します。
//        引数valuesには更新レコードの列名と値がマッピングされたContentValuesインスタンスを設定します。
//        引数whereClauseには更新対象レコードを検索するための条件を指定します。
//        引数whereArgsにはwhereClauseにパラメータ（？で指定）が含まれる場合に置き変わる値を指定します。不要な場合はnullを指定します。

            //nowExpのアップデート，それをlocalExpに入れてやる
            ContentValues cValues = new ContentValues();
            String whereClause = "name = 'Monster'";
            String[] whereArgs = {"Monster"};
            cValues.put("exp", nowExp);//nowExp);
            cValues.put("level", nowLevel);

            try {
                int exOr = db.update("monster", cValues, whereClause, null);
            } catch (Exception e) {
//                db.execSQL("INSERT INTO monster(name,level,exp,zokusei) VALUES('Monster',1,0,'nothing');");
            }

//
//            //localLevelの設定
//            Cursor locur = db.query("monster",new String[] {"level","exp"},null,null,null,null,null);
//            locur.moveToNext();
//            nowLevel = locur.getInt(0);
//            //localExp = locur.getInt(1);

//次のレベルの経験値をマップから算出
//            int mokuhyou = 500;
//            if(exMap.containsKey(nowExp)) {
//                //Toeic準拠レベルの場合は目標までのExpをnextExpとする
//                nextExp = exMap.get(mokuhyou);

//１レベルずつの場合はこちらを使う
//            nextExp = exMap.get(nowLevel+1);
//            }






        }


        toNextExp = nextExp - nowExp;

        mNavigationDrawerFragment.setExp(nowLevel, toNextExp);

        //ローカルにDBバックアップ
        try {
            FileInputStream input = new FileInputStream(db_file);
            FileOutputStream output = new FileOutputStream(ENG_PATH + "/tango.db");
            byte buf[] = new byte[4096];
            int len;
            while ((len = input.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            Log.i("OUT","out失敗¥n"+e);

        }

        // filecopy(db_file,ENG_PATH+"/tango.db");



        //ExOpenHelper exHelper = new ExOpenHelper(this);
        //SQLiteDatabase exDb = exHelper.getWritableDatabase();

        //String sqlDayEx = "select ex from experience where when = today;";
        //Cursor cuex = exDb.query("experience", new String[]{"when", "ex"}, null, null, null, null, null);
        //today.setText(todayEX);
        //until.setText(nowEX);

        db.close();

    }

//----Monster---








    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position+1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:

//                //ListView listDiew=new ListView(this);// = (ListView)findViewById(R.id.listdiew);
//
//                TextView mAtoZ = (TextView)findViewById(R.id.atoz);
//
//                ArrayAdapter<Character> ada;// = new ArrayAdapter<Character>(this,android.R.layout.simple_list_item_1);
//                ada = new ArrayAdapter<Character>(
//                        this,
//                        android.R.layout.simple_list_item_1);
//
//                for(char a='A'; a <='Z'; a++ ) {
//                    // リスト表示用のアラートダイアログ
//                    ada.add(a);
//                }
//
//
//
//                ArrayAdapter<Character> adapt = new ArrayAdapter<Character>(this,android.R.layout.simple_list_item_1);
//
//
////
////                String a = (STri'A';
////                CharSequence[] items = new CharSequence[25];
////                for(int i=0; i<=25; i++) {
////                   items[0] = a;
////                   a++;
////                }
//
////要修正
////                listDiew.setAdapter(ada);
//
//                AlertDialog.Builder listDlg = new AlertDialog.Builder(this);
//                listDlg.setTitle("AtoZ");
////                listDlg.setItems(
////                    items,
////                    new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int which) {
////
////
////                    // リスト選択時の処理
////                    // which は、選択されたアイテムのインデックス
////                    }
////                });
//
////要修正                listDlg.setView(listDiew);
//                listDlg.create();
//                // 表示
//                listDlg.show();
                break;

            case 2:
//                if(first==false) {
                    AtoZFragment atozFragment = new AtoZFragment();
                    atozFragment.show(getFragmentManager(), "contact_us");
//                } else {
//                    first=false;
//                }

                break;

            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                DiaHyoujiFragment diaHyoujiFragment = new DiaHyoujiFragment();
                diaHyoujiFragment.show(getFragmentManager(), "contact_us");
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(),SetActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
