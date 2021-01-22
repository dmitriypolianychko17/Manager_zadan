package com.dmytro.manager_zadan.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dmytro.manager_zadan.adapter.Zadania;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";

    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "manager_zadan";

    private static final String TABLE_NAME = "zadania";
    private static final String _ID = "id";
    private static final String KEY_ACTION = "action"; //Skonczona(1), Nieskonczona(0)
    private static final String TASK_DATE = "task_date";
    private static final String TASK_DESC = "task_desc";
    private static final String BACK_COLOR = "back_color";

    public DatabaseHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY,"
                + KEY_ACTION + " INTEGER,"
                + TASK_DATE + " DATETIME,"
                + BACK_COLOR + " TEXT,"
                + TASK_DESC + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // CRUD
    // Dodawanie
    public long addTask(Zadania zadania){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ACTION, zadania.getAction());
        values.put(TASK_DATE, zadania.getDate());
        values.put(TASK_DESC, zadania.getDesc());
        values.put(BACK_COLOR, zadania.getBackColor());

        //Dodajemy kolumne
        return  db.insert(TABLE_NAME, null, values);
    }

    //Pobieranie po id
    public Zadania getZadania(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + _ID + " = " + id;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor!=null)
            cursor.moveToFirst();

            Zadania zadania = new Zadania();
            zadania.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
            zadania.setAction(cursor.getInt(cursor.getColumnIndex(KEY_ACTION)));
            zadania.setDesc(cursor.getString(cursor.getColumnIndex(TASK_DESC)));
            zadania.setDate(cursor.getString(cursor.getColumnIndex(TASK_DATE)));
            zadania.setBackColor(cursor.getString(cursor.getColumnIndex(BACK_COLOR)));
            return zadania;
    }
    //Pobieranie wszystkich zadan
    public List<Zadania> getAllTask(){
        List<Zadania> zadanias = new ArrayList<Zadania>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                Zadania zadania = new Zadania();
                zadania.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
                zadania.setAction(cursor.getInt(cursor.getColumnIndex(KEY_ACTION)));
                zadania.setDesc(cursor.getString(cursor.getColumnIndex(TASK_DESC)));
                zadania.setDate(cursor.getString(cursor.getColumnIndex(TASK_DATE)));
                zadania.setBackColor(cursor.getString(cursor.getColumnIndex(BACK_COLOR)));
                zadanias.add(zadania);
            } while (cursor.moveToNext());
        }
        return zadanias;
    }

    //Pobieranie zadan ze wzgledu na date
    public List<Zadania> getAllTaskByDate(String date){
        String startDate = "'"+date+" 00:00:00'";
        String endDate = "'"+date+" 23:59:59'";

        List<Zadania> zadanias = new ArrayList<Zadania>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE "+TASK_DATE+" BETWEEN "+startDate+" AND "+endDate+" ORDER BY "+TASK_DATE+" ASC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            do {
                Zadania zadania = new Zadania();
                zadania.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
                zadania.setAction(cursor.getInt(cursor.getColumnIndex(KEY_ACTION)));
                zadania.setDesc(cursor.getString(cursor.getColumnIndex(TASK_DESC)));
                zadania.setDate(cursor.getString(cursor.getColumnIndex(TASK_DATE)));
                zadania.setBackColor(cursor.getString(cursor.getColumnIndex(BACK_COLOR)));
                zadanias.add(zadania);
            } while (cursor.moveToNext());
        }
        return zadanias;
    }

    //Aktualizacja action zadania
    public void updateAction(int id, int action){
        SQLiteDatabase db = this.getWritableDatabase();
        String stringSQL = "UPDATE " + TABLE_NAME + " SET " + KEY_ACTION + " = " + action + " WHERE " + _ID + " = " +id;
        db.execSQL(stringSQL);
    }

    //Aktualizacja zadan
    public  void updateTask(Zadania zadania, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String stringSQL = "UPDATE "+TABLE_NAME+" SET "+KEY_ACTION+" = "+zadania.getAction()+","
                +TASK_DESC+" = '"+zadania.getDesc()+"',"
                +TASK_DATE+" = '"+zadania.getDate()+"',"
                +BACK_COLOR+" = '"+zadania.getBackColor()
                + "' WHERE "+_ID+" = "+id;
        db.execSQL(stringSQL);
    }

    //Usuwanie zadan
    public void deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String stringSQL = "DELETE FROM "+TABLE_NAME+" WHERE "+_ID+" = " + id;
        db.execSQL(stringSQL);
    }

    //zamykanie bazy danych
    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    private  String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}

