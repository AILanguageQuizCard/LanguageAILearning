package com.chunxia.chatgpt.model.message;

import com.chunxia.chatgpt.mmkv.CXMMKV;
import com.chunxia.chatgpt.mmkv.MessageTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MessageManager {

    private static volatile MessageManager instance;

    public static MessageManager getInstance() {
        if (instance == null) {
            synchronized (MessageManager.class) {
                if (instance == null) {
                    instance = new MessageManager();
                }
            }
        }
        return instance;
    }



    private MessageManager() {
    }


    public void saveMessages(String key, ArrayList<Message> messages) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Message.class, new MessageTypeAdapter())
                .create();
        String json = gson.toJson(messages);
        CXMMKV.getInstance().getMMKV().encode(key, json);
    }

    public ArrayList<Message> loadMessages(String key) {
        String json = CXMMKV.getInstance().getMMKV().decodeString(key, "");
        if (json!= null && json.isEmpty()) {
            return new ArrayList<>();
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Message.class, new MessageTypeAdapter())
                .create();
        Type type = new TypeToken<ArrayList<Message>>() {}.getType();
        return gson.fromJson(json, type);
    }


}
