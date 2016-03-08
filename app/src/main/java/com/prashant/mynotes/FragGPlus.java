package com.prashant.mynotes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by prashant on 3/7/16.
 */
public class FragGPlus extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    public SharedPreferences shareP;
    Editor editor;
    private SignInButton signInButton;
    private Button signOutButton;
    //Signing Options
    private GoogleSignInOptions gso;
    //google api client
    private GoogleApiClient mGoogleApiClient;
    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;
    //TextViews
    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewNameIs;
    private TextView textViewEmailIs;
    private NetworkImageView profilePhoto;

    public ImageButton notes;
    public ImageView android;

    //Image Loader
    private ImageLoader imageLoader;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_google_plus, container, false);
        //get from sharedpref
        shareP = this.getActivity().getSharedPreferences("Mypref", Context.MODE_PRIVATE);

        //shareP = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = shareP.edit();
        notes = (ImageButton) v.findViewById(R.id.myNotes);
        textViewName = (TextView) v.findViewById(R.id.textViewName);
        textViewEmail = (TextView) v.findViewById(R.id.textViewEmail);
        profilePhoto = (NetworkImageView) v.findViewById(R.id.profileImage);
        android = (ImageView) v.findViewById(R.id.back);
//Initializing google signin option
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        //Initializing signinbutton
        signInButton = (SignInButton) v.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());

        //Initializing google api client
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Setting onclick listener to signing button
        signInButton.setOnClickListener(this);


        //signout and return to self
        signOutButton = (Button) v.findViewById(R.id.sign_out_button);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating fragments object

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // [START_EXCLUDE]
                                // [END_EXCLUDE]
                            }
                        });
                FragGPlus fragmentGoogle = new FragGPlus();

                //show notes wala fragment
                CreateFragmentGPlus(fragmentGoogle, R.id.container_for_fragment);

            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
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

    public void CreateFragmentGPlus(Fragment frag, int containerForFragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerForFragment, frag);
        fragmentTransaction.commit();
    }

    public void CreateFragment(Fragment frag, int containerForFragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerForFragment, frag);
        fragmentTransaction.commit();
    }
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
    }


    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
            textViewNameIs = (TextView)v.findViewById(R.id.textViewNameIs);
            textViewEmailIs = (TextView)v.findViewById(R.id.textViewEmailIs);
            //Displaying name and email
            textViewEmailIs.setVisibility(View.VISIBLE);
            textViewNameIs.setVisibility(View.VISIBLE);
            textViewName.setText(acct.getDisplayName());
            textViewEmail.setText(acct.getEmail());

            try {
                //Initializing image loader
                imageLoader = CustomVolleyRequest.getInstance(getActivity())
                        .getImageLoader();

                imageLoader.get(acct.getPhotoUrl().toString(),
                        ImageLoader.getImageListener(profilePhoto,
                                R.mipmap.ic_launcher,
                                R.mipmap.ic_launcher));

                //Loading image
                profilePhoto.setImageUrl(acct.getPhotoUrl().toString(), imageLoader);
            }
            catch (Exception e){
                profilePhoto.setImageUrl("https://heatherchristenaschmidt.files.wordpress.com/2011/09/facebook_no_profile_pic2-jpg.gif",imageLoader);
            }
            //hide the sign in button
            com.google.android.gms.common.SignInButton sign_in = (SignInButton) v.findViewById(R.id.sign_in_button);
            ViewGroup layout = (ViewGroup) sign_in.getParent();
            layout.removeView(sign_in);

            shareP.edit().putString("Email", acct.getEmail().toString()).commit();
            shareP.edit().putString("Name", acct.getDisplayName()).commit();

            String gum= "Hello  "+ shareP.getString("Name", acct.getDisplayName());


            Toast.makeText(getActivity(), gum, Toast.LENGTH_LONG).show();

//            editor.putString("Email", acct.getEmail().toString());
//            editor.putString("Name", acct.getDisplayName());

            notes.setVisibility(View.VISIBLE);
            signOutButton = (Button) v.findViewById(R.id.sign_out_button);
            signOutButton.setVisibility(View.VISIBLE);
            android.setVisibility(View.GONE);

        } else {
            //If login fails
            Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            //Calling signin
            signIn();
        }
    }





    public void manualSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
