package com.example.first.homework09;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "demo";

    //For login
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //For Login


    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView signupLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        signupLink = (TextView) findViewById(R.id.link_signup);

        loginButton.setOnClickListener(this);
        signupLink.setOnClickListener(this);

        mAuth = ((FirebaseApplication)getApplication()).getFirebaseAuth();


        ((FirebaseApplication)getApplication()).checkUserLogin(LoginActivity.this);

        Log.d("IN OnCreate....","");


        //For Login
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();

        Log.d("IN OnStrat....","");

      //  mAuth.addAuthStateListener(((FirebaseApplication)getApplication()).mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (((FirebaseApplication)getApplication()).mAuthListener != null) {
            mAuth.removeAuthStateListener(((FirebaseApplication)getApplication()).mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.link_signup:
                 signup();
                break;
        }
    }

    private void signup() {
        Log.d(TAG, "In Signup");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        ((FirebaseApplication)getApplication()).createNewUser(LoginActivity.this, email, password);


    }

    private void login() {
        Log.d(TAG, "In Login");

        if (!validate()) {
            onLoginFailed();
            Toast.makeText(this,"Login Failed! !",Toast.LENGTH_SHORT).show();
            return;
        }


        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this
                );
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        Log.d("Email is:",""+email+"    pwd "+password);


        ((FirebaseApplication)getApplication()).loginAUser(LoginActivity.this, email, password, progressDialog);

     /*   if(!check)
            onLoginFailed();*/
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "failed", Toast.LENGTH_LONG).show();
        loginButton.setEnabled(true);
    }

    private boolean validate() {

        boolean valid = true;
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;

    }


}
