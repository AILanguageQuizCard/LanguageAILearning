package com.chunxia.chatgpt.subscription;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.limurse.iap.IapConnector;
import com.limurse.iap.PurchaseServiceListener;
import com.limurse.iap.SubscriptionServiceListener;

import java.util.Collections;
import java.util.List;
public class SubscriptionUtils {

    private volatile static SubscriptionUtils instance;

    private SubscriptionUtils() {}

    public static SubscriptionUtils getInstance() {
        if (instance == null) {
            synchronized (SubscriptionUtils.class) {
                if (instance == null) {
                    instance = new SubscriptionUtils();
                }
            }
        }
        return instance;
    }

    private final MutableLiveData<Boolean> isBillingClientConnected  = new MutableLiveData<>();
    private IapConnector iapConnector = null;

    public void subscribe(Activity activity) {
        iapConnector.subscribe(activity, "ai_lingo_master_test_monthly");
    }

    public void addPurchaseListener(PurchaseServiceListener listener) {
        iapConnector.addPurchaseListener(listener);

//        iapConnector.addPurchaseListener(new PurchaseServiceListener() {
//            public void onPricesUpdated(@NotNull Map iapKeyPrices) {
//
//            }
//
//            public void onProductPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
//
//            }
//
//            public void onProductRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
//
//            }
//        });
    }

    public void addSubscriptionListener(SubscriptionServiceListener listener) {
        iapConnector.addSubscriptionListener(listener);
    }

    public void initSubscribe(Context context) {
        isBillingClientConnected.setValue(false);

        List<String> subsList = Collections.singletonList("ai_lingo_master_test_monthly");

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


        iapConnector.addBillingClientConnectionListener((status, billingResponseCode) -> {
            Log.d("KSA", "This is the status: "+status+" and response code is: "+billingResponseCode);
            isBillingClientConnected.setValue(status);
        });
    }

}
