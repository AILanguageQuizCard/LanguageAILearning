package com.chunxia.chatgpt.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.subscription.SubscriptionUtils;
import com.chunxia.chatgpt.ui.SubscriptionDescriptionView;
import com.chunxia.chatgpt.ui.SubscriptionOptionView;
import com.limurse.iap.DataWrappers;
import com.limurse.iap.SubscriptionServiceListener;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SubscribeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        initView();
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

    private void initSubscriptionOptionButton(){
        subcriptionView1 = findViewById(R.id.subscription_option1);
        subcriptionView2 = findViewById(R.id.subscription_option2);
        subcriptionView3 = findViewById(R.id.subscription_option3);
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
                subscribe();
            }
        });
    }


    private void onSubscriptionOptionClick() {
        subcriptionView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subcriptionView1.choose();
                subcriptionView2.unchoose();
                subcriptionView3.unchoose();
            }
        });

        subcriptionView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subcriptionView2.choose();
                subcriptionView1.unchoose();
                subcriptionView3.unchoose();
            }
        });

        subcriptionView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subcriptionView3.choose();
                subcriptionView1.unchoose();
                subcriptionView2.unchoose();
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
