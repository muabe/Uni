package com.muabe.sample.unilayout;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.UniLayout;
import com.markjmind.uni.mapper.annotiation.GetView;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.muabe.sample.R;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-10-14
 */

@Layout(R.layout.unilayout_test)
public class LayoutTestFragment extends UniFragment{

    @GetView
    UniLayout layout_xml_bind;

    @Override
    public void onPre() {




        /** Bind-XML Area**/
        BindXml bindXml = new BindXml();
        bindXml.bind(layout_xml_bind);
        bindXml.getTask().execute();
    }

//    private void replace(String mode, UniFragment fragment){
//        fragment.param.add("mode",mode);
//        getFragmentManager()
//                .beginTransaction()
//                .addToBackStack("main")
//                .replace(R.id.main_container, fragment)
//                .commit();
//    }

}
