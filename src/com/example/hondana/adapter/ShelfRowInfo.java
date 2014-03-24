
package com.example.hondana.adapter;

import com.example.hondana.book.Book;

import java.util.ArrayList;

public class ShelfRowInfo {

    private ArrayList<ShelfRow> mRowList;
    private ArrayList<Book> mBookList;
    private int mItemNumInRow;
    private int mRowNums;

    public ShelfRowInfo(ArrayList<Book> bookList, int itemNumInRow) {
        this.mItemNumInRow = itemNumInRow;
        this.mBookList = bookList;
        this.mRowNums = CaculateRowNum();
        this.mRowList = new ArrayList<ShelfRow>();
        initRowsInfo();
    }

    private void initRowsInfo() {
        int book_j = 0;
        for (int row_i = 0; row_i <= mRowNums - 1; row_i++) {
            ShelfRow row = new ShelfRow();
            for (; book_j <= mItemNumInRow * (row_i+1) - 1; book_j++) {
                //最終行のコンテンツが行のデフォルトコンテンツ数より少ない場合
                if (book_j >= mBookList.size()){
                    break;
                }
                row.addBook(mBookList.get(book_j));
            }
            mRowList.add(row);
        }
    }

    private int CaculateRowNum() {
        return mBookList.size() % mItemNumInRow == 0 ? mBookList.size() / mItemNumInRow : mBookList
                .size() / mItemNumInRow + 1;
    }

    public ArrayList<ShelfRow> getRowList() {
        return mRowList;
    }

    public int getRowsCount() {
        return mRowNums;
    }
}
