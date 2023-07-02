package com.chunxia.chatgpt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.activity.RecordingLanguageSettingActivity;
import com.chunxia.chatgpt.activity.SubscribeActivity;
import com.chunxia.chatgpt.activity.VoiceLanguageSettingActivity;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.subscription.SubscriptionUtils;
import com.chunxia.chatgpt.ui.SettingItemView;
import com.limurse.iap.DataWrappers;
import com.limurse.iap.SubscriptionServiceListener;

import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class ChatGptSettingFragment extends Fragment {

    private RelativeLayout payButton;
    private SettingItemView outputVoiceButton;
    private SettingItemView recordVoiceButton;
    private View root;

    public ChatGptSettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        this.root = root;
        initView();
        return root;
    }

    private void initSubscription(){
        SubscriptionUtils.getInstance().initSubscribe(getActivity());
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new XLIntent(getActivity(), SubscribeActivity.class));
            }
        });

    }


    private void initVoiceLanguageButton() {
        outputVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XLIntent(getActivity(), VoiceLanguageSettingActivity.class);
                ActivityUtils.startActivity(intent);

            }
        });
    }

    private void initRecordingLanguageButton() {
        recordVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XLIntent(getActivity(), RecordingLanguageSettingActivity.class);
                ActivityUtils.startActivity(intent);

            }
        });
    }


    public void initView() {
        payButton = root.findViewById(R.id.setting_pay_button);
        outputVoiceButton = root.findViewById(R.id.voice_language_setting_view);
        recordVoiceButton = root.findViewById(R.id.record_language_setting_view);

        initSubscription();
        initVoiceLanguageButton();
        initRecordingLanguageButton();
    }
}
