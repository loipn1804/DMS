package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.Nationality;
import greendao.NationalityDao;
import greendao.Status;
import greendao.StatusDao;

/**
 * Created by USER on 2/24/2016.
 */
public class NationalityController {

    private static NationalityDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getNationalityDao();
    }

    public static void insertOrUpdate(Context context, Nationality mNationality) {
        if (getDao(context).load(mNationality.getCode()) == null) {
            getDao(context).insert(mNationality);
        } else {
            getDao(context).update(mNationality);
        }
    }

    public static Nationality getByCode(Context context, String code) {
        List<Nationality> nationalities = getDao(context).queryRaw(" WHERE code = ?", code);
        if (nationalities.size() > 0) {
            return nationalities.get(0);
        } else {
            return null;
        }
    }

    public static List<Nationality> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
