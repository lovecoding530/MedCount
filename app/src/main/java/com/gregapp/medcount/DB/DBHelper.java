package com.gregapp.medcount.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gregapp.medcount.Helper.MyHelper;
import com.gregapp.medcount.Model.BarcodeItem;
import com.gregapp.medcount.Model.PhotoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kangtle_R on 12/25/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "db_medcount";

    //================== Table barcode==================
    public static final String BARCODE_TABLE = "tbl_barcode";
    public static final String BARCODE_COL_ID = "_id";
    public static final String BARCODE_COL_TIMESTAMP = "timestamp";
    public static final String BARCODE_COL_CODE = "code";

    //================== Table photo====================
    public static final String PHOTO_TABLE = "tbl_photo";
    public static final String PHOTO_COL_ID = "_id";
    public static final String PHOTO_COL_TIMESTAMP = "timestamp";
    public static final String PHOTO_COL_LASTREMINDTIME = "last_remind_time";
    public static final String PHOTO_COL_PHOTO_URL = "photo_url";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strCreateBarcodeTable = "CREATE TABLE " + BARCODE_TABLE + " (" +
                BARCODE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BARCODE_COL_TIMESTAMP + " INTEGER DEFAULT CURRENT_TIMESTAMP, " +
                BARCODE_COL_CODE + " TEXT" + ")";
        db.execSQL(strCreateBarcodeTable); // execute the query...

        String strCreatePhotoTable = "CREATE TABLE " + PHOTO_TABLE + " (" +
                PHOTO_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PHOTO_COL_TIMESTAMP + " INTEGER DEFAULT CURRENT_TIMESTAMP, " +
                PHOTO_COL_LASTREMINDTIME + " INTEGER DEFAULT CURRENT_TIMESTAMP, " +
                PHOTO_COL_PHOTO_URL + " TEXT" + ")";
        db.execSQL(strCreatePhotoTable); // execute the query...
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isExistingBarcode(String barcode){
        SQLiteDatabase db = this.getReadableDatabase();
        String strGetBarcode = String.format("select * from %s where %s='%s'", BARCODE_TABLE, BARCODE_COL_CODE, barcode);
        Cursor cursor = db.rawQuery(strGetBarcode, null);
        boolean isExisting = cursor.moveToFirst();
        cursor.close();
        return isExisting;
    }

    public long saveBarcodeItem(BarcodeItem barcodeItem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BARCODE_COL_TIMESTAMP, barcodeItem.timestamp);
        values.put(BARCODE_COL_CODE, barcodeItem.barcodeContent);

        return db.insert(BARCODE_TABLE, null, values);
    }

    public long savePhotoItem(PhotoItem photoItem){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PHOTO_COL_TIMESTAMP, photoItem.timestamp);
        values.put(PHOTO_COL_LASTREMINDTIME, photoItem.lastRemindTime);
        values.put(PHOTO_COL_PHOTO_URL, photoItem.photoUrl);

        return db.insert(PHOTO_TABLE, null, values);
    }

    public List<BarcodeItem> getAllBarcodeItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        String strGetAll = String.format("select * from %s order by timestamp desc", BARCODE_TABLE);
        Cursor cursor = db.rawQuery(strGetAll, null);
        List<BarcodeItem> allItems = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                long timestamp = cursor.getLong(cursor.getColumnIndex(BARCODE_COL_TIMESTAMP));
                String barcode = cursor.getString(cursor.getColumnIndex(BARCODE_COL_CODE));
                BarcodeItem barcodeItem = new BarcodeItem(timestamp, barcode);
                allItems.add(barcodeItem);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return allItems;
    }

    public List<PhotoItem> getAllPhotoItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        String strGetAll = String.format("select * from %s order by timestamp desc", PHOTO_TABLE);
        Cursor cursor = db.rawQuery(strGetAll, null);
        List<PhotoItem> allItems = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                long timestamp = cursor.getLong(cursor.getColumnIndex(PHOTO_COL_TIMESTAMP));
                long lastRemindTime = cursor.getLong(cursor.getColumnIndex(PHOTO_COL_LASTREMINDTIME));
                String photoUrl = cursor.getString(cursor.getColumnIndex(PHOTO_COL_PHOTO_URL));
                PhotoItem photoItem = new PhotoItem(timestamp, photoUrl, lastRemindTime);
                photoItem.bitmap = MyHelper.getScaledWBitMap(photoUrl, -1);
                allItems.add(photoItem);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return allItems;
    }
}
