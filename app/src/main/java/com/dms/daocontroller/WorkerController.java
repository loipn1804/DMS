package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.Status;
import greendao.StatusDao;
import greendao.Worker;
import greendao.WorkerDao;

/**
 * Created by USER on 2/24/2016.
 */
public class WorkerController {

    private static WorkerDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getWorkerDao();
    }

    public static void insertOrUpdate(Context context, Worker mWorker) {
        if (getDao(context).load(mWorker.getId()) == null) {
            getDao(context).insert(mWorker);
        } else {
            getDao(context).update(mWorker);
        }
    }

    public static Worker getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<Worker> getAll(Context context) {
        return getDao(context).queryRaw(" ORDER BY id DESC");
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
