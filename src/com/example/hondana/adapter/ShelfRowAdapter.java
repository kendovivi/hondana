
package com.example.hondana.adapter;

import android.view.KeyEvent.DispatcherState;

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

    public ShelfRowAdapter(Context context, ArrayList<ShelfRow> rowList,
            BookShelfFragment bookShelfFragment) {
        mContext = context;
        mBookShelfFragment = bookShelfFragment;
        mActivity = (Activity) context;
        mRowList = rowList;
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
            // コンテンツの列目
            final int column = i;
            // 　コンテンツレイアウト
            final RelativeLayout bookView = (RelativeLayout) mRowView.getChildAt(i);
            // △△△　为什么这样写出错
            viewHolder.bookImageView = (ImageView) bookView.findViewById(R.id.bookimage);
            viewHolder.bookCheckBox = (CheckBox) bookView.findViewById(R.id.checkbox);
            viewHolder.bookCheckBox.setOnCheckedChangeListener(mBookShelfFragment
                    .onCheckedChangeListener(row, column, viewHolder.bookImageView));

            // 表示モードによる変更
            switch (mBookShelfFragment.getShelfMode()) {
            // 編集モードの場合　1、checkBox表示　2、thumb　nailクリックして、checkboxのcheck statusを変更
            // 3、 todo:
                case Const.SHELF_MODE_EDIT:
                    viewHolder.bookCheckBox.setVisibility(View.VISIBLE);
                    bookView.setOnClickListener(mBookShelfFragment.onEditModeClickListener());
                    break;
                // ノーマルモードの場合　1、checkBox非表示　2、thumb
                // nailクリックして、コンテンツ詳細画面に移動　　3、todo：
                case Const.SHELF_MODE_NORMAL:
                    viewHolder.bookCheckBox.setVisibility(View.INVISIBLE);
                    bookView.setOnClickListener(mBookShelfFragment.onNormalModeClickListener(row,
                            column));
                    break;
            }
            // 長押しイベント　ノーマルと編集モードを切り替え
            bookView.setOnLongClickListener(mBookShelfFragment.onLongClickListener());
            // 本棚スタイルによる変更
            switch (mBookShelfFragment.getShelfStyle()) {
            // 本棚リスト表示の場合　1、コンテンツ情報を右側に表示する
                case Const.SHELF_STYLE_LIST:
                    viewHolder.bookDetailsLayout.setVisibility(View.VISIBLE);
                    viewHolder.bookTitleView.setText(mCurrentRow.getBookListInRow().get(i)
                            .getBookTitle());
                    viewHolder.bookAuthorView.setText(mCurrentRow.getBookListInRow().get(i)
                            .getBookAuthor());
                    break;
            }

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
