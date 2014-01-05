package com.markjmind.mobile.api.android.ui.view.textview;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.widget.TextView;

public class JwTextViewWrapScale  extends TextView {
	private interface SizeTester {
	    public int onTestSize(int suggestedSize, RectF availableSpace);
	}

	private RectF mTextRect = new RectF();
	private RectF mAvailableSpaceRect;
	private SparseIntArray mTextCachedSizes;
	private TextPaint mPaint;
	private float mMaxTextSize=500f;
	private float mSpacingMult = 1.0f;
	private float mSpacingAdd = 0.0f;
	private float mMinTextSize = 20;
	private int mWidthLimit;
	private static final int NO_LINE_LIMIT = -1;
	private int mMaxLines;

	public JwTextViewWrapScale(Context context) {
	    super(context);
	    initialize();
	}
 
	public JwTextViewWrapScale(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    initialize();
	}

	public JwTextViewWrapScale(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    initialize();
	}

	private void initialize() {
		setIncludeFontPadding( false );
		applyFont(getContext());
	    mPaint = new TextPaint(getPaint());
//	    mMaxTextSize = getTextSize();
	    mAvailableSpaceRect = new RectF();
	    mTextCachedSizes = new SparseIntArray();
	    if (mMaxLines == 0) {
	        // no value was assigned during construction
	        mMaxLines = NO_LINE_LIMIT;
	    }
	    setMinTextSize(1);
	}

	private void applyFont(Context context){
		String typefaceName = null;
		if(getContentDescription()!=null && getContentDescription().length()>0){
			typefaceName =getContentDescription().toString();
		}else{
			return;
		}
		Typeface typeface = null;
		try{
			typeface = Typeface.createFromAsset(context.getAssets(), typefaceName);
			setTypeface(typeface);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}



	@Override
	public void setLineSpacing(float add, float mult) {
	    super.setLineSpacing(add, mult);
	    mSpacingMult = mult;
	    mSpacingAdd = add;
	}

	/**
	 * Set the lower text size limit and invalidate the view
	 * 
	 * @param minTextSize
	 */
	public void setMinTextSize(float minTextSize) {
	    mMinTextSize = minTextSize;
	}



	private final SizeTester mSizeTester = new SizeTester() {
	    @Override
	    public int onTestSize(int suggestedSize, RectF availableSPace) {
	        mPaint.setTextSize(suggestedSize);
	        String text = getText().toString();
	        boolean singleline = true;
	        if (singleline) {
	            mTextRect.bottom = mPaint.getFontSpacing();
	            mTextRect.right = mPaint.measureText(text);
	        } else {
	            StaticLayout layout = new StaticLayout(text, mPaint,
	                    mWidthLimit, Alignment.ALIGN_NORMAL, mSpacingMult,
	                    mSpacingAdd, true);
	            mTextRect.bottom = layout.getHeight();
	            int maxWidth = -1;
	            for (int i = 0; i < layout.getLineCount(); i++) {
	                if (maxWidth < layout.getLineWidth(i)) {
	                    maxWidth = (int) layout.getLineWidth(i);
	                }
	            }
	            mTextRect.right = maxWidth;
	        }

	        mTextRect.offsetTo(0, 0);
	        if (availableSPace.contains(mTextRect)) {
	            // may be too small, don't worry we will find the best match
	            return -1;
	        } else {
	            // too big
	            return 1;
	        }
	    }
	};

	
	 @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	    {
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		    int startSize = (int) mMinTextSize;
		    int heightLimit = getMeasuredHeight() - getCompoundPaddingBottom()- getCompoundPaddingTop();
		    mWidthLimit = getMeasuredWidth() - getCompoundPaddingLeft()- getCompoundPaddingRight();
		    mAvailableSpaceRect.right = mWidthLimit;
		    mAvailableSpaceRect.bottom = heightLimit;
		    int textHeight = efficientTextSizeSearch(startSize, (int) Math.ceil(mMaxTextSize),mSizeTester, mAvailableSpaceRect);
		    super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textHeight);
//		    int textWidth =(int)Math.ceil(getPaint().measureText(getText().toString())- getCompoundPaddingLeft()- getCompoundPaddingRight());
//		    textHeight = (int) Math.ceil(getPaint().getFontSpacing()- getCompoundPaddingBottom()- getCompoundPaddingTop());
//		    this.setMeasuredDimension(textWidth, textHeight);
	    }
	/**
	 * Enables or disables size caching, enabling it will improve performance
	 * where you are animating a value inside TextView. This stores the font
	 * size against getText().length() Be careful though while enabling it as 0
	 * takes more space than 1 on some fonts and so on.
	 * 
	 * @param enable
	 *            enable font size caching
	 */

	private int efficientTextSizeSearch(int start, int end,SizeTester sizeTester, RectF availableSpace) {
	    String text = getText().toString();
	    int key = text == null ? 0 : text.length();
	    int size = mTextCachedSizes.get(key);
	    if (size != 0) {
	        return size;
	    }
	    size = binarySearch(start, end, sizeTester, availableSpace);
	    mTextCachedSizes.put(key, size);
	    return size;
	}

	private static int binarySearch(int start, int end, SizeTester sizeTester,
	        RectF availableSpace) {
	    int lastBest = start;
	    int lo = start;
	    int hi = end - 1;
	    int mid = 0;
	    while (lo <= hi) {
	        mid = (lo + hi) >>> 1;
	        int midValCmp = sizeTester.onTestSize(mid, availableSpace);
	        if (midValCmp < 0) {
	            lastBest = lo;
	            lo = mid + 1;
	        } else if (midValCmp > 0) {
	            hi = mid - 1;
	            lastBest = hi;
	        } else {
	            return mid;
	        }
	    }
	    // make sure to return last best
	    // this is what should always be returned
	    return lastBest;
	}

	@Override
	protected void onTextChanged(final CharSequence text, final int start,
	        final int before, final int after) {
	    super.onTextChanged(text, start, before, after);
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldwidth,
	        int oldheight) {
	    mTextCachedSizes.clear();
	    super.onSizeChanged(width, height, oldwidth, oldheight);
	    if (width != oldwidth || height != oldheight) {
	    }
	}
}
