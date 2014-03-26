
package com.example.hondana.activity;

import android.util.Log;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;
import com.example.hondana.Const;
import com.example.hondana.R;
import com.example.hondana.book.ContentsInfo;
import com.example.hondana.fragment.BookShelfFragment;

public class FirstActivity extends Activity {

    /** 本棚スタイル */
    private int mShelfStyle;
    private int mShelfMode;
    /** */
    private SharedPreferences mPref;
    /** */
    private boolean mIsAppToFinish;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private BookShelfFragment mBookShelfFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mPref = PreferenceManager.getDefaultSharedPreferences(this);

        // 本棚スタイルを決める
        // 回転操作の時savedInstanceStateに保存されている本棚スタイルを取得
        // 保存されていない場合、アプリにfirst enterとして、sharedPreferenceファイルから本棚スタイルを取得
        if (savedInstanceState != null && savedInstanceState.containsKey(Const.SHELF_STYLE)) {
            if (savedInstanceState.containsKey(Const.SHELF_STYLE)) {
                mShelfStyle = savedInstanceState.getInt(Const.SHELF_STYLE, -1);
            }
            if (savedInstanceState.containsKey(Const.SHELF_MODE)) {
                mShelfMode = savedInstanceState.getInt(Const.SHELF_MODE, -1);
            }
        } else {
            setFirstEntryShelfStyleUponPref(mPref);
            mShelfMode = Const.SHELF_MODE_NORMAL;
        }

        clearSelectedData();
        mBookShelfFragment = BookShelfFragment.newInstance(mShelfStyle, mShelfMode);
        // todo: 縦、横方向のレイアウトとfragmentを分けて処理する予定　(現在両方同じ、行にコンテンツ数が違うだけ)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.hondana_main_vertical);
            mFragmentTransaction.add(R.id.fragment_container_vertical, mBookShelfFragment);
        } else {
            setContentView(R.layout.hondana_main_vertical);
            mFragmentTransaction.replace(R.id.fragment_container_vertical, mBookShelfFragment);
        }
        mFragmentTransaction.commit();
        mIsAppToFinish = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Const.SHELF_STYLE, mBookShelfFragment.getShelfStyle());
        outState.putInt(Const.SHELF_MODE, mBookShelfFragment.getShelfMode());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Log.d("onBackPressed", "-- Shelf Mode -->> " + mBookShelfFragment.getShelfMode());
        if (mBookShelfFragment.getShelfMode() == Const.SHELF_MODE_EDIT) {
            mBookShelfFragment.ChangeShelfMode();
        } else {
            if (mIsAppToFinish == true) {
                writeShelfStyleIntoPref();
                super.onBackPressed();
            } else {
                mIsAppToFinish = true;
                Toast.makeText(this, "もう一度押してアプリを終了します", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mIsAppToFinish = false;
                    }

                }, 3000);
            }
        }
    }

    private void setFirstEntryShelfStyleUponPref(SharedPreferences pref) {
        // 設定項目 「本棚表示スタイル」　がチェックされるかどうかを取得
        // チェックされている、かつprefにstyleタグが存在する場合、本棚スタイルをprefから読み込む
        // そうじゃない場合、デフォルトスタイル「本棚表示」を使う
        boolean isSaveStyleChecked = pref.getBoolean("pref_save_shelf_style", false);
        boolean hasStyleInPref = pref.contains(Const.SHELF_STYLE);

        if (isSaveStyleChecked && hasStyleInPref) {
            mShelfStyle = pref.getInt(Const.SHELF_STYLE, -1);
        } else {
            // ここは初入る時のみ呼ばれる
            mShelfStyle = Const.SHELF_STYLE_GRID;
        }

        switch (mShelfStyle) {
            case -1:
                Log.d("setFirstEntryShelfStyleUponPref ", "-- >> 本棚スタイル-1 読み込みエラー");
                break;
            default:
                break;
        }
    }

    // 　todo: 現在本棚スタイルだけ利用されているが、今後sharedPreferenceｓに書き込む項目が増えたら、汎用にならないといけない
    private void writeShelfStyleIntoPref() {
        if (mPref != null) {
            Editor editor = mPref.edit();
            editor.putInt(Const.SHELF_STYLE, mBookShelfFragment.getShelfStyle());
            editor.commit();
        }
    }

    // 删除予定
    private void clearSelectedData() {
        ContentsInfo.setSelectedContents(null);
    }

}
