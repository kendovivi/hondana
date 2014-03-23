package com.example.hondana.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitMapTools {

	public BitMapTools() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param resources
	 *            资源文件
	 * @param resId
	 *            解码位图的id
	 * @param reqWidth
	 *            指定输出位图的宽度
	 * @param reqHeight
	 *            指定输出位图的高度
	 * @return
	 */
	public static Bitmap decodeBitmap(Resources resources, int resId,
			int reqWidth, int reqHeight) {
		// 对位图进行解码的参数设置
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 在对位图进行解码的过程中，避免申请内存空间
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resId, opts);
		// 对图片进行一定比例的压缩
		opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
		// 真正输出位图
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(resources, resId, opts);
	}

	public static int calculateInSampleSize(BitmapFactory.Options opts,
			int reqWidth, int reqHeight) {
		int imageHeight = opts.outHeight;
		int imageWidth = opts.outWidth;
		int inSampleSize = 1; // 压缩比例
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
