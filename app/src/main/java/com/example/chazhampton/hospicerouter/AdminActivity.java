package com.example.chazhampton.hospicerouter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import android.app.AlertDialog.Builder;




public class AdminActivity extends AppCompatActivity {


    private Button btnChangeEmail, btnChangePassword, btnSendResetEmail, btnRemoveUser,
                changeEmail, changePassword, sendEmail, remove, signOut, sendBtn;
        private EditText new_name_text, new_address_text, new_comments_text;
        // Firebase variables
        private DatabaseReference mDatabase;
        private FirebaseDatabase database;
        private FirebaseUser mUser;



        private EditText oldEmail, newEmail, password, newPassword;
        private ProgressBar progressBar;
        private FirebaseAuth.AuthStateListener authListener;

        private FirebaseAuth auth;
        //listView layout
        private ListView listView;
        //arraylist
        private ArrayList<String> arrayList = new ArrayList<>();
        private ArrayList<String> keyList = new ArrayList<>();
        private ArrayAdapter<String> adapter;
        private String Admin_Segue = "true";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin);

            signOut = (Button) findViewById(R.id.sign_out);
            sendBtn = (Button) findViewById(R.id.sendbtn);
            new_name_text = (EditText) findViewById(R.id.new_patient_name);
            new_address_text = (EditText) findViewById(R.id.new_patient_address);
            new_comments_text = (EditText) findViewById(R.id.new_patient_comments);

            //get firebase database instance
            mDatabase = FirebaseDatabase.getInstance().getReference().child("patients");
         //   patientnames = mDatabase.child("patientName");
         //   patientnames = mDatabase.child("patientAddress");
         //   patientnames = mDatabase.child("patientComments");

            listView = (ListView) findViewById(R.id.database_list_view);

          /*  sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });*/

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    AlertDialog.Builder alertDialog = new  AlertDialog.Builder(AdminActivity.this);
                    alertDialog.setTitle("Delete");
                    alertDialog.setMessage("This will remove the patients account!");
                    alertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            arrayList.remove(i);
                            adapter.notifyDataSetChanged();

                            mDatabase.child(keyList.get(i)).removeValue();

                        } });
                    alertDialog.setPositiveButton("Keep", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // alertDialog.dismiss();
                        } });

                    alertDialog.show();
                    return true;
                }
            });


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //Grab info from firebase
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        //Getting the data from snapshot
                        Patient patient = postSnapshot.getValue(Patient.class);

                        //Adding it to a string
                        String expenses = "\nName: "+patient.getPatientName()+"\nPatient address: "+patient.getPatientAddress()+"\nPatient Comments:"+patient.getPatientComments()+"\n\n";

                        keyList.add(postSnapshot.getKey());
                        if (arrayList.contains(expenses)){
                            //already i list. Do not add the new thing
                        }else{
                            arrayList.add(expenses);
                        }
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

                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //add the new employee to the data base to track their miles later during navigation
                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("patients");
                        String patientID = mRef.push().getKey();
                        Patient patient = new Patient(new_name_text.getText().toString(),new_address_text.getText().toString(),new_comments_text.getText().toString());
                        mRef.child(patientID).setValue(patient);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(AdminActivity.this, "Created Patient with Name:" + (new_name_text.getText().toString()), Toast.LENGTH_SHORT).show();
                    }
                });


                signOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){signOut();}
                });


        }
            //sign out method
            public void signOut(){
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                intent.putExtra("Admin_Segue", Admin_Segue);
                startActivity(intent);
                finish();
            }



    }

