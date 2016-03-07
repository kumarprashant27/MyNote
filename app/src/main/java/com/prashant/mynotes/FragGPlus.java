package com.prashant.mynotes;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shishir on 3/7/16.
 */
public class FragGPlus extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_two, container, false);




        return v;
    }
}
