package com.example.hondana.adapter;



import com.example.hondana.book.Book;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hondana.R;

public class BookAdapter extends BaseAdapter {
    
    private Context mContext;
    /** test thumb nail imageId list */
    private ArrayList<Book> mBookList;
    
    public BookAdapter(Context context, ArrayList<Book> bookList){
        mContext = context;
        mBookList = bookList;
    }

    @Override
    public int getCount() {
        return mBookList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        
        View view = convertView;
        final ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        //没被回收的话，就直接使用之前的。若被回收，则创建新的，并添加一些新属性
        if (convertView == null) {
            view = inflater.inflate(R.layout.book_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.bookImageView = (ImageView) view.findViewById(R.id.bookimage);
            viewHolder.bookCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
            //复选框点击监听
            viewHolder.bookCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        buttonView.setChecked(true);
                        mBookList.get(position).setBookSelected(true);
                    } else {
                        buttonView.setChecked(false);
                        mBookList.get(position).setBookSelected(false);
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
        
        viewHolder.bookImageView.setImageBitmap(mBookList.get(position).getBookImage());
        return view;
    }
    
   
    
    static class ViewHolder{
        ImageView bookImageView; 
        TextView bookImageTitleView;
        CheckBox bookCheckBox;
    }
    
}
