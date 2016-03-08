package com.prashant.mynotes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prashant on 3/7/16.
 */
public class FragTwo extends Fragment implements View.OnClickListener {

    public SharedPreferences shareP;

    public List<NotesModel> noteList = new ArrayList<>();

    private EditText title;
    private EditText decsripion;
    private String strtext;
    private int position;
    ImageButton saveButton;
    ImageButton deleteButton;
    ImageButton cancelButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shareP = this.getActivity().getSharedPreferences("Mypref", Context.MODE_PRIVATE);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_two, container, false);
        // If no arguments passed i.e for creating new note
        if (getArguments() == null)
        {
            strtext = "null";
            title = (EditText) v.findViewById(R.id.note_title_content);
            decsripion  = (EditText) v.findViewById(R.id.note_description_content);

        }
        //if argument passed i.e. editing a note
        else {
            strtext = getArguments().getString("NoteID");
            position = Integer.parseInt(strtext);


            NoteDBHandler db = new NoteDBHandler(getActivity());
            try {
                noteList = db.findNotesByNoteID(position);

                NotesModel note = noteList.get(0);


                title = (EditText) v.findViewById(R.id.note_title_content);
                title.setText(note.getNoteTitle());

                decsripion = (EditText) v.findViewById(R.id.note_description_content);
                decsripion.setText(note.getNoteDescription());

            }
            catch (Exception e)
            {
                Toast.makeText(getActivity(), "The demo notes cannot be deleted/modified untill second run", Toast.LENGTH_LONG).show();
            }
        }


        saveButton = (ImageButton) v.findViewById(R.id.imageButtonSave);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   save data
                if(getArguments()==null){
                saveNewNote();
                    Log.e("save new note","");
                }
                else {
                    saveNote();
                    Log.e("save new note", "");
                }
                //creating fragments object
                FragOne fragmentTwo = new FragOne();
                //show edit note wala fragment
                CreateFragment(fragmentTwo, R.id.container_for_fragment);

            }
        });

        deleteButton = (ImageButton) v.findViewById(R.id.imageButtonDelete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   save data
                deleteNote();
                //creating fragments object
                FragOne fragmentTwo = new FragOne();
                //show edit note wala fragment
                CreateFragment(fragmentTwo, R.id.container_for_fragment);

            }
        });


        cancelButton = (ImageButton) v.findViewById(R.id.imageButtonCancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   no change to data
                //creating fragments object
                FragOne fragmentTwo = new FragOne();
                //show edit note wala fragment
                CreateFragment(fragmentTwo, R.id.container_for_fragment);

            }
        });


        return v;
    }
//save a new note
    public void saveNewNote(){
        boolean flag =false;
        NoteDBHandler db = new NoteDBHandler(getActivity());
        NotesModel note = new NotesModel();
        note.setUserId(shareP.getString("Email", "naahi"));
        note.setNoteTitle(title.getText().toString());
        note.setNoteDescription(decsripion.getText().toString());

//        Toast.makeText(getActivity(),note.getNoteDescription()+" "+ note.getNoteTitle(), Toast.LENGTH_LONG).show();


        if ((title.getText().toString().matches("")) && (decsripion.getText().toString().matches("")))
        {
            Toast.makeText(getActivity()," Next Time, do write something ", Toast.LENGTH_SHORT).show();
        }
            else {
            //update note
            flag = db.addNote(note);
            Toast.makeText(getActivity(), flag + " Saved Successfully ", Toast.LENGTH_SHORT).show();
        }
    }
//to update the note
    public void saveNote() {


        NoteDBHandler db = new NoteDBHandler(getActivity());
        noteList = db.findNotesByNoteID(position);
        NotesModel note = noteList.get(0);
        note.setNoteTitle(title.getText().toString());
        note.setNoteDescription(decsripion.getText().toString());
        Log.e(note.getNoteDescription(), note.getNoteTitle());
        //check for blank note
        if ((note.getNoteTitle().matches("")) && (note.getNoteDescription().matches(""))) {
            Toast.makeText(getActivity()," A note left blank is a note not needed ", Toast.LENGTH_LONG).show();
            db.deleteNote(note.getNoteId());
        }else{
            //update note
            int flag = db.updateNote(note);
            Toast.makeText(getActivity(), flag + " Note/s added ", Toast.LENGTH_SHORT).show();
        }
    }
//to delete the current note
    public void deleteNote(){
        NoteDBHandler db = new NoteDBHandler(getActivity());
       int flag = db.deleteNote(position);
        Toast.makeText(getActivity(), flag + " Note/s deleted ", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onClick(View v) {

    }
    public void CreateFragment(Fragment frag, int containerForFragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerForFragment, frag);
        fragmentTransaction.commit();
    }

}
