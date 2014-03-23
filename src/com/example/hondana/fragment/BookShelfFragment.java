package com.example.hondana.fragment;

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
	/** 书架显示 GRID / LIST */
	private int mShelfStyle;
	/** 书架每行contents个数 */
	private int mNumsPerRow;
	/** 实体类要修改 */
	private Book mBook;
	/** 当前显示的contents */
	private ArrayList<Book> mBookList;
	/** checkbox选中的contents */
	private ArrayList<Book> mSelectedBookList;
	/** 用来获得书架每行信息 */
	private ShelfRowInfo mRowInfo;
	/** 书架行数组 */
	private ArrayList<ShelfRow> mRowList;
	/** 书架行适配器 */
	private ShelfRowAdapter mShelfRowAdapter;
	/** 书架 */
	private ListView mListView;
	/** 书架头部 */
	private LinearLayout mBookShelfHeaderView;
	/** 书架底部 */
	private LinearLayout mBookShelfFooterView;
	/** 显示选中按钮 */
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
			// 计算屏幕宽度，判断一行显示多少本书
			mNumsPerRow = caculateNumInRow(mActivity);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment, container, false);
		mListView = (ListView) view.findViewById(R.id.shelf_listview_v);
		showSelectedBtn = (Button) view
				.findViewById(R.id.test_show_selected_btn);
		// navigation drawer部分
		mNaviListView = (ListView) view.findViewById(R.id.navi_drawer_listview);
		// 头部和底部
		mBookShelfFooterView = (LinearLayout) inflater.inflate(
				R.layout.bookshelf_footer, mListView, false);
		mBookShelfHeaderView = (LinearLayout) inflater.inflate(
				R.layout.bookshelf_header, mListView, false);

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
		// 显示选中按钮监听器设定
		showSelectedBtn.setOnClickListener(this);
		// 设定tag，在BookListView画背景时，可以判断是否为第一行
		mBookShelfHeaderView.setTag("ListViewHeader");
		// 设定ListView头部和底部 要修改
		mListView.addFooterView(mBookShelfFooterView);
		mListView.addHeaderView(mBookShelfHeaderView);
		// 设定ListView适配器部分
		mShelfRowAdapter = new ShelfRowAdapter(mActivity, mRowList, this);
		mListView.setAdapter(mShelfRowAdapter);
		// navidrawer适配器部分
		mNaviListView.setAdapter(new NaviDrawerListAdapter(mActivity));
		mNaviListView.setOnItemClickListener(onNaviDrawerItemClickListener());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.show_selected_btn:
			showSelected();
			break;
		}
	}

	// 显示checkbox选中的contents
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
		ft.replace(R.id.fragment_container_vertical,
				BookShelfFragment.newInstance(mShelfStyle));
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

				// 拖动设定开始（要修改）
				ClipData.Item item = new ClipData.Item("icondrag");
				ClipData dragData = new ClipData("icondrag",
						new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN },
						item);
				View.DragShadowBuilder shadow = new DragShadowBuilder(v);
				v.startDrag(dragData, shadow, null, 0);
				v.setOnDragListener(onDragListener);
				// 拖动设定结束
				return false;
			}
		};
		return listener;
	}

	/**
	 * 定义书籍上checkbox选中状态更改的监听
	 * 
	 * @param row
	 *            书籍所在行
	 * @param column
	 *            书籍所在列
	 * @param bookImageView
	 *            书籍的图像view
	 * @return
	 */
	public OnCheckedChangeListener onCheckedChangeListener(final int row,
			final int column, final ImageView bookImageView) {

		OnCheckedChangeListener listener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton checkBox,
					boolean isChecked) {
				Book book = mRowList.get(row).getBookListInRow().get(column);
				if (isChecked) {
					bookImageView
							.setBackgroundResource(R.drawable.border_pressed);
				} else {
					bookImageView.setBackgroundResource(R.drawable.border_no);
				}
				book.setBookSelected(isChecked);
				checkBox.setChecked(isChecked);
			}
		};

		return listener;
	}

	/**
	 * naviDrawer 每个按钮的事件监听
	 * 
	 * @return
	 */
	private OnItemClickListener onNaviDrawerItemClickListener() {
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 切换书架视图
				switch (position) {
				// 书架视图切换按钮
				case 0:
					changeShelfStyle();
					break;
				// 进入设定画面
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

	public int getShelfStyle() {
		return mShelfStyle;
	}

	public boolean getIsEdit() {
		return mIsEdit;
	}

	// 长点击cell之后，画面进入编辑模式 (checkbox可见， cell点击改变checkbox状态)， 设定监听
	public void ChangeToEditModeLayout() {
		mIsEdit = true;
		Button showSelBtn = (Button) mListView
				.findViewById(R.id.show_selected_btn);
		showSelBtn.setVisibility(View.VISIBLE);
		showSelBtn.setOnClickListener(this);

		// 重新调用adapter的getview（）， 描画新编辑画面的listview
		mListView.invalidateViews();
	}

	//
	public void ChangeToNormalModeLayout() {
		mIsEdit = false;
		Button showSelBtn = (Button) mListView
				.findViewById(R.id.show_selected_btn);
		showSelBtn.setVisibility(View.GONE);
		mListView.invalidateViews();
	}

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
	 * contents的拖动监听
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
				// linearLayout 要修改
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
	 * 计算屏幕一行放多少个contents
	 */
	public int caculateNumInRow(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		return width / Const.CONTENT_WIDTH;
	}

}
