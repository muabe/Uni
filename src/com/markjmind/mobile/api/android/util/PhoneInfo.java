package com.markjmind.mobile.api.android.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneInfo {
	
	/**
	 * 핸드폰번호
	 * @return
	 */
	public static String getPhoneNumber(Context context){
		TelephonyManager telM = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNum = telM.getLine1Number();
		return phoneNum;
	}
}
