package com.example.chatgpt.activity;

import static com.example.chatgpt.activity.ActivityIntentKeys.CHAT_ACTIVITY_START_MODE;
import static com.example.chatgpt.activity.ActivityIntentKeys.START_WORDS;
import static com.example.chatgpt.activity.ActivityIntentKeys.SYSTEM_COMMAND;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.example.chatgpt.R;
import com.example.chatgpt.chatapi.MultiRoundChatAiApi;
import com.example.chatgpt.adapter.chat.ChatAdapter;
import com.example.chatgpt.common.XLIntent;
import com.example.chatgpt.model.Message;
import com.example.chatgpt.model.TextMessage;
import com.example.chatgpt.model.VoiceMessage;
import com.example.chatgpt.voicerecord.VoiceRecordActivity;

import com.example.chatgpt.voicerecord.models.Events;
import com.example.chatgpt.voicetotext.VoiceToTextModel;
import com.material.components.utils.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class ChatGptChatActivity extends AppCompatActivity {

    private static String TAG = "ChatGptChatActivity";
    private ImageView btn_send;
    private ImageView voiceMessageButton;
    private EditText inputMessageEditText;
    private ChatAdapter adapter;
    private RecyclerView recyclerView;


    private ActionBar actionBar;

    private MultiRoundChatAiApi multiRoundChatAiApi;

    private EventBus bus = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_chatgpt);

        initEventBus();
        initStatusBar();

        initMultiRoundChatAiApi(getIntent().getStringExtra(SYSTEM_COMMAND),
                getIntent().getIntExtra(CHAT_ACTIVITY_START_MODE, 0));
        initComponent(getIntent().getStringExtra(START_WORDS));
        initVoiceMessageButton();
    }

    private void initEventBus(){
        bus = EventBus.getDefault();
        bus.register(this);
    }


    private void initMultiRoundChatAiApi(String systemCommand, int mode) {
        multiRoundChatAiApi = new MultiRoundChatAiApi(systemCommand, mode);
    }

    private void initStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        WindowInsetsControllerCompat wic = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (wic != null) {
            // true表示Light Mode，状态栏字体呈黑色，反之呈白色
            wic.setAppearanceLightStatusBars(true);
        }
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

    public void initVoiceMessageButton() {
        voiceMessageButton = findViewById(R.id.voice_message_button);
        voiceMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new XLIntent(ActivityUtils.getTopActivity(), VoiceRecordActivity.class);
                ActivityUtils.getTopActivity().startActivity(intent);

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gotRecordEndEvent(Events.RecordingCompleted event){
        String path = event.getPath();
        Log.i("RecorderFragment", path);
        adapter.insertItem(new VoiceMessage(adapter.getItemCount(), true,
                        true, Tools.getFormattedTimeEvent(System.currentTimeMillis()), path ));
    }

    public void initComponent(String startWords) {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ChatAdapter(getApplication());
        recyclerView.setAdapter(adapter);
        // todo 将初始message换成选择要输入的话
        Message initialMessage = new TextMessage(adapter.getItemCount(), startWords, false,
                adapter.getItemCount() % 5 == 0, Tools.getFormattedTimeEvent(System.currentTimeMillis()));
        adapter.insertItem(initialMessage);

        btn_send = findViewById(R.id.send_button);
        inputMessageEditText = findViewById(R.id.input_message_edittext);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendChat();
            }
        });

//        inputMessageEditText.addTextChangedListener(contentWatcher);

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
        final String msg = inputMessageEditText.getText().toString();
        if (msg.isEmpty()) return;
        adapter.insertItem(new TextMessage(adapter.getItemCount(), msg,
                true, adapter.getItemCount() % 5 == 0,
                Tools.getFormattedTimeEvent(System.currentTimeMillis())));
        inputMessageEditText.setText("");
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        multiRoundChatAiApi.sendMessageInThread(msg,
                reply -> ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.insertItem(new TextMessage(adapter.getItemCount(), reply,
                                false, adapter.getItemCount() % 5 == 0,
                                Tools.getFormattedTimeEvent(System.currentTimeMillis())));

                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    }
                })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        multiRoundChatAiApi.cancelAllCurrentThread();
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
