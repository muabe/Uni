package com.markjmind.fragmenttest;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.markjmind.uni.UniView;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-01
 */

@Layout(R.layout.uniview)
public class TestUniView extends UniView {
    @GetView
    Button button;

    public TestUniView(Context context) {
        super(context);
    }

    public TestUniView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestUniView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onPost(int requestCode) {
        setBackgroundColor(Color.parseColor("#ff0000"));
        button.setText("하이");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewGroup)getParent()).setVisibility(View.GONE);
            }
        });
    }
}
