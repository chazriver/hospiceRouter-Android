package com.example.chazhampton.hospicerouter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputCompanyName;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String Userauth;
    private int miles = 0; // used to set initial miles to zero


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputName = (EditText) findViewById(R.id.name_signup);
        inputEmail = (EditText) findViewById(R.id.email_signup);
        inputPassword = (EditText) findViewById(R.id.password_signup);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        inputCompanyName = (EditText) findViewById(R.id.company_name_signup);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String userType = "User";//Default typs is User, must be changed to Admin on the database itself for security
                                //add the new employee to the data base to track their miles later during navigation
                               // DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users");
                               DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("companies").child(inputCompanyName.getText().toString()).child("users");//add user to the new organization ID

                                String patientID = mRef.push().getKey();
                                User user = new User(inputName.getText().toString(),inputEmail.getText().toString(),inputPassword.getText().toString(),userType,miles);
                                mRef.child(patientID).setValue(user);

                              /*  DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("users");
                                mRef.child(inputName.getText().toString()).push().setValue(inputName.getText().toString());
                                mRef.child(inputName.getText().toString()).child("email").push().setValue(inputEmail.getText().toString());
                                mRef.child(inputName.getText().toString()).child("password").push().setValue(inputPassword.getText().toString());
                                mRef.child(inputName.getText().toString()).child("miles").push().setValue(0); */
                                Toast.makeText(SignupActivity.this, "created User with Email:" + (inputEmail.getText().toString()) + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent appInfo = new Intent(SignupActivity.this, MainActivity.class);
                                    appInfo.putExtra("org_name",inputCompanyName.getText().toString());
                                    startActivity(appInfo);
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}