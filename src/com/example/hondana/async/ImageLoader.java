
package com.example.hondana.async;

import android.widget.ImageView;

import com.example.hondana.book.Book;

import android.graphics.Bitmap;

import android.os.AsyncTask;

public class ImageLoader extends AsyncTask<Book, Integer, Bitmap> {

    /** 读取中的图片，最终显示用 */
    private Bitmap mContentCover;
    private Bitmap mLoadingCover;
    private ImageView mImageView;
    
    public ImageLoader(ImageView imageView) {
        mImageView = imageView; 
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Book... params) {
        mContentCover = params[0].getBookImage();
        return mContentCover;
    };

    protected void onPostExecute(Bitmap bitmap) {
        mImageView.setImageBitmap(mContentCover);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        
        super.onProgressUpdate(values);
    }
}
