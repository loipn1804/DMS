package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.CaseImage;
import greendao.CaseImageDao;
import greendao.CaseObj;
import greendao.CaseObjDao;

/**
 * Created by USER on 2/19/2016.
 */
public class CaseImageController {

    private static CaseImageDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCaseImageDao();
    }

    public static void insertOrUpdate(Context context, CaseImage mCase) {
        if (getDao(context).load(mCase.getId()) == null) {
            getDao(context).insert(mCase);
        } else {
            getDao(context).update(mCase);
        }
    }

    public static void getById(Context context, Long id) {
        getDao(context).loadByRowId(id);
    }

    public static List<CaseImage> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearById(Context context, long id) {
        getDao(context).deleteByKey(id);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}