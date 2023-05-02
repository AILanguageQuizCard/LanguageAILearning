package com.chunxia.chatgpt.mmkv;


import android.app.Application;

import com.chunxia.chatgpt.model.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class CXMMKV {

    private static volatile CXMMKV instance;

    private final MMKV mmkv;

    public CXMMKV(MMKV mmkv) {
        this.mmkv = mmkv;
    }

    public static CXMMKV getInstance() {
        if (instance == null) {
            synchronized (CXMMKV.class) {
                if (instance == null) {
                    instance = new CXMMKV(MMKV.defaultMMKV());
                }
            }
        }
        return instance;
    }

    public MMKV getMMKV() {
        return mmkv;
    }

    public static void init(Application application) {
        MMKV.initialize(application);
    }

    public void saveMessages(String key, ArrayList<Message> messages) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Message.class, new MessageTypeAdapter())
                .create();
        String json = gson.toJson(messages);
        mmkv.encode(key, json);
    }

    public ArrayList<Message> loadMessages(String key) {
        String json = mmkv.decodeString(key, "");
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Message.class, new MessageTypeAdapter())
                .create();
        Type type = new TypeToken<ArrayList<Message>>() {}.getType();
        return gson.fromJson(json, type);
    }

}
