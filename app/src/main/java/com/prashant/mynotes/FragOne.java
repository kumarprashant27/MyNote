package com.prashant.mynotes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shishir on 3/7/16.
 */
public class FragOne extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerView;
    public List<NotesModel> noteList = new ArrayList<>();
    public NotesAdapter mAdapter;
    public String positionInString;
    ImageButton addNetNoteButton;


    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one, container, false);

        preparedata();
        //Toast.makeText(getContext(), "hithere", Toast.LENGTH_LONG).show();
        recyclerView = (RecyclerView)v.findViewById(R.id.my_recycler_view);
        mAdapter = new NotesAdapter(noteList);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        //add default decoreation with divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        //add prashant's image as divider only for 5.0 and above
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getDrawable(R.drawable.divider),true, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                NotesModel note = noteList.get(position);
                Toast.makeText(getActivity(), note.getNoteId() + " is selected!", Toast.LENGTH_SHORT).show();
//                EditNote();

            }

            @Override
            public void onLongClick(View view, int position) {
                NotesModel note = noteList.get(position);
                positionInString = Integer.toString(note.getNoteId());

                FragTwo fragmentTwo = new FragTwo();
                CreateFragment(fragmentTwo, R.id.container_for_fragment);


            }
        }));

        addNetNoteButton = (ImageButton) v.findViewById(R.id.imageButtonAddNewNote);

        addNetNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   New Note
                //creating fragments object
                FragTwo fragmentTwo = new FragTwo();
                //show edit note wala fragment
                CreateEmptyFragment(fragmentTwo, R.id.container_for_fragment);

            }
        });
        return v;

    }


    @Override
    public void onClick(View v) {

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FragOne.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FragOne.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void preparedata()
    {

        NoteDBHandler db = new NoteDBHandler(getActivity());
        if(db.checkForNull()== 0){
            NotesModel notedata = new NotesModel();
            notedata.setUserId("demonstration@demo.com");
            notedata.setNoteTitle("Demo Note");
            notedata.setNoteDescription("Here you can add your note content");
            db.addNote(notedata);

            Toast.makeText(getActivity(), "Welcome to the MyNotes",Toast.LENGTH_LONG).show();
        }

        //dummy data
//        String title;
//        for (int i=1 ; i<3 ; i++) {
//            NotesModel notedata = new NotesModel();
//            notedata.setUserId("kpt@g.com");
//            title = "title no "+ i;
//            notedata.setNoteTitle(title);
//            notedata.setNoteDescription("note content");
//            db.addNote(notedata);
//            //noteList.add(notedata);
//
//        }


        noteList = db.findNotesByUser("demonstration@demo.com");
        //noteList = db.findNotesByUser(shareP.getString("Email","naahi"));
    }

    public void EditNote(){

        FragTwo fragmentTwo = new FragTwo();
        MainActivity mn = new MainActivity();
        mn.CreateFragment(fragmentTwo, R.id.container_for_fragment);

    }

    public void CreateFragment(Fragment frag, int containerForFragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("NoteID", positionInString);
        frag.setArguments(args);
        fragmentTransaction.replace(containerForFragment, frag);
        fragmentTransaction.commit();
    }

    public void CreateEmptyFragment(Fragment frag, int containerForFragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Bundle args = new Bundle();
//        args.putString("NoteID", positionInString);
//        frag.setArguments(args);
        fragmentTransaction.replace(containerForFragment, frag);
        fragmentTransaction.commit();
    }
}

