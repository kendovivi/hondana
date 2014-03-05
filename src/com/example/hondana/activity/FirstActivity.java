
package com.example.hondana.activity;

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

public class FirstActivity extends Activity implements OnItemLongClickListener, OnItemClickListener {
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

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {
        // 显示所有的check box，要修改
        for (int i = 0; i < mGridView.getChildCount(); i++) {
            CheckBox cb = (CheckBox) mGridView.getChildAt(i).findViewById(R.id.checkbox);
            cb.setVisibility(View.VISIBLE);
        }
        mShowSelBtn.setVisibility(View.VISIBLE);
        // 给图片加边框，之后放到touch事件里

        // 设定拖曳事件 要修改
        // ClipData.Item item = new ClipData.Item(view.getTag());
        // ClipData dragData = new ClipData(view.getTag(), item);
        //mGridView.getChildAt(position).startDrag(null, new DragBackgroundBuilder(view), null, 0);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View ivew, int position, long arg3) {
        Intent intent = new Intent();
        intent.setClass(this, ShowIntroductionActivity.class);
        intent.putExtra(Const.BOOK_ONCLICK, position);
        startActivity(intent);
    }

    public void showSelected(View view) {
        mSelBooks = getSelectedBookList();
        // 所有check box不显示， 要修改
        for (int i = 0; i < mGridView.getChildCount(); i++) {
            CheckBox cb = (CheckBox) mGridView.getChildAt(i).findViewById(R.id.checkbox);
            cb.setVisibility(View.INVISIBLE);
        }

        String str = "selected book list : ";
        if (mSelBooks != null && mSelBooks.size() > 0) {
            for (Book book : mSelBooks) {
                str += book.getBookName() + " ";
            }
        } else {
            str = "no books selected";
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        mShowSelBtn.setVisibility(View.GONE);
    }

    private void initBooks() {
        mAllBooks = mBook.getAllBooks(this);
    }

    private ArrayList<Book> getSelectedBookList() {
        ArrayList<Book> selBooks = new ArrayList<Book>();
        for (Book book : mAllBooks) {
            if (book.getBookSelected()) {
                selBooks.add(book);
            }
        }
        return selBooks;
    }

}
