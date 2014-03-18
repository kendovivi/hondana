
package com.example.hondana.activity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBook = new Book();
        // 初次默认为书架
        Intent intent = getIntent();
        mShelfStyle = intent.getIntExtra(Const.SHELF_STYLE, Const.GRID);
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
            setContentView(R.layout.hondana_main_horizontal);
            mGridView = (GridView) this.findViewById(R.id.shelf_gridview_h);
            // mGridView.setAdapter(new BookShelfRowAdapter(this, mAllBooks));
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
}
