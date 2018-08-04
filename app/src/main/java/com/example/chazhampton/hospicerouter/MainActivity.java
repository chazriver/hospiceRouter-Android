package com.example.chazhampton.hospicerouter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.oob.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.chazhampton.hospicerouter.Patient;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;


public class MainActivity extends AppCompatActivity implements PermissionsListener {

    private Button signOut;
    //private String myUserEmailVal = getIntent().getStringExtra("userEmailKey");


    // Firebase variables
    private DatabaseReference mDatabase;
    private DatabaseReference patientRef;
    private DatabaseReference patientnames;
    private DatabaseReference patientcomments;
    private DatabaseReference patientaddress;
    private FirebaseUser mUser;
    private PermissionsManager permissionsManager;


    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    //listView layout
    private ListView listView;
    //arraylist
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String myUserEmailVal = getIntent().getStringExtra("userEmailKey");
        String org_name = getIntent().getStringExtra("org_name");

        //get firebase database instance
        mDatabase = FirebaseDatabase.getInstance().getReference().child("companies").child(org_name).child("patients");
        /*
          patientnames = mDatabase.child("patientName");
          patientnames = mDatabase.child("patientAddress");
          patientnames = mDatabase.child("patientComments");
        */
        listView = (ListView) findViewById(R.id.database_list_view_main);

         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                Intent appInfo = new Intent(MainActivity.this, NavigationActivity.class);
                Object patient2 = ((ArrayAdapter) listView.getAdapter()).getItem(position);//
                String patient_val = patient2.toString();
                System.out.println(patient2);

                appInfo.putExtra("patientvalue", patient_val);
                appInfo.putExtra("userEmailKey", myUserEmailVal);
                appInfo.putExtra("org_name", org_name);
                startActivity(appInfo);
            }
        });


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Grab info from firebase and output on log w/ TAG
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.v("MainActivityTAG",""+ childDataSnapshot.getKey()); //displays the key for the node
                    Log.v("MainActivityTAG",""+ childDataSnapshot.child("name").getValue());   //gives the value for given keyname
                }

                //Grab info from firebase
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting the data from snapshot
                    Patient patient = postSnapshot.getValue(Patient.class);

                    //Adding it to a string
                    String expenses = "\nName: "+patient.getPatientName()+"\nPatient address: "+patient.getPatientAddress()+"\nPatient Comments:"+patient.getPatientComments()+"\n\n";

                    String name = patient.getPatientName();
                    String address = patient.getPatientAddress();
                    String comments = patient.getPatientComments();

                    //System.out.println(expenses);
                    arrayList.add(expenses);
                    if(arrayList.size() == 1)
                    {
                        adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
                        listView.setAdapter(adapter);
                    }
                    else if(arrayList.size() > 1)
                    {
                       adapter.notifyDataSetChanged();
                    }

                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
//        setSupportActionBar(toolbar);

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        mUser = FirebaseAuth.getInstance().getCurrentUser();

       // System.out.println(mUser.getEmail().toString());
        //System.out.println("@@@@");

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        signOut = (Button) findViewById(R.id.sign_out);




        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });



    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Create an instance of LOST location engine
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationPlugin();
        } else {
            finish();
        }
    }
}

