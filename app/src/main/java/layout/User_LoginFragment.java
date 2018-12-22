package layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anashamidkh.parkingsystem.AreaActivity;
import com.anashamidkh.parkingsystem.CancelBookingActivity;
import com.anashamidkh.parkingsystem.MyBookingsActivity;
import com.anashamidkh.parkingsystem.R;
import com.anashamidkh.parkingsystem.SubmitFeedbackActivity;
import com.anashamidkh.parkingsystem.UserDescription;
import com.google.firebase.auth.FirebaseAuth;

public class User_LoginFragment extends Fragment {
    private Button mNewBookingBtn;
    private Button mMyBookingsBtn;
    private Button mCancelBookingBtn;
    private Button mMyDetails;
    private Button mFeedbackBtn;
    private Button mLogoutBtn_User_Login;

    private FirebaseAuth mFirebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user__login, container, false);

        mNewBookingBtn = (Button) v.findViewById(R.id.newBookingBtn);
        mMyBookingsBtn = (Button) v.findViewById(R.id.myBookingsBtn);
        mCancelBookingBtn = (Button) v.findViewById(R.id.cancelBookingBtn);
        mMyDetails = (Button) v.findViewById(R.id.myDetails);
        mFeedbackBtn = (Button) v.findViewById(R.id.feedbackBtn);
        mLogoutBtn_User_Login = (Button) v.findViewById(R.id.logoutBtn_User_Login);

        mNewBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AreaActivity.class);
                startActivity(i);
            }
        });

        mMyBookingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyBookingsActivity.class);
                startActivity(i);
            }
        });

        mCancelBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CancelBookingActivity.class);
                startActivity(i);
            }
        });

        mMyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserId = mFirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                Intent i = UserDescription.newIntent(getActivity(),currentUserId);
                startActivity(i);
            }
        });

        mFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SubmitFeedbackActivity.class);
                startActivity(i);
            }
        });

        mLogoutBtn_User_Login.setOnClickListener(new View.OnClickListener() {
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
