
package com.example.hondana.adapter;

import android.widget.ImageView;

import android.widget.LinearLayout;

import android.view.LayoutInflater;

import com.example.hondana.R;

import android.content.Context;

import android.widget.TextView;

import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;

public class NaviDrawerListAdapter extends BaseAdapter {

    private Context mContext;
    private LinearLayout view;
    private ViewHolder viewHolder;

    public NaviDrawerListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 5;
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
        viewHolder = new ViewHolder();
        if (convertView == null) {
            view = (LinearLayout) inflater.inflate(R.layout.navi_drawer_list_item, null);
            viewHolder.drawerIcon = (ImageView) view.findViewById(R.id.navi_drawer_item_icon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.drawerIcon.setImageResource(android.R.drawable.btn_star);
        return view;
    }

    static class ViewHolder {
        ImageView drawerIcon;
    }

}
