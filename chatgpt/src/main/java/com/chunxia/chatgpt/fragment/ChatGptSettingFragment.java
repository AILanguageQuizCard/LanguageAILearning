package com.chunxia.chatgpt.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.activity.MotherLanguageSettingActivity;
import com.chunxia.chatgpt.activity.SubscribeActivity;
import com.chunxia.chatgpt.activity.LearningLanguageSettingActivity;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.subscription.SubscriptionManager;
import com.chunxia.chatgpt.ui.SettingItemView;

public class ChatGptSettingFragment extends Fragment {

    private static final String TAG = "ChatGptSettingFragment";

    private RelativeLayout payButton;
    private SettingItemView learningLanguageButton;
    private SettingItemView motherLanguageButton;
    private SettingItemView languageDifficultyButton;
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

    private void initSubscription() {
        payButton = root.findViewById(R.id.setting_pay_button);

        if (SubscriptionManager.getInstance().isSubscribed()) {
            payButton.setVisibility(View.GONE);
        }
        SubscriptionManager.getInstance().registerSubscriptionListener(new SubscriptionManager.SubscriptionUpdateListener() {
            @Override
            public void onUpdatedSubscription(String sku) {
                Log.i(TAG, "onUpdatedSubscription: " + sku);
                payButton.setVisibility(View.GONE);
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo initSubscription之后，拿到订阅内容后更新
                startActivity(new XLIntent(getActivity(), SubscribeActivity.class));
            }
        });
    }


    private void initLearningLanguageButton() {
        learningLanguageButton = root.findViewById(R.id.voice_language_setting_view);
        learningLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XLIntent(getActivity(), LearningLanguageSettingActivity.class);
                ActivityUtils.startActivity(intent);

            }
        });
    }

    private void initRecordingLanguageButton() {
        motherLanguageButton = root.findViewById(R.id.record_language_setting_view);
        motherLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XLIntent(getActivity(), MotherLanguageSettingActivity.class);
                ActivityUtils.startActivity(intent);

            }
        });
    }

    public void initView() {
        initSubscription();
        initLearningLanguageButton();
        initRecordingLanguageButton();
        initLanguageDifficultyButton();
    }

    private void initLanguageDifficultyButton() {
        languageDifficultyButton = root.findViewById(R.id.language_difficulty_setting_view);
        languageDifficultyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChoiceDialog();
            }
        });
    }


    private void showSingleChoiceDialog() {

        final String[] Options = new String[] {
                getResources().getString(R.string.setting_language_difficulty_option1),
                getResources().getString(R.string.setting_language_difficulty_option2),
                getResources().getString(R.string.setting_language_difficulty_option3)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.setting_language_difficulty_title));
        builder.setSingleChoiceItems(Options, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton(R.string.setting_language_difficulty_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(R.string.setting_language_difficulty_cancel, null);
        builder.show();
    }


}
