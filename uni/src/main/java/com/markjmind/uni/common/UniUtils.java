package com.markjmind.uni.common;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

public class UniUtils {

    public static String getUniqueId(Context context){
        String deviceId;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }else{
            deviceId = android.os.Build.SERIAL;
        }
        return deviceId;
    }
}
