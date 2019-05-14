package com.genialabs.dermia.Controllers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.genialabs.dermia.Models.AlbumMain;
import com.genialabs.dermia.Models.Prediction;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "geniadb";
    private static final String TABLE_Predictions = "predictions";
    private static final String TABLE_AlbumMain = "albummanin";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PREDICTION = "prediction";
    private static final String KEY_URI = "uri";
    private static final String KEY_DATE = "date";
    private static final String KEY_RESUME = "resume";
    private static final String KEY_ID_ALBUM = "idalbum";

    private static final String KEY_ALBUM_NAME = "albumname";
    private static final String KEY_ALBUM_PLUS = "albumplus";
    private static final String KEY_BODY_PART = "bodypart";

    public DbHandler(Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Predictions + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_PREDICTION + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_RESUME + " TEXT,"
                + KEY_ID_ALBUM + " TEXT DEFAULT '0',"
                + KEY_URI + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE);

        String CREATE_TABLE2 = "CREATE TABLE " + TABLE_AlbumMain + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_ALBUM_NAME + " TEXT DEFAULT '0',"
                + KEY_ALBUM_PLUS + " TEXT,"
                + KEY_BODY_PART + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE2);

    }

    //SI CAMBIO ALGO EN LA BD ACTUALIZAR AL USUARIO DESDE AQUI
    private static final String DATABASE_SIN_IDALBUM = "ALTER TABLE "
            + TABLE_Predictions + " ADD COLUMN " + KEY_ID_ALBUM + " string DEFAULT '0';";
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        //HACER LOS CAMBIOS ASI EN VES DE LO DE ABAJO
        /*if (oldVersion < 2) {
            db.execSQL(DATABASE_SIN_IDALBUM);
        }*/
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Predictions);//me la borraria cada vez que cambio de version
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AlbumMain);//me la borraria cada vez que cambio de version
        // Create tables again
        onCreate(db);
    }

    public long insertPrediction(String name, String pred, String uri, String date,String resume, String idalbum){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_NAME, name);
        cValues.put(KEY_PREDICTION, pred);
        cValues.put(KEY_URI, uri);
        cValues.put(KEY_DATE, date);
        cValues.put(KEY_RESUME, resume);
        cValues.put(KEY_ID_ALBUM, idalbum);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_Predictions,null, cValues);
        db.close();
        return newRowId;
    }

    public long insertAlbum(String albumname, String bodypart, String albumplus){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ALBUM_NAME, albumname);
        cValues.put(KEY_BODY_PART, bodypart);
        cValues.put(KEY_ALBUM_PLUS, albumplus);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_AlbumMain,null, cValues);
        db.close();
        return newRowId;
    }

    public ArrayList<HashMap<String, String>>  getPrediction(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT name, prediction, date, resume FROM "+ TABLE_Predictions;
        Cursor cursor = db.query(TABLE_Predictions, new String[]{KEY_NAME, KEY_PREDICTION, KEY_URI,KEY_DATE, KEY_RESUME}, KEY_ID+ "=?",new String[]{String.valueOf(id)},null, null, null, null);
        if (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("name",cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            user.put("prediction",cursor.getString(cursor.getColumnIndex(KEY_PREDICTION)));
            user.put("uri",cursor.getString(cursor.getColumnIndex(KEY_URI)));
            user.put("date",cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            user.put("resume",cursor.getString(cursor.getColumnIndex(KEY_RESUME)));
            userList.add(user);
        }
        return  userList;
    }
    public ArrayList<Prediction> getPredsFromAlbum(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT name, prediction, uri, id, date, resume, idalbum FROM "+ TABLE_Predictions + " WHERE idalbum =="+id+ " ORDER BY date DESC";
        Cursor cursor = db.rawQuery(query,null);
        ArrayList<Prediction> predsList = new ArrayList<>();

        while (cursor.moveToNext()){
            Prediction pred = new Prediction(cursor.getString(cursor.getColumnIndex(KEY_URI)),
                    cursor.getString(cursor.getColumnIndex(KEY_PREDICTION)),
                    Integer.valueOf(cursor.getString(cursor.getColumnIndex(KEY_NAME))),
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_RESUME)),
                    cursor.getString(cursor.getColumnIndex(KEY_ID_ALBUM))
            );
            predsList.add(pred);
        }
        return  predsList;

    }
    public ArrayList<Prediction> getAllPredictions(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Prediction> predsList = new ArrayList<>();
        String query = "SELECT name, prediction, uri, id, date, resume, idalbum FROM "+ TABLE_Predictions + " ORDER BY date DESC";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            Prediction pred = new Prediction(cursor.getString(cursor.getColumnIndex(KEY_URI)),
                    cursor.getString(cursor.getColumnIndex(KEY_PREDICTION)),
                    Integer.valueOf(cursor.getString(cursor.getColumnIndex(KEY_NAME))),
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_RESUME)),
                    cursor.getString(cursor.getColumnIndex(KEY_ID_ALBUM))
            );
            predsList.add(pred);
        }
        return  predsList;
    }
    // Delete User Details
    public void DeletePred(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Predictions, KEY_ID+" = ?",new String[]{String.valueOf(userid)});
        db.close();
    }

    public void DeleteAlbum(int userid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AlbumMain, KEY_ID+" = ?",new String[]{String.valueOf(userid)});
        db.close();
    }
    // Update User Details
    public int UpdatePredDetails(String pred, String uri, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(KEY_PREDICTION, pred);
        cVals.put(KEY_URI, uri);
        int count = db.update(TABLE_Predictions, cVals, KEY_ID+" = ?",new String[]{String.valueOf(id)});
        return  count;
    }
    public int UpdateAlbumImage(String id,String uri){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cVals = new ContentValues();
        cVals.put(KEY_ALBUM_PLUS, uri);
        int count = db.update(TABLE_AlbumMain, cVals, KEY_ID+" = ?",new String[]{String.valueOf(id)});
        return  count;
    }
    public ArrayList<AlbumMain> getAllAlbumsMain(){
        ArrayList<AlbumMain> albumList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT id, albumname, bodypart, albumplus FROM "+ TABLE_AlbumMain + " ORDER BY id DESC";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            AlbumMain album = new AlbumMain(cursor.getString(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_ALBUM_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_BODY_PART)),
                    cursor.getString(cursor.getColumnIndex(KEY_ALBUM_PLUS))
            );
            albumList.add(album);
        }
        return albumList;
    }
}
