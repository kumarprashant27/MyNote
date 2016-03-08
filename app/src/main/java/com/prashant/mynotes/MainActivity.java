package com.prashant.mynotes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
/**
 * Created by prashant on 3/7/16.
 */

public class MainActivity extends AppCompatActivity {
    SharedPreferences shareP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences prefs = this.getSharedPreferences("MyPref", 0);
        //initiate share preferences
        SharedPreferences prefs = this.getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.commit();


       // if (shareP.getString("Email","kuch bhi") == null) {
//
//            Toast.makeText(getApplicationContext(),shareP.getString("Email","naahi"),Toast.LENGTH_LONG).show();
//            //creating fragments object
//            FragOne fragmentOne = new FragOne();
//            //shareP.edit().putBoolean("firstRun",false).commit();
//            //show notes wala fragment
//            CreateFragment(fragmentOne, R.id.container_for_fragment);


//        }
//        else {
//
            Toast.makeText(getApplicationContext(), "Welcome to MyNotes", Toast.LENGTH_LONG).show();
            //creating fragments object
            FragGPlus fragmentGoogle = new FragGPlus();
            //show notes wala fragment
            CreateFragmentGPlus(fragmentGoogle, R.id.container_for_fragment);

//        }

    }


// function to initiate Google sign in fragment and put it into the container

    public void CreateFragmentGPlus(Fragment frag, int containerForFragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerForFragment, frag);
        fragmentTransaction.commit();
    }
//function to create normal fragment
    public void CreateFragment(Fragment frag, int containerForFragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerForFragment, frag);
        fragmentTransaction.commit();
    }
}
