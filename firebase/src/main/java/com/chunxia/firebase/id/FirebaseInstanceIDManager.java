package com.chunxia.firebase.id;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.chunxia.firebase.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseInstanceIDManager {

    private static final String TAG = "FirebaseInstanceIDManage";
    private volatile static FirebaseInstanceIDManager instance;

    FirebaseFirestore db;

    private FirebaseInstanceIDManager() {
        db = FirebaseFirestore.getInstance();
    }

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
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return androidId;
    }


    public void firstInitInDatabase(Context context) {
        Map<String, Object> data = new HashMap<>();
        Long remainingTrail = 10L;
        data.put(User.remainingTrailKey, remainingTrail);
        String androidId = getAndroidId(context);

        db.collection("User").document(androidId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        User myUser = new User();
                        myUser.setAndroidId(androidId);
                        myUser.setRemainingTrail(remainingTrail);
                        setUser(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "add user in firebase failed");
                    }
                });
    }


    public static interface OnGetUserListener {
        void onGetUserSuccess(Map<String, Object> map);

        void onGetUserFailed();

        void onUserNotExist();
    }


    private User user;

    private boolean isUpdated = false;

    public Long getRemainingTrail() {
        if (isUpdated) {
            return user.getRemainingTrail();
        } else {
            return null;
        }
    }


    private final List<OnUpdateListener> listenerList = new ArrayList<>();

    public void addUpdataListener(OnUpdateListener listener) {
        listenerList.add(listener);
    }

    public void removeUpdateListener(OnUpdateListener listener) {
        listenerList.remove(listener);
    }

    public static interface OnUpdateListener {
        void onUpdateSuccess(User user);
    }


    public void checkUserStatusWhenLauncher(Context context) {
        OnGetUserListener listener = new OnGetUserListener() {
            @Override
            public void onGetUserSuccess(Map<String, Object> map) {
                // 记录用户
                setUser(map);
            }

            @Override
            public void onGetUserFailed() {
                Log.i(TAG, "get user from firebase failed");
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
                                // document.getData 返回的是hashMap
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                listener.onGetUserSuccess(document.getData());
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


    private void setUser(Map<String, Object> map) {
        if (user == null) {
            user = User.firebaseMap2User(map);
        } else {
            user = User.firebaseMap2User(user, map);
        }
        isUpdated = true;
        for(OnUpdateListener listener: listenerList) {
            listener.onUpdateSuccess(user);
        }
    }

    private void setUser(User newUser) {
        user = newUser;
        isUpdated = true;
        for(OnUpdateListener listener: listenerList) {
            listener.onUpdateSuccess(user);
        }
    }

    private void setRemainingTrail(Long trail) {
        user.setRemainingTrail(trail);
        isUpdated = true;
        for(OnUpdateListener listener: listenerList) {
            listener.onUpdateSuccess(user);
        }
    }


    public void decreaseRemainingTrail(Context context) {
        OnGetUserListener listener = new OnGetUserListener() {
            @Override
            public void onGetUserSuccess(Map<String, Object> map) {
                // 记录用户
                setUser(map);
                Long nextTrail = user.getRemainingTrail() - 1;
                decreaseRemainingTrailHelper(context, nextTrail);
            }

            @Override
            public void onGetUserFailed() {
                Log.i(TAG, "get user from firebase failed");
            }

            @Override
            public void onUserNotExist() {
            }
        };
        getUser(context, listener);
    }


    public void decreaseRemainingTrailHelper(Context context, Long trail) {
        db.collection("User").document(getAndroidId(context))
                .update(User.remainingTrailKey, trail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        setRemainingTrail(trail);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

}