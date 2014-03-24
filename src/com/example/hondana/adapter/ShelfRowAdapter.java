
package com.example.hondana.adapter;

import android.widget.ListView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hondana.Const;
import com.example.hondana.R;
import com.example.hondana.async.ImageLoader;
import com.example.hondana.book.Book;
import com.example.hondana.fragment.BookShelfFragment;
import java.util.ArrayList;

public class ShelfRowAdapter extends BaseAdapter {

    private Activity mActivity;
    private Context mContext;
    private ViewHolder viewHolder;
    /** 本棚Fragment */
    private BookShelfFragment mBookShelfFragment;
    /** 本棚表示内容リスト */
    private ArrayList<ShelfRow> mRowList;
    /** getViewを行っている行 */
    private ShelfRow mCurrentRow;
    /** getViewを行っている行のview */
    private LinearLayout mRowView;

    private int mCellsNumInRow;
    /** getViewを行っている行にあるる各コンテンツview */
    private RelativeLayout mItemView;
    /** 本棚表示スタイル */
    private int mShelfStyle;

    // /** 当前正在创建的getView的item所处行数是否为gridView的第一行 */
    // private static boolean mIsHeader;
    // /** 当前正在创建的getView的item所处行数是否为gridView的最后一行 */
    // private static boolean mIsBottom;
    // /** 前一个创建的getView的item的位置 */
    // private int lastPosition = -1;
    // private final static int SCROLL_UP = 0;
    // private final static int SCROLL_DOWN = 1;
    // /** 滚轴滚动方向 */
    // private int mScrollOrientation;

    public ShelfRowAdapter(Context context, ArrayList<ShelfRow> rowList,
            BookShelfFragment bookShelfFragment) {
        mContext = context;
        mBookShelfFragment = bookShelfFragment;
        mActivity = (Activity) context;
        mRowList = rowList;
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

    @Override
    public View getView(final int row, View convertView, ViewGroup parent) {
        mRowView = (LinearLayout) convertView;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ListView mListView = (ListView) mActivity.findViewById(R.id.shelf_listview_v);
        mCurrentRow = (ShelfRow) getItem(row);
        // 行にあるコンテンツ数
        mCellsNumInRow = mCurrentRow.getBookListInRow().size();
        if (convertView == null) {
            mRowView = (LinearLayout) inflater.inflate(R.layout.bookshelf_row, null);

            // 行にあるコンテンツを行のviewに追加する、△△△要修正
            for (int i = 0; i < mCellsNumInRow; i++) {
                mItemView = (RelativeLayout) inflater.inflate(R.layout.book_list_item, mRowView,
                        false);
                viewHolder = new ViewHolder();
                viewHolder.bookImageView = (ImageView) mItemView.findViewById(R.id.bookimage);
                viewHolder.bookCheckBox = (CheckBox) mItemView.findViewById(R.id.checkbox);
                setImage(viewHolder.bookImageView, mCurrentRow.getBookListInRow().get(i));

                viewHolder.bookTitleView = (TextView) mItemView.findViewById(R.id.book_title);
                viewHolder.bookAuthorView = (TextView) mItemView.findViewById(R.id.book_author);
                viewHolder.bookDetailsLayout = (RelativeLayout) mItemView
                        .findViewById(R.id.item_details_layout);

                mRowView.addView(mItemView);
            }
            mRowView.setPadding(10, 5, 15, 47);

            // △△△　setTagの役割は、後で要調査
            mRowView.setTag(viewHolder);
        } else {
            mRowView = (LinearLayout) convertView;
            viewHolder = (ViewHolder) mRowView.getTag();
            for (int i = 0; i < mCellsNumInRow; i++) {
                setImage(viewHolder.bookImageView, mCurrentRow.getBookListInRow().get(i));
            }
        }

        // 行にある各コンテンツのイベントを定義　△△△要整理
        for (int i = 0; i < mRowView.getChildCount(); i++) {
            final int column = i;
            final RelativeLayout bookView = (RelativeLayout) mRowView.getChildAt(i);
            // △△△　为什么这样写出错
            viewHolder.bookImageView = (ImageView) bookView.findViewById(R.id.bookimage);
            viewHolder.bookCheckBox = (CheckBox) bookView.findViewById(R.id.checkbox);
            viewHolder.bookCheckBox.setOnCheckedChangeListener(mBookShelfFragment
                    .onCheckedChangeListener(row, column, viewHolder.bookImageView));
            // 表示モード(編集　/ 普通)により、checkboxを表示するかの判断
            if (mBookShelfFragment.getIsEdit()) {
                viewHolder.bookCheckBox.setVisibility(View.VISIBLE);
                bookView.setOnClickListener(mBookShelfFragment.onEditClickListener());
            } else {
                viewHolder.bookCheckBox.setVisibility(View.INVISIBLE);
                bookView.setOnClickListener(mBookShelfFragment.onNoEditClickListener(row, column));
            }
            bookView.setOnLongClickListener(mBookShelfFragment.onLongClickListener());

            // リスト表示の場合、コンテンツ詳細情報とソート機能レイアウトを表示
            if (mShelfStyle == Const.LIST) {
                viewHolder.bookDetailsLayout.setVisibility(View.VISIBLE);
                viewHolder.bookTitleView.setText(mCurrentRow.getBookListInRow().get(i)
                        .getBookTitle());
                viewHolder.bookAuthorView.setText(mCurrentRow.getBookListInRow().get(i)
                        .getBookAuthor());
            }
        }

        if (mShelfStyle == Const.LIST) {
            mListView.findViewById(R.id.sort).setVisibility(View.VISIBLE);
        }
        return mRowView;
    }

    static class ViewHolder {
        ImageView bookImageView;
        TextView bookImageTitleView;
        CheckBox bookCheckBox;
        RelativeLayout bookDetailsLayout;
        TextView bookTitleView;
        TextView bookAuthorView;
    }

    /**
     * 本棚コンテンツのthumb　nailを設定
     * 
     * @param imageView　
     * @param book　コンテンツ
     */
    private void setImage(ImageView imageView, Book book) {
        // Asynctaskに入り、bitmapのresizeやcache計算などを行う
        ImageLoader loader = new ImageLoader(imageView, mActivity);
        loader.execute(book);
    }
}
