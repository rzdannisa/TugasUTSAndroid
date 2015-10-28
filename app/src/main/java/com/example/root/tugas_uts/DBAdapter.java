package com.example.root.tugas_uts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import domain.Kucing;

/**
 * Created by root on 28/10/15.
 */
public class DBAdapter extends SQLiteOpenHelper{
    private static final String DB_NAME ="datakucing";
    private static final String TABLE_NAME = "kucing";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "nama_kucing";
    private static final String COL_GENDER = "gender_kucing";
    private static final String COL_JENIS = "jenis_kucing";
    private static final String DROP_TABLE = "DROP TABLE IF EXIST" + TABLE_NAME + ";";
    private SQLiteDatabase sqLiteDatabase = null;

    public DBAdapter(Context context) { super(context, DB_NAME, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) { createTable(db); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion ) {
        db.execSQL(DROP_TABLE);
    }

    public void openDB() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = getWritableDatabase();
        }
    }

    public void closeDB() {
        if (sqLiteDatabase != null) {
            if (sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
    }

    public void createTable (SQLiteDatabase db) {
        db.execSQL("CREATE TABLE"+TABLE_NAME+"("+COL_ID+"INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+COL_NAME+"TEXT,"+COL_GENDER+"TEXT);");
    }

    public void save(Kucing kucing) {
        sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, kucing.getNama_kucing());
        contentValues.put(COL_GENDER, kucing.getGender_kucing());
        contentValues.put(COL_JENIS, kucing.getJenis_kucing());

        sqLiteDatabase.insertWithOnConflict(TABLE_NAME, null, contentValues, sqLiteDatabase.CONFLICT_IGNORE);

        sqLiteDatabase.close();
    }

    public void updateKucing(Kucing kucing) {
        sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, kucing.getNama_kucing());
        cv.put(COL_GENDER, kucing.getGender_kucing());
        cv.put(COL_JENIS, kucing.getJenis_kucing());
        String whereClause = COL_ID+"==?";
        String whereArgs[] = new String[] {kucing.getId()};
        sqLiteDatabase.update(TABLE_NAME, cv, whereClause, whereArgs);
        sqLiteDatabase.close();
    }

    public void delete(Kucing kucing) {
        sqLiteDatabase = getWritableDatabase();
        String whereClause = COL_ID+"==?";
        String whereArgs[] = new String[] { String.valueOf(kucing.getId()) };
        sqLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
        sqLiteDatabase.close();
    }

    public void deleteAll() {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }

    public List<Kucing> getAllKucing() {
        sqLiteDatabase = getWritableDatabase();

        Cursor cursor = this.sqLiteDatabase.query(TABLE_NAME, new  String[] {
                COL_ID, COL_NAME, COL_GENDER, COL_JENIS }, null, null, null, null, null);
                List<Kucing> kucings = new ArrayList<>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Kucing kucing = new Kucing();
                kucing.setId(cursor.getString(cursor.getColumnIndex(COL_ID)));
                kucing.setNama_kucing(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                kucing.setGender_kucing(cursor.getString(cursor.getColumnIndex(COL_GENDER)));
                kucing.setJenis_kucing(cursor.getString(cursor.getColumnIndex(COL_JENIS)));
                kucings.add(kucing);
            }
            sqLiteDatabase.close();
            return kucings;
        } else {
            sqLiteDatabase.close();
            return new ArrayList<Kucing>();
        }
    }
