package com.chunxia.chatgpt.subscription;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.limurse.iap.DataWrappers;
import com.limurse.iap.IapConnector;
import com.limurse.iap.PurchaseServiceListener;
import com.limurse.iap.SubscriptionServiceListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubscriptionManager {

    private static final String TAG = "SubscriptionUtils";
    private volatile static SubscriptionManager instance;

    private SubscriptionManager() {}

    public static SubscriptionManager getInstance() {
        if (instance == null) {
            synchronized (SubscriptionManager.class) {
                if (instance == null) {
                    instance = new SubscriptionManager();
                }
            }
        }
        return instance;
    }

    private final MutableLiveData<Boolean> isBillingClientConnected  = new MutableLiveData<>();
    private IapConnector iapConnector = null;

    public void subscribe(Activity activity, String sku) {
        iapConnector.subscribe(activity, sku);
    }

    public void addPurchaseListener(PurchaseServiceListener listener) {
        iapConnector.addPurchaseListener(listener);
    }

    public static final String SKU_ID_MONTHLY = "ai_lingo_master_test_monthly";
    public static final String SKU_ID_SEASONLY = "ai_lingo_master_test_seasonly";

    public static final String SKU_ID_YEARLY = "ai_lingo_master_test_yearly";


    private Set<String> validSkus = new HashSet<>();

    private List<SubscriptionUpdateListener> listenerList = new ArrayList<>();

    public void registerSubscriptionListener(SubscriptionUpdateListener subscriptionUpdateListener) {
        listenerList.add(subscriptionUpdateListener);
    }

    public void unregisterSubscriptionListener(SubscriptionUpdateListener subscriptionUpdateListener) {
        listenerList.remove(subscriptionUpdateListener);
    }

    public interface SubscriptionUpdateListener {
        void onUpdatedSubscription(String sku);
    }


    public void addValidSubscription(String s) {
        if (s == null || s.isEmpty()) {
            return;
        }
        Log.i(TAG, "addValidSubscription: " + s);
        validSkus.add(s);

        for (SubscriptionUpdateListener listener : listenerList) {
            listener.onUpdatedSubscription(s);
        }
    }

    public boolean isValidSubscription(String s) {
        return validSkus.contains(s);
    }

    public boolean isSubscribed() {
        Log.i(TAG, "isSubscribed, validSkus: " + validSkus.toString());
        return !validSkus.isEmpty();
    }


    public void addSubscriptionListener(SubscriptionServiceListener listener) {
        iapConnector.addSubscriptionListener(listener);
    }

    public void initSubscribe(Context context) {
        isBillingClientConnected.setValue(false);

        List<String> subsList = new ArrayList<>();
        subsList.add(SKU_ID_MONTHLY);
        subsList.add(SKU_ID_SEASONLY);
        subsList.add(SKU_ID_YEARLY);

        iapConnector = new IapConnector(
                context,
                Collections.emptyList(),
                Collections.emptyList(),
                subsList,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoDzmho/JeA63fyUxZXR9ZyC" +
                        "p+UpQnceEpQO6aEFbN9fJjZZNR0+PjYOuPjg1MA6yB43lH1FUOvYgwt1ED71SQaqE1a" +
                        "3VEOxtR4BSl5IEFh5EjeTFXkIY20xx23Fftk5ia21p3St5BD3VA+kKF/5wfJffx9zcD" +
                        "0dPj8fsdTc9RR/0Gf8d+h7BZUmsJy+odkVHNjXcaEwvoPUPKGkXksNvImQrGaiBpA7j" +
                        "4K3L/mygkOb77hJ6q7pQgiFApw4zafoQvoT9fFg8cRm7Ny+8n8KweQnYYP0y8CFeJ+B" +
                        "How3AgeWYP3h4+JpfP7N2M1UKXZBtBt23XHyp5r0iQBH3RQoGpwIDAQAB",
                true
        );

        SubscriptionServiceListener listener = new SubscriptionServiceListener() {
            public void onSubscriptionRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                addValidSubscription(purchaseInfo.getSku());
            }

            public void onSubscriptionPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                addValidSubscription(purchaseInfo.getSku());
            }

            public void onPricesUpdated(@NotNull Map<String, DataWrappers.ProductDetails> iapKeyPrices) {
            }
        };
        addSubscriptionListener(listener);

        iapConnector.addBillingClientConnectionListener((status, billingResponseCode) -> {
            Log.d("KSA", "This is the status: "+status+" and response code is: "+billingResponseCode);
            isBillingClientConnected.setValue(status);
        });
    }

}
