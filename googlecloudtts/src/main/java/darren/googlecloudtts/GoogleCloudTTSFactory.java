package darren.googlecloudtts;

import darren.googlecloudtts.api.GoogleText2VoiceAPIKey;
import darren.googlecloudtts.api.SynthesizeApi;
import darren.googlecloudtts.api.SynthesizeApiImpl;
import darren.googlecloudtts.api.VoicesApi;
import darren.googlecloudtts.api.VoicesApiImpl;

/**
 * Author: Changemyminds.
 * Date: 2020/12/17.
 * Description:
 * Reference:
 */
public class GoogleCloudTTSFactory {
    private volatile static GoogleCloudTTS googleCloudTTS;

    public static GoogleCloudTTS create() {
//        if (googleCloudTTS == null) {
//            synchronized (GoogleCloudTTSFactory.class) {
//                if (googleCloudTTS == null) {
//                    GoogleCloudAPIConfig config = new GoogleCloudAPIConfig(apiKey);
//                    googleCloudTTS = create(config);
//                }
//            }
//        }
        GoogleCloudAPIConfig config = new GoogleCloudAPIConfig(GoogleText2VoiceAPIKey.getApiKey());
        return create(config);
    }

    private static GoogleCloudTTS create(GoogleCloudAPIConfig config) {
        SynthesizeApi synthesizeApi = new SynthesizeApiImpl(config);
        VoicesApi voicesApi = new VoicesApiImpl(config);
        return new GoogleCloudTTS(synthesizeApi, voicesApi);
    }
}
