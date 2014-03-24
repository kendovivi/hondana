
package com.example.hondana.adapter;

import com.example.hondana.Const;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.hondana.R;

public class NaviDrawerListAdapter extends BaseAdapter {

    private Context mContext;
    /** navigation drawer各行のview */
    private LinearLayout mNaviDrawerItemView;

    public NaviDrawerListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return Const.NUMS_IN_NAVI;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            mNaviDrawerItemView = (LinearLayout) inflater.inflate(R.layout.navi_drawer_list_item,
                    null);
            viewHolder.drawerItemImgView = (ImageView) mNaviDrawerItemView
                    .findViewById(R.id.navi_drawer_item_icon);
            mNaviDrawerItemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) mNaviDrawerItemView.getTag();
        }
        // Navigation Drawer行ごとに、違うアイコンimgをつける
        switch (position) {
        // 設定アイコン
            case 4:
                viewHolder.drawerItemImgView
                        .setImageResource(R.drawable.navi_drawer_setting_selector);
                break;
            // ひとまずデフォルトアイコンを使う
            default:
                viewHolder.drawerItemImgView
                        .setImageResource(R.drawable.navi_drawer_shelf_style_selector);
        }
        return mNaviDrawerItemView;
    }

    static class ViewHolder {
        /** navigation drawer各行のアイテムアイコンImageView */
        ImageView drawerItemImgView;
    }

}
