package com.dms.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.activity.DiaryShiftDetailActivity;
import com.dms.activity.DiaryVisitorDetailActivity;

import java.util.ArrayList;
import java.util.List;

import greendao.DiaryShift;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryShiftAdapter extends BaseAdapter {

    private List<DiaryShift> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    public DiaryShiftAdapter(Activity activity, List<DiaryShift> listData) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    public void setListData(List<DiaryShift> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.row_diary_shift, null);
            holder = new ViewHolder();

            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.txtId = (TextView) convertView.findViewById(R.id.txtId);
            holder.txtEntryTime = (TextView) convertView.findViewById(R.id.txtEntryTime);
            holder.lnlExitTime = (LinearLayout) convertView.findViewById(R.id.lnlExitTime);
            holder.txtExitTime = (TextView) convertView.findViewById(R.id.txtExitTime);
            holder.txtUsername = (TextView) convertView.findViewById(R.id.txtUsername);
            holder.txtContent = (TextView) convertView.findViewById(R.id.txtContent);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtId.setText("#" + listData.get(position).getId());
        holder.txtEntryTime.setText(listData.get(position).getEnter_time());
        if (listData.get(position).getIs_exited() == 1) {
            holder.lnlExitTime.setVisibility(View.VISIBLE);
            holder.txtExitTime.setText(listData.get(position).getExit_time());
        } else {
            holder.lnlExitTime.setVisibility(View.GONE);
        }
        holder.txtUsername.setText(listData.get(position).getName());
        holder.txtContent.setText(listData.get(position).getContent());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DiaryShiftDetailActivity.class);
                intent.putExtra("diary_id", listData.get(position).getId());
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        TextView txtId;
        TextView txtEntryTime;
        LinearLayout lnlExitTime;
        TextView txtExitTime;
        TextView txtUsername;
        TextView txtContent;
    }
}
