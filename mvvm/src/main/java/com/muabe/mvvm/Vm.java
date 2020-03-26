package com.muabe.mvvm;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.muabe.mvvm.databinding.ActivityMainBinding;

public class Vm {
    String a;
    String b;

    ActivityMainBinding binding;
    public Vm(ActivityMainBinding binding){
        this.binding = binding;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public void clickbtn(View view){
        Toast.makeText(view.getContext(), "dklsjdf", Toast.LENGTH_SHORT).show();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setA("asdlkjf;alksjdf;lkasjd;flkajsd;lfja;s");
            }
        }.execute();
    }
}
