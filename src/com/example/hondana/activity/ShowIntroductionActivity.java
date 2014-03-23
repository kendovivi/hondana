
package com.example.hondana.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hondana.Const;
import com.example.hondana.R;
import com.example.hondana.book.Book;
import com.example.hondana.utils.BitMapTools;

import java.util.ArrayList;

public class ShowIntroductionActivity extends Activity {
    private static final int FROM_ALL = 0;
    private static final int FROM_SECLECTED = 1;

    private int mBookPosition;
    private Book mBookToShow;
    private ArrayList<Book> mBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_introduction);

        Book book = new Book();
        Intent intent = getIntent();
        mBookPosition = intent.getIntExtra(Const.BOOK_ONCLICK, 0);
        int flag = intent.getIntExtra(Const.FROM_ALL_OR_SEL, 0) ;

        switch (flag) {
            case FROM_ALL:
                mBookList = book.getAllBooks(this);
                break;
            case FROM_SECLECTED:
                mBookList = Book.getSelectedList();
                break;
        }

        mBookToShow = mBookList.get(mBookPosition);

        ImageView bookImageView = (ImageView) this.findViewById(R.id.intro_book_image);
        TextView bookTextView = (TextView) this.findViewById(R.id.intro_book_name);

        int resId = mBookToShow.getBookImgId();
        Bitmap bitmap = BitMapTools.decodeBitmap(getResources(), resId, 75, 100);
        bookImageView.setImageBitmap(bitmap);
        bookTextView.setText(mBookToShow.getBookName());
    }
}
