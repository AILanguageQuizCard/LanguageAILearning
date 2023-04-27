package com.chunxia.chatgpt.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.chunxia.chatgpt.R;
import com.limurse.iap.DataWrappers;
import com.limurse.iap.IapConnector;
import com.limurse.iap.PurchaseServiceListener;
import com.limurse.iap.SubscriptionServiceListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SubscriptionActivity extends AppCompatActivity {
    MutableLiveData<Boolean> isBillingClientConnected  = new MutableLiveData<>();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subscription_sample);

        isBillingClientConnected.setValue(false);

        List<String> subsList = Collections.singletonList("ai_lingo_master_test_monthly");

        IapConnector iapConnector = new IapConnector(
                this,
                Collections.emptyList(),
                Collections.emptyList(),
                subsList,
                "LICENSE KEY",
                true
        );


        iapConnector.addBillingClientConnectionListener((status, billingResponseCode) -> {
            Log.d("KSA", "This is the status: "+status+" and response code is: "+billingResponseCode);
            isBillingClientConnected.setValue(status);
        });

        iapConnector.addPurchaseListener(new PurchaseServiceListener() {
            public void onPricesUpdated(@NotNull Map iapKeyPrices) {

            }

            public void onProductPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                if (purchaseInfo.getSku().equals("plenty")) {

                } else if (purchaseInfo.getSku().equals("yearly")) {

                } else if (purchaseInfo.getSku().equals("moderate")) {

                } else if (purchaseInfo.getSku().equals("base")) {

                } else if (purchaseInfo.getSku().equals("quite")) {

                }
            }

            public void onProductRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {

            }
        });
        iapConnector.addSubscriptionListener(new SubscriptionServiceListener() {
            public void onSubscriptionRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
            }

            public void onSubscriptionPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                if (purchaseInfo.getSku().equals("subscription")) {

                }
            }

            public void onPricesUpdated(@NotNull Map iapKeyPrices) {

            }
        });

        ImageView btPurchaseCons = findViewById(R.id.btPurchaseCons);
        btPurchaseCons.setOnClickListener(it ->
                iapConnector.purchase(this, "base")
        );

        ImageView btnMonthly = findViewById(R.id.btnMonthly);
        btnMonthly.setOnClickListener(it ->
                iapConnector.subscribe(this, "ai_lingo_master_test_monthly")
        );

        ImageView btnYearly = findViewById(R.id.btnYearly);
        btnYearly.setOnClickListener(it ->
                iapConnector.purchase(this, "yearly")
        );

        ImageView btnQuite = findViewById(R.id.btnQuite);
        btnQuite.setOnClickListener(it ->
                iapConnector.purchase(this, "quite")
        );

        ImageView btnModerate = findViewById(R.id.btnModerate);
        btnModerate.setOnClickListener(it ->
                iapConnector.purchase(this, "moderate")
        );

        ImageView btnUltimate = findViewById(R.id.btnUltimate);
        btnUltimate.setOnClickListener(it ->
                iapConnector.purchase(this, "plenty")
        );
    }
}
