package com.prashant.mynotes;

import java.sql.Timestamp;

/**
 * Created by prashant on 3/5/16.
 */
public class NotesModel {




    private int noteId;
    private Timestamp timeOfCreate;
    private String userId;
    private String noteTitle;
    private String noteDescription;

//    NotesModel (String text1, String text2){
//        noteTitle = text1;
//        noteDescription = text2;
//    }

    public Timestamp getTimeOfCreate() {
        return timeOfCreate;
    }

    public void setTimeOfCreate(Timestamp timeOfCreate) {
        this.timeOfCreate = timeOfCreate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }
    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }




}
