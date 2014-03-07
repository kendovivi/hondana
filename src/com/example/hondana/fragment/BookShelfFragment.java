
package com.example.hondana.fragment;

import android.app.FragmentTransaction;

import android.app.FragmentManager;

import com.example.hondana.Const;

import com.example.hondana.activity.ShowIntroductionActivity;

import android.content.Intent;

import android.widget.ImageView;

import android.content.Context;

import android.view.WindowManager;

import android.view.View.OnTouchListener;

import android.view.MotionEvent;

import android.view.MotionEvent;

import android.view.View.OnClickListener;

import android.widget.Toast;

import android.widget.AdapterView.OnItemClickListener;

import android.widget.Button;

import android.widget.CheckBox;

import android.widget.AdapterView;

import android.widget.AdapterView.OnItemLongClickListener;

import java.util.ArrayList;

import com.example.hondana.book.Book;

import com.example.hondana.adapter.BookAdapter;

import android.widget.GridView;

import com.example.hondana.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.Bundle;

import android.app.Fragment;

public class BookShelfFragment extends Fragment implements OnClickListener {
    private Book mBook;
    private ArrayList<Book> mBookList;
    private ArrayList<Book> mSelectedBookList;

    private View view;
    private GridView mGridView;
    private Button showSelectedBtn;

    private int mPosition;

    private WindowManager mWindowManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBook = new Book();
        mSelectedBookList = Book.getSelectedList();
        if (mSelectedBookList != null) {
            mBookList = mSelectedBookList;
        } else {
            mBookList = mBook.getAllBooks(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment, container, false);
        mGridView = (GridView) view.findViewById(R.id.shelf_gridview_v);
        showSelectedBtn = (Button) view.findViewById(R.id.test_show_selected_btn);
        mWindowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

        // 显示选中按钮监听器设定
        showSelectedBtn.setOnClickListener(this);
        // gridview 的适配器和监听器的设定
        mGridView.setAdapter(new BookAdapter(getActivity(), mBookList));
        mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View itemView, int position,
                    long arg3) {
                for (int i = 0; i < mGridView.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) mGridView.getChildAt(i).findViewById(
                            R.id.checkbox);
                    checkBox.setVisibility(View.VISIBLE);
                }
                showSelectedBtn.setVisibility(View.VISIBLE);
                return false;
            }

        });
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View itemView, int position, long arg3) {
                mPosition = position;
                Intent intent = new Intent();
                intent.setClass(getActivity(), ShowIntroductionActivity.class);
                intent.putExtra(Const.BOOK_ONCLICK, position);
                getActivity().startActivity(intent);
            }

        });

        // 移动监听
        /*
         * mGridView.setOnTouchListener(new OnTouchListener() {
         * @Override public boolean onTouch(View v, MotionEvent event) { int x =
         * (int) event.getX(); int y = (int) event.getY(); int action =
         * event.getAction(); if (action == MotionEvent.ACTION_MOVE) {
         * Toast.makeText(getActivity(), "当前坐标 X-" + x + " Y-" + y, 1).show();
         * //
         * mWindowManager.removeView((ImageView)mGridView.getChildAt(mPosition)
         * .findViewById(R.id.bookimage)); } return false; } });
         */

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_show_selected_btn:
                showSelected();
                break;
        }
    }

    private void showSelected() {
        for (int i = 0; i < mGridView.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) mGridView.getChildAt(i).findViewById(R.id.checkbox);
            checkBox.setVisibility(View.INVISIBLE);
        }

        ArrayList<Book> list = new ArrayList<Book>();
        for (Book book : mBookList) {
            if (book.getBookSelected()) {
                list.add(book);
            }
        }
        mSelectedBookList = list;
        //替换当前fragment，显示选中的booklist
        Book.setSelectedList(mSelectedBookList);
        /*Intent intent = new Intent();
        intent.setClass(getActivity(), ShowSelectedBooksActivity.class);
        getActivity().startActivity(intent);
        Toast.makeText(getActivity(), String.valueOf(mSelectedBookList.size()), Toast.LENGTH_SHORT).show();*/

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container_vertical, new BookShelfFragment());
        ft.commit();
        showSelectedBtn.setVisibility(View.INVISIBLE);
    }
}
