package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.DiaryVisitor;
import greendao.DiaryVisitorDao;

/**
 * Created by USER on 2/29/2016.
 */
public class DiaryVisitorController {

    private static DiaryVisitorDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDiaryVisitorDao();
    }

    public static void insertOrUpdate(Context context, DiaryVisitor mDiaryVisitor) {
        if (getDao(context).load(mDiaryVisitor.getId()) == null) {
            getDao(context).insert(mDiaryVisitor);
        } else {
            getDao(context).update(mDiaryVisitor);
        }
    }

    public static DiaryVisitor getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<DiaryVisitor> getAll(Context context) {
        return getDao(context).queryRaw(" ORDER BY id DESC");
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
