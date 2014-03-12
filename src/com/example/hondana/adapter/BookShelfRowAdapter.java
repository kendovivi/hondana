
package com.example.hondana.adapter;

import com.example.hondana.Const;

import com.example.hondana.activity.ShowIntroductionActivity;

import android.content.Intent;

import android.widget.Toast;

import android.view.View.OnClickListener;

import android.widget.RelativeLayout;

import android.widget.LinearLayout;

import android.widget.ListView;

import com.example.hondana.view.BookListView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hondana.R;
import com.example.hondana.book.Book;
import java.util.ArrayList;

public class BookShelfRowAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<BookShelfRow> mRowList;
    /** 当前正在创建的getView的item所处行数 */
    private BookShelfRow mCurrentRow;
    /** 当前正在创建的getView的item所处行数是否为gridView的第一行 */
    private static boolean mIsHeader;
    /** 当前正在创建的getView的item所处行数是否为gridView的最后一行 */
    private static boolean mIsBottom;
    /** 前一个创建的getView的item的位置 */
    private int lastPosition = -1;
    private final static int SCROLL_UP = 0;
    private final static int SCROLL_DOWN = 1;
    /** 滚轴滚动方向 */
    private int mScrollOrientation;
    private LinearLayout mRowView;
    private RelativeLayout mItemView;

    private Activity mActivity;
    private ListView mListView;
    private ViewHolder viewHolder;

    public BookShelfRowAdapter(Context context, ArrayList<BookShelfRow> rowList) {
        mContext = context;
        mActivity = (Activity) context;
        mRowList = rowList;
        mScrollOrientation = SCROLL_DOWN;
    }

    @Override
    public int getCount() {
        return mRowList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRowList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 滚轴滚动时，每个item都会call这个方法
     * 该处将当前行，是否为gridview底部等信息传入BookGridView，dispatchDraw会根据此信息来描绘背景（顶部，内容，底部）
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 将背景描绘信息传入BookGridView
        // int itemsPerRow = 0;
        // if (mListView != null) {
        // //itemsPerRow = mListView.getNumColumns();
        // }
        // if (position > lastPosition) {
        // mScrollOrientation = SCROLL_DOWN;
        // } else {
        // mScrollOrientation = SCROLL_UP;
        // }
        // if (position >= getCount() - (itemsPerRow + 1) && mScrollOrientation
        // == SCROLL_DOWN) {
        // setIsGridViewBottom(true);
        // } else {
        // setIsGridViewBottom(false);
        // }
        // if (position <= itemsPerRow || position == 0) {
        // setIsGridViewHeader(true);
        // } else {
        // setIsGridViewHeader(false);
        // }
        // lastPosition = position;

        mRowView = (LinearLayout) convertView;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = (ListView) mActivity.findViewById(R.id.shelf_listview_v);
        mCurrentRow = (BookShelfRow) getItem(position);
        // 没被回收的话，就直接使用之前的。若被回收，则创建新的，并添加一些新属性
        if (convertView == null) {
            mRowView = (LinearLayout) inflater.inflate(R.layout.bookshelf_row, null);

            for (int i = 0; i < mCurrentRow.getBookListInRow().size(); i++) {
                mItemView = (RelativeLayout) inflater.inflate(R.layout.book_list_item, mRowView,
                        false);
                viewHolder = new ViewHolder();
                viewHolder.bookImageView = (ImageView) mItemView.findViewById(R.id.bookimage);
                viewHolder.bookImageView.setImageBitmap(mCurrentRow.getBookListInRow().get(i).getBookImage());
                viewHolder.bookCheckBox = (CheckBox) mItemView.findViewById(R.id.checkbox);
                // 复选框点击监听
                viewHolder.bookCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            buttonView.setChecked(true);
                            viewHolder.bookImageView
                                    .setBackgroundResource(R.drawable.border_pressed);
                            // mRowList.get(position).setBookSelected(true);

                        } else {
                            buttonView.setChecked(false);
                            viewHolder.bookImageView.setBackgroundResource(R.drawable.border_no);
                            // mBookList.get(position).setBookSelected(false);
                        }
                    }

                });
                mItemView.setOnClickListener(new OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, ShowIntroductionActivity.class);
                        intent.putExtra(Const.BOOK_ONCLICK, 0);
                        mActivity.startActivity(intent);
                    }
                });
                mRowView.addView(mItemView);
            }
            mRowView.setPadding(10, 5, 15, 47);
            
            // ?
            mRowView.setTag(viewHolder);
        } else {
            // ?
            viewHolder = (ViewHolder) mRowView.getTag();
        }

        // viewHolder.bookImageView.setBackgroundResource(R.drawable.sample1);
        // viewHolder.bookImageView.setImageBitmap(mRowList.get(position).getBookImage());
        return mRowView;
    }

    static class ViewHolder {
        ImageView bookImageView;
        TextView bookImageTitleView;
        CheckBox bookCheckBox;
    }

    private void setIsGridViewBottom(boolean isBottom) {
        this.mIsBottom = isBottom;
    }

    public static boolean getIsGridViewBottom() {
        return mIsBottom;
    }

    private void setIsGridViewHeader(boolean isHeader) {
        this.mIsHeader = isHeader;
    }

    public static boolean getIsGridViewHeader() {
        return mIsHeader;
    }
}
