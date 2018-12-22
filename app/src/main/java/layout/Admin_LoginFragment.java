package layout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anashamidkh.parkingsystem.R;
import com.anashamidkh.parkingsystem.ShowListActivity;
import com.anashamidkh.parkingsystem.ViewBookingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Admin_LoginFragment extends Fragment {
    private Button mAddParkingArea;
    private Button mViewBookingsBtn;
    private Button mViewUsersBtn;
    private Button mViewFeedbackBtn;
    private Button mLogoutBtn_Admin_Login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_admin__login, container, false);

        mAddParkingArea = (Button) v.findViewById(R.id.addParkingArea);
        mViewBookingsBtn = (Button) v.findViewById(R.id.viewBookingsBtn);
        mViewUsersBtn = (Button) v.findViewById(R.id.viewUsersBtn);
        mViewFeedbackBtn = (Button) v.findViewById(R.id.viewFeedbackBtn);
        mLogoutBtn_Admin_Login = (Button) v.findViewById(R.id.logoutBtn_Admin_Login);

        mAddParkingArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Generates Raw Data in Firebase Database
                /*ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setMessage("Adding slots data in to Firebase Database");
                mProgressDialog.show();

                DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();

                HashMap<String,String> dataMap = new HashMap<String,String >();


                dataMap.put("Test UserId","ppiee");
                dataMap.put("Test Date","06/10/2017");
                dataMap.put("Test Time","17:00 - 18:00");
                mDatabaseRef.child("Parking Areas").child("Gulshan-e-Iqbal").child("Slot 1").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","oowgj");
                dataMap.put("Test Date","09/08/2017");
                dataMap.put("Test Time","15:00 - 16:00");
                mDatabaseRef.child("Parking Areas").child("Gulshan-e-Iqbal").child("Slot 2").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","gsgnk");
                dataMap.put("Test Date","28/08/2017");
                dataMap.put("Test Time","21:00 - 22:00");
                mDatabaseRef.child("Parking Areas").child("Gulshan-e-Iqbal").child("Slot 3").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","sfjsjs");
                dataMap.put("Test Date","03/08/2017");
                dataMap.put("Test Time","19:00 - 20:00");
                mDatabaseRef.child("Parking Areas").child("Gulshan-e-Iqbal").child("Slot 4").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","wgowwo");
                dataMap.put("Test Date","14/08/2017");
                dataMap.put("Test Time","16:30 - 18:30");
                mDatabaseRef.child("Parking Areas").child("Gulshan-e-Iqbal").child("Slot 5").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","poitrwo");
                dataMap.put("Test Date","29/09/2017");
                dataMap.put("Test Time","18:00 - 19:00");
                mDatabaseRef.child("Parking Areas").child("Gulshan-e-Iqbal").child("Slot 6").child("Test Booking").setValue(dataMap);


                dataMap.put("Test UserId","petkep");
                dataMap.put("Test Date","07/11/2017");
                dataMap.put("Test Time","15:00 - 17:00");
                mDatabaseRef.child("Parking Areas").child("Gulistan-e-Jauhar").child("Slot 1").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","ppoepy");
                dataMap.put("Test Date","03/09/2017");
                dataMap.put("Test Time","14:00 - 16:00");
                mDatabaseRef.child("Parking Areas").child("Gulistan-e-Jauhar").child("Slot 2").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","porpur");
                dataMap.put("Test Date","28/011/2017");
                dataMap.put("Test Time","20:00 - 22:00");
                mDatabaseRef.child("Parking Areas").child("Gulistan-e-Jauhar").child("Slot 3").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","xsfgwji");
                dataMap.put("Test Date","04/08/2017");
                dataMap.put("Test Time","18:00 - 19:00");
                mDatabaseRef.child("Parking Areas").child("Gulistan-e-Jauhar").child("Slot 4").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","qqrrgowwo");
                dataMap.put("Test Date","19/08/2017");
                dataMap.put("Test Time","19:00 - 20:00");
                mDatabaseRef.child("Parking Areas").child("Gulistan-e-Jauhar").child("Slot 5").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","poitrwo");
                dataMap.put("Test Date","26/09/2017");
                dataMap.put("Test Time","20:00 - 21:00");
                mDatabaseRef.child("Parking Areas").child("Gulistan-e-Jauhar").child("Slot 6").child("Test Booking").setValue(dataMap);


                dataMap.put("Test UserId","wow");
                dataMap.put("Test Date","11/11/2017");
                dataMap.put("Test Time","16:00 - 17:00");
                mDatabaseRef.child("Parking Areas").child("Saddar").child("Slot 1").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","vxb");
                dataMap.put("Test Date","02/09/2017");
                dataMap.put("Test Time","20:00 - 21:00");
                mDatabaseRef.child("Parking Areas").child("Saddar").child("Slot 2").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","mzp");
                dataMap.put("Test Date","25/11/2017");
                dataMap.put("Test Time","21:00 - 22:00");
                mDatabaseRef.child("Parking Areas").child("Saddar").child("Slot 3").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","xsxxji");
                dataMap.put("Test Date","06/09/2017");
                dataMap.put("Test Time","15:00 - 17:00");
                mDatabaseRef.child("Parking Areas").child("Saddar").child("Slot 4").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","mzkf");
                dataMap.put("Test Date","17/08/2017");
                dataMap.put("Test Time","17:00 - 19:00");
                mDatabaseRef.child("Parking Areas").child("Saddar").child("Slot 5").child("Test Booking").setValue(dataMap);

                dataMap.put("Test UserId","ufxq");
                dataMap.put("Test Date","25/09/2017");
                dataMap.put("Test Time","21:00 - 22:00");
                mDatabaseRef.child("Parking Areas").child("Saddar").child("Slot 6").child("Test Booking").setValue(dataMap);

                mProgressDialog.dismiss();*/

                Toast.makeText(getActivity(),"This button is disabled by developer.",Toast.LENGTH_LONG).show();
            }
        });

        mViewBookingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ViewBookingsActivity.class);
                startActivity(i);
            }
        });

        mViewUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = ShowListActivity.newIntent(getActivity(),"viewUsers");
                startActivity(i);
            }
        });

        mViewFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = ShowListActivity.newIntent(getActivity(),"viewFeedbacks");
                startActivity(i);
            }
        });

        mLogoutBtn_Admin_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                getActivity().finish();
            }
        });

        return v;
    }
}
