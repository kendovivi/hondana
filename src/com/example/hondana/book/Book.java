
package com.example.hondana.book;

import android.content.Context;
import com.example.hondana.R;
import java.util.ArrayList;

public class Book {
    /** コンテンツタイトル */
    private String mBookName = "bookname";
    /** コンテンツ画像resource Id */
    private int mBookImgResId;
    /** 最近読んだコンテンツであるか */
    private boolean mBookIsRec = false;
    /** 選択されたコンテンツであるか */
    private boolean mBookSelected = false;

    // 削除予定、parcebleに変更？
    public static ArrayList<Book> sSelectedList = null;

    public Book() {

    };

    private Book(String bookName, int resId) {
        this.mBookName = bookName;
        this.mBookImgResId = resId;
    };

    private Book(String bookName, int resId, boolean bookIsRec) {
        this.mBookName = bookName;
        this.mBookImgResId = resId;
        this.mBookIsRec = bookIsRec;
    }

    public String getBookName() {
        return mBookName;
    }

    public void setBookName(String bookName) {
        this.mBookName = bookName;
    }

    public int getBookImgId() {
        return mBookImgResId;
    }

    public void setBookImage(int resId) {
        this.mBookImgResId = resId;
    }

    public boolean getBookIsRec() {
        return mBookIsRec;
    }

    public void setBookIsRec(boolean bookIsRec) {
        this.mBookIsRec = bookIsRec;
    }

    public boolean getBookSelected() {
        return mBookSelected;
    }

    public void setBookSelected(boolean bookSelected) {
        this.mBookSelected = bookSelected;
    }

    public ArrayList<Book> getAllBooks(Context c) {
        ArrayList<Book> bookList = new ArrayList<Book>();
        bookList.add(new Book("sample1", R.drawable.sample1));
        bookList.add(new Book("sample2", R.drawable.sample1));
        bookList.add(new Book("sample3", R.drawable.sample3));
        bookList.add(new Book("sample4", R.drawable.sample4));
        bookList.add(new Book("sample5", R.drawable.sample5));
        bookList.add(new Book("sample6", R.drawable.sample6));
        bookList.add(new Book("sample7", R.drawable.sample7));
        bookList.add(new Book("sample8", R.drawable.sample8));
        bookList.add(new Book("sample9", R.drawable.sample9));
        bookList.add(new Book("sample10", R.drawable.sample10));
        return bookList;
    }

    public ArrayList<Book> getRecBooks(Context c) {
        ArrayList<Book> bookList = new ArrayList<Book>();
        bookList.add(new Book("sample5", R.drawable.sample7, true));
        bookList.add(new Book("sample6", R.drawable.sample6, true));
        return bookList;
    }

    public static ArrayList<Book> getSelectedList() {
        return sSelectedList;
    }

    public static void setSelectedList(ArrayList<Book> selectedList) {
        sSelectedList = selectedList;
    }
}
