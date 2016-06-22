package com.dms.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dms.R;
import com.dms.activity.BaseActivity;
import com.dms.adapter.CaseAdapter;
import com.dms.adapter.NotificationAdapter;
import com.dms.interfaces.MenuCallback;
import com.dms.view.ScrollInterfacedListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 1/21/2016.
 */
public class NotificationFragment extends Fragment implements View.OnClickListener {

    private MenuCallback menuCallback;
    private RelativeLayout rltMenu;
    private ListView lvData;

    public void setMenuCallback(MenuCallback menuCallback) {
        this.menuCallback = menuCallback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        rltMenu = (RelativeLayout) view.findViewById(R.id.rltMenu);
        lvData = (ListView) view.findViewById(R.id.lvData);

        rltMenu.setOnClickListener(this);
    }

    private void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("" + i);
        }

        NotificationAdapter notificationAdapter = new NotificationAdapter(getActivity(), list);
        lvData.setAdapter(notificationAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltMenu:
                menuCallback.OpenMenu();
                break;
        }
    }
}
