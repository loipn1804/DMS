package com.dms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.dms.R;
import com.dms.adapter.DiaryPagerAdapter;
import com.dms.adapter.NotificationAdapter;
import com.dms.interfaces.MenuCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 1/22/2016.
 */
public class DiaryFragment extends Fragment implements View.OnClickListener {

    private MenuCallback menuCallback;
    private RelativeLayout rltMenu;

    private PagerSlidingTabStrip tabStrip;
    private ViewPager viewPager;
    private DiaryPagerAdapter dealPagerAdapter;

    public void setMenuCallback(MenuCallback menuCallback) {
        this.menuCallback = menuCallback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        rltMenu = (RelativeLayout) view.findViewById(R.id.rltMenu);

        tabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        rltMenu.setOnClickListener(this);
    }

    private void initData() {
        dealPagerAdapter = new DiaryPagerAdapter(getChildFragmentManager(), getActivity());
        viewPager.setAdapter(dealPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        tabStrip.setViewPager(viewPager);
        tabStrip.setTextColor(getResources().getColor(R.color.txt_black_33));
        tabStrip.setDividerColor(getResources().getColor(R.color.transparent));
        tabStrip.setIndicatorColor(getResources().getColor(R.color.main_color));
        tabStrip.setUnderlineColor(getResources().getColor(R.color.transparent));
//        tabStrip.setIndicatorHeight(10);
        tabStrip.setTextSize((int) getResources().getDimension(R.dimen.txt_15sp));
        tabStrip.setBackgroundColor(getResources().getColor(R.color.bg_tab_trip));
//        tabStrip.setTextColorResource(R.color.main_color);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rltMenu:
                menuCallback.OpenMenu();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
