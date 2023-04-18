package com.example.chatgpt.voiceplay;

import static android.os.Build.VERSION.SDK_INT;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;


public class MyDrawableCompat {
    public static void setColorFilter(@NonNull Drawable drawable, @ColorInt int color) {
        if (SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_IN));
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }
}