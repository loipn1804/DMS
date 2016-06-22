package com.dms.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dms.R;
import com.dms.interfaces.ItemListCallback;
import com.dms.interfaces.MultichoiceCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 2/22/2016.
 */
public class MultichoiceAdapter extends BaseAdapter {

    private List<String> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private MultichoiceCallback multichoiceCallback;

    public MultichoiceAdapter(Activity activity, List<String> listData, MultichoiceCallback multichoiceCallback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.multichoiceCallback = multichoiceCallback;
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
            convertView = this.layoutInflater.inflate(R.layout.row_multichoice, null);
            holder = new ViewHolder();

            holder.txt = (TextView) convertView.findViewById(R.id.txt);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt.setText(listData.get(position));
        holder.txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                multichoiceCallback.checkedChange(position, isChecked);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        TextView txt;
        CheckBox checkBox;
    }
}
