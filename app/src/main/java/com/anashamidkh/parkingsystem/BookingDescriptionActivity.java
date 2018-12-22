package com.anashamidkh.parkingsystem;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingDescriptionActivity extends AppCompatActivity {
    private static final String EXTRA_ITEM_KEY = "com.anashamidkh.parkingsystem.item_key";
    private static final String EXTRA_ACTIVITY_NAME_KEY = "com.anashamidkh.parkingsystem.activity_name_key";
    private EditText mBookingId_BookingDesc;
    private EditText mName_BookingDesc;
    private EditText mArea_BookingDesc;
    private EditText mSlot_BookingDesc;
    private EditText mDate_BookingDesc;
    private EditText mTime_BookingDesc;

    private Button mRemove_BookingDesc;
    private String mItem;
    private String mActivityName;

    private String mBookingId;
    private String mUserName;
    private String mAreaName;
    private String mSlotNumber;
    private String mDate;
    private String mTime;
    private String mUserId;

    private boolean check;

    private DatabaseReference mDatabaseRef;

    public static Intent newIntent(Context packageContext,String item,String activityName){
        Intent i = new Intent(packageContext,BookingDescriptionActivity.class);
        i.putExtra(EXTRA_ITEM_KEY,item);
        i.putExtra(EXTRA_ACTIVITY_NAME_KEY,activityName);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_description);

        mItem = getIntent().getStringExtra(EXTRA_ITEM_KEY);
        mActivityName = getIntent().getStringExtra(EXTRA_ACTIVITY_NAME_KEY);

        mBookingId_BookingDesc = (EditText) findViewById(R.id.bookingId_BookingDesc);
        mName_BookingDesc = (EditText) findViewById(R.id.name_BookingDesc);
        mArea_BookingDesc = (EditText) findViewById(R.id.area_BookingDesc);
        mSlot_BookingDesc = (EditText) findViewById(R.id.slot_BookingDesc);
        mDate_BookingDesc = (EditText) findViewById(R.id.date_BookingDesc);
        mTime_BookingDesc = (EditText) findViewById(R.id.time_BookingDesc);
        mRemove_BookingDesc = (Button) findViewById(R.id.remove_BookingDesc);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        if(mActivityName.equals("ViewBookingsActivity")){
            mRemove_BookingDesc.setVisibility(View.VISIBLE);
            mRemove_BookingDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabaseRef = mDatabaseRef.child("Parking Areas").child(mAreaName).child(mSlotNumber).child(mBookingId);
                    check = false;
                    mDatabaseRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(BookingDescriptionActivity.this,"Booking Successfully removed by Admin",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            });
        }

        getBookingInfo();
        check = true;
        mDatabaseRef.child("Parking Areas").child(mAreaName).child(mSlotNumber).child(mBookingId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(check == true){

                            mUserId = dataSnapshot.child("User Id").getValue().toString();
                            mDate = dataSnapshot.child("Date").getValue().toString();
                            mTime = dataSnapshot.child("Time").getValue().toString();

                            DatabaseReference mDatabaseRef2 = FirebaseDatabase.getInstance().getReference();
                            mDatabaseRef2.child("Users").child(mUserId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mUserName = dataSnapshot.child("Name").getValue().toString();
                                    mName_BookingDesc.setText(mUserName);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            mBookingId_BookingDesc.setText(mBookingId);
                            mArea_BookingDesc.setText(mAreaName);
                            mSlot_BookingDesc.setText(mSlotNumber);
                            mDate_BookingDesc.setText(mDate);
                            mTime_BookingDesc.setText(mTime);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
    private void getBookingInfo(){                  //Function Definition to split "mItem" and get relevant information from "mItem"
        String x[] = mItem.split("\n");             //Splitting item at "\n"
        String y[] = x[0].split(": ");              //Accessing bookingId and splitting at ": "
        mBookingId = y[1];                          //Storing bookingId

        String a = mBookingId.substring(mBookingId.length()-1);
        if(a.equals("G"))
            mAreaName = "Gulshan-e-Iqbal";
        else if(a.equals("J"))
            mAreaName = "Gulistan-e-Jauhar";
        else if(a.equals("S"))
            mAreaName = "Saddar";

        String s = mBookingId.substring(mBookingId.length()-2,mBookingId.length()-1);
        mSlotNumber = "Slot " +s;
    }
}
