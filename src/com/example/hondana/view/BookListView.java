
package com.example.hondana.view;

import android.widget.ListView;

import com.example.hondana.adapter.ShelfRowAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.GridView;
import com.example.hondana.R;

public class BookListView extends ListView {
    /** 全屏幕高 */
    private int mScreenHeight;
    /** 全屏幕宽 */
    private int mScreenWidth;
    /** 每一行的书架背景图片 */
    private Bitmap mShelfBackground;
    /** 书架底部背景图片 */
    private Bitmap mShelfFooterBackground;
    private Bitmap mShelfHeaderBackground;
    /** 列数 */
    private int mNumOfColumns;
    private boolean mIsBottom;
    private boolean mIsHeader;

    public BookListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mShelfBackground = BitmapFactory.decodeResource(getResources(),
                R.drawable.new_shelf_background);
        mShelfFooterBackground = BitmapFactory.decodeResource(getResources(),
                R.drawable.bookshelf_footer);
        mShelfHeaderBackground = BitmapFactory.decodeResource(getResources(),
                R.drawable.bookshelf_header);
        mNumOfColumns = 4;
    }

    /**
     * 计算当前屏幕的总行数，来描画书架背景图片 滚轴滚动超过一行时，该方法被执行
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        // mIsBottom = BookShelfRowAdapter.getIsGridViewBottom();
        // mIsHeader = BookShelfRowAdapter.getIsGridViewHeader();
        // 获取
        int rowCount = getChildCount() / mNumOfColumns;
        int top = rowCount > 0 ? getChildAt(0).getTop() : 0;
        int backgroundImgHeight = mShelfBackground.getHeight();
        int backgroundImgWidth = mShelfBackground.getWidth();
        int mScreenHeight = getHeight();
        int mScreenWidth = getWidth();

        for (int y = top; y < mScreenHeight; y += backgroundImgHeight) {
            for (int x = 0; x < mScreenWidth; x += backgroundImgWidth) {
                canvas.drawBitmap(mShelfBackground, x, y, null);
            }
        }
        super.dispatchDraw(canvas);
    }

    /**
     * 如果gridview滚轴不能往下继续滚动时，改边底部背景
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
            int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
            boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                maxOverScrollX, maxOverScrollY, isTouchEvent);
    }
}
