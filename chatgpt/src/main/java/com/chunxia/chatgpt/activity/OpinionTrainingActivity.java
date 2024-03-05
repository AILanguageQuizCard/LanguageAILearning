package com.chunxia.chatgpt.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.blankj.utilcode.util.ActivityUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.chatapi.TrainingMaterial;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.model.message.Message;
import com.chunxia.chatgpt.model.message.MessageManager;
import com.chunxia.chatgpt.model.message.TextMessage;
import com.chunxia.chatgpt.model.opinion.OpinionManager;
import com.google.android.flexbox.FlexboxLayout;
import com.material.components.utils.Tools;

import java.util.ArrayList;

public class OpinionTrainingActivity extends AppCompatActivity {
    private final String TAG = "TopicTrainingActivity";
    private ProgressBar progress_bar;
    private EditText et_search;
    private View lyt_content;
    private LinearLayout pendingView;

    private TextView hintQuestionTextView;

    private FlexboxLayout flexboxLayout;

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
        Tools.setSystemBarColor(this, R.color.chat_background);
        initStatusBar(R.color.chat_background);
    }

    private void initStatusBar(int colorId) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(colorId));

        WindowInsetsControllerCompat wic = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (wic != null) {
            // true表示Light Mode，状态栏字体呈黑色，反之呈白色
            wic.setAppearanceLightStatusBars(true);
        }
    }

    private void initComponent() {
        hintQuestionTextView = findViewById(R.id.activity_topic_training_hint_question);
        hintQuestionTextView.setText(R.string.opinion_training_hint_question);

        lyt_content = findViewById(R.id.lyt_content);
        et_search = findViewById(R.id.et_search);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);
        flexboxLayout = findViewById(R.id.lyt_popular_keyword_container);
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

        flexboxLayout.removeAllViews();
        int count = 5;
        ArrayList<String> randomTopicList = new OpinionManager().getRandomTopicList(count, this);

        for (int i = 0; i < count; i++) {
            // Create new Button
            Button btn = new Button(this);

            // Set layout parameters
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            btn.setLayoutParams(params);

            // Set the properties of the button
            btn.setText(randomTopicList.get(i));
            btn.setAllCaps(false);
            btn.setTextColor(ContextCompat.getColor(this, R.color.white));

            // Set the background drawable
            btn.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_rounded_colorprimary));

            // Set the click listener
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Do something in response to button click
                    popKeywordClick(v);
                }
            });

            flexboxLayout.addView(btn);
        }

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
        initOpinionChat(topic);
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

    private void initOpinionChat(String topic) {
        TrainingMaterial trainingMaterial = new TrainingMaterial();

        trainingMaterial.getOpinionObservable(topic, new TrainingMaterial.ReceiveSentencePatternExamplesCallback() {
            @Override
            public void onReceiveData(String reply) {
                onPendingEnd();

                Class<?> clazz = null;
                clazz = ChatActivity.class;
                String chatMode = "sentence_pattern";
                ArrayList<Message> messages = new ArrayList<>();

                messages.add(new TextMessage(1, topic, true, true, com.chunxia.chatgpt.tools.Tools.getFormattedTimeEvent(System.currentTimeMillis())));
                messages.add(new TextMessage(1, reply, false, false, com.chunxia.chatgpt.tools.Tools.getFormattedTimeEvent(System.currentTimeMillis())));

                MessageManager.getInstance().saveMessages(ActivityIntentKeys.getActivityChatModeKey(chatMode), messages);
                Intent intent = new XLIntent(ActivityUtils.getTopActivity(), clazz)
                        .putString(ActivityIntentKeys.ACTIVITY_CHAT_MODE, chatMode);

                ActivityUtils.getTopActivity().startActivity(intent);

            }
        });
    }
}
