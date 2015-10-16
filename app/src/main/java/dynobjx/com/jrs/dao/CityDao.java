package dynobjx.com.jrs.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import dynobjx.com.jrs.dao.City;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table CITY.
*/
public class CityDao extends AbstractDao<City, Long> {

    public static final String TABLENAME = "CITY";

    /**
     * Properties of entity City.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CityId = new Property(1, Integer.class, "cityId", false, "CITY_ID");
        public final static Property CityName = new Property(2, String.class, "cityName", false, "CITY_NAME");
        public final static Property ProvinceId = new Property(3, Integer.class, "provinceId", false, "PROVINCE_ID");
    };


    public CityDao(DaoConfig config) {
        super(config);
    }
    
    public CityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CITY' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'CITY_ID' INTEGER," + // 1: cityId
                "'CITY_NAME' TEXT," + // 2: cityName
                "'PROVINCE_ID' INTEGER);"); // 3: provinceId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CITY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, City entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer cityId = entity.getCityId();
        if (cityId != null) {
            stmt.bindLong(2, cityId);
        }
 
        String cityName = entity.getCityName();
        if (cityName != null) {
            stmt.bindString(3, cityName);
        }
 
        Integer provinceId = entity.getProvinceId();
        if (provinceId != null) {
            stmt.bindLong(4, provinceId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public City readEntity(Cursor cursor, int offset) {
        City entity = new City( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // cityId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // cityName
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3) // provinceId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, City entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCityId(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setCityName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setProvinceId(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(City entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(City entity) {
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
