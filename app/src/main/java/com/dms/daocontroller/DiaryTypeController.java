package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.DiaryType;
import greendao.DiaryTypeDao;
import greendao.Status;
import greendao.StatusDao;

/**
 * Created by USER on 2/26/2016.
 */
public class DiaryTypeController {

    private static DiaryTypeDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDiaryTypeDao();
    }

    public static void insertOrUpdate(Context context, DiaryType mDiaryType) {
        if (getDao(context).load(mDiaryType.getId()) == null) {
            getDao(context).insert(mDiaryType);
        } else {
            getDao(context).update(mDiaryType);
        }
    }

    public static DiaryType getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<DiaryType> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
