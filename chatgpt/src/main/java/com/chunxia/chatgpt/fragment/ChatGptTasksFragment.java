package com.chunxia.chatgpt.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ThreadUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.firebase.activity.FirebaseUIActivity;
import com.chunxia.chatgpt.activity.SubscribeActivity;
import com.chunxia.chatgpt.chatapi.StrongCommandToChatGPT;
import com.chunxia.chatgpt.adapter.task.TaskAdapter;
import com.chunxia.chatgpt.adapter.task.TaskRecyclerViewItemDecoration;
import com.chunxia.chatgpt.adapter.task.TopicInfo;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.subscription.SubscriptionManager;
import com.chunxia.chatgpt.ui.SubscriptionReminderView;
import com.chunxia.firebase.id.FirebaseInstanceIDManager;
import com.chunxia.firebase.model.User;

import java.util.ArrayList;
import java.util.List;


public class ChatGptTasksFragment extends Fragment {

    private static final String TAG = "ChatGptTasksFragment";

    private View root;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;

    public ChatGptTasksFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_chatgpt, container, false);
        this.root = root;
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initSubscriptionView();
        initSubscriptionReminderView();
        initSettingButton();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FirebaseInstanceIDManager.getInstance().removeUpdateListener(onFirebaseUserUpdateListener);
        SubscriptionManager.getInstance().unregisterSubscriptionListener(subscriptionUpdateListener);
    }

    public void initSettingButton() {
        ImageView settingButton = this.root.findViewById(R.id.chat_task_top_view_setting);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XLIntent intent = new XLIntent(getActivity(), FirebaseUIActivity.class);
                startActivity(intent);
            }
        });
    }

    SubscriptionReminderView subscriptionReminderView = null;

    FirebaseInstanceIDManager.OnUpdateListener onFirebaseUserUpdateListener = new FirebaseInstanceIDManager.OnUpdateListener() {
        @Override
        public void onUpdateSuccess(User user) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (subscriptionReminderView == null) return;
                    subscriptionReminderView.setTitle(user.getRemainingTrail().intValue());
                    subscriptionReminderView.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    public void initSubscriptionReminderView() {
        subscriptionReminderView = this.root.findViewById(R.id.subscription_reminder_view);
        if (SubscriptionManager.getInstance().isSubscribed()) {
            subscriptionReminderView.setVisibility(View.GONE);
        } else {
            Long remainingTrail = FirebaseInstanceIDManager.getInstance().getRemainingTrail();
            if (remainingTrail != null) {
                subscriptionReminderView.setTitle(remainingTrail.intValue());
                subscriptionReminderView.setVisibility(View.VISIBLE);
            }
            FirebaseInstanceIDManager.getInstance().addUpdataListener(onFirebaseUserUpdateListener);
        }
    }

    private List<TopicInfo> getDatas() {
        List<TopicInfo> mylist = new ArrayList<>();

        mylist.add(new TopicInfo(getString(R.string.topic_title_1), getString(R.string.topic_description_1),
                false, StrongCommandToChatGPT.NORMAL_CHAT_MODE, "Say anything to me!", "Answer user's questions",
                "", R.drawable.topic_view_blue, 0));

        mylist.add(new TopicInfo(getString(R.string.topic_title_2), getString(R.string.topic_description_2),
                false, StrongCommandToChatGPT.STRONG_COMMAND_MODE, "Say anything to me!", StrongCommandToChatGPT.ENGLISH_ONLY_COMMAND,
                StrongCommandToChatGPT.ENGLISH_ONLY_COMMAND_ADD_TO_USER, R.drawable.topic_view_green, 1));

        mylist.add(new TopicInfo(getString(R.string.topic_title_3), getString(R.string.topic_description_3),
                false, StrongCommandToChatGPT.STRONG_COMMAND_MODE, "Say any English to me, and I will correct it for you.",
                StrongCommandToChatGPT.CORRECT_ENGLISH_EXPRESSION_COMMAND, StrongCommandToChatGPT.CORRECT_ENGLISH_EXPRESSION_COMMAND_ADD_TO_USER,
                R.drawable.topic_view_blue, 2));

        mylist.add(new TopicInfo(getString(R.string.topic_title_4), getString(R.string.topic_description_4),
                false, StrongCommandToChatGPT.STRONG_COMMAND_MODE, "If you have any expressions that you don't know how to say in English, just ask me.",
                StrongCommandToChatGPT.TRANSLATION_TO_ENGLISH_COMMAND, StrongCommandToChatGPT.TRANSLATION_TO_ENGLISH_COMMAND_ADD_TO_USER ,
                R.drawable.topic_view_green, 3));

        mylist.add(new TopicInfo(getString(R.string.topic_title_5), getString(R.string.topic_description_5),
                false, StrongCommandToChatGPT.STRONG_COMMAND_MODE, "Chat with me!",
                StrongCommandToChatGPT.INFORMAL_ENGLISH_ONLY_COMMAND, StrongCommandToChatGPT.INFORMAL_ENGLISH_ONLY_COMMAND_ADD_TO_USER,
                R.drawable.topic_view_blue, 4));
        return mylist;
    }


    SubscriptionManager.SubscriptionUpdateListener subscriptionUpdateListener = new SubscriptionManager.SubscriptionUpdateListener() {
        @Override
        public void onUpdatedSubscription(String sku) {
            Log.i(TAG, "onUpdatedSubscription: " + sku);
            if (subscriptionReminderView == null) return;
            subscriptionReminderView.setVisibility(View.GONE);
        }
    };


    // 需要补全的地方
    // 1. 生命周期优雅感知
    // 2. 多个库 okhttp、retrofit、eventbus、rxjava、glide、
    // 3. kotlin 用法 协程
    // 4. 架构
    // 5. 安卓常用design pattern
    // 6. Fragment
    // 7. 线程库
    // 8.

    private void initSubscriptionView() {
        SubscriptionReminderView subscriptionReminderView = root.findViewById(R.id.subscription_reminder_view);

        subscriptionReminderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new XLIntent(getActivity(), SubscribeActivity.class));
            }
        });

        if (SubscriptionManager.getInstance().isSubscribed()) {
            subscriptionReminderView.setVisibility(View.GONE);
        }
        SubscriptionManager.getInstance().registerSubscriptionListener(subscriptionUpdateListener);
    }

    private void initRecyclerView() {
        adapter = new TaskAdapter(getDatas());
        recyclerView = root.findViewById(R.id.task_fragment_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new TaskRecyclerViewItemDecoration());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        initTopBar();
    }


    private void initTopBar(){
        ImageView imageView = root.findViewById(R.id.chat_task_top_view_search);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
