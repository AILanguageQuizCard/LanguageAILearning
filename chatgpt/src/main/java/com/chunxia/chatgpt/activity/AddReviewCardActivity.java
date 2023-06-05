package com.chunxia.chatgpt.activity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review_card);

        String question = getIntent().getStringExtra(ActivityIntentKeys.SENTENCE_CARD_QUESTION);
        String answer = getIntent().getStringExtra(ActivityIntentKeys.SENTENCE_CARD_ANSWER);


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


    public void initSubmitButtonClicked() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = questionEditText.getText().toString();
                String answer = answerEditText.getText().toString();

                ReviewCardManager.addOneLearnCard(new SentenceCard(answer, question));

                Toast.makeText(AddReviewCardActivity.this,
                        "Question: " + question + "\nAnswer: " + answer,
                        Toast.LENGTH_LONG).show();
            }
        });

    }

}
