package com.chunxia.chatgpt.adapter.chat;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.model.Message;
import com.chunxia.chatgpt.model.TextMessage;
import com.chunxia.chatgpt.model.VoiceMessage;
import com.chunxia.chatgpt.texttovoice.Text2VoiceModel;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterChatGptChat";

    private final int VOICE_INITIAL = 0;
    private final int VOICE_PLAYING = 1;
    private final int VOICE_PAUSED = 2;

    private ArrayList<Message> items = new ArrayList<>();

    public ArrayList<Message> getItems() {
        return items;
    }

    private final Application application;

    public ChatAdapter(Application app) {
        application = app;
    }


    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == Constant.CHAT_ME) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_chatgpt_me, parent, false);
            vh = new ChatItemViewHolder(v, viewType);
        } else if ( viewType == Constant.CHAT_YOU){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_chaggpt_you, parent, false);
            vh = new ChatItemViewHolder(v, viewType);
        } else if (viewType == Constant.CHAT_ME_VOICE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_voice_message, parent, false);
            vh = new VoiceMessageItemViewHolder(v);
        }
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ChatItemViewHolder) {
            int realPosition = holder.getAdapterPosition();

            if (items.get(realPosition) instanceof TextMessage) {
                final TextMessage m = (TextMessage) items.get(realPosition);
                ChatItemViewHolder vItem = (ChatItemViewHolder) holder;
                vItem.textContentView.setText(m.getContent());
                if (vItem.viewType == Constant.CHAT_ME) {
                    vItem.textTimeView.setText(m.getDate());
                }

                ((ChatItemViewHolder) holder).setText2VoiceModel(new Text2VoiceModel(application));
                setOnClickPlayVoiceButton(vItem, m);
                setOnClickCopyContentButton(vItem);
            }

        } else if (holder instanceof VoiceMessageItemViewHolder) {
            VoiceMessageItemViewHolder vItem = (VoiceMessageItemViewHolder) holder;
            int realPosition = holder.getAdapterPosition();
            if (items.get(realPosition) instanceof VoiceMessage) {
                final VoiceMessage m = (VoiceMessage) items.get(realPosition);
                vItem.recordingPlayerView.setAudio(m.getPath());
            }
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
                    if (!(m instanceof TextMessage)) {
                        return;
                    }

                    if(vItem.voiceStatus == VOICE_INITIAL) {

                        // google cloud 的文字转语音模型，读中文的时候，会把最后的句号读出来，所以，直接把句号替换成逗号
                        String realS =((TextMessage) m).getContent().replace("。", ",");
                        MediaPlayer.OnCompletionListener comletionCallback = new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                vItem.playVoiceButton.setImageResource(R.drawable.ic_play_arrow);
                                vItem.voiceStatus = VOICE_INITIAL;
                            }
                        };
                        // todo 已经从google cloud获取到语音的，不需要再次请求
                        vItem.getText2VoiceModel().onSpeak(realS, comletionCallback, new CompletableObserver() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                            }

                            @Override
                            public void onComplete() {
                                Log.i(TAG, "speak success");
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e(TAG, "Speak failed", e);
                            }
                        } );
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

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!this.items.get(position).isVoice()) {
            return this.items.get(position).isFromMe() ? Constant.CHAT_ME : Constant.CHAT_YOU;
        } else {
            return Constant.CHAT_ME_VOICE;
        }
    }

    public void insertItem(Message item) {
        this.items.add(item);
        notifyItemInserted(getItemCount());
    }

    public void setItems(ArrayList<Message> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}
