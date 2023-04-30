package com.chunxia.chatgpt.activity;

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
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.adapter.chat.ChatAdapter;
import com.chunxia.chatgpt.chatapi.MultiRoundChatAiApi;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.tools.Tools;
import com.chunxia.chatgpt.voicerecord.VoiceRecordActivity;
import com.chunxia.chatgpt.voicetotext.VoiceToTextModel;
import com.chunxia.chatgpt.model.Message;
import com.chunxia.chatgpt.model.TextMessage;
import com.chunxia.chatgpt.model.VoiceMessage;

import com.chunxia.chatgpt.voicerecord.models.Events;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

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

        initMultiRoundChatAiApi(getIntent().getStringExtra(ActivityIntentKeys.SYSTEM_COMMAND),
                getIntent().getIntExtra(ActivityIntentKeys.CHAT_ACTIVITY_START_MODE, 0));
        initComponent(getIntent().getStringExtra(ActivityIntentKeys.START_WORDS));
        initVoiceMessageButton();
    }

    private void initEventBus() {
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
                // todo 添加隐私政策 以满足google play上架要求
                requestAudio();

            }
        });
    }

    private void jump2AudioRecord() {
        Intent intent = new XLIntent(ActivityUtils.getTopActivity(), VoiceRecordActivity.class);
        ActivityUtils.getTopActivity().startActivity(intent);
    }

    private void requestAudio() {
        if (XXPermissions.isGranted(ChatActivity.this, Permission.RECORD_AUDIO)) {
            jump2AudioRecord();
            return;
        }

        XXPermissions.with(this)
                .permission(Permission.RECORD_AUDIO)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        Toast.makeText(ChatActivity.this, "获取录音权限成功", Toast.LENGTH_SHORT).show();
                        jump2AudioRecord();
                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
                            Toast.makeText(ChatActivity.this, "被永久拒绝授权，请手动授予录音权限", Toast.LENGTH_SHORT).show();
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(ChatActivity.this, permissions);
                        } else {
                            Toast.makeText(ChatActivity.this, "获取录音权限失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gotRecordEndEvent(Events.RecordingCompleted event) {
        String path = event.getPath();
        Log.i("RecorderFragment", path);
        adapter.insertItem(new VoiceMessage(adapter.getItemCount(), true,
                true, Tools.getFormattedTimeEvent(System.currentTimeMillis()), path));
        ThreadUtils.getSinglePool().execute(new Runnable() {
            @Override
            public void run() {
                String v2tResult = VoiceToTextModel.transcribeAudioFile(path, getApplication());
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sendChat(v2tResult);
                    }
                });
            }
        });
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

    private void sendChat(String msg) {
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

    private void sendChat() {
        final String msg = inputMessageEditText.getText().toString();
        sendChat(msg);
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