package com.markjmind.fragmenttest;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.markjmind.uni.Uni;
import com.markjmind.uni.UniTask;
import com.markjmind.uni.UniView;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.progress.UniProgress;

public class MainActivity extends Activity {

    ViewGroup lyt;
    Menu1Fragment menu1Fragment;
    Menu2Fragment menu2Fragment;
    Menu3Fragment menu3Fragment;
    FragmentManager fm;
    UniView uni;

    boolean isTask = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isTask) {
            setContentView(R.layout.ok);
            ViewGroup frame = (ViewGroup)findViewById(R.id.frame);
            Content content = new Content(this);
            content.add(frame);
            Content2 content2 = new Content2(this);
            content2.add(frame);
            Uni.bind(this, R.id.aaa, new TaskTest(this)).excute();
        }else {


            setContentView(R.layout.activity_main);


            lyt = (ViewGroup) findViewById(R.id.lyt);
            menu1Fragment = new Menu1Fragment();
            menu2Fragment = new Menu2Fragment();
            menu3Fragment = new Menu3Fragment();

            menu1Fragment.progress.set(UniProgress.DIALOG, new SimpleProgressBar());
            fm = getFragmentManager();
            uni = (UniView) findViewById(R.id.uni);
            uni.excute();

//        menu1Fragment.getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                menu1Fragment.setBackStack(true);
//            }
//        });
//
//        menu3Fragment.getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                menu3Fragment.setBackStack(true);
//            }
//        });


            findViewById(R.id.menu1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fm.beginTransaction().replace(R.id.lyt, menu1Fragment).addToBackStack(null).commit();
                }
            });

            findViewById(R.id.menu2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fm.beginTransaction().replace(R.id.lyt, menu2Fragment).addToBackStack(null).commit();
                }
            });

            findViewById(R.id.menu3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menu3Fragment.param.add("ok", "okok");
                    menu3Fragment.param.add("c", "ccc");
                    menu3Fragment.progress.param.add("textName", "하이1");
                    fm.beginTransaction().replace(R.id.lyt, menu3Fragment).addToBackStack(null).commit();
                }
            });
        }
    }

   @Override
    public void onBackPressed() {
       if(fm == null){
           super.onBackPressed();
       }else if(fm.getBackStackEntryCount() > 1){
            fm.popBackStack();
       }else{
            super.onBackPressed();
       }
    }


    class TaskTest extends UniTask{
        @GetView
        Button button4;
        Context context;
        public TaskTest(Context context){
            this.context = context;
        }

        @Override
        public void onPost() {
            button4.setText("alksd");
        }
    }
}
