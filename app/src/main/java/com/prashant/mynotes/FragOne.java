package com.prashant.mynotes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by prashant on 3/7/16.
 */
public class FragOne extends Fragment implements View.OnClickListener{
    SharedPreferences shareP;


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

        shareP = this.getActivity().getSharedPreferences("Mypref", Context.MODE_PRIVATE);

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
                Toast.makeText(getActivity(), "Time Created " +note.getTimeOfCreate() + ". ", Toast.LENGTH_SHORT).show();
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

        Log.e(shareP.getString("Email", "naahi"),"");
        Log.e(shareP.getString("Email", "naahi"), " outside if");
        if(db.checkForNullbyUser(shareP.getString("Email","naahi")) == 0){
            Log.e(shareP.getString("Email", "naahi"), " inside if");

            NotesModel notedata = new NotesModel();
            notedata.setUserId(shareP.getString("Email", "naahi"));
            //demo note for frist time users
            notedata.setNoteTitle("Demo Note");
            notedata.setNoteDescription("Here you can add your note content. \nThe Notes can be edited or deleted by pressing the deisred note for a little bit longer duration. \nYou might already have figured out that the button below will let you make new notes.");
            boolean i = db.addNote(notedata);
            if (i){
                Log.e(shareP.getString("Email", "naahi"), " ho gaya entry bhai");
            }
            noteList.add(notedata);

            NotesModel notedata1 = new NotesModel();

            //a tip
            notedata1.setUserId(shareP.getString("Email","naahi"));
            notedata1.setNoteTitle("Tip:");
            notedata1.setNoteDescription("A note left blank is a note not needed, it will automatically be deleted. Go ahead and create a note!!");
            boolean j = db.addNote(notedata1);
            if (j){
                Log.e(shareP.getString("Email", "naahi"), "  ye bhi ho gaya bhaiyo ");
            }

            noteList.add(notedata1);

            Toast.makeText(getActivity(), "Welcome to the MyNotes",Toast.LENGTH_LONG).show();
        }

        else
        {
            Log.e(shareP.getString("Email", "naahi"), " inside else");

            noteList = db.findNotesByUser(shareP.getString("Email", "naahi"));
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

