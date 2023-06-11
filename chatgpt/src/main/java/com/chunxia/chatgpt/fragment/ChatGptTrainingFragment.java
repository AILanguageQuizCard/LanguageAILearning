package com.chunxia.chatgpt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.adapter.task.TaskRecyclerViewItemDecoration;
import com.chunxia.chatgpt.adapter.training.TrainingAdapter;
import com.chunxia.chatgpt.adapter.training.TrainingInfo;
import com.chunxia.chatgpt.adapter.training.TrainingType;


import java.util.ArrayList;
import java.util.List;


public class ChatGptTrainingFragment extends Fragment {

    private View root;
    private RecyclerView recyclerView;
    private TrainingAdapter adapter;

    public ChatGptTrainingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_training_chatgpt, container, false);
        this.root = root;
        initRecyclerView();
        return root;
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
