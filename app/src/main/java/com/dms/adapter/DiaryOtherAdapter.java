package com.dms.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dms.R;

import java.util.ArrayList;
import java.util.List;

import greendao.DiaryOther;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryOtherAdapter extends BaseAdapter {

    private List<DiaryOther> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    public DiaryOtherAdapter(Activity activity, List<DiaryOther> listData) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    public void setListData(List<DiaryOther> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.row_diary_other, null);
            holder = new ViewHolder();

            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.txtId = (TextView) convertView.findViewById(R.id.txtId);
            holder.txtCreatedTime = (TextView) convertView.findViewById(R.id.txtCreatedTime);
            holder.txtCreatedBy = (TextView) convertView.findViewById(R.id.txtCreatedBy);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.txtContent = (TextView) convertView.findViewById(R.id.txtContent);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtId.setText("#" + listData.get(position).getId());
        holder.txtCreatedTime.setText(listData.get(position).getCreated_at());
        holder.txtCreatedBy.setText(listData.get(position).getCreated_by_name());
        holder.txtStatus.setText(listData.get(position).getStatus() + "");
        holder.txtContent.setText(listData.get(position).getContent());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        TextView txtId;
        TextView txtCreatedTime;
        TextView txtCreatedBy;
        TextView txtStatus;
        TextView txtContent;
    }
}
