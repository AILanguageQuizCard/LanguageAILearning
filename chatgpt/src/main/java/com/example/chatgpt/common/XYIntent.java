package com.example.chatgpt.common;

import android.content.Context;
import android.content.Intent;


public class XYIntent extends Intent {

    public XYIntent(Context context, Class<?> clazz) {
        super(context, clazz);
    }

    public XYIntent putString(String key, String s) {
        this.putExtra(key, s);
        return this;
    }

    public XYIntent putInt(String key, int s) {
        this.putExtra(key, s);
        return this;
    }

    public XYIntent putBoolean(String key, boolean s) {
        this.putExtra(key, s);
        return this;
    }


}
