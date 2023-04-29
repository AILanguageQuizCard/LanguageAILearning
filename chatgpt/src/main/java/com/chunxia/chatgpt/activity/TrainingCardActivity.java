package com.chunxia.chatgpt.activity;

import static com.chunxia.chatgpt.activity.ActivityIntentKeys.TOPIC_TRAINING_RESULT_KEY;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.material.components.R;
import com.material.components.utils.Tools;

import java.util.ArrayList;

public class TrainingCardActivity extends AppCompatActivity {

    private static final int MAX_STEP = 10;

    private ViewPager viewPager;
    private Button btnNext;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<String> sentencesList = new ArrayList<>();
    private final ArrayList<Integer> imageList = new ArrayList<>();


    private void initData(ArrayList<String> resultList) {
        titleList.add("Ready to Travel");
        titleList.add("Pick the Ticket");
        titleList.add("Flight to Destination");
        titleList.add("Enjoy Holiday");
        titleList.add("Ready to Travel");
        titleList.add("Pick the Ticket");
        titleList.add("Ready to Travel");
        titleList.add("Ready to Travel");
        titleList.add("Ready to Travel");
        titleList.add("Ready to Travel");

        imageList.add(R.drawable.img_wizard_1);
        imageList.add(R.drawable.img_wizard_2);
        imageList.add(R.drawable.img_wizard_3);
        imageList.add(R.drawable.img_wizard_4);
        imageList.add(R.drawable.img_wizard_1);
        imageList.add(R.drawable.img_wizard_2);
        imageList.add(R.drawable.img_wizard_3);
        imageList.add(R.drawable.img_wizard_4);
        imageList.add(R.drawable.img_wizard_1);
        imageList.add(R.drawable.img_wizard_2);

        sentencesList = resultList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_wizard_overlap);

        Intent intent = getIntent();
        ArrayList<String> resultList = (ArrayList<String>) intent.getSerializableExtra(TOPIC_TRAINING_RESULT_KEY);
        initData(resultList);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        btnNext = (Button) findViewById(R.id.btn_next);

        // adding bottom dots
        bottomProgressDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
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


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = viewPager.getCurrentItem() + 1;
                if (current < MAX_STEP) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    finish();
                }
            }
        });

        Tools.setSystemBarColor(this, R.color.grey_10);
        Tools.setSystemBarLight(this);
    }


    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

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

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_card_wizard, container, false);
            ((TextView) view.findViewById(R.id.title)).setText(titleList.get(position));
            ((TextView) view.findViewById(R.id.description)).setText(sentencesList.get(position));
            ((ImageView) view.findViewById(R.id.image)).setImageResource(imageList.get(position));

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return titleList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}