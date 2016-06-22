package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.CaseObj;
import greendao.CaseObjDao;

/**
 * Created by USER on 2/19/2016.
 */
public class CaseController {

    private static CaseObjDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCaseObjDao();
    }

    public static void insertOrUpdate(Context context, CaseObj mCase) {
        if (getDao(context).load(mCase.getId()) == null) {
            getDao(context).insert(mCase);
        } else {
            getDao(context).update(mCase);
        }
    }

    public static CaseObj getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<CaseObj> getAll(Context context) {
        return getDao(context).queryRaw(" ORDER BY id DESC");
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
