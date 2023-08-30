package com.chunxia.chatgpt.subscription;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.chunxia.firebase.config.RemoteConfig;
import com.limurse.iap.DataWrappers;
import com.limurse.iap.IapConnector;
import com.limurse.iap.SubscriptionServiceListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    public void unsubscribe(Activity activity, String sku) {
        iapConnector.unsubscribe(activity, sku);
    }

    public static final String SKU_ID_MONTHLY = "ai_lingo_master_test_monthly";
    public static final String SKU_ID_SEASONLY = "ai_lingo_master_test_seasonly";
    public static final String SKU_ID_YEARLY = "ai_lingo_master_test_yearly";


    public void addValidSubscription(String s) {
        if (s == null || s.isEmpty()) {
            return;
        }
        SubscriptionInfoProvider.getInstance().addValidSubscription(s);
    }

    private void addProductsInfo(Map<String, DataWrappers.ProductDetails> iapKeyPrices) {
        SubscriptionInfoProvider.getInstance().setSPI(iapKeyPrices);
    }


    private String getKey() {
        return RemoteConfig.getInstance().getSubscriptionKey();
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
                getKey(),
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
                addProductsInfo(iapKeyPrices);
            }
        };
        iapConnector.addSubscriptionListener(listener);

        iapConnector.addBillingClientConnectionListener((status, billingResponseCode) -> {
            Log.d("KSA", "This is the status: "+status+" and response code is: "+billingResponseCode);
            isBillingClientConnected.setValue(status);
        });
    }

}
