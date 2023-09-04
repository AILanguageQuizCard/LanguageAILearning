package com.chunxia.chatgpt.activity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.base.AppFragment;
import com.chunxia.chatgpt.base.BaseActivity;
import com.chunxia.chatgpt.base.FragmentPagerAdapter;
import com.chunxia.chatgpt.fragment.ChatGptReviewFragment;
import com.chunxia.chatgpt.fragment.ChatGptSettingFragment2;
import com.chunxia.chatgpt.fragment.ChatGptTasksFragment;
import com.chunxia.chatgpt.fragment.ChatGptTrainingFragment;
import com.chunxia.chatgpt.navigationbar.NavigationAdapter;
import com.chunxia.chatgpt.tools.Tools;


public class BottomNavigationLightActivity extends BaseActivity implements NavigationAdapter.OnNavigationListener{

    private ViewPager mViewPager;
    private RecyclerView mNavigationView;

    private NavigationAdapter mNavigationAdapter;
    private FragmentPagerAdapter<AppFragment<?>> mPagerAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bottom_navigation_light_chatgpt;
    }

    @Override
    protected void initView() {
        initComponent();
        initStatusBar();
    }

    @Override
    protected void initData() {
        mPagerAdapter = new FragmentPagerAdapter<>(this);
        mPagerAdapter.addFragment(ChatGptTasksFragment.newInstance());
        mPagerAdapter.addFragment(ChatGptTrainingFragment.newInstance());
        mPagerAdapter.addFragment(ChatGptReviewFragment.newInstance());
        mPagerAdapter.addFragment(ChatGptSettingFragment2.newInstance());
        mViewPager.setAdapter(mPagerAdapter);

        onNewIntent(getIntent());
    }

    @Override
    public boolean onNavigationItemSelected(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
                mViewPager.setCurrentItem(position);
                return true;
            default:
                return false;
        }
    }

    private void initStatusBar() {
        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);
    }


    private void initComponent() {
//        Tools.setSystemBarColor(this, R.color.grey_5);
//        Tools.setSystemBarLight(this);

        mViewPager = findViewById(R.id.vp_home_pager);
        mNavigationView = findViewById(R.id.rv_home_navigation);

        mNavigationAdapter = new NavigationAdapter(this);
        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem(getString(R.string.home_nav_index),
                ContextCompat.getDrawable(this, R.drawable.home_home_selector)));
        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem(getString(R.string.home_nav_found),
                ContextCompat.getDrawable(this, R.drawable.home_found_selector)));
        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem(getString(R.string.home_nav_message),
                ContextCompat.getDrawable(this, R.drawable.home_message_selector)));
        mNavigationAdapter.addItem(new NavigationAdapter.MenuItem(getString(R.string.home_nav_me),
                ContextCompat.getDrawable(this, R.drawable.home_me_selector)));
        mNavigationAdapter.setOnNavigationListener(this);
        mNavigationView.setAdapter(mNavigationAdapter);

    }


}