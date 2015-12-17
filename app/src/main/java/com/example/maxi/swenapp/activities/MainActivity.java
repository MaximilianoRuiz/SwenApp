package com.example.maxi.swenapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.maxi.swenapp.R;
import com.example.maxi.swenapp.fragments.MainFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }

}
