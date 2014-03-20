
package com.example.hondana.activity;

import android.content.SharedPreferences.Editor;

import android.content.SharedPreferences;

import android.preference.PreferenceManager;

import android.app.ActionBar;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import com.example.hondana.Const;
import com.example.hondana.R;
import com.example.hondana.book.Book;
import com.example.hondana.fragment.BookShelfFragment;
import java.util.ArrayList;

public class FirstActivity extends Activity {
    /** gridview for all books */
    private GridView mGridView;
    private int mShelfStyle;

    private Book mBook;
    private ArrayList<Book> mAllBooks;
    private ArrayList<Book> mSelBooks;

    private Button mShowSelBtn;
    private boolean mFinishFlag;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private BookShelfFragment mBookShelfFragment;

    SharedPreferences mSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // actionbar调查
        // ActionBar actionBar = getActionBar();
        // actionBar.setDisplayShowHomeEnabled(false);
        // actionBar.setDisplayShowTitleEnabled(false);

        // get preference
        mSp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean saveStylePref = mSp.getBoolean("pref_shelf_style", false);
        int shelfStylePref = mSp.getInt(Const.SHELF_STYLE, -1);
        mBook = new Book();
        // 初次默认为书架, 要修改
        if (savedInstanceState != null) {
            mShelfStyle = savedInstanceState.getInt(Const.SHELF_STYLE, Const.GRID);
        } else {
            // 第一次进入，读取设定值，若为真则使用列表显示
            // 若pref中每次退出时记录当时style为真
            if (saveStylePref && shelfStylePref != -1) {
                mShelfStyle = shelfStylePref;
            } else {
                mShelfStyle = Const.GRID;
            }
        }
        // Intent intent = getIntent();
        // mShelfStyle = intent.getIntExtra(Const.SHELF_STYLE, Const.GRID);
        initBooks();
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mBookShelfFragment = BookShelfFragment.newInstance(mShelfStyle);
        // 纵向
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.hondana_main_vertical);
            mFragmentTransaction.add(R.id.fragment_container_vertical, mBookShelfFragment);
            // 横向
        } else {
            setContentView(R.layout.hondana_main_vertical);
            mFragmentTransaction.replace(R.id.fragment_container_vertical, mBookShelfFragment);
        }
        mFragmentTransaction.commit();
        mShowSelBtn = (Button) this.findViewById(R.id.test_show_selected_btn);

        // 结束程序标识
        mFinishFlag = false;
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
     * menu点击
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

    private void initBooks() {
        Book.setSelectedList(null);
        mAllBooks = mBook.getAllBooks(this);
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
        // 名字getisedit要修改
        if (mBookShelfFragment.getIsEdit()) {
            mBookShelfFragment.ChangeToNormalModeLayout();
        } else {
            if (mFinishFlag == true) {
                saveShelfStyleToPref();
                super.onBackPressed();
            } else {
                mFinishFlag = true;
                Toast.makeText(this, "もう一度押すとアプリが終了します", 2).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mFinishFlag = false;
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
