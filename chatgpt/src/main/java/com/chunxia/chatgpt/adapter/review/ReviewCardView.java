package com.chunxia.chatgpt.adapter.review;

import static com.chunxia.chatgpt.tools.Tools.dpToPx;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chunxia.chatgpt.R;

public class ReviewCardView extends LinearLayout {

    private TextView mLTextView;
    private TextView tLTextView;
    private float defaultTargetTextSize;
    private float tLTextSize;
    private float mLTextSize;
    private int topMargin, middleMargin, bottomMargin;
    private String TAG = "ReviewCardView";

    public ReviewCardView(Context context) {
        this(context, null);
    }

    public ReviewCardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReviewCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ReviewCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context, attrs, defStyleAttr);
    }


    public void setmLText(String mLText) {
        mLTextView.setText(mLText);
    }

    public void settLText(String tLText) {
        tLTextView.setText(tLText);
    }


    private void initViews(Context context, AttributeSet attrs, int defStyleAttr) {

        // 读取自定义属性的值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReviewCardView);
        String mLText = typedArray.getString(R.styleable.ReviewCardView_mLText);
        String tLText = typedArray.getString(R.styleable.ReviewCardView_tLText);

        int mLTextStyle = typedArray.getInt(R.styleable.ReviewCardView_mLTextStyle, Typeface.NORMAL);
        int tLTextStyle = typedArray.getInt(R.styleable.ReviewCardView_tLTextStyle, Typeface.NORMAL);
        int mLTextColor = typedArray.getColor(R.styleable.ReviewCardView_mLTextColor, Color.BLACK);
        int tLTextColor = typedArray.getColor(R.styleable.ReviewCardView_tLTextColor, Color.BLACK);
        mLTextSize = typedArray.getDimension(R.styleable.ReviewCardView_mLTextSize, dpToPx(context, 20));
        tLTextSize = typedArray.getDimension(R.styleable.ReviewCardView_tLTextSize, dpToPx(context, 22));
        typedArray.recycle();

        this.setOrientation(LinearLayout.VERTICAL);
        mLTextView = new TextView(context);
        mLTextView.setText(mLText);
        tLTextView = new TextView(context);
        tLTextView.setText(tLText);

        mLTextView.setTypeface(null, mLTextStyle);
        tLTextView.setTypeface(null, tLTextStyle);

        mLTextView.setTextColor(mLTextColor);
        tLTextView.setTextColor(tLTextColor);

        mLTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLTextSize);
        tLTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tLTextSize);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tLTextView.setAutoSizeTextTypeUniformWithConfiguration(15, 24, 2, TypedValue.COMPLEX_UNIT_SP);
        }
        middleMargin = dpToPx(context, 30);

        LayoutParams mLLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(mLTextView, mLLayoutParams);

        LayoutParams tLLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tLLayoutParams.topMargin = middleMargin;
        this.addView(tLTextView, tLLayoutParams);

        defaultTargetTextSize = tLTextView.getTextSize();
    }


    public void setQuizMode() {
        tLTextView.setVisibility(View.GONE);
    }

    public void setAnswerMode() {
        tLTextView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        // todo 动态调整字体大小
//        float h1 = measureTextViewHeight(tLTextView, tLTextView.getTextSize());
//        Log.i(TAG, "h1:" + h1);
//
//        int totalHeight = getMeasuredHeight();
//        float targetHeight = totalHeight * 0.6f;
//
//        Paint mLanguagePaint = mLTextView.getPaint();
//        Paint tLanguagePaint = tLTextView.getPaint();
//
//        float mLTextHeight = mLanguagePaint.descent() - mLanguagePaint.ascent();
//        float tLTextHeight = tLanguagePaint.descent() - tLanguagePaint.ascent();
//
//        float totalTextHeight = mLTextHeight + tLTextHeight + middleMargin;
//
//        if (totalTextHeight > targetHeight) {
//            float scale = targetHeight / totalTextHeight;
//            float newtLTextSize = defaultTargetTextSize * scale;
//            tLTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newtLTextSize);
//        } else {
//            tLTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTargetTextSize);
//        }
//
//        requestLayout();
        // 再次调用super.onMeasure以使用新的文本大小
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float measureTextViewHeight(TextView textView, float textSize) {
        Paint paint = textView.getPaint();
        paint.setTextSize(textSize);//设置字体大小
        float oneLine = paint.descent() - paint.ascent();
        int lineNumber = textView.getLineCount();
        float space = textView.getLineSpacingExtra();
        return oneLine * lineNumber + space * (lineNumber - 1);
    }

    public void setSentence(String s) {
        tLTextView.setText(s);
    }

    public void setTranslation(String s) {
        mLTextView.setText(s);
    }

    public String getSentence() {
        return (String) tLTextView.getText();
    }

    public String getTranslation() {
        return (String) mLTextView.getText();
    }
}
