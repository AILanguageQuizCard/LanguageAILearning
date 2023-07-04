package darren.googlecloudtts;


import com.chunxia.mmkv.KVUtils;

public class GoogleCloudText2VoiceManager {
    private static final String MMKV_NAME = "GoogleCloudText2VoiceManager";


    public static void setTextVoicePath(String s, String path) {
        KVUtils.get().putString(MMKV_NAME, s, path);
    }

    public static String getTextVoicePath(String s) {
        return KVUtils.get().getString(MMKV_NAME, s, "");
    }

    public static boolean isTextVoicePathExist(String s) {
        return KVUtils.get().contains(MMKV_NAME, s);
    }

}
