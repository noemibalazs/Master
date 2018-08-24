package com.example.android.master.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.master.R;

public class MainActivity extends AppCompatActivity {

    private Boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {

            MainFragment fragment = new MainFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().add(R.id.replace, fragment).commit();

            if (findViewById(R.id.container) != null) {
                isTablet = true;

            } else {
                isTablet = false;
            }

        }

    }


    public boolean tabletMode(){
        return  isTablet;
    }
}
