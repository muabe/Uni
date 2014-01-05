package com.markjmind.mobile.api.android.ui.view.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.widget.TextView;

public class JwTextViewFonts extends TextView{
	public JwTextViewFonts(Context context) {
	    super(context);
	    setIncludeFontPadding(false);
	    applyTypeface(context, null);
	}

	public JwTextViewFonts(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    setIncludeFontPadding(false);
	    applyTypeface(context, attrs);
	}

	public JwTextViewFonts(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    setIncludeFontPadding(false);
	    applyTypeface(context, attrs);
	}

	public void applyFont(String typefaceName, Context context,AttributeSet attrs){
		Typeface typeface = null;
		try{
			int style = Typeface.NORMAL;
			if(attrs!=null){
			 int[] attrsArray = new int[] {
				        android.R.attr.textStyle // 0
				    };
				typeface = Typeface.createFromAsset(context.getAssets(), typefaceName);
				TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
				style = ta.getInt(0, Typeface.NORMAL);
				ta.recycle();
			}
			setTypeface(typeface, style);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void setFont(String typefaceName, int style){
		Typeface typeface = null;
		try{
//			int style = Typeface.NORMAL;
			typeface = Typeface.createFromAsset(getContext().getAssets(), typefaceName);
			setTypeface(typeface, style);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void setFont(String typefaceName){
		int style = Typeface.NORMAL;
		setFont(typefaceName,style);

	}
	
	
	private void applyTypeface(Context context, AttributeSet attrs){
		String typefaceName = null;
		if(getContentDescription()!=null && getContentDescription().length()>0){
			typefaceName =getContentDescription().toString();
		}
		applyFont(typefaceName, context, attrs);
	}
}
