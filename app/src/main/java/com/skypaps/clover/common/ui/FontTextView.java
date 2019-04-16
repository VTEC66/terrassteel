package com.skypaps.clover.common.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.skypaps.clover.R;
import com.skypaps.clover.common.utils.FontUtil;

import androidx.appcompat.widget.AppCompatTextView;

public class FontTextView extends AppCompatTextView {

    private Context context;

    public FontTextView(Context context) {
        super(context);
        this.context = context;
    }

    public FontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
        Typeface typeface = FontUtil.SF_PRO_REGULAR.asTypeface(context);

        if (typedArray != null) {
            FontUtil fontType = FontUtil.values()[typedArray.getInteger(R.styleable.FontTextView_fontType, 0)];

            if (!TextUtils.isEmpty(fontType.name())) {
                typeface = fontType.asTypeface(context);
            }

            typedArray.recycle();
        }

        if (typeface != null) {
            setTypeface(typeface);
        }
    }


    public void setFont(FontUtil font){
        if (!TextUtils.isEmpty(font.name())) {
            Typeface typeface = font.asTypeface(context);

            setTypeface(typeface);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}