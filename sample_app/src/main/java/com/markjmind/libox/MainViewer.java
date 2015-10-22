package com.markjmind.libox;

import android.view.View;

import com.markjmind.libox.dietary.DietaryViewer;
import com.markjmind.libox.exercise.ExerciseViewer;
import com.markjmind.libox.test.Test1;
import com.markjmind.libox.test.Test2;
import com.markjmind.uni.Viewer;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;
import com.markjmind.uni.annotiation.OnClick;

/**
 * Created by codemasta on 2015-09-16.
 */

@Layout(R.layout.mainlayout)
public class MainViewer extends Viewer {
    @GetView View menu1;

    @Override
    public void onPost(int requestCode) {
        Viewer.build(ExerciseViewer.class,getActivity()).change(R.id.main_frame);
    }

    @OnClick(ids={R.id.menu1, R.id.menu2, R.id.menu3, R.id.menu4})
    public void click(View view){
        if(view.getId()==R.id.menu1) {
            Viewer.build(ExerciseViewer.class, getActivity()).change(R.id.main_frame);
        }else if(view.getId()==R.id.menu2) {
            Viewer.build(DietaryViewer.class,getActivity()).setAsync(true).change(R.id.main_frame);
        }else if(view.getId()==R.id.menu3) {
            Viewer.build(Test1.class,getActivity()).setAsync(true).change(R.id.main_frame);
        }else if(view.getId()==R.id.menu4) {
            Viewer.build(Test2.class,getActivity()).setAsync(true).change(R.id.main_frame);
        }
    }
}
