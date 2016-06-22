package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.DiaryAll;
import greendao.DiaryAllDao;
import greendao.Status;
import greendao.StatusDao;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryAllController {

    private static DiaryAllDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDiaryAllDao();
    }

    public static void insertOrUpdate(Context context, DiaryAll mDiaryAll) {
        if (getDao(context).load(mDiaryAll.getId()) == null) {
            getDao(context).insert(mDiaryAll);
        } else {
            getDao(context).update(mDiaryAll);
        }
    }

    public static DiaryAll getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<DiaryAll> getAll(Context context) {
        return getDao(context).queryRaw(" ORDER BY id DESC");
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
