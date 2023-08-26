package com.chunxia.chatgpt.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.subscription.SubscriptionManager;
import com.chunxia.chatgpt.tools.Tools;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class BottomNavigationLightActivity extends AppCompatActivity {

    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initSubscription();

        setContentView(R.layout.activity_bottom_navigation_light_chatgpt);
        initNavigation();

        initComponent();
        initStatusBar();

    }


    private void initStatusBar() {
        Tools.setSystemBarColor(this, R.color.white);
        Tools.setSystemBarLight(this);
    }

    private void initNavigation() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        NavController navController = Navigation.findNavController(this, R.id.fragment_chatgpt_container);
        NavigationUI.setupWithNavController(navigation, navController);

    }


    private void initComponent() {
//        Tools.setSystemBarColor(this, R.color.grey_5);
//        Tools.setSystemBarLight(this);
    }

    private void initSubscription() {
        SubscriptionManager.getInstance().initSubscribe(this);
    }


    boolean isNavigationHide = false;

    private void animateNavigation(final boolean hide) {
        if (isNavigationHide && hide || !isNavigationHide && !hide) return;
        isNavigationHide = hide;
        int moveY = hide ? (2 * navigation.getHeight()) : 0;
        navigation.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }


}