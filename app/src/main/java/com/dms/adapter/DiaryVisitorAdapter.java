package com.dms.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.activity.DiaryVisitorDetailActivity;
import com.dms.daocontroller.DiaryTypeController;
import com.dms.staticfunction.StaticFunction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import greendao.DiaryAll;
import greendao.DiaryType;
import greendao.DiaryVisitor;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryVisitorAdapter extends BaseAdapter {

    private List<DiaryVisitor> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public DiaryVisitorAdapter(Activity activity, List<DiaryVisitor> listData) {
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

    public void setListData(List<DiaryVisitor> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.row_diary_visitor, null);
            holder = new ViewHolder();

            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.imvVisitor = (ImageView) convertView.findViewById(R.id.imvVisitor);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtIsExit = (TextView) convertView.findViewById(R.id.txtIsExit);
            holder.txtEntryTime = (TextView) convertView.findViewById(R.id.txtEntryTime);
            holder.txtExitTime = (TextView) convertView.findViewById(R.id.txtExitTime);
            holder.txtContent = (TextView) convertView.findViewById(R.id.txtContent);
            holder.lnlExitTime = (LinearLayout) convertView.findViewById(R.id.lnlExitTime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.displayImage(StaticFunction.BASE_URL + listData.get(position).getImage_1(), holder.imvVisitor, options);
        holder.txtName.setText(listData.get(position).getName());
        holder.txtEntryTime.setText(listData.get(position).getEnter_time());
        if (listData.get(position).getIs_exited() == 0) {
            holder.txtIsExit.setText("Entered");
            holder.txtIsExit.setBackgroundResource(R.drawable.bg_green_circle);
            holder.lnlExitTime.setVisibility(View.GONE);
        } else {
            holder.txtIsExit.setText("Exited");
            holder.txtIsExit.setBackgroundResource(R.drawable.bg_red_circle);
            holder.lnlExitTime.setVisibility(View.VISIBLE);
            holder.txtExitTime.setText(listData.get(position).getExit_time());
        }
        holder.txtContent.setText(listData.get(position).getContent());

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DiaryVisitorDetailActivity.class);
                intent.putExtra("diary_id", listData.get(position).getId());
                activity.startActivity(intent);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        ImageView imvVisitor;
        TextView txtName;
        TextView txtIsExit;
        TextView txtEntryTime;
        TextView txtExitTime;
        TextView txtContent;
        LinearLayout lnlExitTime;
    }
}
