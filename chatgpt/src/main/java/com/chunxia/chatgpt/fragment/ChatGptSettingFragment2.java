package com.chunxia.chatgpt.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.activity.BottomNavigationLightActivity;
import com.chunxia.chatgpt.activity.LearningLanguageSettingActivity;
import com.chunxia.chatgpt.activity.MotherLanguageSettingActivity;
import com.chunxia.chatgpt.activity.SubscribeActivity;
import com.chunxia.chatgpt.base.AppFragment;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.subscription.SubscriptionInfoProvider;
import com.chunxia.chatgpt.tools.Tools;
import com.chunxia.chatgpt.ui.SubscriptionSettingReminderView;
import com.chunxia.firebase.id.FirebaseInstanceIDManager;
import com.chunxia.firebase.model.User;
import com.chunxia.firebase.model.UserUnInitException;

public class ChatGptSettingFragment2 extends AppFragment<BottomNavigationLightActivity> {

    private static final String TAG = "ChatGptSettingFragment";

    private SubscriptionSettingReminderView subscriptionButton;
    private LinearLayout learningLanguageButton;
    private LinearLayout motherLanguageButton;
    private LinearLayout languageDifficultyButton;

    public ChatGptSettingFragment2() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting2;
    }


    private void initStatusBar() {
        Tools.setSystemBarColor(getActivity(), R.color.white);
        Tools.setSystemBarLight(getActivity());
    }

    SubscriptionInfoProvider.SubscriptionUpdatedListener updateValidSubscriptionListener = new SubscriptionInfoProvider.SubscriptionUpdatedListener() {
        @Override
        public void onSubscriptionUpdated(String sku) {
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
        SubscriptionInfoProvider.getInstance().removeSubscriptionUpdatedListener(updateValidSubscriptionListener);
    }



    private void initSubscription() {
        subscriptionButton = findViewById(R.id.subscription_reminder_view);
        SubscriptionInfoProvider.getInstance().addSubscriptionUpdatedListener(updateValidSubscriptionListener);

        if (SubscriptionInfoProvider.getInstance().isSubscribed()) {
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

        subscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo initSubscription之后，拿到订阅内容后更新
                startActivity(new XLIntent(getActivity(), SubscribeActivity.class));
            }
        });
    }

    private void initLearningLanguageButton() {
        learningLanguageButton = findViewById(R.id.voice_language_setting_view);
        learningLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XLIntent(getActivity(), LearningLanguageSettingActivity.class);
                ActivityUtils.startActivity(intent);

            }
        });
    }


    private LinearLayout contactUsButton;

    private void initContactUsButton() {
        contactUsButton = findViewById(R.id.fragment_setting_contact_us);
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
        motherLanguageButton = findViewById(R.id.record_language_setting_view);
        motherLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XLIntent(getActivity(), MotherLanguageSettingActivity.class);
                ActivityUtils.startActivity(intent);

            }
        });
    }


    public static ChatGptSettingFragment2 newInstance() {
        return new ChatGptSettingFragment2();
    }

    @Override
    public void initView() {
        initStatusBar();
        initSubscription();
        initLearningLanguageButton();
        initRecordingLanguageButton();
        initLanguageDifficultyButton();
        initContactUsButton();
    }

    @Override
    protected void initData() {

    }

    private void initLanguageDifficultyButton() {
        languageDifficultyButton = findViewById(R.id.language_difficulty_setting_view);
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
