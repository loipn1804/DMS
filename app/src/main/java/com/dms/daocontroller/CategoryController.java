package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import greendao.Category;
import greendao.CategoryDao;
import greendao.Dormitory;
import greendao.DormitoryDao;

/**
 * Created by USER on 2/19/2016.
 */
public class CategoryController {

    private static CategoryDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getCategoryDao();
    }

    public static void insertOrUpdate(Context context, Category mCategory) {
        if (getDao(context).load(mCategory.getId()) == null) {
            getDao(context).insert(mCategory);
        } else {
            getDao(context).update(mCategory);
        }
    }

    public static Category getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static void getAll(Context context) {
        getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
