package com.markjmind.uni.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by MarkJ on 2016-11-28.
 */

public class EditTextUtils {

    private static void hideKeyboard(View view){
        if ( view != null ) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm.isActive()) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void hideKeyboard(Activity activity){
        hideKeyboard(activity.getCurrentFocus());
    }

    public static void hideKeyboard(Dialog dialog){
        hideKeyboard(dialog.getCurrentFocus());
    }


}
