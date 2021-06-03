package com.example.gcpurchasesapp;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GroceryClub.db";
    public static final String TABLE_NAME = "Products_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "PRODUCT_DESC";
    public static final String COL_3 = "CATEGORY";
    public static final String COL_4 = "QTY_BOUGHT";
    public static final String COL_5 = "QTY_PER_PERSON";
    public static final String COL_6 = "PRICE_PER_UNIT";
    public static final String COL_7= "TOTAL_PURCHASE_PRICE";
    public static final String COL_8 = "STORE";
    public static final String COL_9 = "DATE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT,PRODUCT_DESC TEXT, CATEGORY TEXT,QTY_BOUGHT INTEGER,QTY_PER_PERSON INTEGER,PRICE_PER_UNIT INTEGER," +
                "TOTAL_PURCHASE_PRICE INTEGER,STORE TEXT,DATE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String description, String category, String qtyBought, String qtyPP, String unitPrice,String totalPrice, String store,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, description);
        contentValues.put(COL_3, category);
        contentValues.put(COL_4, qtyBought);
        contentValues.put(COL_5, qtyPP);
        contentValues.put(COL_6, unitPrice);
        contentValues.put(COL_7, totalPrice);
        contentValues.put(COL_8, store);
        contentValues.put(COL_9, date);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+ TABLE_NAME,null);
        return res;
    }

    public Cursor getDataByCategory(String c)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+ TABLE_NAME + " where CATEGORY = '"+ c+"'",null);

        return res;
    }

    public Cursor getDataByStore(String s)
    {


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+ TABLE_NAME + " where STORE = '"+ s+"' COLLATE NOCASE",null);
        return res;
    }

    public Cursor search(String x)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+ TABLE_NAME + " where PRODUCT_DESC like'%"+ x
                +"%' COLLATE NOCASE",null);
        return res;
    }


  /*  public Cursor searchByDate(String x,String y)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+ TABLE_NAME + " where DATE  between '"+ x
                +"' AND '"+ y +"'",null);
        return res;
    }*/

    public Cursor searchByID(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+ TABLE_NAME + " where ID = '"+ id+"'",null);
        return res;

    }




    public boolean updateData(String ID,String desc,String cat, String qtyBought,String qtyPP,String unitPrice,String totalPrice, String store, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, desc);
        contentValues.put(COL_3, cat);
        contentValues.put(COL_4, qtyBought);
        contentValues.put(COL_5, qtyPP);
        contentValues.put(COL_6, unitPrice);
        contentValues.put(COL_7, totalPrice);
        contentValues.put(COL_8, store);
        contentValues.put(COL_9, date);
        db.update(TABLE_NAME,contentValues,"ID=?",new String[] {ID});
        return true;
    }


    public Integer deleteData(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID=?",new String[] {ID});

    }

    public int getTotal(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor tot=db.rawQuery("select SUM(TOTAL_PURCHASE_PRICE) from "+ TABLE_NAME,null);

        tot.moveToFirst();
        int i = tot.getInt(0);
        tot.close();
        return i;

    }
}
