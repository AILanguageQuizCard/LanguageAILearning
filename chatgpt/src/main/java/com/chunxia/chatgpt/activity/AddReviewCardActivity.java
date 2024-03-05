package com.chunxia.chatgpt.activity;

import static com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_CHAT_ADD_TO_REVIEW_CARD;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_REVIEW_CARD_EDITED_SENTENCES_LIST;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.adapter.chat.ChoosedItem;
import com.chunxia.chatgpt.model.review.ReviewCardManager;
import com.chunxia.chatgpt.model.review.SentenceCard;
import com.chunxia.chatgpt.tools.Tools;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class AddReviewCardActivity extends AppCompatActivity {

    private EditText questionEditText;

    private EditText answerEditText;
    private EditText topicEditText;

    private Button submitButton;

    private String topic = "";

    private String question= "";

    private String answer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review_card);

        questionEditText = findViewById(R.id.questionEditText);
        answerEditText = findViewById(R.id.answerEditText);
        topicEditText = findViewById(R.id.topicEditText);
        submitButton = findViewById(R.id.submitButton);

        initStatusBar();
        initData();
        initAllTopicButtons();
        initSubmitButtonClicked();

    }


    private void initStatusBar() {
        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);
    }


    public void initAllTopicButtons() {
        FlexboxLayout flexboxLayout = findViewById(R.id.activity_add_review_card_keywords_flexbox);
        List<String> allTopics = ReviewCardManager.getInstance().getAllTopics();

        flexboxLayout.removeAllViews();
        for (String topic : allTopics) {
            Button button = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            button.setLayoutParams(params);
            button.setBackgroundResource(R.drawable.btn_rounded_colorprimary);
            button.setText(topic);
            button.setAllCaps(false);
            button.setTextColor(ContextCompat.getColor(this, R.color.black));
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    topicEditText.setText(topic);
                }
            });
            flexboxLayout.addView(button);
        }
    }

    public void initData() {
        ArrayList<ChoosedItem> choosedItems = getIntent().getParcelableArrayListExtra(ACTIVITY_CHAT_ADD_TO_REVIEW_CARD);
        if (choosedItems!=null) {
            question = choosedItems.get(0).text;
            answer = choosedItems.get(1).text;
        } else {
            question = getIntent().getStringExtra(ActivityIntentKeys.ACTIVITY_ADD_REVIEW_SENTENCE_CARD_QUESTION);
            answer = getIntent().getStringExtra(ActivityIntentKeys.ACTIVITY_ADD_REVIEW_SENTENCE_CARD_ANSWER);
        }

        topic = getIntent().getStringExtra(ActivityIntentKeys.ACTIVITY_ADD_REVIEW_SENTENCE_CARD_TOPIC);

        questionEditText.setText(question);
        answerEditText.setText(answer);
        topicEditText.setText(topic);
    }


    ArrayList<SentenceCard> editedSentencesCards = new ArrayList<>();

    private void setResult() {
        Intent resultIntent = new Intent();
        SentenceCard sentenceCard = new SentenceCard(answer, question, topic);
        if (!editedSentencesCards.contains(sentenceCard)){
            editedSentencesCards.add(sentenceCard);
        }
        resultIntent.putParcelableArrayListExtra(ACTIVITY_REVIEW_CARD_EDITED_SENTENCES_LIST, editedSentencesCards);
        setResult(RESULT_OK, resultIntent);
    }


    public void initSubmitButtonClicked() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newQuestion = questionEditText.getText().toString();
                String newAnswer = answerEditText.getText().toString();
                topic = topicEditText.getText().toString();
                if (topic == null || topic.isEmpty()) {
                    topic = "default";
                }

                SentenceCard newCard = new SentenceCard(newAnswer, newQuestion, topic);
                if (answer == null && question == null) {
                    ReviewCardManager.getInstance().addOneSentenceCardInTopicReviewSets(topic, newCard);
                } else {
                    assert answer != null;
                    assert question != null;
                    SentenceCard oldCard = new SentenceCard(answer, question, topic);
                    ReviewCardManager.getInstance().addOneSentenceCardInTopicReviewSets(topic, oldCard, newCard);
                }

                answer = newAnswer;
                question = newQuestion;

                setResult();

                ReviewCardManager.getInstance().setEditedSentenceCard(newCard);
                Toast.makeText(AddReviewCardActivity.this,
                        "Question: " + question + "\nAnswer: " + answer,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

}
