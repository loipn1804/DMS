package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.CaseActionTaken;
import greendao.CaseActionTakenDao;

/**
 * Created by USER on 2/19/2016.
 */
public class CaseActionTakenController {

    private static CaseActionTakenDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCaseActionTakenDao();
    }

    public static void insertOrUpdate(Context context, CaseActionTaken mCase) {
        if (getDao(context).load(mCase.getId()) == null) {
            getDao(context).insert(mCase);
        } else {
            getDao(context).update(mCase);
        }
    }

    public static void getById(Context context, Long id) {
        getDao(context).loadByRowId(id);
    }

    public static List<CaseActionTaken> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
