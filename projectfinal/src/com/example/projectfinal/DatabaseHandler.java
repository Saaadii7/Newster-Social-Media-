package com.example.projectfinal;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "MydataDB";
 
    // Contacts table name
    private static final String TABLE_NEWS = "news";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_Title = "Title";
    private static final String KEY_Image = "DImage";
    private static final String KEY_Description = "Description";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NEWS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_Title + " TEXT,"
                + KEY_Description + " TEXT," + KEY_Image + " BLOB);";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new contact
    void addContact(Integer id,String Title, String Desc, byte[] img) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID,id);
        values.put(KEY_Title, Title);
        values.put(KEY_Description,Desc);
        values.put(KEY_Image,img);
 
        // Inserting Row
        db.insert(TABLE_NEWS, null, values);
        db.close(); // Closing database connection
    }


 
    // Getting All Contacts
    public ArrayList<mydata> getAllContacts() {
        ArrayList<mydata> list = new ArrayList<mydata>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NEWS;
 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                mydata dta = new mydata();
                dta.id=cursor.getInt(0);
                dta.Title=(cursor.getString(1));
                dta.Description=(cursor.getString(2));
                dta.imgbitmap=cursor.getBlob(3);
                // Adding contact to list
                list.add(dta);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return list;
    }
 

    // Deleting single contact
    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NEWS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
 
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NEWS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    } 
}