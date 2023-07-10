package com.chunxia.chatgpt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.subscription.SubscriptionManager;
import com.chunxia.chatgpt.ui.SubscriptionDescriptionView;
import com.chunxia.chatgpt.ui.SubscriptionOptionView;
import com.limurse.iap.DataWrappers;
import com.limurse.iap.SubscriptionServiceListener;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SubscribeActivity extends AppCompatActivity {

    private static final String TAG = "SubscribeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        initView();
        initSubscriptionInfo();
    }

    private SubscriptionOptionView subcriptionView1;
    private SubscriptionOptionView subcriptionView2;
    private SubscriptionOptionView subcriptionView3;

    private SubscriptionDescriptionView descriptionView1;

    private SubscriptionDescriptionView descriptionView2;

    private SubscriptionDescriptionView descriptionView3;

    private SubscriptionDescriptionView descriptionView4;

    private ImageView closeView;

    private TextView subscriptionButton;

    private void initView() {
        initCloseButton();
        initSubscriptionOptionButton();
        setDescriptionView();
        initSubscriptionButton();
    }

    private void initSubscriptionInfo() {
        SubscriptionServiceListener listener = new SubscriptionServiceListener() {
            public void onSubscriptionRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                SubscriptionManager.getInstance().addValidSubscription(purchaseInfo.getSku());
            }

            public void onSubscriptionPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                SubscriptionManager.getInstance().addValidSubscription(purchaseInfo.getSku());
            }

            public void onPricesUpdated(@NotNull Map<String, DataWrappers.ProductDetails> iapKeyPrices) {
                // for 循环遍历map，需要遍历key和value
                DataWrappers.ProductDetails monthlyDetails = iapKeyPrices.get(SubscriptionManager.SKU_ID_MONTHLY);
                if(monthlyDetails != null) {
                    subcriptionView1.setPrice(monthlyDetails.getPrice());
                }
                DataWrappers.ProductDetails seasonlyDetail = iapKeyPrices.get(SubscriptionManager.SKU_ID_SEASONLY);
                if(seasonlyDetail != null) {
                    subcriptionView2.setPrice(seasonlyDetail.getPrice());
                }
                DataWrappers.ProductDetails yearlyDetail = iapKeyPrices.get(SubscriptionManager.SKU_ID_YEARLY);
                if(yearlyDetail != null) {
                    subcriptionView3.setPrice(yearlyDetail.getPrice());
                }

            }
        };

        SubscriptionManager.getInstance().addSubscriptionListener(listener);
    }

    private void setDescriptionView() {
        descriptionView1 = findViewById(R.id.subscription_description_view1);
        descriptionView2 = findViewById(R.id.subscription_description_view2);
        descriptionView3 = findViewById(R.id.subscription_description_view3);
        descriptionView4 = findViewById(R.id.subscription_description_view4);

        descriptionView1.setAll(R.drawable.subscription_description1, getString(R.string.subscription_description_title1), getString(R.string.subscription_description_detail1));
        descriptionView2.setAll(R.drawable.subscription_description2, getString(R.string.subscription_description_title2), getString(R.string.subscription_description_detail2));
        descriptionView3.setAll(R.drawable.subscription_description3, getString(R.string.subscription_description_title3), getString(R.string.subscription_description_detail3));
        descriptionView4.setAll(R.drawable.subscription_description4, getString(R.string.subscription_description_title4), getString(R.string.subscription_description_detail4));
    }

    private void initSubscriptionOptionButton() {
        subcriptionView1 = findViewById(R.id.subscription_option1);
        subcriptionView2 = findViewById(R.id.subscription_option2);
        subcriptionView3 = findViewById(R.id.subscription_option3);
        // todo 验证不同地区的价格，是否正确
        subcriptionView1.setTitle(getString(R.string.subscription_option_title1));
        subcriptionView2.setTitle(getString(R.string.subscription_option_title2));
        subcriptionView3.setTitle(getString(R.string.subscription_option_title3));

        subcriptionView1.choose();
        onSubscriptionOptionClick();
    }

    private void initCloseButton() {
        closeView = findViewById(R.id.subscribe_close_imageview);
        closeView.setOnClickListener(v -> finish());
    }

    private void initSubscriptionButton() {
        subscriptionButton = findViewById(R.id.subscribe_button);
        subscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo 根据不同的选中的方案，来订阅不同的套餐
                subscribe(choosedSkuID);
            }
        });
    }

    private String choosedSkuID = SubscriptionManager.SKU_ID_MONTHLY;

    private void onSubscriptionOptionClick() {
        subcriptionView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosedSkuID = SubscriptionManager.SKU_ID_MONTHLY;
                subcriptionView1.choose();
                subcriptionView2.unchoose();
                subcriptionView3.unchoose();
            }
        });

        subcriptionView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosedSkuID = SubscriptionManager.SKU_ID_SEASONLY;
                subcriptionView2.choose();
                subcriptionView1.unchoose();
                subcriptionView3.unchoose();
            }
        });

        subcriptionView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosedSkuID = SubscriptionManager.SKU_ID_YEARLY;
                subcriptionView3.choose();
                subcriptionView1.unchoose();
                subcriptionView2.unchoose();
            }
        });
    }

    private void subscribe(String sku) {
        SubscriptionManager.getInstance().subscribe(SubscribeActivity.this, sku);
    }
}
