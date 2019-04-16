package com.skypaps.clover.common.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.skypaps.clover.common.utils.FontUtil;

import androidx.appcompat.widget.AppCompatButton;

public class FontButton extends AppCompatButton {

    public FontButton(Context context) {
        super(context);
        init(context);
    }

    public FontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface typeface = FontUtil.SF_PRO_BOLD.asTypeface(context);

        if (typeface != null) {
            setTypeface(typeface);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }
}
