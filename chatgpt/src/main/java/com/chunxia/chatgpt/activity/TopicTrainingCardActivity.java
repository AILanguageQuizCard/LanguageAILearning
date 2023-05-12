package com.chunxia.chatgpt.activity;

import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_ACTIVITY_TOPIC_KEY;
import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_RESULT_KEY;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.adapter.topiccard.TopicCardViewPagerAdapter;
import com.material.components.utils.Tools;

import java.util.ArrayList;

public class TopicTrainingCardActivity extends AppCompatActivity {

    private int currentCardNum = 10;
    private String currentTopic;
    private ViewPager viewPager;
    private Button btnNext;
    private TopicCardViewPagerAdapter topicCardViewPagerAdapter;
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> sentencesList = new ArrayList<>();
    private final ArrayList<Integer> imageList = new ArrayList<>();


    private void initData(ArrayList<String> resultList) {
        for(int i = 0; i < currentCardNum; i++) {
            titleList.add(currentTopic);
        }
        for(int i = 0; i < currentCardNum; i++) {
            imageList.add(R.drawable.img_wizard_1);
        }

        sentencesList = resultList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_wizard_overlap);

        Intent intent = getIntent();

        currentTopic = intent.getStringExtra(TOPIC_TRAINING_ACTIVITY_TOPIC_KEY);
        ArrayList<String> resultList = (ArrayList<String>) intent.getSerializableExtra(TOPIC_TRAINING_RESULT_KEY);
        currentCardNum = resultList.size();
        initData(resultList);

        // adding bottom dots
        bottomProgressDots(0);

        initNextButton();
        initViewPager();
        initSystemBar();
    }

    private void initSystemBar() {
        Tools.setSystemBarColor(this, R.color.grey_10);
        Tools.setSystemBarLight(this);
    }

    private void initNextButton() {
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = viewPager.getCurrentItem() + 1;
                if (current < currentCardNum) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    finish();
                }
            }
        });
    }

    private void initViewPager() {
        topicCardViewPagerAdapter = new TopicCardViewPagerAdapter(this.getApplication(), titleList, sentencesList);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(topicCardViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(0, 0, 0, 0);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin_overlap));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem() == titleList.size() - 1) {
                    btnNext.setText("Get Started");
                } else {
                    btnNext.setText("Next");
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