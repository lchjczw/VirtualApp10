package io.virtualapp.widgets.fittext;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import io.virtualapp.R;
public class FitTextView extends BaseTextView {
    private boolean mMeasured = false;
    private boolean mNeedFit = true;
    protected float mOriginalTextSize = 0;
    private float mMinTextSize, mMaxTextSize;
    protected CharSequence mOriginalText;
    protected volatile boolean mFittingText = false;
    protected FitTextHelper mFitTextHelper;
    public FitTextView(Context context) {
        this(context, null);
    }
    public FitTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public FitTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mOriginalTextSize = getTextSize();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, new int[]{
                    R.attr.ftMaxTextSize,
                    R.attr.ftMinTextSize,
                    });
            mMaxTextSize = a.getDimension(0, mOriginalTextSize * 2.0f);
            mMinTextSize = a.getDimension(1, mOriginalTextSize / 2.0f);
            a.recycle();
        } else {
            mMinTextSize = mOriginalTextSize;
            mMaxTextSize = mOriginalTextSize;
        }
    }
    protected FitTextHelper getFitTextHelper() {
        if (mFitTextHelper == null) {
            mFitTextHelper = new FitTextHelper(this);
        }
        return mFitTextHelper;
    }
    public float getMinTextSize() {
        return mMinTextSize;
    }
    public void setMinTextSize(float minTextSize) {
        mMinTextSize = minTextSize;
    }
    public float getMaxTextSize() {
        return mMaxTextSize;
    }
    public void setMaxTextSize(float maxTextSize) {
        mMaxTextSize = maxTextSize;
    }
    public boolean isNeedFit() {
        return mNeedFit;
    }
    public void setNeedFit(boolean needFit) {
        mNeedFit = needFit;
    }
    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        mOriginalTextSize = getTextSize();
    }
    public float getOriginalTextSize() {
        return mOriginalTextSize;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == View.MeasureSpec.UNSPECIFIED
                && heightMode == View.MeasureSpec.UNSPECIFIED) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, mOriginalTextSize);
            mMeasured = false;
        } else {
            mMeasured = true;
            fitText(getOriginalText());
        }
    }
    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        mOriginalText = text;
        super.setText(text, type);
        fitText(text);
    }
    public CharSequence getOriginalText() {
        return mOriginalText;
    }
    protected void fitText(CharSequence text) {
        if (!mNeedFit) {
            return;
        }
        if (!mMeasured || mFittingText || mSingleLine || TextUtils.isEmpty(text))
            return;
        mFittingText = true;
        TextPaint oldPaint = getPaint();
        float size = getFitTextHelper().fitTextSize(oldPaint, text, mMaxTextSize, mMinTextSize);
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        super.setText(getFitTextHelper().getLineBreaks(text, getPaint()));
        mFittingText = false;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}