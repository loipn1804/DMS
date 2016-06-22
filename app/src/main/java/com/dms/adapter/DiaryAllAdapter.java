package com.dms.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.daocontroller.DiaryTypeController;

import java.util.ArrayList;
import java.util.List;

import greendao.DiaryAll;
import greendao.DiaryType;

/**
 * Created by USER on 1/22/2016.
 */
public class DiaryAllAdapter extends BaseAdapter {

    private List<DiaryAll> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    public DiaryAllAdapter(Activity activity, List<DiaryAll> listData) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
    }

    public void setListData(List<DiaryAll> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.row_diary_all, null);
            holder = new ViewHolder();

            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.viewBottom = convertView.findViewById(R.id.viewBottom);
            holder.txtId = (TextView) convertView.findViewById(R.id.txtId);
            holder.txtType = (TextView) convertView.findViewById(R.id.txtType);
            holder.txtContent = (TextView) convertView.findViewById(R.id.txtContent);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.txtCreatedBy = (TextView) convertView.findViewById(R.id.txtCreatedBy);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtId.setText("#" + listData.get(position).getId());
        DiaryType diaryType = DiaryTypeController.getById(activity, listData.get(position).getDiary_type_id());
        if (diaryType != null) {
            holder.txtType.setText(diaryType.getName());
        } else {
            holder.txtType.setText("");
        }
        holder.txtContent.setText(listData.get(position).getContent());
        holder.txtTime.setText(listData.get(position).getCreated_at());
        holder.txtCreatedBy.setText(listData.get(position).getCreated_by_name());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (position == getCount() - 1) {
            holder.viewBottom.setVisibility(View.VISIBLE);
        } else {
            holder.viewBottom.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        View viewBottom;

        TextView txtId;
        TextView txtType;
        TextView txtContent;
        TextView txtTime;
        TextView txtCreatedBy;
    }
}
