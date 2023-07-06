package com.chunxia.chatgpt.activity;

import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_ACTIVITY_LEARNING_MATERIAL_KEY;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_ACTIVITY_MODE_KEY;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_GRAMMAR;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_SENTENCE_PATTERN;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_TOPIC;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.activity.dataholder.DataHolder;
import com.chunxia.chatgpt.adapter.topiccard.LearningMaterialCardAdapter;
import com.chunxia.chatgpt.chatapi.TrainingMaterial;
import com.chunxia.chatgpt.model.review.AllLearningMaterialCard;
import com.chunxia.chatgpt.model.review.SentenceCard;
import com.chunxia.chatgpt.model.review.TopicTestCard;
import com.chunxia.chatgpt.voicerecord.models.Events;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class TopicTrainingCardActivity extends AppCompatActivity {

    private static final String TAG = "TopicTrainingCardActivity";
    private int currentCardNum;

    private static final int MAX_CARD_NUM = 15;
    private ViewPager viewPager;

    private View parentView;
    private Button btnNext;
    private LearningMaterialCardAdapter adapter;
    private AllLearningMaterialCard learningMaterialCard;

    private ProgressBar progressBar;

    private LinearLayout container;
    private String mode;

    private void initData(AllLearningMaterialCard learningMaterialCard) {
        mode =  getIntent().getStringExtra(TOPIC_TRAINING_ACTIVITY_MODE_KEY);
        this.learningMaterialCard = learningMaterialCard;
        currentCardNum = getAllSize();
    }

    private int getAllSize() {
        if (learningMaterialCard == null) return 0;
        return learningMaterialCard.size();
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopAllVoice();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_wizard_overlap);

        EventBus.getDefault().register(this);
        initData((AllLearningMaterialCard) DataHolder.getInstance().getData(TOPIC_TRAINING_ACTIVITY_LEARNING_MATERIAL_KEY));

        // adding bottom dots
        bottomProgressDots(0);

        initView();
        initNextButton();
        initViewPager();
        initStatusBar();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        parentView = findViewById(android.R.id.content);
        container = (LinearLayout) findViewById(R.id.activity_train_card_container);
        progressBar = (ProgressBar) findViewById(R.id.activity_train_card_progress_bar);
        progressBar.setVisibility(View.GONE);
    }

    private void initStatusBar() {
        com.chunxia.chatgpt.tools.Tools.setSystemBarColor(this, R.color.white);
        com.chunxia.chatgpt.tools.Tools.setSystemBarLight(this);
    }


    private void initNextButton() {
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setVisibility(View.INVISIBLE);
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int current = viewPager.getCurrentItem() + 1;
//                if (current < currentCardNum) {
//                    // move to next screen
//                    viewPager.setCurrentItem(current);
//                } else {
//                    finish();
//                }
//            }
//        });
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


    private void insertNewCard(String topic, ArrayList<SentenceCard> sentenceCards, ArrayList<TopicTestCard> topicTestCards) {
        AllLearningMaterialCard newCards = new AllLearningMaterialCard(sentenceCards, topicTestCards, topic);
        learningMaterialCard.addSentenceCard(newCards.getSentenceCards());
        learningMaterialCard.addTopicTestCard(newCards.getTopicTestCards());

        progressBar.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);

        adapter.notifyDataSetChanged();
        int oldSize = currentCardNum;
        currentCardNum = getAllSize();
        viewPager.setCurrentItem(oldSize - 1);
        btnNext.setVisibility(View.INVISIBLE);
        bottomProgressDots(oldSize - 1);

    }

    private void getMoreData(String topic){
        progressBar.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);

        TrainingMaterial trainingMaterial = new TrainingMaterial();


        TrainingMaterial.ReceiveTrainMaterialCallback receiveTrainMaterialCallback = new TrainingMaterial.ReceiveTrainMaterialCallback() {
            @Override
            public void onReceiveData(ArrayList<SentenceCard> sentenceCards, ArrayList<TopicTestCard> topicTestCards) {
                insertNewCard(topic, sentenceCards, topicTestCards);
            }
        };

        if (isTopicTrainingMode()) {
            trainingMaterial.prepareSentenceData(topic, receiveTrainMaterialCallback);
        } else if (isSentencePatternTrainingMode()) {
            trainingMaterial.prepareSentencePatternExamplesData(topic, receiveTrainMaterialCallback);
        } else if (isGrammarTrainingMode()) {
            trainingMaterial.prepareGrammarExamplesData(topic, receiveTrainMaterialCallback);
        }
    }

    private void initViewPager() {
        adapter = new LearningMaterialCardAdapter(this.getApplication(), this.learningMaterialCard);
        viewPager = (ViewPager) findViewById(R.id.activity_train_card_view_pager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(30, 0, 30, 0);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin_overlap));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem() == getAllSize() - 1) {
                    btnNext.setText("generate more for me");
                    btnNext.setVisibility(View.VISIBLE);
                    btnNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currentCardNum >= MAX_CARD_NUM) return;
                            getMoreData(learningMaterialCard.getTopic());

                        }
                    });

                } else {
                    btnNext.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[currentCardNum];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.light_green_600), PorterDuff.Mode.SRC_IN);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void snackBarIconSuccess(Events.ShowSnackBar event) {
        final Snackbar snackbar = Snackbar.make(parentView, "", Snackbar.LENGTH_SHORT);
        //inflate view
        View custom_view = getLayoutInflater().inflate(com.material.components.R.layout.snackbar_icon_text, null);

        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
        Snackbar.SnackbarLayout snackBarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackBarView.setPadding(0, 0, 0, 0);


        ((TextView) custom_view.findViewById(com.material.components.R.id.message)).setText(event.getMessage());
        ((ImageView) custom_view.findViewById(com.material.components.R.id.icon)).setImageResource(com.material.components.R.drawable.ic_done);
        (custom_view.findViewById(com.material.components.R.id.parent_view)).setBackgroundColor(getResources().getColor(com.material.components.R.color.light_blue_300));
        snackBarView.addView(custom_view, 0);
        snackbar.show();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

}