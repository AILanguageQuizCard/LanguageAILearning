package com.chunxia.chatgpt.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.activity.ReviewCardActivity;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.ui.SettingItemView;


public class ChatGptReviewFragment extends Fragment {

    private SettingItemView startReviewButton;
    private View root;

    public ChatGptReviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_review, container, false);
        this.root = root;
        initView();
        return root;
    }


    private void initStartReviewButton() {
        startReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new XLIntent(getActivity(), ReviewCardActivity.class);
                ActivityUtils.startActivity(intent);
            }
        });
    }


    public void initView() {
        startReviewButton = root.findViewById(R.id.voice_language_setting_view);
        initStartReviewButton();
    }


}


