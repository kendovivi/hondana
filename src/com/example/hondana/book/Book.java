
package com.example.hondana.book;


public class Book {
    /** コンテンツタイトル */
    private String mBookTitle;
    /** コンテンツ画像resource Id */
    private int mBookImgResId;
    /** 最近読んだコンテンツであるか */
    private String mBookAuthor;
    private boolean mBookIsRec = false;
    /** 選択されたコンテンツであるか */
    private boolean mIsSelected = false;

    public Book(String title, int resId, String author) {
        this.mBookTitle = title;
        this.mBookImgResId = resId;
        this.mBookAuthor = author;
    };

    public String getBookTitle() {
        return mBookTitle;
    }

    public void setBookTitle(String title) {
        this.mBookTitle = title;
    }

    public int getBookImgId() {
        return mBookImgResId;
    }

    public String getBookAuthor() {
        return mBookAuthor;
    }
    
    public void setBookAuthor(String author) {
        this.mBookAuthor = author;
    }
    
    public boolean getBookIsRec() {
        return mBookIsRec;
    }

    public void setBookIsRec(boolean bookIsRec) {
        this.mBookIsRec = bookIsRec;
    }

    public boolean getBookSelected() {
        return mIsSelected;
    }

    public void setBookSelected(boolean isSelected) {
        this.mIsSelected = isSelected;
    }

}
