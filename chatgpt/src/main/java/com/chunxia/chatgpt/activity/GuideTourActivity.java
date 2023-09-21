package com.chunxia.chatgpt.activity;

import static com.chunxia.chatgpt.mmkv.MMKVConstant.GUIDE_TOUR_MMKV_NAME;
import static com.chunxia.chatgpt.mmkv.MMKVConstant.GUIDE_TOUR_SHOWED_KEY;

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

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.base.BaseActivity;
import com.chunxia.mmkv.KVUtils;

import java.util.ArrayList;
import java.util.Arrays;


public class GuideTourActivity extends BaseActivity {
    private static final int MAX_STEP = 4;

    private ViewPager viewPager;
    private Button btnNext;
    private MyViewPagerAdapter myViewPagerAdapter;

    ArrayList<String> aboutTitleList = new ArrayList<>() ;
    ArrayList<String> aboutDescriptionList = new ArrayList<>();
    ArrayList<Integer> aboutImagesList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide_tour;
    }

    @Override
    protected void initView() {
        if (getGuideTourShowed()) {
            jumpToMainActivity();
        }
        setStatusBarColor(R.color.guide_tour_color);
    }


    @Override
    protected void initData() {
        aboutImagesList.add(R.drawable.women_learning_language);
        aboutImagesList.add(R.drawable.effortlessly_create_quizcards);
        aboutImagesList.add(R.drawable.talk_to_robot);
        aboutImagesList.add(R.drawable.tailor_learning_content);

        aboutTitleList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.about_title_array)));
        aboutDescriptionList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.about_description_array)));
    }

    private Boolean getGuideTourShowed() {
        return KVUtils.get().getBoolean(GUIDE_TOUR_MMKV_NAME, GUIDE_TOUR_SHOWED_KEY, false);
    }


    private void setGuideTourShowed(boolean showed) {
        KVUtils.get().putBoolean(GUIDE_TOUR_MMKV_NAME, GUIDE_TOUR_SHOWED_KEY, showed);
    }


    private void jumpToMainActivity() {
        Intent intent = new Intent(this, BottomNavigationLightActivity.class);
        // 必须先跳转，再结束activity，否则会呈现一个类似跳转动画的效果
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager = (ViewPager) findViewById(R.id.guide_tour_view_pager);
        btnNext = (Button) findViewById(R.id.guide_tour_btn_next);

        // adding bottom dots
        bottomProgressDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(0, 0, 0, 0);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(com.material.components.R.dimen.viewpager_margin_overlap));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem() == aboutTitleList.size() - 1) {
                    btnNext.setText("Get Started");
                } else {
                    btnNext.setText("Continue");
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
                    setGuideTourShowed(true);
                    jumpToMainActivity();
                }
            }
        });

    }


    private void bottomProgressDots(int current_index) {

        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.guide_tour_layout_dots);

        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(com.material.components.R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(com.material.components.R.color.grey_20), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(com.material.components.R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(com.material.components.R.color.light_green_600), PorterDuff.Mode.SRC_IN);
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

            View view = layoutInflater.inflate(R.layout.item_guide_tour, container, false);
            ((TextView) view.findViewById(R.id.item_guide_tour_title)).setText(aboutTitleList.get(position));
            ((TextView) view.findViewById(R.id.item_guide_tour_descripion)).setText(aboutDescriptionList.get(position));
            ((ImageView) view.findViewById(R.id.item_guide_tour_image)).setImageResource(aboutImagesList.get(position));

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return aboutTitleList.size();
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