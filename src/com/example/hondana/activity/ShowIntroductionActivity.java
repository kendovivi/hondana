
package com.example.hondana.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hondana.Const;
import com.example.hondana.R;
import com.example.hondana.book.Book;
import java.util.ArrayList;

public class ShowIntroductionActivity extends Activity {
    private int mBookId;
    private Book mBook;
    private ArrayList<Book> mBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_introduction);

        Intent intent = getIntent();
        mBookId = intent.getIntExtra(Const.BOOK_ONCLICK, 0);

        Book book = new Book();
        mBookList = book.getAllBooks(this);
        mBook = mBookList.get(mBookId);

        ImageView bookImageView = (ImageView) this.findViewById(R.id.intro_book_image);
        TextView bookTextView = (TextView) this.findViewById(R.id.intro_book_name);

        bookImageView.setImageBitmap(mBook.getBookImage());
        bookTextView.setText(mBook.getBookName());
    }
}
