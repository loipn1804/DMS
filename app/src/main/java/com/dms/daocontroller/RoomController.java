package com.dms.daocontroller;

import android.content.Context;

import com.dms.activity.MyApplication;

import java.util.List;

import greendao.Level;
import greendao.LevelDao;
import greendao.Room;
import greendao.RoomDao;

/**
 * Created by USER on 2/18/2016.
 */
public class RoomController {

    private static RoomDao getDao(Context c) {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getRoomDao();
    }

    public static void insertOrUpdate(Context context, Room mRoom) {
        if (getDao(context).load(mRoom.getId()) == null) {
            getDao(context).insert(mRoom);
        } else {
            getDao(context).update(mRoom);
        }
    }

    public static Room getById(Context context, Long id) {
        return getDao(context).loadByRowId(id);
    }

    public static List<Room> getByLevelId(Context context, String level_id) {
        return getDao(context).queryRaw(" WHERE level_id = ?", level_id);
    }

    public static List<Room> getAll(Context context) {
        return getDao(context).loadAll();
    }

    public static void clearAll(Context context) {
        getDao(context).deleteAll();
    }
}
