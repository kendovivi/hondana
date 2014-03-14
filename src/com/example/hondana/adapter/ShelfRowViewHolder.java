package com.example.hondana.adapter;

import android.widget.ImageView;

import android.widget.CheckBox;

import java.util.ArrayList;

public class ShelfRowViewHolder {
    private ArrayList<CheckBox> mCheckBoxsInRow;
    private ArrayList<ImageView> mBookImageViewsInRow;
    
    public ShelfRowViewHolder() {
        mCheckBoxsInRow = new ArrayList<CheckBox>();
        mBookImageViewsInRow = new ArrayList<ImageView>();
    }
    
    public void addCheckBox(CheckBox checkBox) {
        mCheckBoxsInRow.add(checkBox);
    }
    
    public CheckBox getCheckBoxInRowByColumn(int column) {
        return mCheckBoxsInRow.get(column);
    }
    
    public void addImageView(ImageView imageView) {
        mBookImageViewsInRow.add(imageView);
    }
    
    public ImageView getBookImageViewInRowByColumn(int column) {
        return mBookImageViewsInRow.get(column);
    }
}
