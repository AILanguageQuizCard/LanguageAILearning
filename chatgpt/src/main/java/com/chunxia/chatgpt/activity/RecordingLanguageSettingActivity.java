package com.chunxia.chatgpt.activity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.adapter.settingItem.RecordingLanguageSettingAdapter;
import com.chunxia.chatgpt.adapter.settingItem.SettingInfo;
import com.chunxia.chatgpt.mmkv.CXMMKV;
import com.chunxia.chatgpt.mmkv.MMKVConstant;
import com.chunxia.chatgpt.voicerecord.voicetotext.GoogleVoiceToTextLanguageTools;

import java.util.ArrayList;

public class RecordingLanguageSettingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecordingLanguageSettingAdapter adapter;

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
        adapter = new RecordingLanguageSettingAdapter(getLanguageData());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ((TextView) findViewById(R.id.voice_language_title_view)).setText(R.string.setting_item_recording_language);
    }

    private ArrayList<SettingInfo> getLanguageData() {
        ArrayList<SettingInfo> arrayList = new ArrayList<>();
        String setLanguage = CXMMKV.getInstance().getMMKV().getString(MMKVConstant.SETTING_RECORDING_VOICE_LANGUAGE_KEY,
                MMKVConstant.SETTING_RECORDING_VOICE_LANGUAGE_DEFAULT_VALUE);

        ArrayList<GoogleVoiceToTextLanguageTools.GoogleVoiceToTextLanguage> googleLanguages =
                GoogleVoiceToTextLanguageTools.getLanguages(this);
        for (GoogleVoiceToTextLanguageTools.GoogleVoiceToTextLanguage lang: googleLanguages) {
            arrayList.add(new SettingInfo(lang.getLanguageName(), lang.getLanguageName().equals(setLanguage)));
        }

        return arrayList;
    }

}
