package com.material.components.chatgpt.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.material.components.R;
import com.material.components.model.Message;
import com.material.components.texttovoice.main.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdapterChatGptChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterChatGptChat";
    private final int CHAT_ME = 100;
    private final int CHAT_YOU = 200;

    private List<Message> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private MainViewModel mMainViewModel;

    public interface OnItemClickListener {

        void onItemClick(View view, Message obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterChatGptChat(Context context, MainViewModel mainViewModel) {
        mMainViewModel = mainViewModel;
        ctx = context;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textContentView;
        public TextView textTimeView;
        public View lytParentView;
        public ImageView playVoiceButton;

        public ItemViewHolder(View v) {
            super(v);
            textContentView = v.findViewById(R.id.text_content);
            textTimeView = v.findViewById(R.id.text_time);
            lytParentView = v.findViewById(R.id.lyt_parent);
            playVoiceButton = v.findViewById(R.id.whatsapp_telegram_you_play_imageview);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == CHAT_ME) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_telegram_me, parent, false);
            vh = new ItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_chaggpt_you, parent, false);
            vh = new ItemViewHolder(v);
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            final Message m = items.get(position);
            ItemViewHolder vItem = (ItemViewHolder) holder;
            vItem.textContentView.setText(m.getContent());
            vItem.textTimeView.setText(m.getDate());
            vItem.lytParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, m, position);
                    }
                }
            });

            if (vItem.playVoiceButton != null) {
                vItem.playVoiceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSpeak(m.getContent());
                    }
                });
            }
        }
    }

    void onSpeak(String text) {
        mMainViewModel.speak(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(t -> initTTSVoice())
                .subscribe(new CompletableObserver() {
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
                });
    }

    private void initTTSVoice() {
        String languageCode = "en-AU";
        String voiceName = "en-AU-Neural2-A";
        float pitch = 0.0f;
        float speakRate = 1.0f;
        mMainViewModel.initTTSVoice(languageCode, voiceName, pitch, speakRate);
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