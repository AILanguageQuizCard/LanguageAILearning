package com.example.chatgpt.fragment;


import static com.example.chatgpt.activity.ActivityIntentKeys.BEFORE_USER_MESSAGE_COMMAND;
import static com.example.chatgpt.activity.ActivityIntentKeys.CHAT_ACTIVITY_START_MODE;
import static com.example.chatgpt.activity.ActivityIntentKeys.START_WORDS;
import static com.example.chatgpt.activity.ActivityIntentKeys.SYSTEM_COMMAND;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.CORRECT_ENGLISH_EXPRESSION_COMMAND;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.CORRECT_ENGLISH_EXPRESSION_COMMAND_ADD_TO_USER;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.ENGLISH_ONLY_COMMAND;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.ENGLISH_ONLY_COMMAND_ADD_TO_USER;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.INFORMAL_ENGLISH_ONLY_COMMAND;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.INFORMAL_ENGLISH_ONLY_COMMAND_ADD_TO_USER;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.STRONG_COMMAND_MODE;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.NORMAL_CHAT_MODE;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.TRANSLATION_TO_ENGLISH_COMMAND;
import static com.example.chatgpt.chatapi.StrongCommandToChatgpt.TRANSLATION_TO_ENGLISH_COMMAND_ADD_TO_USER;

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
                Intent intent = new XYIntent(getActivity(), ChatGptChatActivity.class)
                        .putInt(CHAT_ACTIVITY_START_MODE, NORMAL_CHAT_MODE);
                newIntentWithString(intent, START_WORDS, "Say anything to me!");
                newIntentWithString(intent, SYSTEM_COMMAND, "Answer user's questions");
                requireActivity().startActivity(intent);
            }
        });

        root.findViewById(R.id.button_start_translate_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XYIntent(getActivity(), ChatGptChatActivity.class)
                        .putInt(CHAT_ACTIVITY_START_MODE, STRONG_COMMAND_MODE)
                        .putString(START_WORDS, "Say anything to me!")
                        .putString(SYSTEM_COMMAND, ENGLISH_ONLY_COMMAND)
                        .putString(BEFORE_USER_MESSAGE_COMMAND, ENGLISH_ONLY_COMMAND_ADD_TO_USER);
                requireActivity().startActivity(intent);
            }
        });

        root.findViewById(R.id.button_correct_english_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XYIntent(getActivity(), ChatGptChatActivity.class)
                        .putInt(CHAT_ACTIVITY_START_MODE, STRONG_COMMAND_MODE)
                        .putString(START_WORDS, "Say any English to me, and I will correct it for you.")
                        .putString( SYSTEM_COMMAND, CORRECT_ENGLISH_EXPRESSION_COMMAND)
                        .putString(BEFORE_USER_MESSAGE_COMMAND, CORRECT_ENGLISH_EXPRESSION_COMMAND_ADD_TO_USER);
                requireActivity().startActivity(intent);
            }
        });

        root.findViewById(R.id.button_english_expression_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XYIntent(getActivity(), ChatGptChatActivity.class)
                        .putInt(CHAT_ACTIVITY_START_MODE, STRONG_COMMAND_MODE)
                        .putString(START_WORDS, "If you have any expressions that you don't know how to say in English, just ask me.")
                        .putString(SYSTEM_COMMAND, TRANSLATION_TO_ENGLISH_COMMAND)
                        .putString(BEFORE_USER_MESSAGE_COMMAND, TRANSLATION_TO_ENGLISH_COMMAND_ADD_TO_USER);
                ActivityUtils.startActivity(intent);
            }
        });

        root.findViewById(R.id.button_english_oral_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XYIntent(getActivity(), ChatGptChatActivity.class)
                        .putInt(CHAT_ACTIVITY_START_MODE, STRONG_COMMAND_MODE)
                        .putString(START_WORDS, "Chat with me!")
                        .putString(SYSTEM_COMMAND, INFORMAL_ENGLISH_ONLY_COMMAND)
                        .putString(BEFORE_USER_MESSAGE_COMMAND, INFORMAL_ENGLISH_ONLY_COMMAND_ADD_TO_USER);
                ActivityUtils.startActivity(intent);
            }
        });

    }

}