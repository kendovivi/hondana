
package com.example.hondana.fragment;

import com.example.hondana.book.ContentsInfo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hondana.Const;
import com.example.hondana.R;
import com.example.hondana.activity.SettingActivity;
import com.example.hondana.activity.ShowIntroductionActivity;
import com.example.hondana.adapter.NaviDrawerListAdapter;
import com.example.hondana.adapter.ShelfRow;
import com.example.hondana.adapter.ShelfRowAdapter;
import com.example.hondana.adapter.ShelfRowInfo;
import com.example.hondana.book.Book;

public class BookShelfFragment extends Fragment implements OnClickListener {
    private Activity mActivity;
    /** 本棚表示スタイル GRID / LIST */
    private int mShelfStyle;
    /** 本棚ソート順　 */
    private int mSortType;
    /** 各行になるコンテンツの数 */
    private int mNumsPerRow;
    /** 表示するコンテンツリスト */
    private ArrayList<Book> mBookList;
    /** checkboxで選択されたコンテンツリスト */
    private ArrayList<Book> mSelectedBookList;
    /** 行の情報を持つ変数 */
    private ShelfRowInfo mRowInfo;
    /** 行リスト */
    private ArrayList<ShelfRow> mRowList;
    /** 各行のアダプッタ */
    private ShelfRowAdapter mShelfRowAdapter;
    /** 本棚view */
    private ListView mListView;
    /** 本棚ヘッド */
    private LinearLayout mBookShelfHeaderView;
    /** 本棚フォト */
    private LinearLayout mBookShelfFooterView;
    /** 選択されたコンテンツを表示する用ボタン */
    private Button mShowSelectedBtn;
    private Button mSortByTitleBtn;
    private Button mSortByAuthorBtn;

    /** Navigation Drawer view */
    private ListView mNaviListView;
    /** 表示するデータソース： 全部 / checkboxの選択 */
    private int mShowFlag;
    /** 本棚モードは編集モードであるか */
    private boolean mIsEdit;
    private static final int FROM_ALL = 0;
    private static final int FROM_SELECTED = 1;

    /**
     * @param shelfStyle　本棚表示スタイル
     * @param sortType　ソート順
     * @return
     */
    public static BookShelfFragment newInstance(int shelfStyle, int sortType) {
        BookShelfFragment bookShelfFragment = new BookShelfFragment();
        Bundle data = new Bundle();
        data.putInt(Const.SHELF_STYLE, shelfStyle);
        data.putInt(Const.SORT_TYPE, sortType);
        bookShelfFragment.setArguments(data);
        return bookShelfFragment;
    }

    /**
     * 本棚のfragmentをインスタンスする。ソート順を指定しない場合、デフォルトでタイトル順にする
     * 
     * @param shelfStyle　本棚表示スタイル
     * @return
     */
    public static BookShelfFragment newInstance(int shelfStyle) {
        return BookShelfFragment.newInstance(shelfStyle, Const.SORT_BY_CONTENT_TITLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mShelfStyle = getArguments().getInt(Const.SHELF_STYLE);

        System.out.println("-- shelfstyle --- >>" + mShelfStyle);
        switch (mShelfStyle) {
            case Const.GRID:
                // 行にあるコンテンツ数を計算
                mNumsPerRow = caculateNumInRow(mActivity);
                break;
            case Const.LIST:
                mNumsPerRow = 1;
                break;
        }
        // △△△選択リストがある場合、選択リストを表示する　要修正
        mSelectedBookList = ContentsInfo.getSelectedContents();

        if (mSelectedBookList != null) {
            mBookList = mSelectedBookList;
            mShowFlag = FROM_SELECTED;
        } else {
            ContentsInfo contentsInfo = new ContentsInfo();
            mBookList = contentsInfo.getTestContents();
            mShowFlag = FROM_ALL;
        }
        mSortType = getArguments().getInt(Const.SORT_TYPE);
        mBookList = ContentsInfo.sortByTitle(mBookList, mSortType);
        // コンテンツ数情報により、行リストを作る
        mRowInfo = new ShelfRowInfo(mBookList, mNumsPerRow);
        mRowList = mRowInfo.getRowList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.shelf_listview_v);
        mShowSelectedBtn = (Button) view
                .findViewById(R.id.test_show_selected_btn);
        // Navigation Drawerレイアウト
        mNaviListView = (ListView) view.findViewById(R.id.navi_drawer_listview);
        // ヘッドとフォトのレイアウト
        mBookShelfFooterView = (LinearLayout) inflater.inflate(
                R.layout.bookshelf_footer, mListView, false);
        mBookShelfHeaderView = (LinearLayout) inflater.inflate(
                R.layout.bookshelf_header, mListView, false);

        // 删除预定
        mSortByTitleBtn = (Button) mBookShelfHeaderView.findViewById(R.id.sort_btn_byTitle);
        mSortByAuthorBtn = (Button) mBookShelfHeaderView.findViewById(R.id.sort_btn_byAuthor);
        if (mSortType == Const.SORT_BY_CONTENT_TITLE) {
            mSortByTitleBtn.setBackgroundResource(R.drawable.border_pressed);
            mSortByAuthorBtn.setBackgroundResource(R.drawable.border_no);
        } else {
            mSortByTitleBtn.setBackgroundResource(R.drawable.border_no);
            mSortByAuthorBtn.setBackgroundResource(R.drawable.border_pressed);
        }

        mSortByTitleBtn.setOnClickListener(this);
        mSortByAuthorBtn.setOnClickListener(this);

        return view;
    }

    // @Override
    // public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // inflater.inflate(R.menu.hondana_menu, menu);
    // super.onCreateOptionsMenu(menu, inflater);
    // }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mShowSelectedBtn.setOnClickListener(this);
        // 　本棚背景を描画する時一行目がヘッドを分かれるようにヘッドのviewにtagをつける
        mBookShelfHeaderView.setTag("ListViewHeader");
        // 本棚にヘッド、フォトを追加
        mListView.addFooterView(mBookShelfFooterView);
        mListView.addHeaderView(mBookShelfHeaderView);
        // 本棚各行内容adapterを設定
        mShelfRowAdapter = new ShelfRowAdapter(mActivity, mRowList, this);
        mListView.setAdapter(mShelfRowAdapter);
        // Navigation Drawer　
        mNaviListView.setAdapter(new NaviDrawerListAdapter(mActivity));
        mNaviListView.setOnItemClickListener(onNaviDrawerItemClickListener());
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.show_selected_btn:
                showSelected();
                break;
            case R.id.sort_btn_byTitle:
                if (mSortType == Const.SORT_BY_CONTENT_TITLE) {
                    Toast.makeText(mActivity, "今の並び順は既にタイトル順です", 1).show();
                } else {
                    ft.replace(R.id.fragment_container_vertical,
                            BookShelfFragment.newInstance(mShelfStyle, Const.SORT_BY_CONTENT_TITLE));
                    // mSortByAuthorBtn.setBackgroundResource(R.drawable.border_no);
                    // mSortByTitleBtn.setBackgroundResource(R.drawable.border_pressed);
                }
                break;
            case R.id.sort_btn_byAuthor:
                if (mSortType == Const.SORT_BY_CONTENT_AUTHOR) {
                    Toast.makeText(mActivity, "今の並び順は既に著者順です", 1).show();
                } else {
                    ft.replace(R.id.fragment_container_vertical, BookShelfFragment.newInstance(
                            mShelfStyle, Const.SORT_BY_CONTENT_AUTHOR));
                    // mSortByTitleBtn.setBackgroundResource(R.drawable.border_no);
                    // mSortByAuthorBtn.setBackgroundResource(R.drawable.border_pressed);
                }
                break;
        }
        ft.commit();
    }

    /**
     * checkboxで選択されたコンテンツを表示する
     */
    public void showSelected() {
        ArrayList<Book> list = new ArrayList<Book>();
        for (Book book : mBookList) {
            if (book.getBookSelected()) {
                list.add(book);
            }
        }
        mSelectedBookList = list;
        ContentsInfo.setSelectedContents(mSelectedBookList);
        // △△△fragmentを再描画する。選択されたコンテンツリストが空ではないので、表示される△△△
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container_vertical,
                BookShelfFragment.newInstance(mShelfStyle));
        ft.commit();
        mShowSelectedBtn.setVisibility(View.INVISIBLE);
    }

    /**
     * 本棚普通モードでのコンテンツthumb nailクリックイベント定義
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
                int position = mShelfStyle == Const.GRID ? row * 4 + column
                        : row;
                intent.putExtra(Const.BOOK_ONCLICK, position);
                intent.putExtra(Const.FROM_ALL_OR_SEL, mShowFlag);
                mActivity.startActivity(intent);
            }
        };
        return listener;
    }

    /**
     * 本棚編集モードでのコンテンツthumb nail長押しイベント定義　△△△(同下合并预定)
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
     * 本棚普通モードでのコンテンツthumb nail長押しイベント定義
     * 
     * @return
     */
    public OnLongClickListener onLongClickListener() {
        OnLongClickListener listener = new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                ChangeShelfMode();

                // 拖动设定开始（要修改）
                // ClipData.Item item = new ClipData.Item("icondrag");
                // ClipData dragData = new ClipData("icondrag",
                // new String[] {
                // ClipDescription.MIMETYPE_TEXT_PLAIN
                // },
                // item);
                // View.DragShadowBuilder shadow = new DragShadowBuilder(v);
                // v.startDrag(dragData, shadow, null, 0);
                // v.setOnDragListener(onDragListener);
                // // 拖动设定结束
                return false;
            }
        };
        return listener;
    }

    /**
     * コンテンツCheckboxのcheckステータスが変わった時のイベント定義
     * 
     * @param row コンテンツがある行
     * @param column コンテンツがある列
     * @param bookImageView コンテンツのthumb nail image view
     * @return
     */
    public OnCheckedChangeListener onCheckedChangeListener(final int row,
            final int column, final ImageView bookImageView) {

        OnCheckedChangeListener listener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton checkBox,
                    boolean isChecked) {
                Book book = mRowList.get(row).getBookListInRow().get(column);
                // checkされた場合thumb　nailにborderをつける
                if (isChecked) {
                    bookImageView
                            .setBackgroundResource(R.drawable.border_pressed);
                    // checkがはずされた場合　thumb nailのborderを消す
                } else {
                    bookImageView.setBackgroundResource(R.drawable.border_no);
                }
                // △△△ ????
                book.setBookSelected(isChecked);
                checkBox.setChecked(isChecked);
            }
        };

        return listener;
    }

    /**
     * Navigation Drawer各アイテムイベント定義
     * 
     * @return
     */
    private OnItemClickListener onNaviDrawerItemClickListener() {
        OnItemClickListener listener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                    int position, long arg3) {
                switch (position) {
                // 本棚スタイル切り替え　(本棚表示　/　リスト表示)
                    case 0:
                        changeShelfStyle();
                        break;
                    // 設定画面を起動
                    case 4:
                        Intent intent = new Intent();
                        intent.setClass(mActivity, SettingActivity.class);
                        mActivity.startActivity(intent);
                        break;
                }
            }
        };
        return listener;
    }

    /** 本棚スタイルを取得 */
    public int getShelfStyle() {
        return mShelfStyle;
    }

    /** 本棚モードを取得 */
    public boolean getIsEdit() {
        return mIsEdit;
    }

    /**
     * 本棚モードの切り替え
     */
    public void ChangeShelfMode() {
        Button showSelBtn = (Button) mListView
                .findViewById(R.id.show_selected_btn);
        // 編集モードの場合、普通モードに変更し、ボタンを消す
        if (mIsEdit) {
            mIsEdit = false;
            showSelBtn.setVisibility(View.GONE);
            // 　普通モードの場合、編集モードに変更し、ボタンを表示
        } else {
            mIsEdit = true;
            showSelBtn.setVisibility(View.VISIBLE);
            showSelBtn.setOnClickListener(this);
        }
        mListView.invalidateViews();
    }

    /**
     * 本棚表示スタイルの切り替え
     */
    private void changeShelfStyle() {
        mShelfStyle = mShelfStyle == Const.GRID ? Const.LIST : Const.GRID;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_vertical,
                BookShelfFragment.newInstance(mShelfStyle));
        fragmentTransaction.commit();
    }

    /**
     * コンテンツdrag and dropイベント　未完成
     */
    public OnDragListener onDragListener = new OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            final int action = event.getAction();
            RelativeLayout view = (RelativeLayout) v;
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (event.getClipDescription().hasMimeType(
                            ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        // view.setColorFilter(Color.BLUE);

                        view.invalidate();
                        return (true);
                    } else {
                        return (false);
                    }
                case DragEvent.ACTION_DRAG_ENTERED:
                    // view.setColorFilter(Color.GREEN);
                    view.invalidate();
                    return (true);
                case DragEvent.ACTION_DRAG_LOCATION:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    // view.setColorFilter(Color.BLUE);
                    view.invalidate();
                    break;
                case DragEvent.ACTION_DROP:
                    // linearLayout 要修正
                    View droppointview = (View) event.getLocalState();

                    ViewGroup row = (ViewGroup) v.getParent();
                    int index = row.indexOfChild(droppointview);
                    row.removeView(v);

                    row.addView(v, 0);
                    view.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (event.getResult()) {
                        Log.d("drag move", "dropped");
                    } else {
                        Log.d("drag move", "drop failed");
                    }
                    break;

            }
            return false;
        }
    };

    /**
     * ディスプレイの横にコンテンツをいくつかいれられる計算を行う
     */
    public int caculateNumInRow(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width / Const.CONTENT_WIDTH;
    }

}
