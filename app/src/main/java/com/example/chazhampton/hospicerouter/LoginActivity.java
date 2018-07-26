package com.example.chazhampton.hospicerouter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset, Adminbtn;
    private String Userauth;
    private DatabaseReference mRef;
    private String Key_admin = "", Key_email = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        String Admin_Segue = getIntent().getStringExtra("Admin_Segue");//check for admin

        if (auth.getCurrentUser() != null && !Admin_Segue.equals("true")) {//we do not wish to auto log admin accounts
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);//DONOT let this be main..... total_hours_wasted = 5;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        Adminbtn = (Button) findViewById(R.id.Adminbtn);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        Adminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);




                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);


                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                }
                                else{

                                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users");

                                    Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("userType").equalTo("Admin");
                                    Query query2 = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("userEmail").equalTo(inputEmail.getText().toString());

                                    query.addListenerForSingleValueEvent(new ValueEventListener() {//Find all instances of Admin acc.'s in database and grab key
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            // There may be multiple users with the email address, so we need to loop over the matches
                                            for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {//when found store this value

                                                Key_admin = userSnapshot.getKey();
                                                System.out.println(userSnapshot.getKey() + "<-- is getKey() Key_admin");

                                                query2.addListenerForSingleValueEvent(new ValueEventListener() {//we now look for the key of the entered email in the db
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        // There may be multiple users with the email address, so we need to loop over the matches
                                                        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {

                                                            Key_email = userSnapshot.getKey();
                                                            System.out.println(userSnapshot.getKey() + "<-- is getKey() Key_email");
                                                            System.out.println(Key_email + "<-- is getKey() Key_email FROM VARIABLE");

                                                            System.out.println(Key_admin + " " + Key_email + " *is key admin and jey email respectively");
                                                            if (Key_admin.equals(Key_email)){//if both keys match then the entered email belongs to an admin account
                                                                System.out.println(Key_admin + " " + Key_email + " *is key admin and jey email respectively");
                                                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);//Send to admin screen
                                                                startActivity(intent);
                                                                finish();
                                                            }else {//Key missmatch, send to MainActivity
                                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                intent.putExtra("userEmailKey", Key_email.toString());
                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Log.w("ERROR", "find email address:onCancelled", databaseError.toException());
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            Log.w("ERROR", "find email address:onCancelled", databaseError.toException());
                                        }
                                    });

                                    }
                            }
                        });
            }
        });
    }
}