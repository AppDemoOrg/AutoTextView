package com.abt.auto;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

/**
 * @描述： @AutoFitTextView
 * @作者： @黄卫旗
 * @创建时间： @2018/5/22
 */
public class AutoFitTextView extends AppCompatTextView {

    private static final String TAG = AutoFitTextView.class.getSimpleName();

    private static float DEFAULT_MIN_TEXT_SIZE = 12; // unit px
    private static float DEFAULT_MAX_TEXT_SIZE = 50; // unit px

    private TextPaint mTextPaint;
    private float mMinTextSize; // Attributes
    private float mMaxTextSize; // Attributes

    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        mTextPaint = new TextPaint();
        mTextPaint.set(this.getPaint());
        // max size defaults to the intially specified text size unless it is too small
        mMaxTextSize = this.getTextSize();
        if (mMaxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            mMaxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }
        mMinTextSize = DEFAULT_MIN_TEXT_SIZE;
    }

    /**
     * Re size the font so the specified text fits in the text box * assuming the text box is the specified width.
     */
    private void refitText(String text, int textWidth, int textHeight) {
        if (textWidth > 0 && textHeight > 0) {
            // allow diplay rect
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            int availableHeight = textHeight - this.getPaddingBottom() - this.getPaddingTop();
            // by the line calculate allow displayWidth
            int autoWidth = availableWidth;
            float mult = 1f;
            float add = 0;
            if (Build.VERSION.SDK_INT > 16) {
                mult = getLineSpacingMultiplier();
                add = getLineSpacingExtra();
            } else {
                // the mult default is 1.0f,if you need change ,you can reflect invoke this field;
            }
            float trySize = mMaxTextSize;
            mTextPaint.setTextSize(trySize);
            int oldline = 1, newline = 1;
            while ((trySize > mMinTextSize)) {
                // calculate text singleline width。
                int displayW = (int) mTextPaint.measureText(text);
                // calculate text singleline height。
                int displaH = round(mTextPaint.getFontMetricsInt(null) * mult + add);
                if (displayW < autoWidth) {
                    break;
                }

                // TODO 打开这里可以换行
                /* newline = availableHeight / displaH; // calculate maxLines
                if (newline > oldline) {
                    oldline = newline;
                    autoWidth = availableWidth * newline; // if line change ,calculate new autoWidth
                    continue;
                }*/

                // try more small TextSize
                trySize -= 1;
                if (trySize <= mMinTextSize) {
                    trySize = mMinTextSize;
                    break;
                }
                mTextPaint.setTextSize(trySize);
            }
            // setMultiLine
            if (newline >= 2) {
                this.setSingleLine(false);
                this.setMaxLines(newline);
            }
            Log.d(TAG, "trySize = "+trySize);
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        refitText(text.toString(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.e(TAG, "new(" + w + "," + h + ") old(" + oldw + "" + oldh + ")");
        if (w != oldw || h != oldh) {
            refitText(this.getText().toString(), w, h);
        }
    }

    public static int round(float value) {
        long lx = (long) (value * (65536 * 256f));
        return (int) ((lx + 0x800000) >> 24);
    }

}
