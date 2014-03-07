package com.example.hondana.activity;

import com.example.hondana.fragment.BookShelfFragment;

import android.app.FragmentTransaction;

import android.app.FragmentManager;

import com.example.hondana.R;

import com.example.hondana.book.Book;

import java.util.ArrayList;

import android.os.Bundle;

import android.app.Activity;

public class ShowSelectedBooksActivity extends Activity {
    
    private ArrayList<Book> mSelBookList;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hondana_main_vertical);
        mSelBookList = Book.getSelectedList();
        
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_vertical, new BookShelfFragment());
        fragmentTransaction.commit();
    }
}
