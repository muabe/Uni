package com.muabe.sample;

import android.view.View;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.OnClick;
import com.muabe.sample.unilayout.LayoutDetailFragment;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-10-14
 */

@Layout(R.layout.main_fragment)
public class MainFragment extends UniFragment{

    /********************* UniLayout *********************/
    @OnClick
    public void btn_extends_layout(View view){
        replace("extends", new LayoutDetailFragment());
    }

    @OnClick
    public void btn_xml_layout(View view){
        replace("xml", new LayoutDetailFragment());
    }

    @OnClick
    public void btn_bind_layout(View view){
        replace("bind", new LayoutDetailFragment());
    }


    /********************* UniFragment *********************/

    @OnClick
    public void btn_extends_fragment(View view){

    }

    @OnClick
    public void btn_xml_fragment(View view){

    }

    @OnClick
    public void btn_bind_fragment(View view){

    }


    /********************* UniDialog *********************/

    @OnClick
    public void btn_extends_dialog(View view){

    }

    @OnClick
    public void btn_bind_dialog(View view){

    }


    private void replace(String mode, UniFragment fragment){
        fragment.param.add("mode",mode);
        getFragmentManager()
                .beginTransaction()
                .addToBackStack("main")
                .replace(R.id.main_container, fragment)
                .commit();
    }
}
