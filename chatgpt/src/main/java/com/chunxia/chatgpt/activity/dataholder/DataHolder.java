package com.chunxia.chatgpt.activity.dataholder;


import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class DataHolder {

    private final Map<String, WeakReference<Object>> dataList = new HashMap<>();

    private static volatile DataHolder instance;

    public static DataHolder getInstance() {

        if (instance == null) {

            synchronized (DataHolder.class) {

                if (instance == null) {

                    instance = new DataHolder();

                }

            }

        }

        return instance;

    }

    public void setData(String key, Object o) {

        WeakReference<Object> value = new WeakReference<>(o);

        dataList.put(key, value);

    }

    public Object getData(String key) {

        WeakReference<Object> reference = dataList.get(key);

        if (reference != null) {

            Object o = reference.get();

            return o;

        }

        return null;

    }

}