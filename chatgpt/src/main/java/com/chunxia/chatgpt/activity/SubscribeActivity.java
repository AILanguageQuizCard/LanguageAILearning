package com.chunxia.chatgpt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.subscription.SubscriptionUtils;
import com.limurse.iap.DataWrappers;
import com.limurse.iap.SubscriptionServiceListener;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SubscribeActivity extends AppCompatActivity {

    private Button subscribeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        initView();
    }


    private void initView() {
        subscribeButton = findViewById(R.id.subscribe_button);
        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe();
            }
        });
    }

    private void subscribe() {

        SubscriptionUtils.getInstance().addSubscriptionListener(new SubscriptionServiceListener() {
            public void onSubscriptionRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
            }

            public void onSubscriptionPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
//                if (purchaseInfo.getSku().equals("subscription")) { }
                // todo 订阅成功之后，给予用于VIP权限
                // todo 如果在google cloud console变更了价格，客户端需要做什么变更么
            }

            public void onPricesUpdated(@NotNull Map iapKeyPrices) {

            }
        });

        SubscriptionUtils.getInstance().subscribe(SubscribeActivity.this);
    }
}
