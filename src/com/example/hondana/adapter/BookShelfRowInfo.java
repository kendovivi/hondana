
package com.example.hondana.adapter;

import com.example.hondana.book.Book;

import java.util.ArrayList;

public class BookShelfRowInfo {

    private ArrayList<BookShelfRow> mRowList;
    private ArrayList<Book> mBookList;
    private int mItemNumInRow;
    private int mRowNums;

    public BookShelfRowInfo(ArrayList<Book> bookList, int itemNumInRow) {
        this.mItemNumInRow = itemNumInRow;
        this.mBookList = bookList;
        this.mRowNums = CaculateRowNum();
        this.mRowList = new ArrayList<BookShelfRow>();
        initRowsInfo();
    }

    private void initRowsInfo() {
        int book_j = 0;
        for (int row_i = 0; row_i <= mRowNums - 1; row_i++) {
            BookShelfRow row = new BookShelfRow();
            for (; book_j <= mItemNumInRow * (row_i+1) - 1; book_j++) {
                //如果最后一行row不满
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

    public ArrayList<BookShelfRow> getRowList() {
        return mRowList;
    }

    public int getRowsCount() {
        return mRowNums;
    }
}
