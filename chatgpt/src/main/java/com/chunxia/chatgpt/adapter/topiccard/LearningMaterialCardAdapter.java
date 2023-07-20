package com.chunxia.chatgpt.adapter.topiccard;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.model.review.AllLearningMaterialCard;
import com.chunxia.chatgpt.model.review.ReviewCardManager;
import com.chunxia.chatgpt.texttovoice.Text2VoiceModel;
import com.chunxia.chatgpt.ui.ExpandTextView;
import com.chunxia.chatgpt.voicerecord.models.Events;

import org.greenrobot.eventbus.EventBus;

public class LearningMaterialCardAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater = null;
    private final Text2VoiceModel text2VoiceModel;
    private final Context context;

    private final AllLearningMaterialCard learningMaterialCard;

    public LearningMaterialCardAdapter(Application application, AllLearningMaterialCard learningMaterialCard) {
        this.context = application;
        text2VoiceModel = new Text2VoiceModel();
        this.learningMaterialCard = learningMaterialCard;
    }


    public void stopAllVoice() {
        text2VoiceModel.stop();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        View view = layoutInflater.inflate(R.layout.item_card_wizard, container, false);
        ((TextView) view.findViewById(R.id.topic_card_title)).setText(learningMaterialCard.getTopic());
        TextView sentenceView = view.findViewById(R.id.topic_card_sentence);
        sentenceView.setText(learningMaterialCard.getSentenceCards().get(position).getSentence());
        TextView translateView = view.findViewById(R.id.topic_card_translation);
        String translation = learningMaterialCard.getSentenceCards().get(position).getTranslation();
        ExpandTextView expandTextView = new ExpandTextView(context);
        expandTextView.setMaxLine(5)
                .setMargin(R.dimen.training_card_left_right_margin)
                .setColorStr("#346CE9");
        expandTextView.show(translateView, translation);
        ImageView playButton = view.findViewById(R.id.topic_card_play_button);
        ImageView favoriteButton = view.findViewById(R.id.topic_card_favorite_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2VoiceModel.onSpeak(learningMaterialCard.getSentenceCards().get(position).getSentence());
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewCardManager.getInstance().addOneSentenceCardInTopicReviewSets(learningMaterialCard.getTopic(),
                        learningMaterialCard.getSentenceCards().get(position));
                EventBus.getDefault().post(new Events.ShowSnackBar(context.getString(R.string.train_card_add_to_review_set_successfully)));

            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        // todo fix
//        Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'int com.chunxia.chatgpt.model.review.AllLearningMaterialCard.size()' on a null object reference
//        at com.chunxia.chatgpt.adapter.topiccard.LearningMaterialCardAdapter.getCount(LearningMaterialCardAdapter.java:82)
//        at androidx.viewpager.widget.ViewPager.setAdapter(ViewPager.java:532)
//        at com.chunxia.chatgpt.activity.TopicTrainingCardActivity.initViewPager(TopicTrainingCardActivity.java:217)
//        at com.chunxia.chatgpt.activity.TopicTrainingCardActivity.onCreate(TopicTrainingCardActivity.java:96)
//        at android.app.Activity.performCreate(Activity.java:8508)
//        at android.app.Activity.performCreate(Activity.java:8472)
//        at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1417)
//        at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:3783)
//        at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3948) 
//        at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:101) 
//        at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:135) 
//        at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:95) 
//        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2402) 
//        at android.os.Handler.dispatchMessage(Handler.java:106) 
//        at android.os.Looper.loopOnce(Looper.java:211) 
//        at android.os.Looper.loop(Looper.java:300) 
//        at android.app.ActivityThread.main(ActivityThread.java:8143) 
//        at java.lang.reflect.Method.invoke(Native Method) 
//        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:580) 
//        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1028) 
        if (learningMaterialCard == null) return 0;
        return learningMaterialCard.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
