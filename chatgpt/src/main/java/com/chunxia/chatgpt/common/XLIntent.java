package com.chunxia.chatgpt.common;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;


public class XLIntent extends Intent {

    public XLIntent(Context context, Class<?> clazz) {
        super(context, clazz);
    }

    public XLIntent putString(String key, String s) {
        this.putExtra(key, s);
        return this;
    }

    public XLIntent putInt(String key, int s) {
        this.putExtra(key, s);
        return this;
    }

    public XLIntent putBoolean(String key, boolean s) {
        this.putExtra(key, s);
        return this;
    }

    public XLIntent putStringArrayList(String key, ArrayList<String> list) {
        this.putExtra(key, (Serializable) list);
        return this;
    }


}
