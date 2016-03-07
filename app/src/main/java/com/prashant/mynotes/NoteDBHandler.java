package com.prashant.mynotes;

/**
 * Created by shishir on 3/5/16.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NoteDBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "noteItDB.db";
    private static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = "noteId";
    public static final String COLUMN_USERID = "userId";
    public static final String COLUMN_NOTETITLE = "noteTitle";
    public static final String COLUMN_NOTEDESCRIPTION = "noteDescription";
    public static final String COLUMN_TIMESTAMP = "timeStamp";

    public NoteDBHandler(Context context, String name,
                         SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public NoteDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERID + " TEXT, "
                + COLUMN_NOTETITLE + " TEXT, "
                + COLUMN_NOTEDESCRIPTION + " TEXT, "
                + COLUMN_TIMESTAMP + "DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";
        try{
            db.execSQL(CREATE_PRODUCTS_TABLE);
            Log.e("DATABASE:","CREATED");
        }catch(Exception e){
            Log.e(null,"Some error...can't create");
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addNote(NotesModel note) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERID, note.getUserId());
        values.put(COLUMN_NOTETITLE, note.getNoteTitle());
        values.put(COLUMN_NOTEDESCRIPTION, note.getNoteDescription());

        SQLiteDatabase db = this.getWritableDatabase();

//        db.insert(TABLE_NAME, null, values);
//        db.close();

        boolean createSuccessful = db.insert(TABLE_NAME, null, values) > 0;
        db.close();
        return createSuccessful;
    }

    public int updateNote(NotesModel note){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NOTETITLE,note.getNoteTitle()); //These Fields should be your String values of actual column names
        cv.put(COLUMN_NOTEDESCRIPTION, note.getNoteDescription());
        SQLiteDatabase db = this.getWritableDatabase();
        //number of rows changed
        int result = db.update(TABLE_NAME, cv, COLUMN_ID + " = " + note.getNoteId(), null);
        db.close();
        return result;

    }

    public int deleteNote(int NoteIdToDelete){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_ID + " = " + NoteIdToDelete, null);
        return result;
    }

    public int checkForNull(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM notes";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        return icount;
    }



    public ArrayList<NotesModel> findNotesByUser(String byUser) {

        ArrayList<NotesModel> NoteListbyUser = new ArrayList<NotesModel>();
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERID + " =  \"" + byUser + "\"";
//        NotesModel userNotes = new NotesModel();

//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        try {

            // Move to first row
            if (!cursor.moveToFirst())
                return NoteListbyUser;
            do {

                NotesModel userNotes = new NotesModel();
                userNotes.setNoteId(Integer.parseInt(cursor.getString(0)));
                userNotes.setUserId(cursor.getString(1));
                userNotes.setNoteTitle(cursor.getString(2));
                userNotes.setNoteDescription(cursor.getString(3));
                userNotes.setTimeOfCreate(Timestamp.valueOf(cursor.getString(4)));

                NoteListbyUser.add(userNotes);

            }  while(cursor.moveToNext());
            cursor.close();
            db.close();
            return NoteListbyUser;
        }
        catch (Exception e){
            Log.e("bas kar","!!!");
        }
        finally {
            cursor.close();
            db.close();
        }


//
//        if (cursor.moveToFirst()) {
//            while (cursor.isAfterLast() == false){
//            userNotes.setNoteId(Integer.parseInt(cursor.getString(0)));
//            userNotes.setUserId(cursor.getString(1));
//            userNotes.setNoteTitle(cursor.getString(2));
//            userNotes.setNoteDescription(cursor.getString(3));
//            userNotes.setTimeOfCreate(Timestamp.valueOf(cursor.getString(4)));
//
//            NoteListbyUser.add(userNotes);}
//
//        } else {
//            userNotes = null;
//        }
//        cursor.close();
//        db.close();
//        return NoteListbyUser;

        return NoteListbyUser;
    };

    public ArrayList<NotesModel> findNotesByNoteID(int NoteID) {

        ArrayList<NotesModel> NoteListbyNoteID = new ArrayList<NotesModel>();
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = "+  NoteID ;
//        NotesModel userNotes = new NotesModel();

//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        try {

            // Move to first row
            if (!cursor.moveToFirst())
                return NoteListbyNoteID;
            do {
                //Todo todo = new Todo();
                NotesModel userNotes = new NotesModel();
                userNotes.setNoteId(Integer.parseInt(cursor.getString(0)));
                userNotes.setUserId(cursor.getString(1));
                userNotes.setNoteTitle(cursor.getString(2));
                userNotes.setNoteDescription(cursor.getString(3));
                userNotes.setTimeOfCreate(Timestamp.valueOf(cursor.getString(4)));

//                todo.id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
//                todo.from = cursor.getString(cursor.getColumnIndex(COLUMN_FROM));
//                todo.to = cursor.getString(cursor.getColumnIndex(COLUMN_TO));
//                todo.title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
//                todo.tag = cursor.getString(cursor.getColumnIndex(COLUMN_TAG));
//                arList.add(todo);

                NoteListbyNoteID.add(userNotes);

            }  while(cursor.moveToNext());
            cursor.close();
            db.close();
            return NoteListbyNoteID;
        }
        catch (Exception e){
            Log.e("bas kar","!!!");
        }
        finally {
            cursor.close();
            db.close();
        }


//
//        if (cursor.moveToFirst()) {
//            while (cursor.isAfterLast() == false){
//            userNotes.setNoteId(Integer.parseInt(cursor.getString(0)));
//            userNotes.setUserId(cursor.getString(1));
//            userNotes.setNoteTitle(cursor.getString(2));
//            userNotes.setNoteDescription(cursor.getString(3));
//            userNotes.setTimeOfCreate(Timestamp.valueOf(cursor.getString(4)));
//
//            NoteListbyUser.add(userNotes);}
//
//        } else {
//            userNotes = null;
//        }
//        cursor.close();
//        db.close();
//        return NoteListbyUser;

        return NoteListbyNoteID;
    };

    //    public boolean createNote(NotesModel createdNote){
//
//
//        //String query = "INSERT INTO " + TABLE_NAME + " (userId, noteTitle, noteDescription) VALUES ( \"" + userId + "\", \"" + noteTitle + "\", \"" + noteDescription + "\" )";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//
//        values.put(COLUMN_USERID, createdNote.getUserId());
//        values.put(COLUMN_NOTETITLE, createdNote.getNoteTitle());
//        values.put(COLUMN_NOTEDESCRIPTION, createdNote.getNoteDescription());
//        values.put(COLUMN_TIMESTAMP, getDateTime());
//
//        boolean createSuccessful = db.insert(TABLE_NAME, null, values) > 0;
//        db.close();
//
//        return createSuccessful;
//    }
// public boolean farji ()
// {
//     boolean flag  = false;
//
//     SQLiteDatabase dbfarji = this.getWritableDatabase();
//     ;
//
//     String query = "INSERT INTO notes (userId, noteTitle, noteDescription ) values (\"kpt@gmail.com\" , \"testtitle \" , \"test descrip \")";
//
//     try {
//         dbfarji.execSQL(query);
//         flag  = true;
//     }
//     catch (Exception e) {
//         flag = false;
//     }
//
//     return flag;
// }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }



}