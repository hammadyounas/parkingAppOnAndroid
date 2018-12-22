package com.anashamidkh.parkingsystem;

import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyBookingsActivity extends AppCompatActivity {

    private ListView mListView_MyBookings;
    private ArrayList<String> mListItems = new ArrayList<>();

    private String mCurrentUserId;

    private String mBookingId;
    private String mParkingAreaName;
    private String mSlotName;
    private String mDate;
    private String mTime;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        mListView_MyBookings = (ListView) findViewById(R.id.listView_MyBookings);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mListItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(ResourcesCompat.getColor(getResources(),R.color.fontColor,null));
                text.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.backgroundColorBtn,null));
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                //text.setBackgroundColor(getResources().getColor(R.color.backgroundColorBtn));
                return view;
            }
        };

        mListView_MyBookings.setAdapter(arrayAdapter);
        mListView_MyBookings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = mListItems.get(position);
                String activityName = "MyBookingsActivity";
                Intent i = BookingDescriptionActivity.newIntent(MyBookingsActivity.this,item,activityName);
                startActivity(i);
            }
        });
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Parking Areas");
        mCurrentUserId = mFirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mListItems.clear();
                for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                    for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                        for(DataSnapshot snapshot3 : snapshot2.getChildren()){
                            if((snapshot3.hasChild("Date")) && (snapshot3.hasChild("Time"))){
                                if(snapshot3.child("User Id").getValue().toString().equals(mCurrentUserId)){
                                    String item;

                                    mBookingId = snapshot3.getKey().toString();
                                    mParkingAreaName = snapshot1.getKey().toString();
                                    String a[] = snapshot2.getKey().toString().split(" ");
                                    mSlotName = a[1];
                                    mDate = snapshot3.child("Date").getValue().toString();
                                    mTime = snapshot3.child("Time").getValue().toString();

                                    item = "Booking Id: "+mBookingId + "\nParking Area: "+mParkingAreaName + "\nSlot: "+mSlotName +
                                            "\nDate: "+mDate + "\nTime: "+mTime;
                                    mListItems.add(item);
                                }
                            }
                        }
                    }
                }
                if(mListItems.isEmpty()){
                    mListItems.add("You don't have any booking so far");
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}