package com.vtec.terrassteel.camera;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.RestrictTo;


public class CameraCommon {

    public static final int FACING_BACK = 0;
    public static final int FACING_FRONT = 1;
    public static final int FLASH_OFF = 0;
    public static final int FLASH_ON = 1;
    public static final int FLASH_AUTO = 2;
    public static final int FLASH_TORCH = 3;
    public static final int FOCUS_OFF = 0;
    public static final int FOCUS_AUTO = 1;
    public static final int FOCUS_CONTINUOUS = 2;
    public static final int SENSOR_PRESET_NONE = 0;
    public static final int SENSOR_PRESET_ACTION = 1;
    public static final int SENSOR_PRESET_PORTRAIT = 2;
    public static final int SENSOR_PRESET_LANDSCAPE = 3;
    public static final int SENSOR_PRESET_NIGHT = 4;
    public static final int SENSOR_PRESET_NIGHT_PORTRAIT = 5;
    public static final int SENSOR_PRESET_THEATRE = 6;
    public static final int SENSOR_PRESET_BEACH = 7;
    public static final int SENSOR_PRESET_SNOW = 8;
    public static final int SENSOR_PRESET_SUNSET = 9;
    public static final int SENSOR_PRESET_STEADYPHOTO = 10;
    public static final int SENSOR_PRESET_FIREWORKS = 11;
    public static final int SENSOR_PRESET_SPORTS = 12;
    public static final int SENSOR_PRESET_PARTY = 13;
    public static final int SENSOR_PRESET_CANDLELIGHT = 14;
    public static final int SENSOR_PRESET_BARCODE = 15;
    public static final int PREVIEW_EFFECT_NONE = 0;
    public static final int PREVIEW_EFFECT_MONO = 1;
    public static final int PREVIEW_EFFECT_NEGATIVE = 2;
    public static final int PREVIEW_EFFECT_SOLARIZE = 3;
    public static final int PREVIEW_EFFECT_SEPIA = 4;
    public static final int PREVIEW_EFFECT_POSTERIZE = 5;
    public static final int PREVIEW_EFFECT_WHITEBOARD = 6;
    public static final int PREVIEW_EFFECT_BLACKBOARD = 7;
    public static final int PREVIEW_EFFECT_AQUA = 8;

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FACING_BACK, FACING_FRONT})
    @interface Facing {
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FLASH_OFF, FLASH_ON, FLASH_AUTO, FLASH_TORCH})
    @interface Flash {
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FOCUS_OFF, FOCUS_AUTO, FOCUS_CONTINUOUS})
    @interface Focus {
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SENSOR_PRESET_NONE, SENSOR_PRESET_ACTION, SENSOR_PRESET_PORTRAIT,
            SENSOR_PRESET_LANDSCAPE, SENSOR_PRESET_NIGHT, SENSOR_PRESET_NIGHT_PORTRAIT,
            SENSOR_PRESET_THEATRE, SENSOR_PRESET_BEACH, SENSOR_PRESET_SNOW, SENSOR_PRESET_SUNSET,
            SENSOR_PRESET_STEADYPHOTO, SENSOR_PRESET_FIREWORKS, SENSOR_PRESET_SPORTS,
            SENSOR_PRESET_PARTY, SENSOR_PRESET_CANDLELIGHT, SENSOR_PRESET_BARCODE})
    @interface SensorPreset {
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PREVIEW_EFFECT_NONE, PREVIEW_EFFECT_MONO, PREVIEW_EFFECT_NEGATIVE,
            PREVIEW_EFFECT_SOLARIZE, PREVIEW_EFFECT_SEPIA, PREVIEW_EFFECT_POSTERIZE,
            PREVIEW_EFFECT_WHITEBOARD, PREVIEW_EFFECT_BLACKBOARD, PREVIEW_EFFECT_AQUA})
    @interface PreviewEffect {
    }

}
