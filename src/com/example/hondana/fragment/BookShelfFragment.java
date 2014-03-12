
package com.example.hondana.fragment;

import android.provider.OpenableColumns;

import android.view.Menu;
import android.view.MenuInflater;

import android.widget.ImageView;

import com.example.hondana.Const;

import com.example.hondana.activity.ShowIntroductionActivity;

import android.content.Intent;

import android.widget.AdapterView.OnItemClickListener;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import com.example.hondana.R;
import com.example.hondana.adapter.BookAdapter;
import com.example.hondana.book.Book;
import java.util.ArrayList;

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

        //非编辑画面时单击监听，点击显示书籍详细信息
        OnItemClickListener noEditOnItemClickListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View itemView, int position, long arg3) {
                mPosition = position;
                Intent intent = new Intent();
                intent.setClass(getActivity(), ShowIntroductionActivity.class);
                intent.putExtra(Const.BOOK_ONCLICK, position);
                getActivity().startActivity(intent);
            }
        };
        //编辑画面时单击监听，点击选中
        final OnItemClickListener editOnItemClickListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View itemView, int position, long arg3) {
                CheckBox cb = (CheckBox) itemView.findViewById(R.id.checkbox);
                if (cb.isChecked()) {
                    cb.setChecked(false);
                } else {
                    cb.setChecked(true);
                }
            }
        };
        mGridView.setOnItemClickListener(noEditOnItemClickListener);
        mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View itemView, int position,
                    long arg3) {
                for (int i = 0; i < mGridView.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) mGridView.getChildAt(i).findViewById(
                            R.id.checkbox);
                    checkBox.setVisibility(View.VISIBLE);
                    //设置checkbox不会置顶，因此点击的都是imageview
                    checkBox.setFocusable(false);
                }
                //在此更换imageview的单击监听，来控制checkbox的选中状态
                mGridView.setOnItemClickListener(editOnItemClickListener);
                showSelectedBtn.setVisibility(View.VISIBLE);
                ImageView iv = (ImageView) itemView.findViewById(R.id.bookimage);
                iv.setBackgroundResource(R.drawable.border_no);
                getActivity().openOptionsMenu();
                //getActivity().findViewById(R.id.menu_show_selected).setVisibility(View.VISIBLE);
                return false;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
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
