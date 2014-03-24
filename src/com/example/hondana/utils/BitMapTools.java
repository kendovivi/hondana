
package com.example.hondana.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitMapTools {

    public BitMapTools() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param resources ソースファイル
     * @param resId bitmapId
     * @param reqWidth bitmapの長さを指定
     * @param reqHeight bitmapの高さを指定
     * @return
     */
    public static Bitmap decodeBitmap(Resources resources, int resId,
            int reqWidth, int reqHeight) {
        // decode options
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // decode bitmapの際に，メモリの申請をしないように
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, opts);
        // bitmap圧縮比率を定義
        opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
        // メモリを申請
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, opts);
    }

    /**
     * 元bitmap長さ、高さと指定された長さと高さを比較する結果により、圧縮比率を決める
     * 
     * @param opts
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options opts,
            int reqWidth, int reqHeight) {
        int imageHeight = opts.outHeight;
        int imageWidth = opts.outWidth;
        // 压缩比例
        int inSampleSize = 1;
        if (imageHeight > reqHeight || imageWidth > reqWidth) {
            final int heightRatio = Math.round((float) imageHeight
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) imageWidth
                    / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

}
