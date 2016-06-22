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
import com.dms.activity.WorkerDetailActivity;
import com.dms.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import greendao.Worker;

/**
 * Created by USER on 1/21/2016.
 */
public class WorkerAdapter extends BaseAdapter {

    private List<Worker> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public WorkerAdapter(Activity activity, List<Worker> listData) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;

        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(500))
                .showImageForEmptyUri(R.drawable.avatar_default)
                .showImageOnLoading(R.drawable.avatar_default)
                .cacheInMemory(true)
                .cacheOnDisk(true).build();
    }

    public void setListData(List<Worker> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.row_worker, null);
            holder = new ViewHolder();

            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.imvWorker = (ImageView) convertView.findViewById(R.id.imvWorker);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtBarcode = (TextView) convertView.findViewById(R.id.txtBarcode);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.displayImage(StaticFunction.BASE_URL + listData.get(position).getImage_1(), holder.imvWorker, options);
        holder.txtName.setText(listData.get(position).getName());
        holder.txtBarcode.setText(listData.get(position).getBarcode());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WorkerDetailActivity.class);
                intent.putExtra("worker_id", listData.get(position).getId());
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        ImageView imvWorker;
        TextView txtName;
        TextView txtBarcode;
    }
}
