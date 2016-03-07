package com.prashant.mynotes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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
 * Created by shishir on 3/7/16.
 */
public class FragTwo extends Fragment implements View.OnClickListener {

    public List<NotesModel> noteList = new ArrayList<>();

    private EditText title;
    private EditText decsripion;
    private String strtext;
    private int position;
    ImageButton saveButton;
    ImageButton deleteButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_two, container, false);
        strtext = getArguments().getString("NoteID");
        position = Integer.parseInt(strtext);


        NoteDBHandler db = new NoteDBHandler(getActivity());

        noteList = db.findNotesByNoteID(position);
        NotesModel note = noteList.get(0);

        title = (EditText) v.findViewById(R.id.note_title_content);
        title.setText(note.getNoteTitle());

        decsripion = (EditText) v.findViewById(R.id.note_description_content);
        decsripion.setText(note.getNoteDescription());
        saveButton = (ImageButton) v.findViewById(R.id.imageButtonSave);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   save data
                saveNote();
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




        return v;
    }
//to update the note
    public void saveNote(){

        NoteDBHandler db = new NoteDBHandler(getActivity());
        noteList = db.findNotesByNoteID(position);
        NotesModel note = noteList.get(0);
        note.setNoteTitle(title.getText().toString());
        note.setNoteDescription(decsripion.getText().toString());
        //update note
        int flag = db.updateNote(note);
        Toast.makeText(getActivity(), flag + " number of rows ", Toast.LENGTH_SHORT).show();

    }
//to delete the current note
    public void deleteNote(){
        NoteDBHandler db = new NoteDBHandler(getActivity());
       int flag = db.deleteNote(position);
        Toast.makeText(getActivity(), flag + " number of rows ", Toast.LENGTH_SHORT).show();
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
