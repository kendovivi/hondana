package com.example.hondana.adapter;

import com.example.hondana.book.Book;

import android.content.Context;

import android.widget.RelativeLayout;

public class ShelfCell{

    private Context mContext;
    /** 每个单元内所包含的书 */
    private Book mBook;
    /** 每个单元的checkbox选中情况 */
    private boolean mIsChecked;
    
    
    public ShelfCell(Context context) {
        this.mContext = context;
    }

    public boolean ismIsChecked() {
        return mIsChecked;
    }

    public void setCellIsChecked(boolean isChecked) {
        this.mIsChecked = isChecked;
    }

    public Book getBookInCell() {
        return mBook;
    }

    public void setBookInCell(Book book) {
        this.mBook = book;
    }
    
    
    
    
}
