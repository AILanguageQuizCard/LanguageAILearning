package com.example.chatgpt.texttovoice.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.chatgpt.R;

import darren.googlecloudtts.BuildConfig;
import darren.googlecloudtts.GoogleCloudTTS;
import darren.googlecloudtts.GoogleCloudTTSFactory;
import darren.googlecloudtts.model.VoicesList;
import darren.googlecloudtts.parameter.VoiceSelectionParams;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Author: Changemyminds.
 * Date: 2018/6/25.
 * Description:
 * Reference:
 */
public class textToVoiceActivity extends AppCompatActivity {
//    private static final String TAG = "MainActivity";
//
//    private static final long WAIT_TIME = 2000L;
//    private static final int TEXT_TO_SPEECH_CODE = 0x100;
//
//    private long TOUCH_TIME = 0;
//
//    Spinner mSpinnerLanguage;
//
//    Spinner mSpinnerStyle;
//
//    EditText mEditText;
//
//    TextView mTextViewGender;
//
//    TextView mTextViewSampleRate;
//
//    SeekBar mSeekBarPitch;
//
//    SeekBar mSeekBarSpeakRate;
//
//    Button mButtonSpeakPause;
//
//    Button mSpeakButton;
//
//    Button mRefreshButton;
//
//    private void initView() {
//        mSpinnerLanguage = findViewById(R.id.snrIdLanguage);
//        mSpinnerStyle = findViewById(R.id.snrIdStyle);
//        mEditText = findViewById(R.id.ettIdText);
//        mTextViewGender = findViewById(R.id.tvwIdGender);
//        mTextViewSampleRate = findViewById(R.id.tvwIdSampleRate);
//        mSeekBarPitch = findViewById(R.id.sbrIdPitch);
//        mSeekBarSpeakRate = findViewById(R.id.sbrIdSpeakRate);
//        mButtonSpeakPause = findViewById(R.id.btnIdPauseAndResume);
//        mSpeakButton = findViewById(R.id.btnIdSpeak);
//        mRefreshButton = findViewById(R.id.btnIdRefresh);
//    }
//
//
//    private MainViewModel mMainViewModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_text_to_voice);
//
//        initView();
//        initViewValues();
//
//        GoogleCloudTTS googleCloudTTS = GoogleCloudTTSFactory.create(BuildConfig.API_KEY);
//        mMainViewModel = new MainViewModel(getApplication(), googleCloudTTS);
//        initOnclick();
//        onLoading();
//    }
//
//    @Override
//    protected void onDestroy() {
//        mMainViewModel.dispose();
//        super.onDestroy();
//    }
//
//    private void initViewValues() {
//        mSeekBarPitch.setMax(4000);
//        mSeekBarPitch.setProgress(2000);
//
//        mSeekBarSpeakRate.setMax(375);
//        mSeekBarSpeakRate.setProgress(75);
//    }
//
//
//    private void initOnclick() {
//        mSpeakButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onSpeak();
//            }
//        });
//        mButtonSpeakPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onPauseOrResume();
//            }
//        });
//        mRefreshButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onRefresh();
//            }
//        });
//
//    }
//
//    void onSpeak() {
//        mMainViewModel.speak(mEditText.getText().toString())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(t -> initTTSVoice())
//                .subscribe(new CompletableObserver() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        makeToast("Speak success", false);
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        makeToast("Speak failed " + e.getMessage(), true);
//                        Log.e(TAG, "Speak failed", e);
//                    }
//                });
//    }
//
//    void onPauseOrResume() {
//        boolean isPaused = mButtonSpeakPause.getText().toString().equals(getResources().getString(R.string.btnPtSpeechPause));
//        if (isPaused) {
//            mMainViewModel.pause();
//            mButtonSpeakPause.setText(getResources().getString(R.string.btnPtSpeechResume));
//            return;
//        }
//
//        mMainViewModel.resume();
//        mButtonSpeakPause.setText(getResources().getString(R.string.btnPtSpeechPause));
//    }
//
//    void onRefresh() {
//        onLoading();
//    }
//
//    private void onLoading() {
//        mMainViewModel.loading()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe((t) -> {
//                    mSeekBarPitch.setProgress(2000);
//                    mSeekBarSpeakRate.setProgress(75);
//                })
//                .subscribe(new SingleObserver<VoicesList>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                    }
//
//                    @Override
//                    public void onSuccess(@NonNull VoicesList voicesList) {
//                        setLanguages(voicesList.getLanguageCodes());
//                        makeToast("Google Cloud voice update.", false);
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        clearUI();
//                        makeToast("Loading Voice List Error, error code : " + e.getMessage(), true);
//                        Log.e(TAG, "Loading Voice List Error", e);
//                    }
//                });
//
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        exitApp();
//    }
//
//    private void setLanguages(final String[] languages) {
//        ArrayAdapter<String> adapterLanguage = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item
//                , languages);
//        adapterLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        mSpinnerLanguage.setAdapter(adapterLanguage);
//        mSpinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String[] voiceNames = mMainViewModel.getVoiceNames(languages[position]);
//                setStyles(voiceNames);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//    }
//
//    private String getSelectedLanguageCodeText() {
//        return (mSpinnerLanguage.getSelectedItem() != null) ? mSpinnerLanguage.getSelectedItem().toString() : "";
//    }
//
//    private void setStyles(final String[] styles) {
//        // init AdapterStyle
//        ArrayAdapter<String> adapterStyle = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item
//                , styles);
//        adapterStyle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // init mSpinnerStyle
//        mSpinnerStyle.setAdapter(adapterStyle);
//        mSpinnerStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                VoiceSelectionParams params = mMainViewModel.getVoiceSelectionParams(getSelectedLanguageCodeText(), styles[position]);
//                setTextViewGender(params.getSsmlGender().toString());
////                setTextViewSampleRate(String.valueOf(params.getNaturalSampleRateHertz()));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//    }
//
//    private void clearUI() {
//        mSpinnerLanguage.setAdapter(null);
//        mSpinnerStyle.setAdapter(null);
//        setTextViewGender("");
//        setTextViewSampleRate("");
//    }
//
//    private String getSelectedStyleText() {
//        return (mSpinnerStyle.getSelectedItem() != null) ? mSpinnerStyle.getSelectedItem().toString() : "";
//    }
//
//    private void setTextViewGender(String gender) {
//        mTextViewGender.setText(gender);
//    }
//
//    private void setTextViewSampleRate(String sampleRate) {
//        mTextViewSampleRate.setText(sampleRate);
//    }
//
//    private int getProgressPitch() {
//        return mSeekBarPitch.getProgress();
//    }
//
//    private int getProgressSpeakRate() {
//        return mSeekBarSpeakRate.getProgress();
//    }
//
//    private void makeToast(String text, boolean longShow) {
//        Toast.makeText(this, text, (longShow) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
//    }
//
//    private void exitApp() {
//        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
//            // exit app
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
//        } else {
//            TOUCH_TIME = System.currentTimeMillis();
//            makeToast("Press back again to Exit", false);
//        }
//    }
//
//    private void initTTSVoice() {
//        String languageCode = getSelectedLanguageCodeText();
//        String voiceName = getSelectedStyleText();
//        float pitch = ((float) (getProgressPitch() - 2000) / 100);
//        float speakRate = ((float) (getProgressSpeakRate() + 25) / 100);
//
//        mMainViewModel.initTTSVoice(languageCode, voiceName, pitch, speakRate);
//    }
}
