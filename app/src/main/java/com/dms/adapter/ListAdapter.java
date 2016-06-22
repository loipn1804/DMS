package com.dms.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.activity.CaseDetailActivity;
import com.dms.interfaces.ItemListCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 1/19/2016.
 */
public class ListAdapter extends BaseAdapter {

    private List<String> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private ItemListCallback itemListCallback;

    public ListAdapter(Activity activity, List<String> listData, ItemListCallback itemListCallback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.itemListCallback = itemListCallback;
    }

    public void setListData(List<String> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = (listData == null) ? 0 : listData.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.row_list, null);
            holder = new ViewHolder();

            holder.txt = (TextView) convertView.findViewById(R.id.txt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt.setText(listData.get(position));
        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListCallback.ItemClick(position);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        TextView txt;
    }
}
