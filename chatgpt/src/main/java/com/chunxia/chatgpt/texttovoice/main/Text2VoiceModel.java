package com.chunxia.chatgpt.texttovoice.main;

import android.app.Application;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import darren.googlecloudtts.GoogleCloudTTS;
import darren.googlecloudtts.model.VoicesList;
import darren.googlecloudtts.parameter.AudioConfig;
import darren.googlecloudtts.parameter.AudioEncoding;
import darren.googlecloudtts.parameter.VoiceSelectionParams;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

/**
 * Author: Changemyminds.
 * Date: 2020/12/17.
 * Description:
 * Reference:
 */
public class Text2VoiceModel extends AndroidViewModel {
    private GoogleCloudTTS mGoogleCloudTTS;
    private final VoicesList mVoicesList = new VoicesList();

    public Text2VoiceModel(@NonNull Application application, GoogleCloudTTS googleCloudTTS) {
        super(application);
        mGoogleCloudTTS = googleCloudTTS;
    }

    public Single<VoicesList> loading() {
        return Single.fromCallable(() -> mGoogleCloudTTS.load())
                .doOnSuccess(v -> {
                    mVoicesList.clear();
                    mVoicesList.update(v);
                });
    }

    public Completable speak(String text, MediaPlayer.OnCompletionListener comletionCallback) {
        return fromCallable(() -> mGoogleCloudTTS.start(text, comletionCallback));
    }

    public void pause() {
        mGoogleCloudTTS.pause();
    }

    public void resume() {
        mGoogleCloudTTS.resume();
    }


    public void dispose() {
        mGoogleCloudTTS.close();
    }

    public void initTTSVoice(String languageCode, String voiceName, float pitch, float speakRate) {
        mGoogleCloudTTS.setVoiceSelectionParams(new VoiceSelectionParams(languageCode, voiceName))
                .setAudioConfig(new AudioConfig(
                        AudioEncoding.MP3,
                        speakRate,
                        pitch
                ));
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
