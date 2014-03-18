
package com.example.hondana.adapter;

import android.widget.Button;

import com.example.hondana.fragment.BookShelfFragment;

import android.view.View.OnLongClickListener;

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

public class ShelfRowAdapter extends BaseAdapter {

    private Context mContext;
    private BookShelfFragment mBookShelfFragment;
    private ArrayList<ShelfRow> mRowList;
    /** 当前正在创建的getView的item所处行数 */
    private ShelfRow mCurrentRow;
    /** 书架显示的风格， list/shelf */
    private int mShelfStyle;
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

    public ShelfRowAdapter(Context context, ArrayList<ShelfRow> rowList,
            BookShelfFragment bookShelfFragment) {
        mContext = context;
        mBookShelfFragment = bookShelfFragment;
        mActivity = (Activity) context;
        mRowList = rowList;
        mScrollOrientation = SCROLL_DOWN;
        mShelfStyle = mBookShelfFragment.getShelfStyle();
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
    public View getView(final int row, View convertView, ViewGroup parent) {
        mRowView = (LinearLayout) convertView;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = (ListView) mActivity.findViewById(R.id.shelf_listview_v);
        mCurrentRow = (ShelfRow) getItem(row);
        int cellsInRow = mCurrentRow.getBookListInRow().size();
        // 没被回收的话，就直接使用之前的。若被回收，则创建新的，并添加一些新属性
        if (convertView == null) {
            mRowView = (LinearLayout) inflater.inflate(R.layout.bookshelf_row, null);

            for (int i = 0; i < cellsInRow; i++) {
                mItemView = (RelativeLayout) inflater.inflate(R.layout.book_list_item, mRowView,
                        false);
                viewHolder = new ViewHolder();
                viewHolder.bookImageView = (ImageView) mItemView.findViewById(R.id.bookimage);
                viewHolder.bookCheckBox = (CheckBox) mItemView.findViewById(R.id.checkbox);
                viewHolder.bookImageView.setImageBitmap(mCurrentRow.getBookListInRow().get(i)
                        .getBookImage());

                viewHolder.bookTitleView = (TextView) mItemView.findViewById(R.id.book_title);
                viewHolder.bookAuthorView = (TextView) mItemView.findViewById(R.id.book_author);
                viewHolder.bookDetailsLayout = (RelativeLayout) mItemView
                        .findViewById(R.id.item_details_layout);

                mRowView.addView(mItemView);
            }
            mRowView.setPadding(10, 5, 15, 47);

            // settag部分要再查资料
            mRowView.setTag(viewHolder);
        } else {
            mRowView = (LinearLayout) convertView;
            viewHolder = (ViewHolder) mRowView.getTag();
            for (int i = 0; i < cellsInRow; i++) {
                viewHolder.bookImageView.setImageBitmap(mCurrentRow.getBookListInRow().get(i)
                        .getBookImage());
            }
        }

        // 各个item的监听, 要整理
        for (int i = 0; i < mRowView.getChildCount(); i++) {
            final int column = i;
            final RelativeLayout bookView = (RelativeLayout) mRowView.getChildAt(i);
            // 为什么这样写出错
            viewHolder.bookImageView = (ImageView) bookView.findViewById(R.id.bookimage);
            viewHolder.bookCheckBox = (CheckBox) bookView.findViewById(R.id.checkbox);
            viewHolder.bookCheckBox.setOnCheckedChangeListener(mBookShelfFragment
                    .onCheckedChangeListener(row, column, viewHolder.bookImageView));
            // 根据模式，判断checkbox是否显示
            if (mBookShelfFragment.getIsEdit()) {
                viewHolder.bookCheckBox.setVisibility(View.VISIBLE);
                bookView.setOnClickListener(mBookShelfFragment.onEditClickListener());
            } else {
                viewHolder.bookCheckBox.setVisibility(View.INVISIBLE);
                bookView.setOnClickListener(mBookShelfFragment.onNoEditClickListener(row, column));
            }

            // 删除预定
            // ImageView bookImage = (ImageView)
            // bookView.findViewById(R.id.bookimage);
            // CheckBox checkBox = (CheckBox)
            // bookView.findViewById(R.id.checkbox);
            // checkBox.setOnCheckedChangeListener(mBookShelfFragment.onCheckedChangeListener(row,
            // column, bookImage));
            // bookView.setOnClickListener(mBookShelfFragment.onNoEditClickListener(row,
            // column));
            bookView.setOnLongClickListener(mBookShelfFragment.onLongClickListener());

            // 如果是书架是LIST显示模式， 则显示出右边详细信息
            if (mShelfStyle == Const.LIST) {
                viewHolder.bookDetailsLayout.setVisibility(View.VISIBLE);
                viewHolder.bookTitleView.setText("test_title_view");
                viewHolder.bookAuthorView.setText("test_author_view");
            }
        }

        return mRowView;
    }

    private void setCellListener() {

    }

    static class ViewHolder {
        ImageView bookImageView;
        TextView bookImageTitleView;
        CheckBox bookCheckBox;
        RelativeLayout bookDetailsLayout;
        TextView bookTitleView;
        TextView bookAuthorView;
    }
}
