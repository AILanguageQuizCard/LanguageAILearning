package com.chunxia.chatgpt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.subscription.SubscriptionUtils;
import com.limurse.iap.DataWrappers;
import com.limurse.iap.SubscriptionServiceListener;

import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class ChatGptSettingFragment extends Fragment {

    private RelativeLayout payButton;


    public ChatGptSettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        payButton = root.findViewById(R.id.setting_pay_button);
        initView();
        return root;
    }

    public void initView() {
        SubscriptionUtils.getInstance().initSubscribe(getActivity());
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubscriptionUtils.getInstance().addSubscriptionListener(new SubscriptionServiceListener() {
                    public void onSubscriptionRestored(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
                    }

                    public void onSubscriptionPurchased(@NonNull DataWrappers.PurchaseInfo purchaseInfo) {
//                if (purchaseInfo.getSku().equals("subscription")) { }
                        // todo 订阅成功之后，给予用于VIP权限
                    }

                    public void onPricesUpdated(@NotNull Map iapKeyPrices) {

                    }
                });

                SubscriptionUtils.getInstance().subscribe(getActivity());
            }
        });
    }
}


