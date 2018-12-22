package com.anashamidkh.parkingsystem;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShowListActivity extends AppCompatActivity {
    private static final String EXTRA_OPTION_SELECTED = "com.anashamidkh.parkingsystem.option_Selected";

    private ListView mListView_ShowList;
    private ArrayList<String> mListItems = new ArrayList<>();
    private ArrayList<String> mKeys = new ArrayList<>();

    private String mOptionSelected;

    private DatabaseReference mDatabaseRef;

    public static Intent newIntent(Context packageContext,String optionSelected){
        Intent i = new Intent(packageContext,ShowListActivity.class);
        i.putExtra(EXTRA_OPTION_SELECTED,optionSelected);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        mOptionSelected = getIntent().getStringExtra(EXTRA_OPTION_SELECTED);

        //Toast.makeText(this,mOptionSelected,Toast.LENGTH_LONG).show();

        if(mOptionSelected.equalsIgnoreCase("viewFeedbacks"))
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Feedbacks");
        else if(mOptionSelected.equalsIgnoreCase("viewUsers"))
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mListView_ShowList = (ListView) findViewById(R.id.listView_ShowList);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mListItems){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(ResourcesCompat.getColor(getResources(),R.color.fontColor,null));
                text.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.backgroundColorBtn,null));
                //text.setBackgroundColor(getResources().getColor(R.color.backgroundColorBtn));
                return view;
            }
        };

        mListView_ShowList.setAdapter(arrayAdapter);

        mListView_ShowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mOptionSelected.equalsIgnoreCase("viewUsers")){
                    String userId = mKeys.get(position);
                    Intent i = UserDescription.newIntent(ShowListActivity.this,userId);
                    startActivity(i);
                }
                else if(mOptionSelected.equalsIgnoreCase("viewFeedbacks")){
                    String feedbackKey = mKeys.get(position);
                    Intent i = FeedbackDescription.newIntent(ShowListActivity.this,feedbackKey);
                    startActivity(i);
                }
            }
        });

        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String item = "";

                if (mOptionSelected.equalsIgnoreCase("viewUsers")) {
                    if (dataSnapshot.child("AccountType").getValue().toString().equalsIgnoreCase("User")) {
                        item = dataSnapshot.child("Name").getValue().toString();
                    }
                    else
                        return;
                }
                else if(mOptionSelected.equalsIgnoreCase("viewFeedbacks")) {
                    item = dataSnapshot.child("Feedback").getValue().toString();
                }

                mListItems.add(item);
                String key = dataSnapshot.getKey();
                mKeys.add(key);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String item = "";
                if (mOptionSelected.equalsIgnoreCase("viewUsers"))
                    item = dataSnapshot.child("Name").getValue().toString();
                else if(mOptionSelected.equalsIgnoreCase("viewFeedbacks"))
                    item = dataSnapshot.child("Feedback").getValue().toString();

                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);

                mListItems.set(index, item);
                mKeys.add(key);

                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);

                mListItems.remove(index);
                mKeys.remove(key);

                arrayAdapter.notifyDataSetChanged();

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
