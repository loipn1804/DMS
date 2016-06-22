package com.dms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dms.R;
import com.dms.interfaces.MenuCallback;

/**
 * Created by USER on 3/2/2016.
 */
public class AboutFragment extends Fragment implements View.OnClickListener {

    private MenuCallback menuCallback;
    private RelativeLayout rltMenu;

    public void setMenuCallback(MenuCallback menuCallback) {
        this.menuCallback = menuCallback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        rltMenu = (RelativeLayout) view.findViewById(R.id.rltMenu);

        rltMenu.setOnClickListener(this);
    }

    private void initData() {

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
