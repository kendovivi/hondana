package com.example.hondana.adapter;



import android.widget.Toast;

import android.widget.CompoundButton;

import android.widget.CompoundButton.OnCheckedChangeListener;

import android.widget.CheckBox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hondana.R;

public class BookAdapter extends BaseAdapter {
    
    private Context mContext;
    /** test thumb nail imageId list */
    private int[] mThumbNailsIdList;
    
    public BookAdapter(Context context, boolean isRec){
        mContext = context;
        mThumbNailsIdList = isRec? mThumbNailsRecIdList : mThumbNailsAllIdList;
    }

    @Override
    public int getCount() {
        return mThumbNailsIdList.length;
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
        
        View view = convertView;
        final ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        //没被回收的话，就直接使用之前的。若被回收，则创建新的，并添加一些新属性
        if (convertView == null) {
            view = inflater.inflate(R.layout.book_list_item, null);
            viewHolder = new ViewHolder();
            //viewHolder.bookImageTitleView = (TextView) view.findViewById(R.id.bookimagetitle);
            viewHolder.bookImageView = (ImageView) view.findViewById(R.id.bookimage);
            viewHolder.bookCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
            viewHolder.bookCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        buttonView.setChecked(true);
                    } else {
                        buttonView.setChecked(false);
                    }
                }
                
            });
            view.setPadding(15, 0, 15, 0);
            //?
            view.setTag(viewHolder);
        } else {
            //?
            viewHolder = (ViewHolder) view.getTag();
        }
        
        //viewHolder.bookImageTitleView.setText(String.valueOf(position));
        viewHolder.bookImageView.setImageResource(mThumbNailsIdList[position]);
        return view;
    }
    
   
    
    static class ViewHolder{
        ImageView bookImageView; 
        TextView bookImageTitleView;
        CheckBox bookCheckBox;
    }
    
    private int[] mThumbNailsAllIdList = {
            R.drawable.sample1, 
            R.drawable.sample1,
            R.drawable.sample3,
            R.drawable.sample4,
            R.drawable.sample5,
            R.drawable.sample6,
            R.drawable.sample7,
            R.drawable.sample7,
            R.drawable.sample8,
            R.drawable.sample9,
            R.drawable.sample10
    };
    
    private int[] mThumbNailsRecIdList = {
            R.drawable.sample5,
            R.drawable.sample4
    };

}
