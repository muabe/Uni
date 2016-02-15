package com.markjmind.fragmenttest;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.markjmind.uni.UniView;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.Param;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-01-28
 */
public class Menu2Fragment extends Fragment{
    TestUni uniView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        uniView = new TestUni(getActivity());
        uniView.param.add("1", "1");
        uniView.param.add("2","2");
        uniView.param.add("a","aaa");
        uniView.param.add("b","bbb");
        uniView.excute();
        return uniView;
    }

    @Layout(R.layout.uniview)
    class TestUni extends UniView{
        @Param
        String a,b;

        @GetView
        Button button;

        public TestUni(Context context) {
            super(context);
        }

        @Override
        public void onPost(int requestCode) {
            button.setText("유니뷰 버튼");
            Log.e("d", a);
            Log.e("d",b);
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
