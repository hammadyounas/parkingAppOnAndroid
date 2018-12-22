package com.anashamidkh.parkingsystem;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CancelBookingActivity extends AppCompatActivity {
    private EditText mBookigId_CancelBooking;
    private TextView mArea_CancelBooking;
    private TextView mSlot_CancelBooking;
    private TextView mDate_CancelBooking;
    private TextView mTime_CancelBooking;
    private Button mSearch_CancelBooking;
    private Button mCancelBtn_CancelBooking;

    private String mBookingId;
    private String mParkingAreaName;
    private String mSlotNumber;
    private String mCurrentUserId;
    private String mDate;
    private String mTime;
    boolean check;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_booking);

        mBookigId_CancelBooking = (EditText) findViewById(R.id.bookigId_CancelBooking);
        mArea_CancelBooking = (TextView) findViewById(R.id.area_CancelBooking);
        mSlot_CancelBooking = (TextView) findViewById(R.id.slot_CancelBooking);
        mDate_CancelBooking = (TextView) findViewById(R.id.date_CancelBooking);
        mTime_CancelBooking = (TextView) findViewById(R.id.time_CancelBooking);
        mSearch_CancelBooking = (Button) findViewById(R.id.search_CancelBooking);
        mCancelBtn_CancelBooking = (Button) findViewById(R.id.cancelBtn_CancelBooking);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = mFirebaseAuth.getInstance();
        mCurrentUserId = mFirebaseAuth.getCurrentUser().getUid();

        mSearch_CancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = true;
                mBookingId = mBookigId_CancelBooking.getText().toString().toUpperCase();
                String a = mBookingId.substring(mBookingId.length()-1);
                String s = mBookingId.substring(mBookingId.length()-2,mBookingId.length()-1);
                mSlotNumber = "Slot " +s;

                if(a.equalsIgnoreCase("G"))
                    mParkingAreaName = "Gulshan-e-Iqbal";
                else if(a.equalsIgnoreCase("J"))
                    mParkingAreaName = "Gulistan-e-Jauhar";
                else if(a.equalsIgnoreCase("S"))
                    mParkingAreaName = "Saddar";

                mDatabaseRef.child("Parking Areas").child(mParkingAreaName).child(mSlotNumber)
                        .child(mBookingId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(check == true) {
                            if (dataSnapshot.child("User Id").getValue().toString().equals(mCurrentUserId)) {
                                mArea_CancelBooking.setVisibility(View.VISIBLE);
                                mSlot_CancelBooking.setVisibility(View.VISIBLE);
                                mDate_CancelBooking.setVisibility(View.VISIBLE);
                                mTime_CancelBooking.setVisibility(View.VISIBLE);
                                mCancelBtn_CancelBooking.setVisibility(View.VISIBLE);

                                mDate = dataSnapshot.child("Date").getValue().toString();
                                mTime = dataSnapshot.child("Time").getValue().toString();
                                mArea_CancelBooking.setText(mParkingAreaName);
                                mSlot_CancelBooking.setText(mSlotNumber);
                                mDate_CancelBooking.setText(mDate);
                                mTime_CancelBooking.setText(mTime);

                                mCancelBtn_CancelBooking.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mDatabaseRef = mDatabaseRef.child("Parking Areas").child(mParkingAreaName).child(mSlotNumber).child(mBookingId);
                                        check = false;
                                        mDatabaseRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(CancelBookingActivity.this,"Booking Cancelled",Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                            else {
                                Toast.makeText(CancelBookingActivity.this, "This booking Id is not valid for your account", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
            }
        });
    }
}
