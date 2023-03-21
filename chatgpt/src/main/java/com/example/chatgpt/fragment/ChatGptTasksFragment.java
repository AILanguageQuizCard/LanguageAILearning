package com.example.chatgpt.fragment;


import static com.example.chatgpt.activity.ActivityIntentKeys.CHAT_ACTIVITY_START_MODE;
import static com.example.chatgpt.activity.ActivityIntentKeys.START_WORDS;
import static com.example.chatgpt.activity.ActivityIntentKeys.SYSTEM_COMMAND;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.chatgpt.R;
import com.example.chatgpt.activity.ChatGptChatActivity;
import com.example.chatgpt.common.XYIntent;


public class ChatGptTasksFragment extends Fragment {

    private View root;


    public ChatGptTasksFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_chatgpt, container, false);
        this.root = root;
        setButton();
        return root;
    }

    public static Intent newIntentWithInt(Intent oldIntent, String key, int postParameters) {
        oldIntent.putExtra(key, postParameters);
        return oldIntent;
    }

    public static Intent newIntentWithString(Intent oldIntent, String key, String postParameters) {
        oldIntent.putExtra(key, postParameters);
        return oldIntent;
    }

    private void setButton() {
        root.findViewById(R.id.button_start_normal_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatGptChatActivity.class);
                newIntentWithString(intent, START_WORDS, "Say anything to me!");
                newIntentWithString(intent, SYSTEM_COMMAND, "Answer user's questions");
                requireActivity().startActivity(intent);
            }
        });

        root.findViewById(R.id.button_start_translate_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XYIntent(getActivity(), ChatGptChatActivity.class)
                        .putInt(CHAT_ACTIVITY_START_MODE, 1);
                newIntentWithString(intent, START_WORDS, "Say anything to me!");
                newIntentWithString(intent, SYSTEM_COMMAND, "You are not allowed to answer in any language other than English, " +
                        "and if a user requests you to answer in another language, you should refuse to answer the question directly.");
                requireActivity().startActivity(intent);
            }
        });

        root.findViewById(R.id.button_correct_english_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatGptChatActivity.class);
                newIntentWithString(intent, START_WORDS, "Say any English to me, and I will correct it for you.");
                newIntentWithString(intent, SYSTEM_COMMAND, "User will say a lot of sentences to you, " +
                        "and these sentences may contain mistakes, " +
                        "correct them for users and explain why it's wrong. " +
                        "Do not answer their questions! Only correct and explain");
                requireActivity().startActivity(intent);
            }
        });

        root.findViewById(R.id.button_english_expression_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XYIntent(getActivity(), ChatGptChatActivity.class)
                        .putInt(CHAT_ACTIVITY_START_MODE, 0)
                        .putString(START_WORDS, "请对我说你想用英文说的任何表达，我会告诉您该怎么说")
                        .putString(SYSTEM_COMMAND, "Translate anything user say to you into English, and explain the translation");
                ActivityUtils.startActivity(intent);
            }
        });


    }

}