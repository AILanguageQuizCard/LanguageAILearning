package com.chunxia.chatgpt.mmkv;

import com.chunxia.chatgpt.model.message.Message;
import com.chunxia.chatgpt.model.message.TextMessage;
import com.chunxia.chatgpt.model.message.VoiceMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class MessageTypeAdapter extends TypeAdapter<Message> {
    @Override
    public void write(JsonWriter out, Message value) throws IOException {
        Gson gson = new Gson();
        gson.toJson(value, value.getClass(), out);
    }

    @Override
    public Message read(JsonReader in) throws IOException {
        JsonObject jsonObject = new JsonParser().parse(in).getAsJsonObject();
        String messageType = jsonObject.get("messageType").getAsString();

        Gson gson = new Gson();
        if (messageType.equals("TextMessage")) {
            return gson.fromJson(jsonObject, TextMessage.class);
        } else if (messageType.equals("VoiceMessage")) {
            return gson.fromJson(jsonObject, VoiceMessage.class);
        } else {
            throw new IllegalArgumentException("Unknown message type: " + messageType);
        }
    }
}
