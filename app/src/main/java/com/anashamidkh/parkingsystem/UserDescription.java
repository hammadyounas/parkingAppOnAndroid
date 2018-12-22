package com.anashamidkh.parkingsystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDescription extends AppCompatActivity {
    private static final String EXTRA_USER_ID = "com.anashamidkh.parkingsystem.database_key";

    private TextView mName_UserDesc;
    private TextView mEmail_UserDesc;
    private TextView mPhone_UserDesc;

    private String mUserId;

    private DatabaseReference mDatabaseRef;

    public static Intent newIntent(Context packageContext, String userId){
        Intent i = new Intent(packageContext,UserDescription.class);
        i.putExtra(EXTRA_USER_ID,userId);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_description);

        mUserId = getIntent().getStringExtra(EXTRA_USER_ID);

        mName_UserDesc = (TextView) findViewById(R.id.name_UserDesc);
        mEmail_UserDesc = (TextView) findViewById(R.id.email_UserDesc);
        mPhone_UserDesc = (TextView) findViewById(R.id.phone_UserDesc);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mName_UserDesc.setText(dataSnapshot.child("Name").getValue().toString());
                mEmail_UserDesc.setText(dataSnapshot.child("Email").getValue().toString());
                mPhone_UserDesc.setText(dataSnapshot.child("Phone Number").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}