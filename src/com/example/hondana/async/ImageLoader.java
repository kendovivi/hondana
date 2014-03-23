
package com.example.hondana.async;

import android.widget.ImageView;

import com.example.hondana.book.Book;
import com.example.hondana.utils.BitMapTools;

import android.app.Activity;
import android.graphics.Bitmap;

import android.os.AsyncTask;

public class ImageLoader extends AsyncTask<Book, Integer, Bitmap> {

	private Activity mActivity;
    /** 读取中的图片，最终显示用 */
    private int mContentImgId;
    //private Bitmap mLoadingCover;
    private ImageView mImageView;
    
    public ImageLoader(ImageView imageView, Activity activity) {
        mImageView = imageView; 
        mActivity = activity;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Book... params) {
        mContentImgId = params[0].getBookImgId();
        Bitmap bitmap = BitMapTools.decodeBitmap(mActivity.getResources(), mContentImgId, 75, 100);
        return bitmap;
    };

    protected void onPostExecute(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        
        super.onProgressUpdate(values);
    }
}
