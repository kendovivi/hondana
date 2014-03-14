
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

public class ShelfRowAdapter extends BaseAdapter{

    private Context mContext;
    private BookShelfFragment mBookShelfFragment;
    private ArrayList<ShelfRow> mRowList;
    /** 当前正在创建的getView的item所处行数 */
    private ShelfRow mCurrentRow;
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
    
    public ShelfRowAdapter(Context context, ArrayList<ShelfRow> rowList, BookShelfFragment bookShelfFragment) {
        mContext = context;
        mBookShelfFragment = bookShelfFragment;
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
    public View getView(final int row, View convertView, ViewGroup parent) {
        mRowView = (LinearLayout) convertView;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = (ListView) mActivity.findViewById(R.id.shelf_listview_v);
        mCurrentRow = (ShelfRow) getItem(row);
        // 没被回收的话，就直接使用之前的。若被回收，则创建新的，并添加一些新属性
        if (convertView == null) {
            mRowView = (LinearLayout) inflater.inflate(R.layout.bookshelf_row, null);
            for (int i = 0; i < mCurrentRow.getBookListInRow().size(); i++) {
                mItemView = (RelativeLayout) inflater.inflate(R.layout.book_list_item, mRowView,
                        false);
                viewHolder = new ViewHolder();
                viewHolder.bookImageView = (ImageView) mItemView.findViewById(R.id.bookimage);
                viewHolder.bookCheckBox = (CheckBox) mItemView.findViewById(R.id.checkbox);
                viewHolder.bookImageView.setImageBitmap(mCurrentRow.getBookListInRow().get(i).getBookImage());
                
                mRowView.addView(mItemView);
            }
            mRowView.setPadding(10, 5, 15, 47);
            
            // ?
            mRowView.setTag(viewHolder);
        } else {
            // ?
            viewHolder = (ViewHolder) mRowView.getTag();
        }
        
        //各个item的监听, 要修改，是否需要放到fragment
        for (int i=0; i < mRowView.getChildCount(); i++) {
            final int column = i;
            final RelativeLayout bookView = (RelativeLayout) mRowView.getChildAt(i);
            //为什么这样写出错
//            viewHolder.bookImageView = (ImageView) bookView.findViewById(R.id.bookimage);
//            viewHolder.bookCheckBox = (CheckBox) bookView.findViewById(R.id.checkbox);
            final ImageView bookImage = (ImageView) bookView.findViewById(R.id.bookimage);
            CheckBox checkBox = (CheckBox) bookView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(mBookShelfFragment.onCheckedChangeListener(row, column, bookImage));
            bookView.setOnClickListener(mBookShelfFragment.onNoEditClickListener(row, column));
            
            bookView.setOnLongClickListener(new OnLongClickListener() {
                
                @Override
                public boolean onLongClick(View paramView) {
                    final Button btn = (Button) mListView.findViewById(R.id.show_selected_btn);
                    btn.setVisibility(View.VISIBLE);
                    btn.setOnClickListener(new OnClickListener() {
                        
                        @Override
                        public void onClick(View paramView) {
                            for (int i=1;i<=mRowList.size();i++){
                                LinearLayout rowView = (LinearLayout) mListView.getChildAt(i);
                                for (int j=0;j<rowView.getChildCount();j++){
                                    CheckBox cb = (CheckBox) rowView.getChildAt(j).findViewById(R.id.checkbox);
                                    cb.setVisibility(View.INVISIBLE);
                                }
                            }
                            btn.setVisibility(View.GONE);
                            
                            mBookShelfFragment.showSelected();
                        }
                    });
                    
                    //listview中第一行是header，目前为止不包含cell
                    for (int i=1;i<=mRowList.size();i++){
                        LinearLayout rowView = (LinearLayout) mListView.getChildAt(i);
                        for (int j=0;j<rowView.getChildCount();j++){
                            CheckBox cb = (CheckBox) rowView.getChildAt(j).findViewById(R.id.checkbox);
                            cb.setVisibility(View.VISIBLE);
                            rowView.getChildAt(j).setOnClickListener(mBookShelfFragment.onEditClickListener());
                        }
                    }
                    return false;
                }
            });
        }

        return mRowView;
    }

    static class ViewHolder {
        ImageView bookImageView;
        TextView bookImageTitleView;
        CheckBox bookCheckBox;
    }
}
