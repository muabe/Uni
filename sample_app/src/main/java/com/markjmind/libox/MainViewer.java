package com.markjmind.libox;

import android.view.View;
import android.widget.Button;

import com.markjmind.libox.dietary.DietaryViewer;
import com.markjmind.libox.exercise.ExerciseViewer;
import com.markjmind.libox.test.Test1;
import com.markjmind.libox.test.Test2;
import com.markjmind.uni.Viewer;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;

/**
 * Created by codemasta on 2015-09-16.
 */

@Layout(R.layout.mainlayout)
public class MainViewer extends Viewer {
    @GetView
    Button menu1, menu2, menu3, menu4;

    @Override
    public void onPost(int requestCode) {
        Viewer.build(ExerciseViewer.class,getActivity()).change(R.id.main_frame);

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Viewer.build(ExerciseViewer.class,getActivity()).change(R.id.main_frame);
            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Viewer.build(DietaryViewer.class,getActivity()).setAsync(true).change(R.id.main_frame);
            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Viewer.build(Test1.class,getActivity()).setAsync(true).change(R.id.main_frame);
            }
        });

        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Viewer.build(Test2.class,getActivity()).setAsync(true).change(R.id.main_frame);
            }
        });
    }
}
