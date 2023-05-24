package com.chunxia.chatgpt.adapter.review;

import static com.chunxia.chatgpt.tools.Tools.dpToPx;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chunxia.chatgpt.R;

public class ReviewCardView extends LinearLayout {

    private TextView mLTextView;
    private TextView tLTextView;
    private float defaultTargetTextSize;
    private int topMargin, middleMargin, bottomMargin;

    public ReviewCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 读取自定义属性的值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ReviewCardView);
        String mLText = typedArray.getString(R.styleable.ReviewCardView_mLText);
        String tLText = typedArray.getString(R.styleable.ReviewCardView_tLText);

        int mLTextStyle = typedArray.getInt(R.styleable.ReviewCardView_mLTextStyle, Typeface.NORMAL);
        int tLTextStyle = typedArray.getInt(R.styleable.ReviewCardView_tLTextStyle, Typeface.NORMAL);
        int mLTextColor = typedArray.getColor(R.styleable.ReviewCardView_mLTextColor, Color.BLACK);
        int tLTextColor = typedArray.getColor(R.styleable.ReviewCardView_tLTextColor, Color.BLACK);
        float mLTextSize = typedArray.getDimension(R.styleable.ReviewCardView_mLTextSize, dpToPx(context, 10));
        float tLTextSize = typedArray.getDimension(R.styleable.ReviewCardView_tLTextSize, dpToPx(context, 13));
        typedArray.recycle();

        // 初始化视图，如创建中英文TextView等
        initViews(mLText, mLTextStyle, mLTextColor, mLTextSize, tLText, tLTextStyle, tLTextColor, tLTextSize);
    }

    private void initViews(String mLText, int mLTextStyle, int mLTextColor, float mLTextSize, String tLText, int tLTextStyle, int tLTextColor, float tLTextSize) {
        this.setOrientation(LinearLayout.VERTICAL);
        Context context = getContext();
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int totalHeight = getMeasuredHeight();
        float targetHeight = totalHeight * 0.6f;

        Paint mLanguagePaint = mLTextView.getPaint();
        Paint tLanguagePaint = tLTextView.getPaint();

        float mLTextHeight = mLanguagePaint.descent() - mLanguagePaint.ascent();
        float tLTextHeight = tLanguagePaint.descent() - tLanguagePaint.ascent();

        float totalTextHeight = mLTextHeight + tLTextHeight + middleMargin;

        if (totalTextHeight > targetHeight) {
            float scale = targetHeight / totalTextHeight;
            float newtLTextSize = defaultTargetTextSize * scale;
            tLTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newtLTextSize);
        } else {
            tLTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTargetTextSize);
        }

        requestLayout();
        // 再次调用super.onMeasure以使用新的文本大小
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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