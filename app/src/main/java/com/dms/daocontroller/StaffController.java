package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.Staff;
import greendao.StaffDao;
import greendao.Status;
import greendao.StatusDao;

/**
 * Created by USER on 2/23/2016.
 */
public class StaffController {

    private static StaffDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getStaffDao();
    }

    public static void insertOrUpdate(Context context, Staff mStaff) {
        if (getDao(context).load(mStaff.getId()) == null) {
            getDao(context).insert(mStaff);
        } else {
            getDao(context).update(mStaff);
        }
    }

    public static Staff getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<Staff> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
