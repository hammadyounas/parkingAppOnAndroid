package com.anashamidkh.parkingsystem;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private EditText mName_Signup;
    private EditText mEmail_Signup;
    private EditText mPassword_Signup;
    private EditText mPhone_Signup;
    private Button mSignupBtn_Signup;

    private String mName;
    private String mEmail;
    private String mPassword;
    private String mPhone;

    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mName_Signup = (EditText) findViewById(R.id.name_Signup);
        mEmail_Signup = (EditText) findViewById(R.id.email_Signup);
        mPassword_Signup = (EditText) findViewById(R.id.password_Signup);
        mPhone_Signup = (EditText) findViewById(R.id.phone_Signup);
        mSignupBtn_Signup = (Button) findViewById(R.id.signupBtn_Signup);

        mProgressDialog = new ProgressDialog(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mSignupBtn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emptyFields())
                    return;
                mProgressDialog.setMessage("Registering User");
                mProgressDialog.show();
                mFirebaseAuth.createUserWithEmailAndPassword(mEmail,mPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            postDataToFirebaseDatabase();
                        }
                        else{
                            mProgressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Not Registered. Authentication Conflicts", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private boolean emptyFields(){
        mName = mName_Signup.getText().toString();
        mEmail = mEmail_Signup.getText().toString();
        mPassword = mPassword_Signup.getText().toString();
        mPhone = mPhone_Signup.getText().toString();

        if(TextUtils.isEmpty(mName) && TextUtils.isEmpty(mEmail) && TextUtils.isEmpty(mPassword) && TextUtils.isEmpty(mPhone)){
            mName_Signup.setError("Name field is empty");
            mEmail_Signup.setError("Email Address field is empty");
            mPassword_Signup.setError("Password field is empty");
            mPhone_Signup.setError("Phone field is empty");
            return true;
        }
        else if(TextUtils.isEmpty(mName)){
            mName_Signup.setError("Name field is empty");
            return true;
        }
        else if(TextUtils.isEmpty(mEmail)){
            mEmail_Signup.setError("Email Address field is empty");
            return true;
        }
        else if(TextUtils.isEmpty(mPassword)){
            mPassword_Signup.setError("Password field is empty");
            return true;
        }
        else if(TextUtils.isEmpty(mPhone)){
            mPhone_Signup.setError("Phone field is empty");
            return true;
        }
        return false;
    }

    private void postDataToFirebaseDatabase(){
        HashMap<String,String> dataMap = new HashMap<String, String>();
        dataMap.put("AccountType","User");
        dataMap.put("Email",mEmail);
        dataMap.put("Name",mName);
        dataMap.put("Phone Number",mPhone);

        mDatabaseRef.child(mFirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mProgressDialog.dismiss();
                    Toast.makeText(SignupActivity.this,"User Registered Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    mProgressDialog.dismiss();
                    Toast.makeText(SignupActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}