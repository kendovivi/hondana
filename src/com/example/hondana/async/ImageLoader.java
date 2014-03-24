
package com.example.hondana.async;

import android.widget.ImageView;

import com.example.hondana.book.Book;
import com.example.hondana.utils.BitMapTools;

import android.app.Activity;
import android.graphics.Bitmap;

import android.os.AsyncTask;

public class ImageLoader extends AsyncTask<Book, Integer, Bitmap> {

    private Activity mActivity;
    /** drawableにあるコンテンツ画像Id */
    private int mContentImgId;
    /** コンテンツthumb nail読み込み中の画像 */
    private Bitmap mLoadingCover;
    /** コンテンツthumb nailのimageView */
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
