package com.chunxia.firebase.id;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseInstanceIDManager {

    private static final String TAG = "FirebaseInstanceIDManage";
    private volatile static FirebaseInstanceIDManager instance;

    private FirebaseInstanceIDManager() {}

    public static FirebaseInstanceIDManager getInstance() {
        if (instance == null) {
            synchronized (FirebaseInstanceIDManager.class) {
                if (instance == null) {
                    instance = new FirebaseInstanceIDManager();
                }
            }
        }
        return instance;
    }

    String androidId;
    public String getAndroidId(Context context) {
        if (androidId == null) {
            // todo 这是一个耗时请求么？
            androidId =  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return androidId;
    }


    public void firstInitInDatabase(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("remaining_trail", 10);

        db.collection("User").document(getAndroidId(context))
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


    public static interface OnGetUserListener {
        void onGetUserSuccess();
        void onGetUserFailed();
        void onUserNotExist();
    }


    public void checkUserStatusWhenLauncher(Context context) {
        OnGetUserListener listener = new OnGetUserListener() {
            @Override
            public void onGetUserSuccess() {
                // 记录用户
            }

            @Override
            public void onGetUserFailed() {

            }

            @Override
            public void onUserNotExist() {
                // 创建用户
                firstInitInDatabase(context);
            }
        };
        getUser(context, listener);
    }

    public void getUser(Context context, OnGetUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User").document(getAndroidId(context))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                listener.onGetUserSuccess();
                            } else {
                                listener.onUserNotExist();
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            listener.onGetUserFailed();
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }

    public void decreaseRemainingTrail(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("User").document(getAndroidId(context))
                .update("remaining_trail", 10)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

}