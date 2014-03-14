package com.example.hondana.adapter;

import com.example.hondana.book.Book;

import java.util.ArrayList;

public class ShelfRow {
    private ArrayList<Book> mBookList;
    
    public ShelfRow() {
        mBookList = new ArrayList<Book>();
    }

    public ArrayList<Book> getBookListInRow() {
        return mBookList;
    }
    public void setBookListInRow(ArrayList<Book> mBookList) {
        this.mBookList = mBookList;
    }
    
    public void addBook(Book book){
        mBookList.add(book);
    }
    
}
