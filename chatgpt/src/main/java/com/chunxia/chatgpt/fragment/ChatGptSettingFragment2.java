package com.chunxia.chatgpt.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

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
import com.chunxia.firebase.config.RemoteConfig;
import com.chunxia.firebase.id.FirebaseInstanceIDManager;
import com.chunxia.firebase.model.User;
import com.chunxia.firebase.model.UserUnInitException;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import darren.googlecloudtts.exception.ApiException;

public class ChatGptSettingFragment2 extends AppFragment<BottomNavigationLightActivity> {

    private static final String TAG = "ChatGptSettingFragment";

    private SubscriptionSettingReminderView subscriptionButton;
    private LinearLayout learningLanguageButton;
    private LinearLayout motherLanguageButton;
    private LinearLayout languageDifficultyButton;

    private LinearLayout contactUsButton;

    private LinearLayout checkUpdateButton;

    private LinearLayout loginButton;

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

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    ActivityResultLauncher<IntentSenderRequest> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                            String idToken = credential.getGoogleIdToken();
                            if (idToken != null) {
                                // Got an ID token from Google. Use it to authenticate
                                // with your backend.
                                Log.d(TAG, "Got ID token." + credential.getDisplayName());
                                Toast.makeText(getContext(), "Got ID token." + credential.getDisplayName(), Toast.LENGTH_SHORT).show();
                            }
                            Log.d(TAG, "Got ID token.");

                        } catch (com.google.android.gms.common.api.ApiException e) {
                            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                            // ...
                        }
                    } else {

                    }
                }
            });


    private void initLoginButton() {

        if (initSignIn()) return;

        loginButton = findViewById(R.id.login_with_google_view);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender())
                                        .build();
                                activityResultLauncher.launch(intentSenderRequest);
                                Log.d(TAG, "beginSignIn onSuccess");
                            }
                        })
                        .addOnFailureListener(getActivity(), new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // No saved credentials found. Launch the One Tap sign-up flow, or
                                // do nothing and continue presenting the signed-out UI.
                                Log.d(TAG, "beginSignIn on fail" + e.getLocalizedMessage());
                            }
                        });
            }
        });
    }

    private boolean initSignIn() {
        if (getContext() == null) return true;
        oneTapClient = Identity.getSignInClient(getContext());
        signInRequest = BeginSignInRequest.builder()
                .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                        .setSupported(true)
                        .build())
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.google_auth_key))
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();
        return false;
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


    private void initCheckUpdateButton() {
        checkUpdateButton = findViewById(R.id.check_update_button);
        checkUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckUpdateButton();
            }
        });
    }


    private void setCheckUpdateButton() {
        // 本地的版本码和服务器的进行比较
        if (Tools.getVersionCode(getActivity()) < Integer.parseInt(RemoteConfig.getInstance().getLatestAppVersion())) {

            String appPkg = "com.chunxia.chatgpt";

            String marketPkg = "com.android.vending";
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);


            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "已经是最新版本啦！", Toast.LENGTH_SHORT).show();
        }
    }


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
        initCheckUpdateButton();

        initLoginButton();
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
