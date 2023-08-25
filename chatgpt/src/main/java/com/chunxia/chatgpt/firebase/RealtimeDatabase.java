package com.chunxia.chatgpt.firebase;


import android.util.Log;

import androidx.annotation.NonNull;

import com.chunxia.chatgpt.chatapi.PublicMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RealtimeDatabase {

    private static volatile RealtimeDatabase instance;

    public static RealtimeDatabase getInstance() {
        if (instance == null) {
            synchronized (RealtimeDatabase.class) {
                if (instance == null) {
                    instance = new RealtimeDatabase();
                }
            }
        }
        return instance;
    }

    private boolean isInit = false;

    public boolean isInit() {
        return isInit;
    }

    private RealtimeDatabase() {
    }

    public void getChatGptApiKeyWhenLaunch() {
        onRealtimeDatabaseListener listener = new onRealtimeDatabaseListener() {
            @Override
            public void onDataChange(String apiKey) {
                PublicMethod.setApiKey(apiKey);
                isInit = true;
            }
        };
        getChatGptApiKey(listener);
    }

    public void getChatGptApiKeyOnceWhenLaunch() {
        onRealtimeDatabaseListener listener = new onRealtimeDatabaseListener() {
            @Override
            public void onDataChange(String apiKey) {
                PublicMethod.setApiKey(apiKey);
                isInit = true;
            }
        };
        getChatGptApiKeyOnce(listener);
    }



    public static interface onRealtimeDatabaseListener {
        public void onDataChange(String apiKey);
    }

    public void getChatGptApiKey(onRealtimeDatabaseListener listener) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chat_gpt_api_key");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                listener.onDataChange(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getChatGptApiKeyOnce(onRealtimeDatabaseListener listener) {

        // Write a message to the database

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance("https://chatgptprototype-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mDatabase.child("chat_gpt_api_key").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    listener.onDataChange(String.valueOf(task.getResult().getValue()));
                }
            }
        });

    }


}
