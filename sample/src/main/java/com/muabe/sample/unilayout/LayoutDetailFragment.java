package com.muabe.sample.unilayout;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.mapper.annotiation.Layout;
import com.markjmind.uni.mapper.annotiation.Param;
import com.muabe.sample.R;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-10-14
 */

@Layout(R.layout.unilayout)
public class LayoutDetailFragment extends UniFragment {
    @Param
    String mode;

    @Override
    public void onPre() {

    }
}
