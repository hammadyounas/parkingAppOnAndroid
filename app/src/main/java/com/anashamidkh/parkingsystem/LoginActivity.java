package com.anashamidkh.parkingsystem;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import layout.Admin_LoginFragment;
import layout.User_LoginFragment;

public class LoginActivity extends FragmentActivity {
    private static final String EXTRA_LOGIN_TYPE = "com.anashamidkh.parkingsystem.logIn_Type";
    private String mLoginType;

    public static Intent newIntent(Context packageContext, String loginType){
        Intent i = new Intent(packageContext,LoginActivity.class);
        i.putExtra(EXTRA_LOGIN_TYPE,loginType);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginType = getIntent().getStringExtra(EXTRA_LOGIN_TYPE);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.login_fragment_container);

        if(fragment == null){
            if(mLoginType.equalsIgnoreCase("Admin")){
                fragment = new Admin_LoginFragment();
            }
            else if(mLoginType.equalsIgnoreCase("User")){
                fragment = new User_LoginFragment();
            }
            fm.beginTransaction()
                    .add(R.id.login_fragment_container, fragment)
                    .commit();
        }
    }
}
