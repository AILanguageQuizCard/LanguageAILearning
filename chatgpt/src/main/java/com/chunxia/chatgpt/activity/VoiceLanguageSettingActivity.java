package com.chunxia.chatgpt.activity;

import android.content.res.Resources;
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
import com.chunxia.chatgpt.adapter.settingItem.VoiceLanguageSettingAdapter;
import com.chunxia.chatgpt.mmkv.CXMMKV;
import com.chunxia.chatgpt.mmkv.MMKVConstant;
import com.chunxia.chatgpt.tools.GoogleTextToVoiceLanguageTools;

import java.util.ArrayList;

public class VoiceLanguageSettingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VoiceLanguageSettingAdapter adapter;

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
        adapter = new VoiceLanguageSettingAdapter(getLanguageData());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<SettingInfo> getLanguageData() {
        ArrayList<SettingInfo> arrayList = new ArrayList<>();
        String setLanguage = CXMMKV.getInstance().getMMKV().getString(MMKVConstant.SETTING_VOICE_LANGUAGE_KEY,
                MMKVConstant.SETTING_VOICE_LANGUAGE_DEFAULT_VALUE);

        ArrayList<GoogleTextToVoiceLanguageTools.GoogleTextToVoiceLanguage> googleLanguages =
                GoogleTextToVoiceLanguageTools.getLanguages(this);
        for (GoogleTextToVoiceLanguageTools.GoogleTextToVoiceLanguage lang: googleLanguages) {
            arrayList.add(new SettingInfo(lang.getLanguageName(), lang.getLanguageName().equals(setLanguage)));
        }

        return arrayList;
    }

}
