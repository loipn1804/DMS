package greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import greendao.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table USER.
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Role_id = new Property(1, Integer.class, "role_id", false, "ROLE_ID");
        public final static Property Avatar = new Property(2, String.class, "avatar", false, "AVATAR");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Fullname = new Property(4, String.class, "fullname", false, "FULLNAME");
        public final static Property Phone = new Property(5, String.class, "phone", false, "PHONE");
        public final static Property Email = new Property(6, String.class, "email", false, "EMAIL");
        public final static Property Created_at = new Property(7, String.class, "created_at", false, "CREATED_AT");
        public final static Property Updated_at = new Property(8, String.class, "updated_at", false, "UPDATED_AT");
        public final static Property Last_login = new Property(9, String.class, "last_login", false, "LAST_LOGIN");
        public final static Property Is_active = new Property(10, Integer.class, "is_active", false, "IS_ACTIVE");
        public final static Property Remember_token = new Property(11, String.class, "remember_token", false, "REMEMBER_TOKEN");
    };


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'USER' (" + //
                "'ID' INTEGER PRIMARY KEY ," + // 0: id
                "'ROLE_ID' INTEGER," + // 1: role_id
                "'AVATAR' TEXT," + // 2: avatar
                "'NAME' TEXT," + // 3: name
                "'FULLNAME' TEXT," + // 4: fullname
                "'PHONE' TEXT," + // 5: phone
                "'EMAIL' TEXT," + // 6: email
                "'CREATED_AT' TEXT," + // 7: created_at
                "'UPDATED_AT' TEXT," + // 8: updated_at
                "'LAST_LOGIN' TEXT," + // 9: last_login
                "'IS_ACTIVE' INTEGER," + // 10: is_active
                "'REMEMBER_TOKEN' TEXT);"); // 11: remember_token
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'USER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer role_id = entity.getRole_id();
        if (role_id != null) {
            stmt.bindLong(2, role_id);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(3, avatar);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        String fullname = entity.getFullname();
        if (fullname != null) {
            stmt.bindString(5, fullname);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(6, phone);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(7, email);
        }
 
        String created_at = entity.getCreated_at();
        if (created_at != null) {
            stmt.bindString(8, created_at);
        }
 
        String updated_at = entity.getUpdated_at();
        if (updated_at != null) {
            stmt.bindString(9, updated_at);
        }
 
        String last_login = entity.getLast_login();
        if (last_login != null) {
            stmt.bindString(10, last_login);
        }
 
        Integer is_active = entity.getIs_active();
        if (is_active != null) {
            stmt.bindLong(11, is_active);
        }
 
        String remember_token = entity.getRemember_token();
        if (remember_token != null) {
            stmt.bindString(12, remember_token);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // role_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // avatar
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // fullname
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // phone
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // email
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // created_at
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // updated_at
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // last_login
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // is_active
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // remember_token
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRole_id(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setAvatar(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFullname(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPhone(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setEmail(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCreated_at(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUpdated_at(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setLast_login(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setIs_active(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setRemember_token(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(User entity) {
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