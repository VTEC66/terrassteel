package com.vtec.terrassteel.common.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Locale;


public enum FontUtil {

    //Font types in the same order of FontTextView.fontType attrs enum
    //SF-Pro-Text
    SF_PRO_BOLD("fonts/SF-Pro-Text-Bold.otf"),
    SF_PRO_BOLD_ITALIC("fonts/SF-Pro-Text-BoldItalic.otf"),
    SF_PRO_EXTRA_BOLD("fonts/SF-Pro-Text-ExtraBold.otf"),
    SF_PRO_EXTRA_BOLD_ITALIC("fonts/SF-Pro-Text-ExtraBoldItalic.otf"),
    SF_PRO_ITALIC("fonts/SF-Pro-Text-Italic.otf"),
    SF_PRO_LIGHT("fonts/SF-Pro-Text-Light.otf"),
    SF_PRO_LIGHT_ITALIC("fonts/SF-Pro-Text-LightItalic.otf"),
    SF_PRO_REGULAR("fonts/SF-Pro-Text-Regular.otf"),
    SF_PRO_SEMI_BOLD("fonts/SF-Pro-Text-SemiBold.otf"),
    SF_PRO_SEMI_BOLD_ITALIC("fonts/SF-Pro-Text-SemiBoldItalic.otf");

    private final String fileName;

    FontUtil(String fileName) {
        this.fileName = fileName;
    }

    public static FontUtil fromString(String fontName) {
        return FontUtil.valueOf(fontName.toUpperCase(Locale.getDefault()));
    }

    public Typeface asTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), fileName);
    }

}