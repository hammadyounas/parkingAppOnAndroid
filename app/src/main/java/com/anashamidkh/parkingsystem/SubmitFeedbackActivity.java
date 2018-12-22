package com.anashamidkh.parkingsystem;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SubmitFeedbackActivity extends AppCompatActivity {

    private EditText mFeedback_SubmitFeedback;
    private Button mSubmitBtn_SubmitFeedback;

    private String currentUserId;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_feedback);

        mSubmitBtn_SubmitFeedback = (Button) findViewById(R.id.submitBtn_SubmitFeedback);
        mFeedback_SubmitFeedback = (EditText) findViewById(R.id.feedback_SubmitFeedback);

        currentUserId = mFirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Feedbacks");

        mSubmitBtn_SubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = mFeedback_SubmitFeedback.getText().toString();
                if(TextUtils.isEmpty(feedback)){
                    mFeedback_SubmitFeedback.setError("Feedback field is empty");
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = sdf.format(new Date());

                sdf = new SimpleDateFormat("HH:mm");
                String currentTime = sdf.format(new Date());

                HashMap<String,String> dataMap = new HashMap<String,String>();
                dataMap.put("User Id",currentUserId);
                dataMap.put("Feedback",feedback);
                dataMap.put("Date",currentDate);
                dataMap.put("Time",currentTime);

                mDatabaseRef.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SubmitFeedbackActivity.this,"Feedback Submitted Successfully",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                            Toast.makeText(SubmitFeedbackActivity.this,"Feedback Not Submitted. Please Try again",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}