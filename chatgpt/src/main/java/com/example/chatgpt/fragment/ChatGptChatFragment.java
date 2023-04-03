package com.example.chatgpt.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.ThreadUtils;

import com.example.chatgpt.chatapi.MultiRoundChatAiApi;
import com.example.chatgpt.R;
import com.material.components.adapter.AdapterChatTelegram;
import com.material.components.model.Message;
import com.material.components.utils.Tools;


public class ChatGptChatFragment extends Fragment {

    private MultiRoundChatAiApi multiRoundChatAiApi;
    private View root;

    private ImageView btn_send;
    private EditText et_content;
    private AdapterChatTelegram adapter;
    private RecyclerView recycler_view;


    public ChatGptChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat_chatgpt, container, false);
        initMultiRoundChatAiApi("do what the user ask you to do", 0);
        initComponent(root);
        return root;
    }


    private void initMultiRoundChatAiApi(String command, int mode ) {
        multiRoundChatAiApi = new MultiRoundChatAiApi(command, mode);
    }

    public void initComponent(View root) {
        this.root = root;
        recycler_view = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);

        adapter = new AdapterChatTelegram(getActivity());
        recycler_view.setAdapter(adapter);
        adapter.insertItem(new Message(adapter.getItemCount(), "Say anything to me!", false, adapter.getItemCount() % 5 == 0, Tools.getFormattedTimeEvent(System.currentTimeMillis())));
        adapter.insertItem(new Message(adapter.getItemCount(), "Hello!", true, adapter.getItemCount() % 5 == 0, Tools.getFormattedTimeEvent(System.currentTimeMillis())));

        btn_send = root.findViewById(R.id.btn_send);
        et_content = root.findViewById(R.id.text_content);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChat();
            }
        });
        et_content.addTextChangedListener(contentWatcher);

//        (root.findViewById(R.id.lyt_back)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
    }

    private void sendChat() {
        final String msg = et_content.getText().toString();
        if (msg.isEmpty()) return;
        adapter.insertItem(new Message(adapter.getItemCount(), msg,
                true, adapter.getItemCount() % 5 == 0,
                Tools.getFormattedTimeEvent(System.currentTimeMillis())));
        et_content.setText("");
        recycler_view.scrollToPosition(adapter.getItemCount() - 1);
        multiRoundChatAiApi.sendMessageInThread(msg,
                reply -> ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.insertItem(new Message(adapter.getItemCount(), reply,
                                false, adapter.getItemCount() % 5 == 0,
                                Tools.getFormattedTimeEvent(System.currentTimeMillis())));
                        recycler_view.scrollToPosition(adapter.getItemCount() - 1);
                    }
                })
        );
    }


//    @Override
//    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onPostCreate(savedInstanceState, persistentState);
//        hideKeyboard();
//    }
//
//    private void hideKeyboard() {
//        View view = getActivity().getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }


    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable etd) {
            if (etd.toString().trim().length() == 0) {
                btn_send.setImageResource(R.drawable.ic_mic);
            } else {
                btn_send.setImageResource(R.drawable.ic_send);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };






}