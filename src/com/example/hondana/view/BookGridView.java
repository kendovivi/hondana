package com.example.hondana.view;

import android.graphics.Canvas;

import com.example.hondana.R;

import android.graphics.BitmapFactory;

import android.graphics.Bitmap;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class BookGridView extends GridView {
    /** 每一行的书架背景图片 */
    private Bitmap mShelfBackground;
    /** 书架底部背景图片 */
    private Bitmap mShelfFooterBackground;
    /** 列数 */
    private int mNumOfColumns;

    public BookGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mShelfBackground = BitmapFactory.decodeResource(getResources(), R.drawable.new_shelf_background);
        mShelfFooterBackground = BitmapFactory.decodeResource(getResources(), R.drawable.bookshelf_footer);
    }

    /**
     * 计算当前屏幕的总行数，来描画书架背景图片
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        //获取
        int rowCount = getChildCount()/mNumOfColumns;
        int top = rowCount > 0? getChildAt(0).getTop() : 0;
        int backgroundImgHeight = mShelfBackground.getHeight();
        int backgroundImgWidth = mShelfBackground.getWidth();
        int screenHeight = getHeight();
        int screenWidth = getWidth();

        for (int y=top; y < screenHeight; y += backgroundImgHeight) {
            for (int x=0; x < screenWidth; x+=backgroundImgWidth) {
                if (y > screenHeight - backgroundImgHeight) {
                    canvas.drawBitmap(mShelfFooterBackground, x, y, null);
                } else {
                    canvas.drawBitmap(mShelfBackground, x, y, null);
                }
            }
        }
        super.dispatchDraw(canvas);
    }
}
