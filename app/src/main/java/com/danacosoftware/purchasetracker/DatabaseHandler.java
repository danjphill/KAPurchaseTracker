package com.danacosoftware.purchasetracker;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Phillips on 8/7/2016.
 */
public class DatabaseHandler  extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "purchase_tracker_db";
    private static final String TABLE_ITEMS = "items";


    //Table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_BARCODE = "barcode";
    private static final String KEY_TYPE = "type";
    private static final String KEY_COLOUR = "color";
    private static final String KEY_PATTERN = "pattern";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_SIZE = "size";
    private static final String KEY_PRICE = "price";
    private static final String KEY_STORE = "store";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PIECES = "pieces";
    private static final String KEY_ACCESSORIES = "accessories";
    private static final String KEY_PICTURE = "picture";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_BARCODE + " TEXT,"
                + KEY_TYPE + " TEXT," +KEY_COLOUR + " TEXT," +KEY_PATTERN + " TEXT," +KEY_GENDER + " TEXT," +
                KEY_BRAND + " TEXT," +KEY_SIZE + " TEXT," +KEY_PRICE + " TEXT," +KEY_STORE + " TEXT," +KEY_DESCRIPTION +
                " TEXT,"+KEY_PIECES + " TEXT," +KEY_ACCESSORIES + " TEXT,"+ KEY_PICTURE + " TEXT );";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);

    }
    // Adding new contact
    public void addItem(ItemObject Item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BARCODE, Item.getBarcode());
        values.put(KEY_TYPE, Item.getType());
        values.put(KEY_COLOUR, Item.getColour());
        values.put(KEY_PATTERN, Item.getPattern());
        values.put(KEY_GENDER, Item.getGender());
        values.put(KEY_BRAND, Item.getBrand());
        values.put(KEY_SIZE, Item.getSize());
        values.put(KEY_PRICE, Item.getPrice());
        values.put(KEY_STORE, Item.getStore());
        values.put(KEY_DESCRIPTION, Item.getDescription());
        values.put(KEY_PIECES, Item.getPieces());
        values.put(KEY_ACCESSORIES, Item.getAccessories());
        values.put(KEY_PICTURE, Item.getPicture());
        // Inserting Row
        db.insert(TABLE_ITEMS, null, values);
        db.close(); // Closing database connection

        Log.d("AddingItem","KEY_PICTURE");
    }

    public int updateItem(ItemObject Item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BARCODE, Item.getBarcode());
        values.put(KEY_TYPE, Item.getType());
        values.put(KEY_COLOUR, Item.getColour());
        values.put(KEY_PATTERN, Item.getPattern());
        values.put(KEY_GENDER, Item.getGender());
        values.put(KEY_BRAND, Item.getBrand());
        values.put(KEY_SIZE, Item.getSize());
        values.put(KEY_PRICE, Item.getPrice());
        values.put(KEY_STORE, Item.getStore());
        values.put(KEY_DESCRIPTION, Item.getDescription());
        values.put(KEY_PIECES, Item.getPieces());
        values.put(KEY_ACCESSORIES, Item.getAccessories());
        values.put(KEY_PICTURE, Item.getPicture());
        Log.d("Updating",String.valueOf(Item.getID()));
        // updating row
        return db.update(TABLE_ITEMS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(Item.getID()) });
    }

    public void deleteItem(ItemObject Item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + " = ?",
                new String[] { String.valueOf(Item.getID()) });
        db.close();


    }

    public List<ItemObject> getAllItems() {
        List<ItemObject> itemsList = new ArrayList<ItemObject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemObject item = new ItemObject();
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setBarcode(cursor.getString(1));
                item.setType(cursor.getString(2));
                item.setColour(cursor.getString(3));
                item.setPattern(cursor.getString(4));
                item.setGender(cursor.getString(5));
                item.setBrand(cursor.getString(6));
                item.setSize(cursor.getString(7));
                item.setPrice(cursor.getString(8));
                item.setStore(cursor.getString(9));
                item.setDescription(cursor.getString(10));
                item.setPieces(cursor.getString(11));
                item.setAccessories(cursor.getString(12));
                item.setPicture(cursor.getString(13));
                // Adding contact to list
                itemsList.add(item);
            } while (cursor.moveToNext());
        }

        // return contact list
        return itemsList;
    }
    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public ItemObject getItem(int ID){
        Cursor cursor = null;
        ItemObject item = new ItemObject();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            String Itemquery = "SELECT * FROM "+TABLE_ITEMS +" WHERE "+KEY_ID +"="+ID;
            Log.d("Itemquery",Itemquery);
            cursor = db.rawQuery(Itemquery, null);

            if(cursor.moveToFirst()) {
                item.setID(Integer.parseInt(cursor.getString(0)));
                item.setBarcode(cursor.getString(1));
                item.setType(cursor.getString(2));
                item.setColour(cursor.getString(3));
                item.setPattern(cursor.getString(4));
                item.setGender(cursor.getString(5));
                item.setBrand(cursor.getString(6));
                item.setSize(cursor.getString(7));
                item.setPrice(cursor.getString(8));
                item.setStore(cursor.getString(9));
                item.setDescription(cursor.getString(10));
                item.setPieces(cursor.getString(11));
                item.setAccessories(cursor.getString(12));
                item.setPicture(cursor.getString(13));
            }

            return item;
        }finally {

            cursor.close();
        }
    }
}
