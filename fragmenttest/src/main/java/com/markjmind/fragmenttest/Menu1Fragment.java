package com.markjmind.fragmenttest;

import android.widget.TextView;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */

@Layout(R.layout.menu1_frgt)
public class Menu1Fragment extends UniFragment {
    @GetView
    TextView textView;


    @Override
    public void onPost(int requestCode) {
        textView.setText("메뉴1");
    }
}
