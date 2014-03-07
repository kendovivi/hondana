
package com.example.hondana.activity;

import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;

import android.view.MenuInflater;

import android.view.Menu;

import android.app.FragmentTransaction;

import com.example.hondana.fragment.BookShelfFragment;

import android.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.Toast;
import com.example.hondana.Const;
import com.example.hondana.R;
import com.example.hondana.adapter.BookAdapter;
import com.example.hondana.book.Book;
import java.util.ArrayList;

public class FirstActivity extends Activity {
    /** gridview for all books */
    private GridView mGridView;

    private Book mBook;
    private ArrayList<Book> mAllBooks;
    private ArrayList<Book> mSelBooks;

    private Button mShowSelBtn;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBook = new Book();
        initBooks();

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        // 纵向
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.hondana_main_vertical);
            fragmentTransaction.add(R.id.fragment_container_vertical, new BookShelfFragment());
            // 横向
        } else {
            setContentView(R.layout.hondana_main_horizontal);
            mGridView = (GridView) this.findViewById(R.id.shelf_gridview_h);
            mGridView.setAdapter(new BookAdapter(this, mAllBooks));
        }
        fragmentTransaction.commit();
        mShowSelBtn = (Button) this.findViewById(R.id.test_show_selected_btn);

        // 设置onClick效果，需要调查
        // bookGridView.setSelector(android.R.color.transparent);

    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "menu1");
        menu.add(0, 2, 0,"menu2");
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hondana_menu, menu);
        return true;
    }

    private void initBooks() {
        Book.setSelectedList(null);
        mAllBooks = mBook.getAllBooks(this);
    }

}
