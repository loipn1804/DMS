package com.dms.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.dms.daocontroller.DiaryTypeController;
import com.dms.fragment.DiaryAllFragment;
import com.dms.fragment.DiaryOtherFragment;
import com.dms.fragment.DiaryShiftFragment;
import com.dms.fragment.DiaryVisitorFragment;

import java.util.ArrayList;
import java.util.List;

import greendao.DiaryType;

/**
 * Created by USER on 1/22/2016.
 */
public class DiaryPagerAdapter extends FragmentPagerAdapter {

    private Activity activity;
    private List<DiaryType> listDiaryType;

    public DiaryPagerAdapter(FragmentManager fragmentManager, Activity activity) {
        super(fragmentManager);
        this.activity = activity;
        this.listDiaryType = new ArrayList<>();
        this.listDiaryType.add(new DiaryType(0l, "All"));
        this.listDiaryType.addAll(DiaryTypeController.getAll(this.activity));
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                DiaryAllFragment diaryAllFragment = new DiaryAllFragment();
                return diaryAllFragment;
            case 1:
                DiaryVisitorFragment diaryVisitorFragment = new DiaryVisitorFragment();
                return diaryVisitorFragment;
            case 2:
                DiaryShiftFragment diaryShiftFragment = new DiaryShiftFragment();
                return diaryShiftFragment;
            default:
                DiaryOtherFragment diaryOtherFragment = new DiaryOtherFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("diary_type_id", listDiaryType.get(i).getId());
                diaryOtherFragment.setArguments(bundle);
                return diaryOtherFragment;
        }
    }

    @Override
    public int getCount() {

        return listDiaryType.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listDiaryType.get(position).getName();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        super.destroyItem(container, position, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {

        super.finishUpdate(container);
    }

    @Override
    public long getItemId(int position) {

        return super.getItemId(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return super.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

        super.restoreState(state, loader);
    }

    @Override
    public Parcelable saveState() {

        return super.saveState();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {

        super.setPrimaryItem(container, position, object);
    }

    @Override
    public void startUpdate(ViewGroup container) {

        super.startUpdate(container);
    }
}
