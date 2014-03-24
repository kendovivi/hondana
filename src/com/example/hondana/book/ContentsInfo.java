
package com.example.hondana.book;

import java.util.Locale;

import java.text.Collator;

import java.util.Comparator;

import java.util.Collections;

import com.example.hondana.Const;

import com.example.hondana.R;

import java.util.ArrayList;

public class ContentsInfo {

    private ArrayList<Book> mTestContentList;
    private static ArrayList<Book> mSelectedContentList;

    public ContentsInfo() {
        createTestContents();
    }

    public ArrayList<Book> getTestContents() {
        return this.mTestContentList;
    }
    
    /**
     * テストデータを作成
     */
    private void createTestContents() {
        mTestContentList = new ArrayList<Book>();
        mTestContentList.add(new Book("AAA", R.drawable.sample1, "ZZZ"));
        mTestContentList.add(new Book("BBB", R.drawable.sample2, "YYY"));
        mTestContentList.add(new Book("CCC", R.drawable.sample3, "XXX"));
        mTestContentList.add(new Book("DDD", R.drawable.sample4, "GGG"));
        mTestContentList.add(new Book("EEE", R.drawable.sample5, "FFF"));
        mTestContentList.add(new Book("FFF", R.drawable.sample6, "EEE"));
        mTestContentList.add(new Book("GGG", R.drawable.sample7, "DDD"));
        mTestContentList.add(new Book("XXX", R.drawable.sample8, "CCC"));
        mTestContentList.add(new Book("YYY", R.drawable.sample9, "BBB"));
        mTestContentList.add(new Book("ZZZ", R.drawable.sample10, "AAA"));
    }

    /**
     * checkboxで選択されたコンテンツを返す
     * @return
     */
    public static ArrayList<Book> getSelectedContents() {
        return mSelectedContentList;
    }
    
    public static void setSelectedContents(ArrayList<Book> contentList) {
        mSelectedContentList = contentList;
    }
    
    /**
     * 指定順でコンテンツリストを並び替え
     * 
     * @param contentList　並び替え用コンテンツリスト
     * @param sortType ソート順
     * @return　並び替えたコンテンツ
     */
    public static ArrayList<Book> sortByTitle(ArrayList<Book> contentList, int sortType) {
        switch (sortType) {
            case Const.SORT_BY_CONTENT_TITLE:
                Collections.sort(contentList, new Comparator<Book>(){

                    @Override
                    public int compare(Book book1, Book book2) {
                        String title1 = book1.getBookTitle();
                        String title2 = book2.getBookTitle();
                        return Collator.getInstance(Locale.US).compare(title1, title2);
                    }
                    
                });
                break;
            case Const.SORT_BY_CONTENT_AUTHOR:
                Collections.sort(contentList, new Comparator<Book>(){

                    @Override
                    public int compare(Book book1, Book book2) {
                        String author1 = book1.getBookAuthor();
                        String author2 = book2.getBookAuthor();
                        return Collator.getInstance(Locale.US).compare(author1, author2);
                    }
                    
                });
                break;
        }
        
        return contentList;
    }
    
}
