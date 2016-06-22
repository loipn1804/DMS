package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.Dormitory;
import greendao.DormitoryDao;
import greendao.Status;
import greendao.StatusDao;

/**
 * Created by USER on 2/19/2016.
 */
public class StatusController {

    private static StatusDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getStatusDao();
    }

    public static void insertOrUpdate(Context context, Status mStatus) {
        if (getDao(context).load(mStatus.getId()) == null) {
            getDao(context).insert(mStatus);
        } else {
            getDao(context).update(mStatus);
        }
    }

    public static Status getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<Status> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
