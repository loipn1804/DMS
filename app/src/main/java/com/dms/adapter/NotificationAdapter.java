package com.dms.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.interfaces.ItemListCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 1/21/2016.
 */
public class NotificationAdapter extends BaseAdapter {

    private List<String> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    public NotificationAdapter(Activity activity, List<String> listData) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
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
            convertView = this.layoutInflater.inflate(R.layout.row_notification, null);
            holder = new ViewHolder();

            holder.root = (LinearLayout) convertView.findViewById(R.id.root);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
    }
}
