package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.SubCategory;
import greendao.SubCategoryDao;

/**
 * Created by USER on 2/19/2016.
 */
public class SubCategoryController {

    private static SubCategoryDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getSubCategoryDao();
    }

    public static void insertOrUpdate(Context context, SubCategory mSubCategory) {
        if (getDao(context).load(mSubCategory.getId()) == null) {
            getDao(context).insert(mSubCategory);
        } else {
            getDao(context).update(mSubCategory);
        }
    }

    public static void getById(Context context, Long id) {
        getDao(context).loadByRowId(id);
    }

    public static List<SubCategory> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static List<SubCategory> getByCategoryId(Context context, String category_id) {
        return getDao(context).queryRaw(" WHERE category_id = ?", category_id);
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
