
package com.example.hondana.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.hondana.Const;
import com.example.hondana.R;
import com.example.hondana.book.Book;
import com.example.hondana.fragment.BookShelfFragment;

public class FirstActivity extends Activity {
    private int mShelfStyle;

    private boolean mIsAppToFinish;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private BookShelfFragment mBookShelfFragment;

    private SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // actionbar调查
        // ActionBar actionBar = getActionBar();
        // actionBar.setDisplayShowHomeEnabled(false);
        // actionBar.setDisplayShowTitleEnabled(false);

        initTestData();
        if (savedInstanceState != null) {
            mShelfStyle = savedInstanceState.getInt(Const.SHELF_STYLE, Const.GRID);
        } else {
            // sharePreferenceにより、本棚スタイルを決める
            setShelfStyleUponPref();
        }
        initTestData();
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mBookShelfFragment = BookShelfFragment.newInstance(mShelfStyle);
        // 縦方向(現在両方同じ)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.hondana_main_vertical);
            mFragmentTransaction.add(R.id.fragment_container_vertical, mBookShelfFragment);
            // 横方向
        } else {
            setContentView(R.layout.hondana_main_vertical);
            mFragmentTransaction.replace(R.id.fragment_container_vertical, mBookShelfFragment);
        }
        mFragmentTransaction.commit();
        mIsAppToFinish = false;
    }

    /*
     * @Override public void onCreateContextMenu(ContextMenu menu, View v,
     * ContextMenuInfo menuInfo) { super.onCreateContextMenu(menu, v, menuInfo);
     * menu.add(0, 1, 0, "menu1"); menu.add(0, 2, 0,"menu2"); }
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hondana_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Const.SHELF_STYLE, mBookShelfFragment.getShelfStyle());
        super.onSaveInstanceState(outState);
    }

    /**
     * action barイベント定義
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        switch (item.getItemId()) {
        // 重置为grid
            case R.id.menu_edit:
                Book.setSelectedList(null);
                mShelfStyle = Const.GRID;
                mBookShelfFragment = BookShelfFragment.newInstance(mShelfStyle);
                mFragmentTransaction.replace(R.id.fragment_container_vertical, mBookShelfFragment);
                mFragmentTransaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 删除予定
    private void initTestData() {
        Book.setSelectedList(null);
    }

    private void setShelfStyleUponPref() {

        // 設定ファイルを取得
        mSp = PreferenceManager.getDefaultSharedPreferences(this);
        // 設定 APPを終了するとき本棚スタイルを記憶する
        boolean pref_save_shelfStyle = mSp.getBoolean("pref_save_shelf_style", false);
        int pref_shelf_style = mSp.getInt(Const.SHELF_STYLE, -1);
        if (pref_save_shelfStyle && pref_shelf_style != -1) {
            mShelfStyle = pref_shelf_style;
        } else {
            // ここは初入る時のみ呼ばれる
            mShelfStyle = Const.GRID;
        }
    }

    /*
     * @Override public boolean dispatchKeyEvent(KeyEvent event) { if
     * (event.getAction() == KeyEvent.ACTION_DOWN) { if (event.getKeyCode() ==
     * KeyEvent.KEYCODE_BACK) { AlertDialog.Builder alertDialogBuilder = new
     * AlertDialog.Builder(this);
     * alertDialogBuilder.setIcon(R.drawable.ic_launcher);
     * alertDialogBuilder.setTitle("提示");
     * alertDialogBuilder.setMessage("アプリを終了しますか？");
     * alertDialogBuilder.setPositiveButton("終了", new OnClickListener() {
     * @Override public void onClick(DialogInterface dialog, int which) {
     * finish(); } }); alertDialogBuilder.setNegativeButton("キャンセル", new
     * OnClickListener() {
     * @Override public void onClick(DialogInterface dialog, int which) { //
     * 什么都不做 } }); AlertDialog dialog = alertDialogBuilder.create();
     * dialog.show(); } } return super.dispatchKeyEvent(event); }
     */

    @Override
    public void onBackPressed() {
        // △△△名字getisedit要修改
        if (mBookShelfFragment.getIsEdit()) {
            mBookShelfFragment.ChangeShelfMode();
        } else {
            if (mIsAppToFinish == true) {
                saveShelfStyleToPref();
                super.onBackPressed();
            } else {
                mIsAppToFinish = true;
                Toast.makeText(this, "もう一度押してアプリを終了します", 2).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mIsAppToFinish = false;
                    }

                }, 3000);
            }
        }
    }

    private void saveShelfStyleToPref() {
        Editor editor = mSp.edit();
        editor.putInt(Const.SHELF_STYLE, mBookShelfFragment.getShelfStyle());
        editor.commit();
    }

}
