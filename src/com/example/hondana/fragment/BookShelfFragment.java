
package com.example.hondana.fragment;

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

    private View view;
    private GridView mGridView;
    private Button showSelectedBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBook = new Book();
        mBookList = mBook.getAllBooks(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment, container, false);
        mGridView = (GridView) view.findViewById(R.id.shelf_gridview_v);
        showSelectedBtn = (Button) view.findViewById(R.id.test_show_selected_btn);

        //显示选中按钮监听器设定
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
                Toast.makeText(getActivity(), String.valueOf(position), 1).show();
            }

        });
        mGridView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

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
        showSelectedBtn.setVisibility(View.INVISIBLE);
    }
}
