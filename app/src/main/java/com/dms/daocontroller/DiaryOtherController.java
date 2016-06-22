package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.DiaryOther;
import greendao.DiaryOtherDao;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryOtherController {

    private static DiaryOtherDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDiaryOtherDao();
    }

    public static void insertOrUpdate(Context context, DiaryOther mDiaryOther) {
        if (getDao(context).load(mDiaryOther.getId()) == null) {
            getDao(context).insert(mDiaryOther);
        } else {
            getDao(context).update(mDiaryOther);
        }
    }

    public static DiaryOther getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<DiaryOther> getAll(Context context) {
        return getDao(context).queryRaw(" ORDER BY id DESC");
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
