package com.chunxia.chatgpt.activity;

import static com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_REVIEW_CARD_EDITED_ANSWER;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_REVIEW_CARD_EDITED_QUESTION;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.model.review.ReviewCardManager;
import com.chunxia.chatgpt.model.review.SentenceCard;


public class AddReviewCardActivity extends AppCompatActivity {

    private EditText questionEditText;

    private EditText answerEditText;

    private Button submitButton;

    // todo fix it
    private String topic = "";

    private String question= "";

    private String answer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review_card);

        question = getIntent().getStringExtra(ActivityIntentKeys.ACTIVITY_ADD_REVIEW_SENTENCE_CARD_QUESTION);
        answer = getIntent().getStringExtra(ActivityIntentKeys.ACTIVITY_ADD_REVIEW_SENTENCE_CARD_ANSWER);
        topic = getIntent().getStringExtra(ActivityIntentKeys.ACTIVITY_ADD_REVIEW_SENTENCE_CARD_TOPIC);

        questionEditText = findViewById(R.id.questionEditText);
        answerEditText = findViewById(R.id.answerEditText);
        submitButton = findViewById(R.id.submitButton);

        initData(question, answer);
        initSubmitButtonClicked();
    }


    public void initData(String question, String answer) {
        if (question == null || answer == null || question.isEmpty() || answer.isEmpty()) {
            return;
        }

        questionEditText.setText(question);
        answerEditText.setText(answer);
    }



    private void setResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(ACTIVITY_REVIEW_CARD_EDITED_QUESTION, question); // 设置返回的数据，可选
        resultIntent.putExtra(ACTIVITY_REVIEW_CARD_EDITED_ANSWER, answer); // 设置返回的数据，可选
        setResult(RESULT_OK, resultIntent);
    }


    public void initSubmitButtonClicked() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newQuestion = questionEditText.getText().toString();
                String newAnswer = answerEditText.getText().toString();
                SentenceCard oldCard = new SentenceCard(answer, question);
                SentenceCard newCard = new SentenceCard(newAnswer, newQuestion);

                if (ReviewCardManager.getInstance().sentenceCardExistsInTopicReviewSets(topic, oldCard) ) {
                    ReviewCardManager.getInstance().editOneSentenceCardInTopicReviewSets(topic,
                            oldCard,
                            newCard);
                } else {
                    ReviewCardManager.getInstance().addOneSentenceCardInTopicReviewSets(topic, newCard);
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
