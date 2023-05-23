package com.chunxia.chatgpt.activity;

import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_ACTIVITY_TOPIC_KEY;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_QUESTION_RESULT_KEY;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_RESULT_KEY;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_RESULT_NUM_KEY;

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
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.blankj.utilcode.util.ActivityUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.chatapi.TrainingMaterial;
import com.chunxia.chatgpt.common.XLIntent;
import com.chunxia.chatgpt.model.review.LearnCard;
import com.chunxia.chatgpt.model.review.TopicTestCard;
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

        trainingMaterial.prepareData(topic, new TrainingMaterial.ReceiveTrainMaterialCallback() {
            @Override
            public void onReceiveData(ArrayList<LearnCard> learnCards, ArrayList<TopicTestCard> topicTestCards) {
                onPendingEnd();
                Intent intent = new XLIntent(ActivityUtils.getTopActivity(), TopicTrainingCardActivity.class)
                        .putLearnCardArrayList(TOPIC_TRAINING_RESULT_KEY, learnCards)
                        .putLearnTestCardArrayList(TOPIC_TRAINING_QUESTION_RESULT_KEY, topicTestCards)
                        .putString(TOPIC_TRAINING_ACTIVITY_TOPIC_KEY, topic);
                ActivityUtils.getTopActivity().startActivity(intent);
            }
        });
    }



}
