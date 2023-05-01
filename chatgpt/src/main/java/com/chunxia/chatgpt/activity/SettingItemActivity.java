package com.chunxia.chatgpt.activity;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.adapter.settingItem.SettingInfo;
import com.chunxia.chatgpt.adapter.settingItem.SettingItemAdapter;

import java.util.ArrayList;

public class SettingItemActivity  extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SettingItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_setting_item);
        initStatusBar();
        initView();
    }

    private void initStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        WindowInsetsControllerCompat wic = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (wic != null) {
            // true表示Light Mode，状态栏字体呈黑色，反之呈白色
            wic.setAppearanceLightStatusBars(true);
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.language_setting_recyclerView);
        adapter = new SettingItemAdapter(getLanguageData());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<SettingInfo> getLanguageData() {
        ArrayList<SettingInfo> arrayList = new ArrayList<>();
        arrayList.add(new SettingInfo("中文", true));
        arrayList.add(new SettingInfo("English", false));
        arrayList.add(new SettingInfo("Spanish", false));
        arrayList.add(new SettingInfo("Danish", false));
        arrayList.add(new SettingInfo("Japanese", false));

        return arrayList;
    }

}
