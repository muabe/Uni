package com.markjmind.uni.boot;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-12-05
 */

public class FragmentStack {
    public int index = 0;
    public boolean clearPopStackOnResume= false;
    public boolean popStackOnResume = false;

    public void clearHistoryOnResume(){
        clearPopStackOnResume = true;
    }

    public void popStackOnResume(){
        popStackOnResume = true;
    }

    public String getName(String stackName){
        return stackName+"_"+index;
    }
}
