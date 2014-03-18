
package com.example.hondana.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridView;
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
    private boolean finishFlag;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BookShelfFragment mBookShelfFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBook = new Book();
        // 初次默认为书架
        Intent intent = getIntent();
        mShelfStyle = intent.getIntExtra(Const.SHELF_STYLE, Const.GRID);
        initBooks();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        mBookShelfFragment = BookShelfFragment.newInstance(mShelfStyle);
        // 纵向
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.hondana_main_vertical);
            fragmentTransaction.add(R.id.fragment_container_vertical, mBookShelfFragment);
            // 横向
        } else {
            setContentView(R.layout.hondana_main_horizontal);
            mGridView = (GridView) this.findViewById(R.id.shelf_gridview_h);
            // mGridView.setAdapter(new BookShelfRowAdapter(this, mAllBooks));
        }
        fragmentTransaction.commit();
        mShowSelBtn = (Button) this.findViewById(R.id.test_show_selected_btn);

        //
        finishFlag = false;
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
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (item.getItemId()) {
            //重置为grid
            case R.id.menu_edit:
                Book.setSelectedList(null);
                mShelfStyle = Const.GRID;
                mBookShelfFragment = BookShelfFragment.newInstance(mShelfStyle);
                fragmentTransaction.replace(R.id.fragment_container_vertical, mBookShelfFragment);
                fragmentTransaction.commit();
                break;
            //grid, list视图切换
            case R.id.menu_change_shelf_style:
                int shelfStyle = mBookShelfFragment.getShelfStyle();
                if (shelfStyle == Const.GRID) {
                    shelfStyle = Const.LIST;
                } else {
                    shelfStyle = Const.GRID;
                }
                mBookShelfFragment = BookShelfFragment.newInstance(shelfStyle);
                fragmentTransaction.replace(R.id.fragment_container_vertical, mBookShelfFragment);
                fragmentTransaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initBooks() {
        Book.setSelectedList(null);
        mAllBooks = mBook.getAllBooks(this);
    }
}
