package com.anashamidkh.parkingsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SlotsActivity extends AppCompatActivity {
    private static final String EXTRA_SELECTED_OPTION = "com.anashamidkh.parkingsystem.selected_option";

    private Spinner mDurationSpinner_Slots;
    private Spinner mTimeSpinner_Slots;
    private EditText mDate_Slots;
    private Button mSearch_Slots;
    private Button mSlot1_Slots;
    private Button mSlot2_Slots;
    private Button mSlot3_Slots;
    private Button mSlot4_Slots;
    private Button mSlot5_Slots;
    private Button mSlot6_Slots;
    private ProgressDialog mProgressDialog;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    private String mSelectedOption;
    private String mDate;
    private String mTime;
    private String mDuration;
    private String mTimeRange;
    private String mCurrentUserId;
    private String mParkingAreaName;
    private String mBookingId;

    private boolean mSlot1Status = true;                    //False will show slot is booked.
    private boolean mSlot2Status = true;                    //False will show slot is booked.
    private boolean mSlot3Status = true;                    //False will show slot is booked.
    private boolean mSlot4Status = true;                    //False will show slot is booked.
    private boolean mSlot5Status = true;                    //False will show slot is booked.
    private boolean mSlot6Status = true;                    //False will show slot is booked.

    public static Intent newIntent(Context packageContext, String selectedOption){
        Intent i = new Intent(packageContext,SlotsActivity.class);
        i.putExtra(EXTRA_SELECTED_OPTION,selectedOption);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slots);

        mDurationSpinner_Slots = (Spinner) findViewById(R.id.durationSpinner_Slots);
        mTimeSpinner_Slots = (Spinner) findViewById(R.id.timeSpinner_Slots);
        mDate_Slots = (EditText) findViewById(R.id.date_Slots);
        mSearch_Slots = (Button) findViewById(R.id.search_Slots);
        mSlot1_Slots = (Button) findViewById(R.id.slot1_Slots);
        mSlot2_Slots = (Button) findViewById(R.id.slot2_Slots);
        mSlot3_Slots = (Button) findViewById(R.id.slot3_Slots);
        mSlot4_Slots = (Button) findViewById(R.id.slot4_Slots);
        mSlot5_Slots = (Button) findViewById(R.id.slot5_Slots);
        mSlot6_Slots = (Button) findViewById(R.id.slot6_Slots);

        mProgressDialog = new ProgressDialog(this);

        final List<String> time = new ArrayList<String>();
        time.add("08:00");
        time.add("09:00");
        time.add("10:00");
        time.add("11:00");
        time.add("12:00");
        time.add("13:00");
        time.add("14:00");
        time.add("15:00");
        time.add("16:00");
        time.add("17:00");
        time.add("18:00");
        time.add("19:00");
        time.add("20:00");

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,time);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeSpinner_Slots.setAdapter(arrayAdapter1);

        List<String> duration = new ArrayList<String>();
        duration.add("1 Hrs");
        duration.add("2 Hrs");
        duration.add("3 Hrs");

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,duration);

        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mDurationSpinner_Slots.setAdapter(arrayAdapter2);

        mSelectedOption = getIntent().getStringExtra(EXTRA_SELECTED_OPTION);
        mCurrentUserId = mFirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mSearch_Slots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate = mDate_Slots.getText().toString();
                mTime = mTimeSpinner_Slots.getSelectedItem().toString();
                mDuration = mDurationSpinner_Slots.getSelectedItem().toString();

                /***************** Code for Date Validation Starts *****************/
                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                date = cal.getTime();

                if(!(isValidDate2(mDate))) {
                    mDate_Slots.setError("Incorrect Date OR Incorrect Date Format");
                    return;
                }

                try {
                    if (new SimpleDateFormat("dd/MM/yyyy").parse(mDate).before(date)) {
                        mDate_Slots.setError("Previous Date is not allowed");
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if((mDate.length() != 10) || (mDate.charAt(2) != '/') || (mDate.charAt(5) != '/')){
                    mDate_Slots.setError("Invalid Format. Format: DD/MM/YYYY");
                    return;
                }


                /***************** Code for Date Validation Ends *****************/

                if(TextUtils.isEmpty(mDate)){
                    mDate_Slots.setError("Date field is empty");
                    return;
                }

                mProgressDialog.setMessage("Searching available Slots");
                mProgressDialog.show();

                mTimeRange = getTimeRange();      //Calling function to get string with starting and ending time e.g: "15:00 - 17:00"

                if(mSelectedOption.equalsIgnoreCase("Gulshan-e-Iqbal"))
                    mParkingAreaName = "Gulshan-e-Iqbal";
                else if(mSelectedOption.equalsIgnoreCase("Gulistan-e-Jauhar"))
                    mParkingAreaName = "Gulistan-e-Jauhar";
                else if(mSelectedOption.equalsIgnoreCase("Saddar"))
                    mParkingAreaName = "Saddar";

                mDatabaseRef.child("Parking Areas").child(mParkingAreaName).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        getInitialize();                    //Function call to initialize variables and assign colors to slots

                        for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                            for(DataSnapshot snapShot2: snapshot1.getChildren()){
                                if((snapShot2.hasChild("Time")) && snapShot2.hasChild("Date")){
                                    String checkTime = snapShot2.child("Time").getValue().toString();
                                    String checkDate = snapShot2.child("Date").getValue().toString();
                                    if(mDate.equalsIgnoreCase(checkDate) && (new CheckTimeRange(mTimeRange).overlaps(new CheckTimeRange(checkTime)))){
                                            //"CheckTimeRange" is a class. I created this class to match "mTime" is in between "checkTime"
                                            // OR "checkTime" is in between "mTime". Returns true if time slot is not available, and
                                            // return false if time slot is available.
                                            if (snapshot1.getKey().equalsIgnoreCase("Slot 1")) {
                                                mSlot1Status = false;
                                                mSlot1_Slots.setEnabled(false);
                                                mSlot1_Slots.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.slotNotAvailable, null));
                                            } else if (snapshot1.getKey().equalsIgnoreCase("Slot 2")) {
                                                mSlot2Status = false;
                                                mSlot2_Slots.setEnabled(false);
                                                mSlot2_Slots.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.slotNotAvailable, null));
                                            } else if (snapshot1.getKey().equalsIgnoreCase("Slot 3")) {
                                                mSlot3Status = false;
                                                mSlot3_Slots.setEnabled(false);
                                                mSlot3_Slots.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.slotNotAvailable, null));
                                            } else if (snapshot1.getKey().equalsIgnoreCase("Slot 4")) {
                                                mSlot4Status = false;
                                                mSlot4_Slots.setEnabled(false);
                                                mSlot4_Slots.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.slotNotAvailable, null));
                                            } else if (snapshot1.getKey().equalsIgnoreCase("Slot 5")) {
                                                mSlot5Status = false;
                                                mSlot5_Slots.setEnabled(false);
                                                mSlot5_Slots.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.slotNotAvailable, null));
                                            } else if (snapshot1.getKey().equalsIgnoreCase("Slot 6")) {
                                                mSlot6Status = false;
                                                mSlot6_Slots.setEnabled(false);
                                                mSlot6_Slots.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.slotNotAvailable, null));
                                            }
                                    }
                                    else{
                                        getAndAssignedEmptySlots();
                                    }
                                }
                                else{
                                    getAndAssignedEmptySlots();
                                }
                            }
                        }
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private static boolean isValidDate2(String dateString) {
        return dates.contains(dateString);
    }

    private static Set<String> dates = new HashSet<String>();
    static {
        for (int year = 1900; year < 2050; year++) {
            for (int month = 1; month <= 12; month++) {
                for (int day = 1; day <= daysInMonth(year, month); day++) {
                    StringBuilder date = new StringBuilder();
                    date.append(String.format("%02d/", day));
                    date.append(String.format("%02d/", month));
                    date.append(String.format("%04d", year));
                    dates.add(date.toString());
                }
            }
        }
    }

    private static int daysInMonth(int year, int month){
        int days;

        if (year%4 ==0 && month == 2){
            days = 29;
        }else if (month == 2){
            days = 28;
        }else if (month == 4 || month == 6 || month == 9 || month == 11){
            days = 30;
        }else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
            days = 31;
        }else{
            days = 0;
        }
        return days;
    }

    private String getTimeRange(){
        String[] parts = mTime.split(":");
        int time1 = Integer.parseInt(parts[0]);

        String[] parts2 = mDuration.split(" ");
        int time2 = Integer.parseInt(parts2[0]);

        int time3 = time1 + time2;
        String time4 = String.valueOf(time3) + ":00";
        String finalEndTime = mTime + " - " +time4;
        return finalEndTime;
    }

    private void getInitialize(){                          //Function definition to initialize variables and assign colors to slots
        mSlot1_Slots.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.slotAvailable,null));
        mSlot2_Slots.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.slotAvailable,null));
        mSlot3_Slots.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.slotAvailable,null));
        mSlot4_Slots.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.slotAvailable,null));
        mSlot5_Slots.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.slotAvailable,null));
        mSlot6_Slots.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.slotAvailable,null));
        mSlot1Status = true;
        mSlot2Status = true;
        mSlot3Status = true;
        mSlot4Status = true;
        mSlot5Status = true;
        mSlot6Status = true;
        mSlot1_Slots.setEnabled(false);
        mSlot2_Slots.setEnabled(false);
        mSlot3_Slots.setEnabled(false);
        mSlot4_Slots.setEnabled(false);
        mSlot5_Slots.setEnabled(false);
        mSlot6_Slots.setEnabled(false);
    }
    private void getAndAssignedEmptySlots(){
        if(mSlot1Status){
            mSlot1_Slots.setEnabled(true);
            mSlot1Status = false;
            mSlot1_Slots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postDataToFirebaseDatabase("Slot 1");
                    //Toast.makeText(SlotsActivity.this,"Slot 1 Pressed",Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(mSlot2Status){
            mSlot2_Slots.setEnabled(true);
            mSlot2Status = false;
            mSlot2_Slots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postDataToFirebaseDatabase("Slot 2");
                    //Toast.makeText(SlotsActivity.this,"Slot 2 Pressed",Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(mSlot3Status){
            mSlot3_Slots.setEnabled(true);
            mSlot3Status = false;
            mSlot3_Slots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postDataToFirebaseDatabase("Slot 3");
                    //Toast.makeText(SlotsActivity.this,"Slot 3 Pressed",Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(mSlot4Status){
            mSlot4_Slots.setEnabled(true);
            mSlot4Status = false;
            mSlot4_Slots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postDataToFirebaseDatabase("Slot 4");
                    //Toast.makeText(SlotsActivity.this,"Slot 4 Pressed",Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(mSlot5Status){
            mSlot5_Slots.setEnabled(true);
            mSlot5Status = false;
            mSlot5_Slots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postDataToFirebaseDatabase("Slot 5");
                    //Toast.makeText(SlotsActivity.this,"Slot 5 Pressed",Toast.LENGTH_LONG).show();
                }
            });
        }
        else if(mSlot6Status){
            mSlot6_Slots.setEnabled(true);
            mSlot6Status = false;
            mSlot6_Slots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postDataToFirebaseDatabase("Slot 6");
                    //Toast.makeText(SlotsActivity.this,"Slot 6 Pressed",Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    void postDataToFirebaseDatabase(String slotNumber){
        HashMap<String,String> dataMap = new HashMap<String, String>();
        dataMap.put("Date",mDate);
        dataMap.put("Time",mTimeRange);
        dataMap.put("User Id",mCurrentUserId);
        createBookingId(slotNumber);
        //mDatabaseRef.child("Parking Areas").child(mParkingAreaName).child(slotNumber).push().setValue(dataMap);
        mDatabaseRef.child("Parking Areas").child(mParkingAreaName).child(slotNumber).child(mBookingId).setValue(dataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SlotsActivity.this,"Booking Successful",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }
    private void createBookingId(String slotNumber){
        //BookingId Format: Lets say Date is 27/07/2017, Time is 14:00, Slot 4, Area: Gulshan-e-Iqbal
        //so BookingId would be 27144G (DayTime SlotNumber ParkingAreaPrefix)
        String[] a = mDate.split("/");
        String[] b = mTime.split(":");
        String[] c = slotNumber.split(" ");
        String d = "";
        if(mParkingAreaName == "Gulshan-e-Iqbal")
            d = "G";
        else if(mParkingAreaName == "Gulistan-e-Jauhar")
            d = "J";
        if(mParkingAreaName == "Saddar")
            d = "S";
        mBookingId = a[0] + b[0] + c[1] + d;
    }
}