package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.Category;
import greendao.CategoryDao;
import greendao.DormitoryCategory;
import greendao.DormitoryCategoryDao;

/**
 * Created by USER on 2/22/2016.
 */
public class DormitoryCategoryController {

    private static DormitoryCategoryDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getDormitoryCategoryDao();
    }

    public static void insertOrUpdate(Context context, DormitoryCategory mDormitoryCategory) {
        if (getDao(context).load(mDormitoryCategory.getId()) == null) {
            getDao(context).insert(mDormitoryCategory);
        } else {
            getDao(context).update(mDormitoryCategory);
        }
    }

    public static DormitoryCategory getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<DormitoryCategory> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static List<DormitoryCategory> getByDormitoryId(Context context, String dormitory_id) {
        return getDao(context).queryRaw(" WHERE dormitory_id = ?", dormitory_id);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
