package com.example.chazhampton.hospicerouter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class SignupActivityCompany extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputCompanyName;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String Userauth;
    private int miles = 0; // used to set initial miles to zero


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_company);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button_company);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputName = (EditText) findViewById(R.id.name_company_signup);
        inputEmail = (EditText) findViewById(R.id.email_company_signup);
        inputCompanyName = (EditText) findViewById(R.id.company_name);
        inputPassword = (EditText) findViewById(R.id.password_company_signup);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivityCompany.this, ResetPasswordActivity.class));
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
                        .addOnCompleteListener(SignupActivityCompany.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String userType = "Admin";//Default type is Admin when creating a new company, must be changed to User on the database itself for security
                                //add the new employee to the data base to track their miles later during navigation

                                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                                mRef = mRef.child("companies").push();
                                mRef = mRef.child("organization").push();

                                mRef = FirebaseDatabase.getInstance().getReference("companies").child(inputCompanyName.getText().toString()).child("users");//add user to the new organization ID
                                String patientID = mRef.push().getKey();
                                User user = new User(inputName.getText().toString(),inputEmail.getText().toString(),inputPassword.getText().toString(),userType,miles);
                                mRef.child(patientID).setValue(user);
                                Toast.makeText(SignupActivityCompany.this, "created User with Email:" + (inputEmail.getText().toString()) + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);

                                //Since this is creating a new user on a new organization, we do not need ot check if user isalready created since we just created the user i.e. (the admin user)
                                    Intent appInfo = new Intent(SignupActivityCompany.this, AdminActivity.class);//Testing Admin activity page..
                                    appInfo.putExtra("org_name",inputCompanyName.getText().toString());
                                    startActivity(appInfo);
                                    finish();

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