
package com.example.hondana.book;

import android.os.Parcel;

import android.os.Parcelable;

import java.io.Serializable;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.hondana.R;
import java.util.ArrayList;

public class Book implements Parcelable{
    /** 书名 */
    private String mBookName = "bookname";
    /** 书封面图片 */
    private Bitmap mBookImage;
    /** 是否为最近读的 */
    private boolean mBookIsRec = false;
    private boolean mBookSelected = false;

    //要删除，之后改成parcelable
    public static ArrayList<Book> sSelectedList = null;

    public Book(){};
    private Book(String bookName, Bitmap bookImage) {
        this.mBookName = bookName;
        this.mBookImage = bookImage;
    };

    private Book(String bookName, Bitmap bookImage, boolean bookIsRec) {
        this.mBookName = bookName;
        this.mBookImage = bookImage;
        this.mBookIsRec = bookIsRec;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public String getBookName() {
        return mBookName;
    }

    public void setBookName(String bookName) {
        this.mBookName = bookName;
    }

    public Bitmap getBookImage() {
        return mBookImage;
    }

    public void setBookImage(Bitmap bookImage) {
        this.mBookImage = bookImage;
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
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample2", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample3", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample3)));
        bookList.add(new Book("sample4", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample4)));
        bookList.add(new Book("sample5", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample5)));
        bookList.add(new Book("sample6", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample6)));
        bookList.add(new Book("sample7", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample7)));
        bookList.add(new Book("sample8", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample8)));
        bookList.add(new Book("sample9", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample9)));
        bookList.add(new Book("sample10", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample10)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        bookList.add(new Book("sample1", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample1)));
        return bookList;
    }

    public ArrayList<Book> getRecBooks(Context c) {
        ArrayList<Book> bookList = new ArrayList<Book>();
        bookList.add(new Book("sample5", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample7), true));
        bookList.add(new Book("sample6", BitmapFactory.decodeResource(c.getResources(), R.drawable.sample6), true));
        return bookList;
    }
    public static ArrayList<Book> getSelectedList() {
        return sSelectedList;
    }
    public static void setSelectedList(ArrayList<Book> selectedList) {
        sSelectedList = selectedList;
    }
}
