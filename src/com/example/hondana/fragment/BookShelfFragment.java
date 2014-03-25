
package com.example.hondana.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.example.hondana.book.ContentsInfo;
import java.util.ArrayList;

public class BookShelfFragment extends Fragment implements OnClickListener {
    private Activity mActivity;
    /** 本棚表示スタイル GRID / LIST */
    private int mShelfStyle;
    /** 本棚ソート順　 */
    private int mSortType;
    /** 本棚の下にmenuを表示するか */
    private boolean mHasMenu;
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
    private Menu mMenu;

    /** Navigation Drawer layout */
    private DrawerLayout mNaviDrawerLayout;
    /** Navigation Drawer view */
    private ListView mNaviListView;
    /** 表示するデータソース： 全部 / checkboxの選択 */
    private int mShowFlag;
    /** 本棚モードは編集モードであるか */
    private int mShelfMode;
    private static final int FROM_ALL = 0;
    private static final int FROM_SELECTED = 1;

    /**
     * @param shelfStyle　本棚表示スタイル
     * @param sortType　ソート順
     * @return
     */
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
        mShelfMode = Const.SHELF_MODE_NORMAL;
        mShelfStyle = getArguments().getInt(Const.SHELF_STYLE);

        if (savedInstanceState != null && savedInstanceState.containsKey(Const.SORT_TYPE)) {
            mSortType = savedInstanceState.getInt(Const.SORT_TYPE);
        } else {
            // デフォルトでタイトル順で並び
            mSortType = Const.SORT_BY_CONTENT_TITLE;
        }

        System.out.println("-- shelfstyle --- >>" + mShelfStyle);

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

        setRowListUponShelfStyle(mShelfStyle, mBookList);

        // △△△問題あり、menuが複数追加されることになった
        if (mShelfStyle == Const.SHELF_STYLE_LIST) {
            mHasMenu = true;
            setHasOptionsMenu(mHasMenu);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.shelf_listview_v);
        //
        mNaviDrawerLayout = (DrawerLayout) view.findViewById(R.id.navi_drawer_layout);
        // Navigation Drawerリスト
        mNaviListView = (ListView) view.findViewById(R.id.navi_drawer_listview);
        // ヘッドとフォトのレイアウト
        mBookShelfFooterView = (LinearLayout) inflater.inflate(
                R.layout.bookshelf_footer, mListView, false);
        mBookShelfHeaderView = (LinearLayout) inflater.inflate(
                R.layout.bookshelf_header, mListView, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.mMenu = menu;
        inflater.inflate(R.menu.hondana_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 本棚背景を描画する時一行目がヘッドを分かれるようにヘッドのviewにtagをつける
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        // タイトル順で並び替え
            case R.id.actionbar_sort_by_title:
                if (mSortType == Const.SORT_BY_CONTENT_TITLE) {
                    Toast.makeText(mActivity, "今の並び順は既にタイトル順です", Toast.LENGTH_SHORT).show();
                } else {
                    sortContentsInListView(Const.SORT_BY_CONTENT_TITLE);
                }
                break;
            // 著者順で並び替え
            case R.id.actionbar_sort_by_author:
                if (mSortType == Const.SORT_BY_CONTENT_AUTHOR) {
                    Toast.makeText(mActivity, "今の並びは既に著者順です", Toast.LENGTH_SHORT).show();
                } else {
                    sortContentsInListView(Const.SORT_BY_CONTENT_AUTHOR);
                }
                break;
            case R.id.actionbar_show_selected:
                showSelected();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_selected_btn:
                showSelected();
                break;
        }
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
        if (mSelectedBookList.size() >0) {
        ContentsInfo.setSelectedContents(mSelectedBookList);
        setRowListUponShelfStyle(mShelfStyle, mSelectedBookList);
        mShelfRowAdapter = new ShelfRowAdapter(getActivity(), mRowList, this);
        mListView.setAdapter(mShelfRowAdapter);
        mListView.invalidateViews();
        } else {
            Toast.makeText(mActivity, "コンテンツを選択してください", Toast.LENGTH_SHORT).show();
        }
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
                int position = mShelfStyle == Const.SHELF_STYLE_GRID ? row * 4 + column
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
                mNaviDrawerLayout.closeDrawers();
            }
        };
        return listener;
    }

    /** 本棚スタイルを取得 */
    public int getShelfStyle() {
        return mShelfStyle;
    }

    /** 本棚モードを取得 */
    public int getShelfMode() {
        return mShelfMode;
    }

    private int setShelfMode() {

        return 0;
    }

    /**
     * 本棚モードの切り替え　△△△要修正
     */
    public void ChangeShelfMode() {
        // 編集モードの場合、普通モードに変更し、ボタンを消す
        if (mShelfMode == Const.SHELF_MODE_EDIT) {
            mShelfMode = Const.SHELF_MODE_NORMAL;
            setHasOptionsMenu(false);
            // 普通モードの場合、編集モードに変更し、ボタンを表示
        } else {
            mShelfMode = Const.SHELF_MODE_EDIT;
            setHasOptionsMenu(true);
            // 　△△△actionbar の(あるボタン)を見えるようにする　
            MenuItem menuItem = mMenu.findItem(R.id.actionbar_show_selected);
            menuItem.setVisible(true);
            if (mShelfStyle == Const.SHELF_STYLE_GRID) {
                MenuItem title_menu = mMenu.findItem(R.id.actionbar_sort_by_title);
                MenuItem author_menu = mMenu.findItem(R.id.actionbar_sort_by_author);
                title_menu.setVisible(false);
                author_menu.setVisible(false);
            }
        }
        mListView.invalidateViews();
    }

    /**
     * 本棚表示スタイルの切り替え　　△△△要修正
     */
    private void changeShelfStyle() {
        mShelfStyle = mShelfStyle == Const.SHELF_STYLE_GRID ? Const.SHELF_STYLE_LIST
                : Const.SHELF_STYLE_GRID;
        Log.d("changeShelfStyle", "-->> mShelfStyle -->>" + mShelfStyle);
        // クラス変数mRowListをセット
        setRowListUponShelfStyle(mShelfStyle, mBookList);
        // 本棚コンテンツviewを再描画
        mShelfRowAdapter = new ShelfRowAdapter(getActivity(), mRowList, this);
        mListView.setAdapter(mShelfRowAdapter);
        if (mShelfStyle == Const.SHELF_STYLE_GRID) {
            mHasMenu = false;
        } else {
            mHasMenu = true;
        }
        setHasOptionsMenu(mHasMenu);
        mListView.invalidate();
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

    private void sortContentsInListView(int sortType) {
        mSortType = sortType;
        mBookList = ContentsInfo.sortByTitle(mBookList, sortType);
        mRowInfo = new ShelfRowInfo(mBookList, mNumsPerRow);
        mRowList = mRowInfo.getRowList();
        mShelfRowAdapter = new ShelfRowAdapter(mActivity, mRowList, this);
        mListView.setAdapter(mShelfRowAdapter);
        mListView.invalidate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // ソートタイプを記録
        outState.putInt(Const.SORT_TYPE, mSortType);
        super.onSaveInstanceState(outState);
    }

    private void setRowListUponShelfStyle(int shelfStyle, ArrayList<Book> bookList) {
        switch (shelfStyle) {
            case Const.SHELF_STYLE_GRID:
                // 表示モードが本棚の場合、一行のコンテンツ数はディスプレイ次第にする
                mNumsPerRow = caculateNumInRow(mActivity);
                break;
            case Const.SHELF_STYLE_LIST:
                // 表示モードがリストの場合、一行にコンテンツ数を1にする
                mNumsPerRow = 1;
                break;
            default:
                break;
        }
        // コンテンツ数情報により、行リストを作る
        mRowInfo = new ShelfRowInfo(bookList, mNumsPerRow);
        mRowList = mRowInfo.getRowList();
    }

}
