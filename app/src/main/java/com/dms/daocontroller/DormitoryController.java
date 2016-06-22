package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.Dormitory;
import greendao.DormitoryDao;
import greendao.User;
import greendao.UserDao;

/**
 * Created by USER on 2/18/2016.
 */
public class DormitoryController {

    private static DormitoryDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDormitoryDao();
    }

    public static void insertOrUpdate(Context context, Dormitory mDormitory) {
        if (getDao(context).load(mDormitory.getId()) == null) {
            getDao(context).insert(mDormitory);
        } else {
            getDao(context).update(mDormitory);
        }
    }

    public static Dormitory getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<Dormitory> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
