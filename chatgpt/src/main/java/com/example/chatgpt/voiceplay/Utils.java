package com.example.chatgpt.voiceplay;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.example.chatgpt.R;

import java.util.List;
import java.util.Map;

public class Utils {

    public static boolean isEmpty(final Object s) {
        if (s == null) {
            return true;
        }
        if ((s instanceof String) && (((String) s).trim().length() == 0)) {
            return true;
        }
        if (s instanceof Map) {
            return ((Map<?, ?>) s).isEmpty();
        }
        if (s instanceof List) {
            return ((List<?>) s).isEmpty();
        }
        if (s instanceof Object[]) {
            return (((Object[]) s).length == 0);
        }
        return false;
    }

    public static Typeface getRegularFont(Context context) {
        return ResourcesCompat.getFont(context, R.font.roboto_regular);
    }

    public static final boolean IS_TRIAL = false;

    public static void getErrors(final Exception e) {
        if (IS_TRIAL) {
            final String stackTrace = "Pra ::" + Log.getStackTraceString(e);
            System.out.println(stackTrace);
        }
    }
}
