package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.DiaryShift;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DIARY_SHIFT.
*/
public class DiaryShiftDao extends AbstractDao<DiaryShift, Long> {

    public static final String TABLENAME = "DIARY_SHIFT";

    /**
     * Properties of entity DiaryShift.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Diary_type_id = new Property(1, Long.class, "diary_type_id", false, "DIARY_TYPE_ID");
        public final static Property User_id = new Property(2, Long.class, "user_id", false, "USER_ID");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Content = new Property(4, String.class, "content", false, "CONTENT");
        public final static Property Enter_time = new Property(5, String.class, "enter_time", false, "ENTER_TIME");
        public final static Property Exit_time = new Property(6, String.class, "exit_time", false, "EXIT_TIME");
        public final static Property Is_exited = new Property(7, Integer.class, "is_exited", false, "IS_EXITED");
        public final static Property Created_by = new Property(8, Long.class, "created_by", false, "CREATED_BY");
    };


    public DiaryShiftDao(DaoConfig config) {
        super(config);
    }
    
    public DiaryShiftDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DIARY_SHIFT' (" + //
                "'ID' INTEGER PRIMARY KEY ," + // 0: id
                "'DIARY_TYPE_ID' INTEGER," + // 1: diary_type_id
                "'USER_ID' INTEGER," + // 2: user_id
                "'NAME' TEXT," + // 3: name
                "'CONTENT' TEXT," + // 4: content
                "'ENTER_TIME' TEXT," + // 5: enter_time
                "'EXIT_TIME' TEXT," + // 6: exit_time
                "'IS_EXITED' INTEGER," + // 7: is_exited
                "'CREATED_BY' INTEGER);"); // 8: created_by
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DIARY_SHIFT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, DiaryShift entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long diary_type_id = entity.getDiary_type_id();
        if (diary_type_id != null) {
            stmt.bindLong(2, diary_type_id);
        }
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(3, user_id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(5, content);
        }
 
        String enter_time = entity.getEnter_time();
        if (enter_time != null) {
            stmt.bindString(6, enter_time);
        }
 
        String exit_time = entity.getExit_time();
        if (exit_time != null) {
            stmt.bindString(7, exit_time);
        }
 
        Integer is_exited = entity.getIs_exited();
        if (is_exited != null) {
            stmt.bindLong(8, is_exited);
        }
 
        Long created_by = entity.getCreated_by();
        if (created_by != null) {
            stmt.bindLong(9, created_by);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public DiaryShift readEntity(Cursor cursor, int offset) {
        DiaryShift entity = new DiaryShift( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // diary_type_id
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // user_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // content
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // enter_time
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // exit_time
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // is_exited
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8) // created_by
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, DiaryShift entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDiary_type_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setUser_id(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setContent(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setEnter_time(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setExit_time(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIs_exited(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setCreated_by(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(DiaryShift entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(DiaryShift entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}