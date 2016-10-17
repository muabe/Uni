package com.muabe.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new MainFragment())
                .commit();
    }
}
