package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.CaseReferredTo;
import greendao.CaseReferredToDao;

/**
 * Created by USER on 2/19/2016.
 */
public class CaseReferredToController {

    private static CaseReferredToDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCaseReferredToDao();
    }

    public static void insertOrUpdate(Context context, CaseReferredTo mCase) {
        if (getDao(context).load(mCase.getId()) == null) {
            getDao(context).insert(mCase);
        } else {
            getDao(context).update(mCase);
        }
    }

    public static void getById(Context context, Long id) {
        getDao(context).loadByRowId(id);
    }

    public static List<CaseReferredTo> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
