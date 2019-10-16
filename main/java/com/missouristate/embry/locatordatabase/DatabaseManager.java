package com.missouristate.embry.locatordatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "locationDB";
    private static final int DATABASE_VERSION = 8;
    private static final String TABLE_GPS = "GPStable";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String LOCNAME = "location";
    private static final String ID = "id";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //  4/14 Changed ID to be incrementing integer. Replaced old Friends code with Locat class code.
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "create table " + TABLE_GPS + "( " + ID + " integer primary key autoincrement, ";
        sqlCreate += LOCNAME + " TEXT, " + LATITUDE + " TEXT, " + LONGITUDE + " TEXT )";

        db.execSQL(sqlCreate);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "drop table if exists " + TABLE_GPS);
        // Re-create tables
        onCreate(db);
    }

    public void insert(Locat locat) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlInsert = "insert into " + TABLE_GPS;
        sqlInsert +=" values( null, '" + locat.getName() + "', '" + locat.getLat() + "', '" + locat.getLong() + "' )";

        db.execSQL(sqlInsert);
        db.close();
    }

    public void deleteById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlDelete = "delete from " + TABLE_GPS;
        sqlDelete += " where " + ID + " = " + id;

        db.execSQL(sqlDelete);
        db.close();
    }

    public void updateById(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        Locat currentvalues = selectById(id);
        String lat = currentvalues.getLat();
        String lon = currentvalues.getLong();



        String sqlUpdate = "update " + TABLE_GPS;
        sqlUpdate += " set " + LOCNAME + " = '" + name + "', "; //+ ID + " = '" + id + "', "
        sqlUpdate += LATITUDE + " = '" + lat + "', ";
        sqlUpdate += LONGITUDE + " = '" + lon + "'";
        sqlUpdate += " where " + ID + " = " + id;

        db.execSQL(sqlUpdate);
        db.close();
    }


    public ArrayList<Locat> selectAll() {
        String sqlQuery = "select * from " + TABLE_GPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        ArrayList<Locat> locats = new ArrayList<Locat>();
        while(cursor.moveToNext()) {
            Locat currentLocat = new Locat(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            locats.add(currentLocat);
        }
        db.close();
        return locats;
    }

    public Locat selectById(int id) { //might be useful later
        String sqlQuery = "select * from " + TABLE_GPS;
        sqlQuery += " where " + ID + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        Locat locat = null;
        if(cursor.moveToFirst()) {
            locat = new Locat(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }
            return locat;
    }
}
