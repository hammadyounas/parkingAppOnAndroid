package com.anashamidkh.parkingsystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedbackDescription extends AppCompatActivity {
    private static final String EXTRA_FEEDBACK_KEY = "com.anashamidkh.parkingsystem.feedback_key";

    private EditText mName_FeedbackDesc;
    private EditText mFeedback_FeedbackDesc;
    private EditText mDate_FeedbackDesc;
    private EditText mTime_FeedbackDesc;

    private String mFeedbackKey;
    private String mFeedback_UserId;

    private DatabaseReference mDatabaseRef;

    public static Intent newIntent(Context packageContext, String feedbackKey){
        Intent i = new Intent(packageContext,FeedbackDescription.class);
        i.putExtra(EXTRA_FEEDBACK_KEY,feedbackKey);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_description);

        mFeedbackKey = getIntent().getStringExtra(EXTRA_FEEDBACK_KEY);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Feedbacks").child(mFeedbackKey);

        mName_FeedbackDesc = (EditText) findViewById(R.id.name_FeedbackDesc);
        mFeedback_FeedbackDesc = (EditText) findViewById(R.id.feedback_FeedbackDesc);
        mDate_FeedbackDesc = (EditText) findViewById(R.id.date_FeedbackDesc);
        mTime_FeedbackDesc = (EditText) findViewById(R.id.time_FeedbackDesc);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mFeedback_UserId = dataSnapshot.child("User Id").getValue().toString();
                DatabaseReference mDatabaseRef2 = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(mFeedback_UserId);
                mDatabaseRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mName_FeedbackDesc.setText(dataSnapshot.child("Name").getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mFeedback_FeedbackDesc.setText(dataSnapshot.child("Feedback").getValue().toString());
                mDate_FeedbackDesc.setText(dataSnapshot.child("Date").getValue().toString());
                mTime_FeedbackDesc.setText(dataSnapshot.child("Time").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}