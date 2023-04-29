package com.chunxia.chatgpt.activity;

import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_RESULT_KEY;
import static com.chunxia.chatgpt.chatapi.StrongCommandToChatGPT.NORMAL_CHAT_MODE;
import static com.chunxia.chatgpt.chatapi.StrongCommandToChatGPT.TOPIC_TRAINING_PROMPT1;
import static com.chunxia.chatgpt.chatapi.StrongCommandToChatGPT.TOPIC_TRAINING_PROMPT2;
import static com.chunxia.chatgpt.chatapi.StrongCommandToChatGPT.TOPIC_TRAINING_PROMPT3;
import static com.chunxia.chatgpt.chatapi.StrongCommandToChatGPT.TOPIC_TRAINING_PROMPT4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.ActivityUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.chatapi.MultiRoundChatAiApi;
import com.chunxia.chatgpt.common.XLIntent;
import com.material.components.utils.Tools;

import java.util.ArrayList;

public class TopicTrainingActivity extends AppCompatActivity {
    private final String TAG = "TopicTrainingActivity";
    private ProgressBar progress_bar;
    private EditText et_search;
    private View lyt_content;
    private LinearLayout pendingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic_training);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        Tools.setSystemBarColor(this, R.color.amber_500);
    }

    private void initComponent() {
        lyt_content = findViewById(R.id.lyt_content);
        et_search = findViewById(R.id.et_search);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    searchAction(String.valueOf(et_search.getText()));
                    return true;
                }
                return false;
            }
        });

    }

    public void popKeywordClick(View view) {
        if (view instanceof Button) {
            Button b = (Button) view;
            et_search.setText(b.getText());
            hideKeyboard();
            searchAction((String) b.getText());
        }
    }

    private void searchAction(String topic) {
        initPendingView();
        initTopicChat(topic, "English", 10);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_80));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home || item.getItemId() == R.id.action_close) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void initPendingView() {
        progress_bar.setVisibility(View.VISIBLE);
        lyt_content.setVisibility(View.INVISIBLE);
    }

    public void onPendingEnd() {
        progress_bar.setVisibility(View.GONE);
        lyt_content.setVisibility(View.VISIBLE);
    }

    private void initTopicChat(String topic, String language, int num) {

        MultiRoundChatAiApi chatAgent = new MultiRoundChatAiApi("", NORMAL_CHAT_MODE);
        String prompt = TOPIC_TRAINING_PROMPT1 + topic
                + TOPIC_TRAINING_PROMPT2 + language
                + TOPIC_TRAINING_PROMPT3 + num
                + TOPIC_TRAINING_PROMPT4;

        chatAgent.sendMessageInThread(prompt, new MultiRoundChatAiApi.ReceiveOpenAiReply() {
            @Override
            public void onSuccess(String reply) {
                Log.i(TAG, "reply \n " + reply);
                ArrayList<String> results = extractSentences(reply);
                onPendingEnd();
                Log.i(TAG, "10 sentences: \n" + results);

                Intent intent = new XLIntent(ActivityUtils.getTopActivity(), TopicTrainingCardActivity.class)
                        .putStringArrayList(TOPIC_TRAINING_RESULT_KEY, results);
                ActivityUtils.getTopActivity().startActivity(intent);
            }
        });
    }

    private static ArrayList<String> extractSentences(String input) {
        ArrayList<String> sentences = new ArrayList<String>();
        String[] words = input.split("\\|\\|\\|"); // 按照 "|||" 进行分割
        for (String word : words) {
            int startIndex = word.lastIndexOf("***");
            int startIndex2 = startIndex + 3; // 获取内容的起始位置
            if (startIndex != - 1) {
                String sentence = word.substring(startIndex2); // 提取内容
                sentences.add(sentence);
            }
        }
        return sentences;
    }

}
