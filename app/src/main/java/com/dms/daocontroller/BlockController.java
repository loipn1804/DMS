package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.Block;
import greendao.BlockDao;
import greendao.Dormitory;
import greendao.DormitoryDao;

/**
 * Created by USER on 2/18/2016.
 */
public class BlockController {

    private static BlockDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getBlockDao();
    }

    public static void insertOrUpdate(Context context, Block mBlock) {
        if (getDao(context).load(mBlock.getId()) == null) {
            getDao(context).insert(mBlock);
        } else {
            getDao(context).update(mBlock);
        }
    }

    public static Block getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<Block> getByDormitoryId(Context context, String dormitory_id) {
        return getDao(context).queryRaw(" WHERE dormitory_id = ?", dormitory_id);
    }

    public static List<Block> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
