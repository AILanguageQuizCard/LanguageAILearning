package com.chunxia.chatgpt.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ThreadUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.activity.BottomNavigationLightActivity;
import com.chunxia.chatgpt.activity.SubscribeActivity;
import com.chunxia.chatgpt.adapter.task.TaskRecyclerViewItemDecoration;
import com.chunxia.chatgpt.adapter.training.TrainingAdapter;
import com.chunxia.chatgpt.adapter.training.TrainingInfo;
import com.chunxia.chatgpt.adapter.training.TrainingType;
import com.chunxia.chatgpt.base.AppFragment;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.subscription.SubscriptionInfoProvider;
import com.chunxia.chatgpt.tools.Tools;
import com.chunxia.chatgpt.ui.SubscriptionReminderView;
import com.chunxia.firebase.id.FirebaseInstanceIDManager;
import com.chunxia.firebase.model.User;
import com.chunxia.firebase.model.UserUnInitException;

import java.util.ArrayList;
import java.util.List;


public class ChatGptTrainingFragment extends AppFragment<BottomNavigationLightActivity> {

    private static final String TAG = "ChatGptTrainingFragment";
    private RecyclerView recyclerView;
    private TrainingAdapter adapter;

    public ChatGptTrainingFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_training_chatgpt;
    }

    @Override
    protected void initView() {
        initRecyclerView();
        initStatusBar();
        initSubscriptionReminderView();
    }

    @Override
    protected void initData() {

    }


    public static ChatGptTrainingFragment newInstance() {
        return new ChatGptTrainingFragment();
    }


    private void initStatusBar() {
        Tools.setSystemBarColor(getActivity(), R.color.white);
        Tools.setSystemBarLight(getActivity());
    }
    SubscriptionReminderView subscriptionReminderView = null;

    FirebaseInstanceIDManager.OnUpdateListener onFirebaseUserUpdateListener = new FirebaseInstanceIDManager.OnUpdateListener() {
        @Override
        public void onUpdateSuccess(User user) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (subscriptionReminderView == null) return;
                    try {
                        subscriptionReminderView.setTitle(getSubscriptionViewTitle(user));
                        subscriptionReminderView.setVisibility(View.VISIBLE);
                    } catch (UserUnInitException e) {
                        Log.i(TAG, "haven't init user, can not init SubscriptionReminderView now");
                    }
                }
            });
        }
    };

    public String getSubscriptionViewTitle(User user) throws UserUnInitException{
        if (user.trailIsOver()) {
            return getResources().getString(R.string.subscription_reminder_view_trail_over)
                    + getResources().getString(R.string.subscription_reminder_view_trail_over_subscribe_now);
        }

        String s = getResources().getString(R.string.subscription_reminder_view_text1);
        s = s + user.getRemainingTrailTimeString(getContext());
        s = s + getResources().getString(R.string.subscription_reminder_view_text2);
        return s;
    }

    SubscriptionInfoProvider.SubscriptionUpdatedListener updateValidSubscriptionListener = new  SubscriptionInfoProvider.SubscriptionUpdatedListener() {
        @Override
        public void onSubscriptionUpdated(String sku) {
            Log.i(TAG, "onUpdatedSubscription: " + sku);
            if (subscriptionReminderView == null) return;
            subscriptionReminderView.setVisibility(View.GONE);
        }
    };


    public void initSubscriptionReminderView() {
        subscriptionReminderView = findViewById(R.id.subscription_reminder_view);
        SubscriptionInfoProvider.getInstance().addSubscriptionUpdatedListener(updateValidSubscriptionListener);
        if (SubscriptionInfoProvider.getInstance().isSubscribed()) {
            subscriptionReminderView.setVisibility(View.GONE);
        } else {
            try {
                User user = FirebaseInstanceIDManager.getInstance().getUser();
                subscriptionReminderView.setTitle(getSubscriptionViewTitle(user));
                subscriptionReminderView.setVisibility(View.VISIBLE);

            } catch (UserUnInitException e) {
                Log.i(TAG, "haven't init user, can not init SubscriptionReminderView now");
            }
            FirebaseInstanceIDManager.getInstance().addUpdataListener(onFirebaseUserUpdateListener);
        }

        subscriptionReminderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo initSubscription之后，拿到订阅内容后更新
                startActivity(new XLIntent(getActivity(), SubscribeActivity.class));
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FirebaseInstanceIDManager.getInstance().removeUpdateListener(onFirebaseUserUpdateListener);
        SubscriptionInfoProvider.getInstance().removeSubscriptionUpdatedListener(updateValidSubscriptionListener);
    }


    private List<TrainingInfo> getDatas() {
        List<TrainingInfo> mylist = new ArrayList<>();

        mylist.add(new TrainingInfo(getString(R.string.topic_training_1), getString(R.string.topic_training_description_1),
                false, R.drawable.topic_view_blue, TrainingType.TOPIC));

        mylist.add(new TrainingInfo(getString(R.string.topic_training_2), getString(R.string.topic_training_description_2),
                false, R.drawable.topic_view_green, TrainingType.OPINION));

        mylist.add(new TrainingInfo(getString(R.string.topic_training_3), getString(R.string.topic_training_description_3),
                false, R.drawable.topic_view_blue, TrainingType.SENTENCE_PATTERN));

        mylist.add(new TrainingInfo(getString(R.string.topic_training_4), getString(R.string.topic_training_description_4),
                false, R.drawable.topic_view_green, TrainingType.GRAMMAR));

        return mylist;
    }

    private void initRecyclerView() {
        adapter = new TrainingAdapter(getDatas());
        recyclerView = findViewById(R.id.task_fragment_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new TaskRecyclerViewItemDecoration());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        initTopBar();
    }

    private void initTopBar(){
        ImageView imageView = findViewById(R.id.chat_task_top_view_search);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
