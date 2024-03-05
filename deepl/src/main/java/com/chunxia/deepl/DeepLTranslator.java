package com.chunxia.deepl;

import android.os.Build;

import androidx.annotation.WorkerThread;

import com.deepl.api.DeepLException;
import com.deepl.api.Translator;

public class DeepLTranslator {

    private static volatile DeepLTranslator instance = null;
    private Translator translator;

    private DeepLTranslator() {
        String authKey = DeepLManager.getDeepLApiKey();  // Replace with your key
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            translator = new Translator(authKey);
        }
    }

    public static DeepLTranslator getInstance() {
        if (instance == null) {
            synchronized (DeepLTranslator.class) {
                if (instance == null) {
                    instance = new DeepLTranslator();
                }
            }
        }
        return instance;
    }

    @WorkerThread
    public String translateText(String text, String sourceLang, String targetLang){
        try {
            return translator.translateText(text, sourceLang, targetLang).getText();
        } catch (DeepLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @WorkerThread
    public String translateText(String text, String targetLang) {
        try {
            return translator.translateText(text, null, targetLang).getText();
        } catch (DeepLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
