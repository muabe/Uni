package com.markjmind.fragmenttest;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.markjmind.uni.UniView;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.Layout;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class Menu2Fragment extends Fragment{
    TestUni uniView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(uniView==null) {
            uniView = new TestUni(getActivity());
            uniView.excute();
        }

        return uniView;
    }

    @Layout(R.layout.uniview)
    class TestUni extends UniView{
        @GetView
        Button button;

        public TestUni(Context context) {
            super(context);
        }

        @Override
        public void onPost(int requestCode) {
            button.setText("유니뷰 버튼");
          }

        public int getId(String idName, Context context) throws Exception{
//            Class clz = Class.forName(context.getPackageName()+".R$id");
//            Field field = clz.getDeclaredField(idName);
//            return field.getInt(null);

            int resID = context.getResources().getIdentifier(idName, "id", context.getPackageName());
            return resID;
        }
    }
}
