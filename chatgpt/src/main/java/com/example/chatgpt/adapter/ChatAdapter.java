package com.example.chatgpt.adapter;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.chatgpt.R;
import com.example.chatgpt.texttovoice.main.Text2VoiceModel;
import com.example.chatgpt.texttovoice.main.TextToVoiceSetting;
import com.material.components.model.Message;

import java.util.ArrayList;
import java.util.List;

import darren.googlecloudtts.BuildConfig;
import darren.googlecloudtts.GoogleCloudTTS;
import darren.googlecloudtts.GoogleCloudTTSFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterChatGptChat";
    private final int CHAT_ME = 100;
    private final int CHAT_YOU = 200;

    private final int VOICE_INITIAL = 0;
    private final int VOICE_PLAYING = 1;
    private final int VOICE_PAUSED = 2;

    private List<Message> items = new ArrayList<>();

    private final Application application;

    public ChatAdapter(Application app) {
        application = app;
    }

    public class ChatItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textContentView;
        public TextView textTimeView;
        public View lytParentView;
        public ImageView playVoiceButton;
        public ImageView copyContentButton;
        public int voiceStatus = VOICE_INITIAL;
        public int viewType;
        private Text2VoiceModel text2VoiceModel;


        public ChatItemViewHolder(View v, int viewType) {
            super(v);
            textContentView = v.findViewById(R.id.text_content);
            lytParentView = v.findViewById(R.id.lyt_parent);

            if (viewType == CHAT_ME) {
                textTimeView = v.findViewById(R.id.text_time);
            }

            if (viewType == CHAT_YOU) {
                playVoiceButton = v.findViewById(R.id.whatsapp_telegram_you_play_imageview);
                copyContentButton = v.findViewById(R.id.whatsapp_telegram_you_copy_imageview);
            }
            this.viewType = viewType;
        }

        public Text2VoiceModel getText2VoiceModel() {
            return text2VoiceModel;
        }

        public void setText2VoiceModel(Text2VoiceModel text2VoiceModel) {
            this.text2VoiceModel = text2VoiceModel;
        }
    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == CHAT_ME) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_chatgpt_me, parent, false);
            vh = new ChatItemViewHolder(v, viewType);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_chaggpt_you, parent, false);
            vh = new ChatItemViewHolder(v, viewType);
        }
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ChatItemViewHolder) {
            int realPosition = holder.getAdapterPosition();
            final Message m = items.get(realPosition);
            ChatItemViewHolder vItem = (ChatItemViewHolder) holder;
            vItem.textContentView.setText(m.getContent());
            if (vItem.viewType == CHAT_ME) {
                vItem.textTimeView.setText(m.getDate());
            }
            GoogleCloudTTS googleCloudTTS = GoogleCloudTTSFactory.create(BuildConfig.API_KEY);
            ((ChatItemViewHolder) holder).setText2VoiceModel(new Text2VoiceModel(application, googleCloudTTS));
            setOnClickPlayVoiceButton(vItem, m);
            setOnClickCopyContentButton(vItem);
        }
    }

    void setOnClickCopyContentButton(ChatItemViewHolder vItem) {
        if (vItem.copyContentButton != null) {
            vItem.copyContentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) application.getSystemService(Context.CLIPBOARD_SERVICE);
                    String text = String.valueOf(vItem.textContentView.getText());
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                    ToastUtils.make().setDurationIsLong(false).show(R.string.chat_copy_successfully);
                }
            });
        }
    }


    void setOnClickPlayVoiceButton(ChatItemViewHolder vItem, Message m) {
        if (vItem.playVoiceButton != null) {
            vItem.playVoiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(vItem.voiceStatus == VOICE_INITIAL) {

                        // google cloud 的文字转语音模型，读中文的时候，会把最后的句号读出来，所以，直接把句号替换成逗号
                        String realS = m.getContent().replace("。", ",");
                        MediaPlayer.OnCompletionListener comletionCallback = new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                vItem.playVoiceButton.setImageResource(R.drawable.ic_play_arrow);
                                vItem.voiceStatus = VOICE_INITIAL;
                            }
                        };
                        onSpeak(vItem.getText2VoiceModel(), realS, comletionCallback);
                        vItem.playVoiceButton.setImageResource(R.drawable.ic_pause_black);
                        vItem.voiceStatus = VOICE_PLAYING;

                    } else if ( vItem.voiceStatus == VOICE_PLAYING) {
                        vItem.playVoiceButton.setImageResource(R.drawable.ic_play_arrow);
                        vItem.voiceStatus = VOICE_PAUSED;
                        vItem.getText2VoiceModel().pause();

                    } else if (vItem.voiceStatus == VOICE_PAUSED) {
                        vItem.playVoiceButton.setImageResource(R.drawable.ic_pause_black);
                        vItem.voiceStatus = VOICE_PLAYING;
                        vItem.getText2VoiceModel().resume();
                    }

                }
            });
        }
    }

    void onSpeak(Text2VoiceModel text2VoiceModel, String text, MediaPlayer.OnCompletionListener comletionCallback) {
        text2VoiceModel.speak(text, comletionCallback)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(t -> initTTSVoice(text2VoiceModel))
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {;
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "speak success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "Speak failed", e);
                    }
                });
    }

    private void initTTSVoice(Text2VoiceModel text2VoiceModel) {
        String languageCode = TextToVoiceSetting.getLanguageCode();
        String voiceName = TextToVoiceSetting.getVoiceName();
        float pitch = TextToVoiceSetting.getPitch();
        float speakRate = TextToVoiceSetting.getSpeakRate();
        text2VoiceModel.initTTSVoice(languageCode, voiceName, pitch, speakRate);
    }


    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).isFromMe() ? CHAT_ME : CHAT_YOU;
    }

    public void insertItem(Message item) {
        this.items.add(item);
        notifyItemInserted(getItemCount());
    }

    public void setItems(List<Message> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}