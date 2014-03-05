
package com.example.hondana;

import android.graphics.Point;

import android.graphics.drawable.ColorDrawable;

import android.graphics.Color;

import android.graphics.drawable.Drawable;

import android.graphics.Canvas;

import android.view.View;

class DragBackgroundBuilder extends View.DragShadowBuilder {
    /** 拖曳时显示的背景图片 */
    private static Drawable dragBackground;

    public DragBackgroundBuilder(View v) {
        super(v);
        dragBackground = new ColorDrawable(Color.BLUE);
    }

    @Override
    public void onDrawShadow(Canvas canvas) {
        dragBackground.draw(canvas);
    }

    public void onProvideShadowMetrics(Point size, Point touch) {
        int width;
        int height;

        width = getView().getWidth();
        height = getView().getHeight();

        size.set(width, height);
        touch.set(width, height);
    };
    
    
}
