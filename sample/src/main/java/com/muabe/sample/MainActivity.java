package com.muabe.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.markjmind.uni.boot.FragmentBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

//        getFragmentManager()
//                .beginTransaction()
//                .replace(R.id.main_container, new LayoutTestFragment())
//                .commit();
        FragmentBuilder.getBuilder(this)
                .setHistory(false)
                .replace(R.id.main_container, new TestFragment1(R.id.main_container));

        FragmentBuilder.getBuilder(this)
                .setHistory(false)
                .replace(R.id.main_container2, new TestFragment1(R.id.main_container2));
    }

    @Override
    public void onBackPressed() {
        if(!FragmentBuilder.getBuilder(this).popBackStack(R.id.main_container)){
            if(!FragmentBuilder.getBuilder(this).popBackStack(R.id.main_container2)){
                finish();
            }
        }
    }
}
