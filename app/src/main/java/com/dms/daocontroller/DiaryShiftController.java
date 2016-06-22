package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.DiaryShift;
import greendao.DiaryShiftDao;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryShiftController {

    private static DiaryShiftDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDiaryShiftDao();
    }

    public static void insertOrUpdate(Context context, DiaryShift mDiaryShift) {
        if (getDao(context).load(mDiaryShift.getId()) == null) {
            getDao(context).insert(mDiaryShift);
        } else {
            getDao(context).update(mDiaryShift);
        }
    }

    public static DiaryShift getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<DiaryShift> getAll(Context context) {
        return getDao(context).queryRaw(" ORDER BY id DESC");
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
