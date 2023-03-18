package com.material.components.chatgpt.activity;

import static com.material.components.chatgpt.activity.ActivityIntentKeys.START_WORDS;
import static com.material.components.chatgpt.activity.ActivityIntentKeys.SYSTEM_COMMAND;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ThreadUtils;
import com.material.components.R;
import com.material.components.adapter.AdapterChatTelegram;
import com.material.components.chatgpt.MultiRoundChatAiApi;
import com.material.components.model.Message;
import com.material.components.utils.Tools;

import java.util.ArrayList;
import java.util.List;

public class ChatGptChatActivity extends AppCompatActivity {

    private ImageView btn_send;
    private EditText et_content;
    private AdapterChatTelegram adapter;
    private RecyclerView recycler_view;

    private ActionBar actionBar;

    private MultiRoundChatAiApi multiRoundChatAiApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_chatgpt);
        initToolbar();
        initMultiRoundChatAiApi(getIntent().getStringExtra(SYSTEM_COMMAND));
        iniComponent(getIntent().getStringExtra(START_WORDS));
    }

    private void initMultiRoundChatAiApi(String systemCommand) {
        multiRoundChatAiApi = new MultiRoundChatAiApi(systemCommand);
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setTitle(null);
        Tools.setSystemBarColorInt(this, Color.parseColor("#426482"));
    }

    public void iniComponent(String startWords) {
        recycler_view = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setHasFixedSize(true);

        adapter = new AdapterChatTelegram(this);
        recycler_view.setAdapter(adapter);
        Message initialMessage = new Message(adapter.getItemCount(), startWords, false,
                adapter.getItemCount() % 5 == 0, Tools.getFormattedTimeEvent(System.currentTimeMillis()));
        adapter.insertItem(initialMessage);

        btn_send = findViewById(R.id.btn_send);
        et_content = findViewById(R.id.text_content);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChat();
            }
        });
        et_content.addTextChangedListener(contentWatcher);

        findViewById(R.id.lyt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.refresh_chat_imageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Message> items = new ArrayList<>();
                items.add(initialMessage);
                adapter.setItems(items);
            }
        });
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

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_telegram, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}