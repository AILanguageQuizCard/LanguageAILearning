package com.example.chatgpt.voicetotext;

import static com.example.chatgpt.voicerecord.helpers.ConstantsKt.WAV_SAMPLE_RATE;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.ThreadUtils;
import com.example.chatgpt.R;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.SpeechSettings;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class VoiceToTextModel {
    private static SpeechClient speechClient = null;
    private static boolean initBit = false;


    private static void setupGoogleCloudCredentials(Context context) {
        try {
            InputStream credentialsStream = context.getResources().openRawResource(R.raw.credential);
            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(credentialsStream);
            SpeechSettings speechSettings = SpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();
            speechClient = SpeechClient.create(speechSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void init(Application app) {
        if (!initBit){
            setupGoogleCloudCredentials(app);
            initBit = true;
        }
    }


    public static String transcribeAudioFile(String audioFilePath, Application application) {
        init(application);
        try {
            Path path = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                path = Paths.get(audioFilePath);
            }
            byte[] audioBytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                audioBytes = Files.readAllBytes(path);
            }

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(com.google.protobuf.ByteString.copyFrom(audioBytes))
                    .build();

            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(WAV_SAMPLE_RATE)
                    .setLanguageCode("cmn-Hans-CN") // Set the language code
                    .build();

            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            StringBuilder sb = new StringBuilder();
            for (SpeechRecognitionResult result : results) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                sb.append(alternative.getTranscript());
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

