
package com.example.hondana.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.example.hondana.Const;
import com.example.hondana.R;
import com.example.hondana.activity.ShowIntroductionActivity;
import com.example.hondana.adapter.NaviDrawerListAdapter;
import com.example.hondana.adapter.ShelfRow;
import com.example.hondana.adapter.ShelfRowAdapter;
import com.example.hondana.adapter.ShelfRowInfo;
import com.example.hondana.book.Book;
import java.util.ArrayList;

public class BookShelfFragment extends Fragment implements OnClickListener {
    private Activity mActivity;
    private int mShelfStyle;
    private int mNumsPerRow;
    private Book mBook;
    private ArrayList<Book> mBookList;
    private ArrayList<Book> mSelectedBookList;
    private ShelfRowInfo mRowInfo;
    private ArrayList<ShelfRow> mRowList;
    private ShelfRowAdapter mShelfRowAdapter;

    private View view;
    /** shelf */
    private ListView mListView;
    private LinearLayout mBookshelfHeaderView;
    private LinearLayout mBookshelfFooterView;
    private Button showSelectedBtn;

    /** navi_drawer */

    private ListView mNaviListView;
    /** 画面中数据来源： 全部 / 选中 */
    private int mShowFlag;
    /** 当前是否为编辑画面 */
    private boolean mIsEdit;
    private static final int FROM_ALL = 0;
    private static final int FROM_SELECTED = 1;

    public static BookShelfFragment newInstance(int shelfStyle) {
        BookShelfFragment bookShelfFragment = new BookShelfFragment();
        Bundle data = new Bundle();
        data.putInt(Const.SHELF_STYLE, shelfStyle);
        bookShelfFragment.setArguments(data);
        return bookShelfFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mShelfStyle = getArguments().getInt(Const.SHELF_STYLE);
        // 判断书架的显示形式： 书架形式 / 列表形式
        switch (mShelfStyle) {
        // 要修改，将改成根据屏幕宽度来判断一行放多少个cell，暂时定为4个
            case Const.GRID:
                mNumsPerRow = 4;
                break;
            case Const.LIST:
                mNumsPerRow = 1;
                break;
        }
        mBook = new Book();
        // 若有选中的话，则画面切换成选中
        mSelectedBookList = Book.getSelectedList();
        if (mSelectedBookList != null) {
            mBookList = mSelectedBookList;
            mShowFlag = FROM_SELECTED;
        } else {
            mBookList = mBook.getAllBooks(mActivity);
            mShowFlag = FROM_ALL;
        }
        mRowInfo = new ShelfRowInfo(mBookList, mNumsPerRow);
        mRowList = mRowInfo.getRowList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.shelf_listview_v);
        showSelectedBtn = (Button) view.findViewById(R.id.test_show_selected_btn);
        // mWindowManager = (WindowManager)
        // mActivity.getSystemService(Context.WINDOW_SERVICE);

        // 显示选中按钮监听器设定
        showSelectedBtn.setOnClickListener(this);
        // gridview 的适配器和监听器的设定
        mBookshelfFooterView = (LinearLayout) inflater.inflate(R.layout.bookshelf_footer,
                mListView, false);
        mBookshelfHeaderView = (LinearLayout) inflater.inflate(R.layout.bookshelf_header,
                mListView, false);
        mListView.addFooterView(mBookshelfFooterView);
        mListView.addHeaderView(mBookshelfHeaderView);
        mShelfRowAdapter = new ShelfRowAdapter(mActivity, mRowList, this);
        mListView.setAdapter(mShelfRowAdapter);
        // 移动监听
        /*
         * mGridView.setOnTouchListener(new OnTouchListener() {
         * @Override public boolean onTouch(View v, MotionEvent event) { int x =
         * (int) event.getX(); int y = (int) event.getY(); int action =
         * event.getAction(); if (action == MotionEvent.ACTION_MOVE) {
         * Toast.makeText(mActivity, "当前坐标 X-" + x + " Y-" + y, 1).show(); //
         * mWindowManager.removeView((ImageView)mGridView.getChildAt(mPosition)
         * .findViewById(R.id.bookimage)); } return false; } });
         */

        // navigation drawer部分
        mNaviListView = (ListView) view.findViewById(R.id.navi_drawer_listview);
        mNaviListView.setAdapter(new NaviDrawerListAdapter(mActivity));
        mNaviListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                // 切换书架视图
                switch (position) {
                    case 0:
                        changeShelfStyle();
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_selected_btn:
                showSelected();
                break;
        }
    }

    public void showSelected() {
        ArrayList<Book> list = new ArrayList<Book>();
        for (Book book : mBookList) {
            if (book.getBookSelected()) {
                list.add(book);
            }
        }
        mSelectedBookList = list;
        Book.setSelectedList(mSelectedBookList);
        // 替换当前fragment，显示选中的booklist
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container_vertical, BookShelfFragment.newInstance(mShelfStyle));
        ft.commit();
        showSelectedBtn.setVisibility(View.INVISIBLE);
    }

    /**
     * 定义全书籍页面在非编辑模式下的点击监听
     * 
     * @param row
     * @param column
     * @return
     */
    public OnClickListener onNoEditClickListener(final int row, final int column) {
        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mActivity, ShowIntroductionActivity.class);
                int position = mShelfStyle == Const.GRID ? row * 4 + column : row;
                intent.putExtra(Const.BOOK_ONCLICK, position);
                intent.putExtra(Const.FROM_ALL_OR_SEL, mShowFlag);
                mActivity.startActivity(intent);
            }
        };
        return listener;
    }

    /**
     * 定义全书籍在编辑模式下的点击监听
     * 
     * @return
     */
    public OnClickListener onEditClickListener() {
        OnClickListener listener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox);
                if (cb.isChecked()) {
                    cb.setChecked(false);
                } else {
                    cb.setChecked(true);
                }
            }
        };

        return listener;
    }

    /**
     * 定义全书籍在非编辑模式下的长按监听
     * 
     * @return
     */
    public OnLongClickListener onLongClickListener() {
        OnLongClickListener listener = new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                ChangeToEditModeLayout();

                return false;
            }
        };
        return listener;
    }

    /**
     * 定义书籍上checkbox选中状态更改的监听
     * 
     * @param row 书籍所在行
     * @param column 书籍所在列
     * @param bookImageView 书籍的图像view
     * @return
     */
    public OnCheckedChangeListener onCheckedChangeListener(final int row, final int column,
            final ImageView bookImageView) {

        OnCheckedChangeListener listener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton checkBox, boolean isChecked) {
                Book book = mRowList.get(row).getBookListInRow().get(column);
                if (isChecked) {
                    bookImageView.setBackgroundResource(R.drawable.border_pressed);
                } else {
                    bookImageView.setBackgroundResource(R.drawable.border_no);
                }
                book.setBookSelected(isChecked);
                checkBox.setChecked(isChecked);
            }
        };

        return listener;
    }

    public int getShelfStyle() {
        return mShelfStyle;
    }

    public boolean getIsEdit() {
        return mIsEdit;
    }

    // 长点击cell之后，画面进入编辑模式 (checkbox可见， cell点击改变checkbox状态)， 设定监听
    public void ChangeToEditModeLayout() {
        mIsEdit = true;
        Button showSelBtn = (Button) mListView.findViewById(R.id.show_selected_btn);
        showSelBtn.setVisibility(View.VISIBLE);
        showSelBtn.setOnClickListener(this);

        // 重新调用adapter的getview（）， 描画新编辑画面的listview
        mListView.invalidateViews();
    }

    public void ChangeToNormalModeLayout() {
        mIsEdit = false;
        Button showSelBtn = (Button) mListView.findViewById(R.id.show_selected_btn);
        showSelBtn.setVisibility(View.GONE);
        mListView.invalidateViews();
    }

    private void changeShelfStyle() {
        mShelfStyle = mShelfStyle == Const.GRID ? Const.LIST : Const.GRID;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_vertical,
                BookShelfFragment.newInstance(mShelfStyle));
        fragmentTransaction.commit();
    }

}
