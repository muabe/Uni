package com.muabe.mvvm;

import android.os.Bundle;

import com.muabe.mvvm.databinding.ActivityMainBinding;

import java.lang.reflect.Field;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Vm vm = new Vm(binding);
        vm.setA("ㅁㄴㅇㄹㅁㄴㅇㄹㅁㄴㅇ");
        vm.setB("asdfasdfa");

        try {
            Field field = BR.class.getField("vartest");
            binding.setVariable((int)field.get(null), vm);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
