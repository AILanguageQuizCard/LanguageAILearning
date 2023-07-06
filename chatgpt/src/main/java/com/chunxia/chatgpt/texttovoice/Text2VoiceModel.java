package com.chunxia.chatgpt.texttovoice;

import android.app.Application;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.chunxia.chatgpt.mmkv.MMKVConstant;
import com.chunxia.mmkv.KVUtils;

import darren.googlecloudtts.GoogleCloudTTS;
import darren.googlecloudtts.GoogleCloudTTSFactory;
import darren.googlecloudtts.model.VoicesList;
import darren.googlecloudtts.parameter.AudioConfig;
import darren.googlecloudtts.parameter.AudioEncoding;
import darren.googlecloudtts.parameter.VoiceSelectionParams;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Author: Changemyminds.
 * Date: 2020/12/17.
 * Description:
 * Reference:
 */
public class Text2VoiceModel extends AndroidViewModel {
    private final GoogleCloudTTS mGoogleCloudTTS;
    private final VoicesList mVoicesList = new VoicesList();

    public Text2VoiceModel(@NonNull Application application) {
        super(application);
        mGoogleCloudTTS = GoogleCloudTTSFactory.create();
        init();
    }

    public Single<VoicesList> loading() {
        return Single.fromCallable(mGoogleCloudTTS::load).doOnSuccess(v -> {
            mVoicesList.clear();
            mVoicesList.update(v);
        });
    }

    private void init() {
        String setLanguage = KVUtils.get().getString(MMKVConstant.SETTING_VOICE_LANGUAGE_KEY, MMKVConstant.SETTING_VOICE_LANGUAGE_DEFAULT_VALUE);

        String languageCode = TextToVoiceSetting.getLanguageCode(getApplication(), setLanguage);
        String voiceName = TextToVoiceSetting.getVoiceName(getApplication(), setLanguage);
        float pitch = TextToVoiceSetting.getPitch();
        float speakRate = TextToVoiceSetting.getSpeakRate();
        initTTSVoice(languageCode, voiceName, pitch, speakRate);
    }

    public void onSpeak(String text, MediaPlayer.OnCompletionListener completionCallback, CompletableObserver observer) {
        speak(text, completionCallback).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void onSpeak(String text) {
        speak(text, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });
    }


    public Completable speak(String text, MediaPlayer.OnCompletionListener completionCallback) {
        return fromCallable(() -> mGoogleCloudTTS.start(text, completionCallback));
    }

    public void pause() {
        mGoogleCloudTTS.pause();
    }

    public void stop() {
        mGoogleCloudTTS.stop();
    }

    public void resume() {
        mGoogleCloudTTS.resume();
    }


    public void dispose() {
        mGoogleCloudTTS.close();
    }

    private void initTTSVoice(String languageCode, String voiceName, float pitch, float speakRate) {
        mGoogleCloudTTS.setVoiceSelectionParams(new VoiceSelectionParams(languageCode, voiceName)).setAudioConfig(new AudioConfig(AudioEncoding.MP3, speakRate, pitch));
    }

    public String[] getVoiceNames(String languageCode) {
        return mVoicesList.getVoiceNames(languageCode);
    }

    public VoiceSelectionParams getVoiceSelectionParams(String language, String style) {
        return mVoicesList.getGCPVoice(language, style);
    }

    private Completable fromCallable(Runnable runnable) {
        return Completable.fromCallable(() -> {
            runnable.run();
            return Void.TYPE;
        });
    }


}
