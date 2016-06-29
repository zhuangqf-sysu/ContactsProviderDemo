package com.example.zhuangqf.contactsproviderdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by zhuangqf on 6/29/16.
 */
public class MyListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String,Object>>myList;


    public  MyListAdapter(Context mContext,List<Map<String,Object>>myList){
        this.mContext = mContext;
        this.myList = myList;
    }

    @Override
    public int getCount(){
        return myList.size();
    }

    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView header = null;
        TextView name = null;
        if (convertView == null || position < myList.size()) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.list_item, null);
            header = (ImageView) convertView.findViewById(R.id.header);
            name = (TextView) convertView.findViewById(R.id.name);
        }

        header.setImageBitmap((Bitmap)myList.get(position).get("header"));
        name.setText((String)myList.get(position).get("name"));
        return  convertView;
    }

}
