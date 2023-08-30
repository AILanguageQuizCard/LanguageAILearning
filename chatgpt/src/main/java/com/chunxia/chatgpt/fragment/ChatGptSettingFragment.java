package com.chunxia.chatgpt.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.activity.MotherLanguageSettingActivity;
import com.chunxia.chatgpt.activity.SubscribeActivity;
import com.chunxia.chatgpt.activity.LearningLanguageSettingActivity;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.subscription.SubscriptionManager;
import com.chunxia.chatgpt.ui.SettingItemView;
import com.chunxia.chatgpt.ui.SubscriptionSettingReminderView;
import com.chunxia.firebase.id.FirebaseInstanceIDManager;
import com.chunxia.firebase.model.User;
import com.chunxia.firebase.model.UserUnInitException;

public class ChatGptSettingFragment extends Fragment {

    private static final String TAG = "ChatGptSettingFragment";

    private SubscriptionSettingReminderView subscriptionButton;
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


    SubscriptionManager.SubscriptionUpdateListener subscriptionUpdateListener = new SubscriptionManager.SubscriptionUpdateListener() {
        @Override
        public void onUpdatedSubscription(String sku) {
            Log.i(TAG, "onUpdatedSubscription: " + sku);
            if (subscriptionButton == null) return;
            subscriptionButton.setVisibility(View.GONE);
        }
    };

    FirebaseInstanceIDManager.OnUpdateListener onFirebaseUserUpdateListener = new FirebaseInstanceIDManager.OnUpdateListener() {
        @Override
        public void onUpdateSuccess(User user) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (subscriptionButton == null) return;
                    try {
                        subscriptionButton.setTitle(getSubscriptionViewTitle(user));
                        subscriptionButton.setVisibility(View.VISIBLE);
                    } catch (UserUnInitException e) {
                        Log.i(TAG, "haven't init user, can not init SubscriptionReminderView now");
                    }
                }
            });
        }
    };

    public String getSubscriptionViewTitle(User user) throws UserUnInitException {
        if (user.trailIsOver()) {
            return getResources().getString(R.string.subscription_reminder_view_trail_over)
                    + getResources().getString(R.string.subscription_reminder_view_trail_over_subscribe_now);
        }
        String s = getResources().getString(R.string.subscription_reminder_view_text1);
        s = s + user.getRemainingTrailTimeString(getContext());
        s = s + getResources().getString(R.string.subscription_reminder_view_text2);
        return s;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FirebaseInstanceIDManager.getInstance().removeUpdateListener(onFirebaseUserUpdateListener);
        SubscriptionManager.getInstance().unregisterSubscriptionListener(subscriptionUpdateListener);
    }


    private void initSubscription() {
        subscriptionButton = root.findViewById(R.id.subscription_reminder_view);

        if (SubscriptionManager.getInstance().isSubscribed()) {
            subscriptionButton.setVisibility(View.GONE);
        } else {

            try {
                User user = FirebaseInstanceIDManager.getInstance().getUser();
                subscriptionButton.setTitle(getSubscriptionViewTitle(user));
                subscriptionButton.setVisibility(View.VISIBLE);

            } catch (UserUnInitException e) {
                Log.i(TAG, "haven't init user, can not init SubscriptionReminderView now");
            }

            FirebaseInstanceIDManager.getInstance().addUpdataListener(onFirebaseUserUpdateListener);
        }

        SubscriptionManager.getInstance().registerSubscriptionListener(subscriptionUpdateListener);

        subscriptionButton.setOnClickListener(new View.OnClickListener() {
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


    private SettingItemView contactUsButton;

    private void initContactUsButton() {
        contactUsButton = root.findViewById(R.id.fragment_setting_contact_us);
        contactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogContactUS();
            }
        });
    }

    private void showDialogContactUS() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_contact_us);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        (dialog.findViewById(R.id.dialog_contact_us_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
        initContactUsButton();
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

        final String[] Options = new String[]{
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
