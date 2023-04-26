package com.chunxia.chatgpt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.common.XLIntent;
import com.google.android.gms.samples.wallet.activity.CheckoutActivity;


public class ChatGptMeFragment extends Fragment {

    private RelativeLayout payButton;


    public ChatGptMeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        payButton = root.findViewById(R.id.setting_pay_button);
        initView();
        return root;
    }

    public void initView() {
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XLIntent(ActivityUtils.getTopActivity(), CheckoutActivity.class);
                ActivityUtils.getTopActivity().startActivity(intent);
            }
        });

    }


}


