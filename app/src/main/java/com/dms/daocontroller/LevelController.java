package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.Block;
import greendao.BlockDao;
import greendao.Level;
import greendao.LevelDao;

/**
 * Created by USER on 2/18/2016.
 */
public class LevelController {

    private static LevelDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getLevelDao();
    }

    public static void insertOrUpdate(Context context, Level mLevel) {
        if (getDao(context).load(mLevel.getId()) == null) {
            getDao(context).insert(mLevel);
        } else {
            getDao(context).update(mLevel);
        }
    }

    public static Level getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<Level> getByBlockId(Context context, String block_id) {
        return getDao(context).queryRaw(" WHERE block_id = ?", block_id);
    }

    public static List<Level> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}