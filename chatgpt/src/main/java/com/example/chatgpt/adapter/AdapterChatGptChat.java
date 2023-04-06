package com.example.chatgpt.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.example.chatgpt.R;
import com.example.chatgpt.texttovoice.main.MainViewModel;
import com.material.components.model.Message;

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
        public ImageView copyContentButton;
        public int viewType;

        public ItemViewHolder(View v, int viewType) {
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
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == CHAT_ME) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_chatgpt_me, parent, false);
            vh = new ItemViewHolder(v, viewType);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_chaggpt_you, parent, false);
            vh = new ItemViewHolder(v, viewType);
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
            if (vItem.viewType == CHAT_ME) {
                vItem.textTimeView.setText(m.getDate());
            }

            vItem.lytParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, m, position);
                    }
                }
            });
            setOnClickPlayVoiceButton(vItem, m);
            setOnClickCopyContentButton(vItem);
        }
    }

    void setOnClickCopyContentButton(ItemViewHolder vItem) {
        if (vItem.copyContentButton != null) {
            vItem.copyContentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                    String text = String.valueOf(vItem.textContentView.getText());
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                    ToastUtils.make().setDurationIsLong(false).show(R.string.chat_copy_successfully);
                }
            });
        }
    }


    void setOnClickPlayVoiceButton(ItemViewHolder vItem, Message m) {
        if (vItem.playVoiceButton != null) {
            vItem.playVoiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // google cloud 的文字转语音模型，读中文的时候，会把最后的句号读出来，所以，直接把句号替换成逗号
                    String realS = m.getContent().replace("。",",");
                    onSpeak(realS);
                }
            });
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