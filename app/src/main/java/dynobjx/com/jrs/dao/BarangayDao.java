package dynobjx.com.jrs.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import dynobjx.com.jrs.dao.Barangay;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table BARANGAY.
*/
public class BarangayDao extends AbstractDao<Barangay, Long> {

    public static final String TABLENAME = "BARANGAY";

    /**
     * Properties of entity Barangay.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property BrgyId = new Property(1, Integer.class, "brgyId", false, "BRGY_ID");
        public final static Property BrgyName = new Property(2, String.class, "brgyName", false, "BRGY_NAME");
        public final static Property ProvinceId = new Property(3, Integer.class, "provinceId", false, "PROVINCE_ID");
        public final static Property MunicipalityId = new Property(4, Integer.class, "municipalityId", false, "MUNICIPALITY_ID");
    };


    public BarangayDao(DaoConfig config) {
        super(config);
    }
    
    public BarangayDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'BARANGAY' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'BRGY_ID' INTEGER," + // 1: brgyId
                "'BRGY_NAME' TEXT," + // 2: brgyName
                "'PROVINCE_ID' INTEGER," + // 3: provinceId
                "'MUNICIPALITY_ID' INTEGER);"); // 4: municipalityId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BARANGAY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Barangay entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer brgyId = entity.getBrgyId();
        if (brgyId != null) {
            stmt.bindLong(2, brgyId);
        }
 
        String brgyName = entity.getBrgyName();
        if (brgyName != null) {
            stmt.bindString(3, brgyName);
        }
 
        Integer provinceId = entity.getProvinceId();
        if (provinceId != null) {
            stmt.bindLong(4, provinceId);
        }
 
        Integer municipalityId = entity.getMunicipalityId();
        if (municipalityId != null) {
            stmt.bindLong(5, municipalityId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Barangay readEntity(Cursor cursor, int offset) {
        Barangay entity = new Barangay( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // brgyId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // brgyName
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // provinceId
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4) // municipalityId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Barangay entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBrgyId(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setBrgyName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setProvinceId(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setMunicipalityId(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Barangay entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Barangay entity) {
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