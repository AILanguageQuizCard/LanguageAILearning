package darren.googlecloudtts;

import android.media.MediaPlayer;
import android.util.Log;

import com.chunxia.mmkv.KVUtils;

import java.io.IOException;

import darren.googlecloudtts.api.SynthesizeApi;
import darren.googlecloudtts.api.VoicesApi;
import darren.googlecloudtts.model.VoicesList;
import darren.googlecloudtts.parameter.AudioConfig;
import darren.googlecloudtts.exception.ApiException;
import darren.googlecloudtts.parameter.VoiceSelectionParams;
import darren.googlecloudtts.parameter.SynthesisInput;
import darren.googlecloudtts.request.SynthesizeRequest;
import darren.googlecloudtts.response.SynthesizeResponse;
import darren.googlecloudtts.response.VoicesResponse;

/**
 * Author: Changemyminds.
 * Date: 2018/6/24.
 * Description:
 * Reference:
 */
public class GoogleCloudTTS implements AutoCloseable {
    private SynthesizeApi mSynthesizeApi;
    private VoicesApi mVoicesApi;

    private VoiceSelectionParams mVoiceSelectionParams;
    private AudioConfig mAudioConfig;

    private MediaPlayer mMediaPlayer;

    private int mVoiceLength = -1;

    public GoogleCloudTTS(SynthesizeApi synthesizeApi, VoicesApi voicesApi) {
        mSynthesizeApi = synthesizeApi;
        mVoicesApi = voicesApi;
    }

    public GoogleCloudTTS setVoiceSelectionParams(VoiceSelectionParams voiceSelectionParams) {
        mVoiceSelectionParams = voiceSelectionParams;
        return this;
    }

    public GoogleCloudTTS setAudioConfig(AudioConfig audioConfig) {
        mAudioConfig = audioConfig;
        return this;
    }

    public VoicesList load() {
        VoicesResponse response = mVoicesApi.get();
        VoicesList voicesList = new VoicesList();

        for (VoicesResponse.Voices voices : response.getVoices()) {
            String languageCode = voices.getLanguageCodes().get(0);
            VoiceSelectionParams params = new VoiceSelectionParams(
                    languageCode,
                    voices.getName(),
                    voices.getSsmlGender()
            );
            voicesList.add(languageCode, params);
        }

        return voicesList;
    }

    public void start(String audioKey, MediaPlayer.OnCompletionListener completionListener) {
        if (mVoiceSelectionParams == null) {
            throw new NullPointerException("You forget to setVoiceSelectionParams()");
        }

        if (mAudioConfig == null) {
            throw new NullPointerException("You forget to setAudioConfig()");
        }
        try {
            loadAudio(KVUtils.get().getString(audioKey), completionListener);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mVoiceLength = mMediaPlayer.getCurrentPosition();
        }
    }

    public void resume() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying() && mVoiceLength != -1) {
            mMediaPlayer.seekTo(mVoiceLength);
            mMediaPlayer.start();
        }
    }

    public void initAudio(String audioKey, String audioText) {
        SynthesizeRequest request = new SynthesizeRequest(new SynthesisInput(audioText), mVoiceSelectionParams, mAudioConfig);
        try {
            SynthesizeResponse response = mSynthesizeApi.get(request);
            String url = "data:audio/mp3;base64," + response.getAudioContent();
            KVUtils.get().putString(audioKey, url);
        } catch (Exception e) {
            throw new ApiException(e);
        }
    }


    private void loadAudio(String url, MediaPlayer.OnCompletionListener completionListener) throws IOException {
        stop();
        Log.d("GoogleCloudTTS", "loadCachedAudio: " + url);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(completionListener);
        mMediaPlayer.setDataSource(url);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }

    public void close() {
        stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}
