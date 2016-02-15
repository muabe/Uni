package com.markjmind.fragmenttest;

import android.widget.TextView;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.mapper.annotiation.Layout;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */

@Layout(R.layout.menu3_frgt)
public class Menu3Fragment extends UniFragment {
    @GetView
    TextView textView;

    @Override
    public void onPre(int requestCode) {
        textView.setText("준비");
    }

    @Override
    public void onPost(int requestCode) {
        textView.setText("완료");
    }
}
