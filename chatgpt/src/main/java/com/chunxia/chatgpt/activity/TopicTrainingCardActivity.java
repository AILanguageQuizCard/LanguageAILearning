package com.chunxia.chatgpt.activity;

import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_ACTIVITY_LEARNING_MATERIAL_KEY;

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
import com.chunxia.chatgpt.activity.dataholder.DataHolder;
import com.chunxia.chatgpt.adapter.topiccard.LearningMaterialCardAdapter;
import com.chunxia.chatgpt.model.review.AllLearningMaterialCard;
import com.material.components.utils.Tools;

public class TopicTrainingCardActivity extends AppCompatActivity {

    private int currentCardNum;
    private ViewPager viewPager;
    private Button btnNext;
    private LearningMaterialCardAdapter adapter;
    private AllLearningMaterialCard learningMaterialCard;

    private void initData(AllLearningMaterialCard learningMaterialCard) {
        this.learningMaterialCard = learningMaterialCard;
        currentCardNum = getAllSize();
    }

    private int getAllSize() {
        if (learningMaterialCard == null) return 0;
        return learningMaterialCard.size();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_wizard_overlap);

        initData((AllLearningMaterialCard) DataHolder.getInstance().getData(TOPIC_TRAINING_ACTIVITY_LEARNING_MATERIAL_KEY));

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
        adapter = new LearningMaterialCardAdapter(this.getApplication(), this.learningMaterialCard);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
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
                if (viewPager.getCurrentItem() == getAllSize() - 1) {
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