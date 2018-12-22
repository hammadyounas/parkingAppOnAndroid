package com.anashamidkh.parkingsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthenticationActivity extends AppCompatActivity {
    private EditText mEmail_Authen;
    private EditText mPassword_Authen;
    private Button mLoginBtn;
    private Button mSignupBtn;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    private String mLoginType;
    private String mEmail;
    private String mPassword;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEmail_Authen= (EditText) findViewById(R.id.email_Authen);
        mPassword_Authen = (EditText) findViewById(R.id.password_Authen);
        mLoginBtn = (Button) findViewById(R.id.loginBtn_Authen);
        mSignupBtn = (Button) findViewById(R.id.signupBtn_Authen);

        mProgressDialog = new ProgressDialog(this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    //Do Something
                }

            }
        };
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEmail_Authen.getText().toString();
                mPassword = mPassword_Authen.getText().toString();

                if(TextUtils.isEmpty(mEmail) && TextUtils.isEmpty(mPassword)){
                    mEmail_Authen.setError("Email field is empty");
                    mPassword_Authen.setError("Password field is empty");
                    return;
                }
                else if(TextUtils.isEmpty(mEmail)){
                    mEmail_Authen.setError("Email field is empty");
                    return;
                }
                else if(TextUtils.isEmpty(mPassword)){
                    mPassword_Authen.setError("Password field is empty");
                    return;
                }

                mProgressDialog.setMessage("Signing In");
                mProgressDialog.show();

                mFirebaseAuth.signInWithEmailAndPassword(mEmail_Authen.getText().toString(),mPassword_Authen.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            final String currentUserUid = mFirebaseAuth.getInstance().getCurrentUser().getUid();
                            mDatabaseRef.child(currentUserUid).addValueEventListener(new ValueEventListener(){
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mLoginType = dataSnapshot.child("AccountType").getValue(String.class);
                                    mProgressDialog.dismiss();

                                    Intent i = LoginActivity.newIntent(AuthenticationActivity.this,mLoginType);
                                    startActivity(i);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else{
                            mProgressDialog.dismiss();
                            Toast.makeText(AuthenticationActivity.this, "Login failed. Incorrect Email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AuthenticationActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });

    }
    @Override
    protected void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }
}
