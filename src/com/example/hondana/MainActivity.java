
package com.example.hondana;

import android.content.res.Configuration;

import android.widget.CheckBox;

import android.widget.Toast;

import android.view.View.OnLongClickListener;

import android.widget.AdapterView.OnItemLongClickListener;

import android.widget.AdapterView.OnItemClickListener;

import android.view.View.OnClickListener;

import android.view.View;
import android.widget.AdapterView;

import com.example.hondana.adapter.BookAdapter;

import android.widget.GridView;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;

public class MainActivity extends Activity implements OnItemLongClickListener {

    /** Not recent read contents */
    private static final boolean IS_NOT_RECENT = false;
    /** recent read contents */
    private static final boolean IS_RECENT = true;

    /** gridview for all books */
    private GridView mGridView;
    /** gridview for recent books */
    private GridView mRecGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 纵向
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.hondana_main_vertical);
            // 之后需把grid view class化
            mRecGridView = (GridView) this.findViewById(R.id.shelf_recgridview_v);
            // grid view最近读过的书，设置适配器
            mRecGridView.setAdapter(new BookAdapter(this, IS_RECENT));
            mGridView = (GridView) this.findViewById(R.id.shelf_gridview_v);
            // grid view所有书，设置适配器
            mGridView.setAdapter(new BookAdapter(this, IS_NOT_RECENT));

            // 横向
        } else {
            setContentView(R.layout.hondana_main_horizontal);
            mGridView = (GridView) this.findViewById(R.id.shelf_gridview_h);
            mGridView.setAdapter(new BookAdapter(this, IS_NOT_RECENT));
            mRecGridView = (GridView) this.findViewById(R.id.shelf_recgridview_h);
            mRecGridView.setAdapter(new BookAdapter(this, IS_RECENT));

        }
        // 设定grid view item常点击
        mGridView.setOnItemLongClickListener(this);

        // 设置onClick效果，需要调查
        // bookGridView.setSelector(android.R.color.transparent);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {
        Toast.makeText(this, "test postion: " + String.valueOf(position), Toast.LENGTH_SHORT)
                .show();

        // 显示所有的check box，要修改
        for (int i = 0; i < mGridView.getChildCount(); i++) {
            CheckBox cb = (CheckBox) mGridView.getChildAt(i).findViewById(R.id.checkbox);
            cb.setVisibility(View.VISIBLE);
        }

        // 给图片加边框，之后放到touch事件里
        return false;
    }
    
    public void showSelected(View view){
        
    }

}
