package com.chunxia.chatgpt.activity;

import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_ACTIVITY_LEARNING_MATERIAL_KEY;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_ACTIVITY_MODE_KEY;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_GRAMMAR;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_SENTENCE_PATTERN;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_TOPIC;

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
import com.chunxia.chatgpt.activity.dataholder.DataHolder;
import com.chunxia.chatgpt.chatapi.TrainingMaterial;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.model.grammar.GrammarManager;
import com.chunxia.chatgpt.model.message.Message;
import com.chunxia.chatgpt.model.message.MessageManager;
import com.chunxia.chatgpt.model.message.TextMessage;
import com.chunxia.chatgpt.model.review.AllLearningMaterialCard;
import com.chunxia.chatgpt.model.review.SentenceCard;
import com.chunxia.chatgpt.model.review.TopicTestCard;
import com.chunxia.chatgpt.model.sentence_pattern.SentencePatternManager;
import com.chunxia.chatgpt.model.topic.TrainingTopicManager;
import com.google.android.flexbox.FlexboxLayout;
import com.material.components.utils.Tools;

import java.util.ArrayList;

public class TopicTrainingActivity extends AppCompatActivity {
    private final String TAG = "TopicTrainingActivity";
    private ProgressBar progress_bar;
    private EditText et_search;
    private View lyt_content;

    private FlexboxLayout flexboxLayout;
    private TextView hintQuestionTextView;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topic_training);

        initData();

        initToolbar();
        initComponent();
    }

    private void initData() {
        mode = getIntent().getStringExtra(TOPIC_TRAINING_ACTIVITY_MODE_KEY);
        if (mode == null || mode.isEmpty()) {
            mode = "topic_training";
        }
    }


    private boolean isTopicTrainingMode() {
        return mode.equals(TOPIC_TRAINING_TOPIC);
    }

    private boolean isSentencePatternTrainingMode() {
        return mode.equals(TOPIC_TRAINING_SENTENCE_PATTERN);
    }

    private boolean isGrammarTrainingMode() {
        return mode.equals(TOPIC_TRAINING_GRAMMAR);
    }


    private ArrayList<String> getTrainingData(int count) {
        if (isTopicTrainingMode()) {
            return new TrainingTopicManager().getRandomTopicList(count, this);
        } else if (isSentencePatternTrainingMode()) {
            return new SentencePatternManager().getRandomTopicList(count, this);
        } else if (isGrammarTrainingMode()) {
            return new GrammarManager().getRandomTopicList(count, this);
        } else {
            return new TrainingTopicManager().getRandomTopicList(count, this);
        }
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

    private void initHintText() {

        hintQuestionTextView = findViewById(R.id.activity_topic_training_hint_question);

        int id;
        if (isTopicTrainingMode()) {
            id = R.string.topic_training_hint_question;
        } else if (isSentencePatternTrainingMode()) {
            id = R.string.sentence_pattern_hint_question;
        } else if (isGrammarTrainingMode()) {
            id = R.string.grammar_hint_question;
        } else {
            id = R.string.topic_training_hint_question;
        }
        hintQuestionTextView.setText(id);

    }

    private void initComponent() {
        lyt_content = findViewById(R.id.lyt_content);
        et_search = findViewById(R.id.et_search);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);
        flexboxLayout = findViewById(R.id.lyt_popular_keyword_container);

        initHintText();

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
        int count = 10;
        ArrayList<String> randomTopicList = getTrainingData(count);

        for (int i = 0; i < count; i++) {
            Button btn = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            btn.setLayoutParams(params);
            btn.setText(randomTopicList.get(i));
            btn.setAllCaps(false);
            btn.setTextColor(ContextCompat.getColor(this, R.color.white));
            btn.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_rounded_colorprimary));

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
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
        initTopicChat(topic);
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

    private void initTopicChat(String topic) {
        TrainingMaterial trainingMaterial = new TrainingMaterial();

        TrainingMaterial.ReceiveTrainMaterialCallback receiveTrainMaterialCallback = new TrainingMaterial.ReceiveTrainMaterialCallback() {
            @Override
            public void onReceiveData(ArrayList<SentenceCard> sentenceCards, ArrayList<TopicTestCard> topicTestCards) {
                onPendingEnd();
                AllLearningMaterialCard learningMaterialCard = new AllLearningMaterialCard(sentenceCards, topicTestCards, topic);
                DataHolder.getInstance().setData(TOPIC_TRAINING_ACTIVITY_LEARNING_MATERIAL_KEY, learningMaterialCard);
                Intent intent = new XLIntent(ActivityUtils.getTopActivity(), TopicTrainingCardActivity.class)
                        .putString(TOPIC_TRAINING_ACTIVITY_MODE_KEY, mode);
                ActivityUtils.getTopActivity().startActivity(intent);
            }

            @Override
            public void onExtractSentencesFail(String answers) {
                onPendingEnd();
                Toast.makeText(ActivityUtils.getTopActivity(), "Unable to extract answer from GPT", Toast.LENGTH_SHORT).show();

                Class<?> clazz = ChatActivity.class;
                String chatMode = "training_fail";
                ArrayList<Message> messages = new ArrayList<>();

                messages.add(new TextMessage(1, topic, true, true, com.chunxia.chatgpt.tools.Tools.getFormattedTimeEvent(System.currentTimeMillis())));
                messages.add(new TextMessage(1, answers, false, false, com.chunxia.chatgpt.tools.Tools.getFormattedTimeEvent(System.currentTimeMillis())));

                MessageManager.getInstance().saveMessages(ActivityIntentKeys.getActivityChatModeKey(chatMode), messages);
                Intent intent = new XLIntent(ActivityUtils.getTopActivity(), clazz)
                        .putString(ActivityIntentKeys.ACTIVITY_CHAT_MODE, chatMode);

                ActivityUtils.getTopActivity().startActivity(intent);

            }
        };

        if (isTopicTrainingMode()) {
            trainingMaterial.prepareSentenceData(topic, receiveTrainMaterialCallback);
        } else if (isSentencePatternTrainingMode()) {
            trainingMaterial.prepareSentencePatternExamplesData(topic,receiveTrainMaterialCallback);
        } else if (isGrammarTrainingMode()) {
            trainingMaterial.prepareGrammarExamplesData(topic, receiveTrainMaterialCallback);
        }
    }
}
