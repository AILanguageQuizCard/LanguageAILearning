package com.chunxia.chatgpt.adapter.chat;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.model.message.Message;
import com.chunxia.chatgpt.model.message.TextMessage;
import com.chunxia.chatgpt.model.message.VoiceMessage;
import com.chunxia.chatgpt.texttovoice.Text2VoiceModel;
import com.chunxia.chatgpt.tools.Tools;
import com.chunxia.chatgpt.voicerecord.models.Events;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Comparator;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterChatGptChat";

    private final int VOICE_INITIAL = 0;
    private final int VOICE_PLAYING = 1;
    private final int VOICE_PAUSED = 2;

    private ArrayList<Message> items = new ArrayList<>();

    public ArrayList<Message> getItems() {
        return items;
    }

    private ArrayList<ChoosedItem> choosedItems = new ArrayList<>();

    private boolean shouldShowHiddenView = false;

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
                if (vItem.viewType == Constant.CHAT_YOU) {
                    if (m.getText2VoiceModel()!= null ) {
                        m.getText2VoiceModel().stop();
                        ((ChatItemViewHolder) holder).setText2VoiceModel(m.getText2VoiceModel());
                    } else {
                        Text2VoiceModel text2VoiceModel1 = new Text2VoiceModel();
                        ((ChatItemViewHolder) holder).setText2VoiceModel(text2VoiceModel1);
                        m.setText2VoiceModel(text2VoiceModel1);
                    }
                    setOnClickPlayVoiceButton(vItem, m, realPosition);
                    setOnClickCopyContentButton(vItem);
                    setOnClickAddToQuizCardButton(vItem);
                }

//                setLongClick((ChatItemViewHolder) holder);
                showChooseView((ChatItemViewHolder) holder, m.isFromMe(), realPosition);
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


    public ArrayList<ChoosedItem> getChoosedItems(){
        return choosedItems;
    }


    void addOneItemToChoosedItems(ChatItemViewHolder holder, int position) {
        holder.chooseButton.setImageResource(R.drawable.ic_selected3);
        holder.selected = true;
        ChoosedItem item = new ChoosedItem(position, holder.textContentView.getText().toString());
        choosedItems.add(item);
    }

    void removeOneItemFromChoosedItems(ChatItemViewHolder holder, int position) {
        holder.chooseButton.setImageResource(R.drawable.ic_select3);
        holder.selected = false;
        ChoosedItem item = new ChoosedItem(position, holder.textContentView.getText().toString());
        choosedItems.remove(item);
    }

    void showChooseView(ChatItemViewHolder holder, boolean isFromMe, int position) {
        if (shouldShowHiddenView) {
            holder.chooseButton.setVisibility(View.VISIBLE);
            setParentViewMargin(holder, isFromMe, 10);

            holder.chooseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.selected) {
                        removeOneItemFromChoosedItems(holder, position);
                    } else {
                        addOneItemToChoosedItems(holder, position);
                    }
                    choosedItems.sort(Comparator.comparingInt(ChoosedItem::getPosition));
                }
            });

        } else {
            holder.chooseButton.setVisibility(View.GONE);
            removeOneItemFromChoosedItems(holder, position);
            setParentViewMargin(holder, isFromMe, 35);
        }
    }

    private static void setParentViewMargin(ChatItemViewHolder holder, boolean isFromMe, int margin) {
        if (!isFromMe) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.lytParentView.getLayoutParams();
            // todo 原来的右边距 - 选中按钮的宽度 = 10，此处不应该写死的
            layoutParams.setMargins(0, 0, Tools.dpToPx(holder.lytParentView.getContext(), margin), 0);
            holder.lytParentView.setLayoutParams(layoutParams);

        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.meCardViewContainer.getLayoutParams();
            layoutParams.setMargins(Tools.dpToPx(holder.meCardViewContainer.getContext(), margin), 0, 0, 0);
            holder.meCardViewContainer.setLayoutParams(layoutParams);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void showChooseView() {
        shouldShowHiddenView = true;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void hideChooseView() {
        shouldShowHiddenView = false;
        notifyDataSetChanged();
    }


    void chooseQuestionAndAnswer() {
        showChooseView();
        EventBus.getDefault().post(new Events.ShowAddToQuizCardView());
    }


    void setLongClick(ChatItemViewHolder vItem) {
        Context context = vItem.cardView.getContext();

        vItem.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 加载弹出窗口的布局
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.item_chat_popup_window, null);

                // 创建弹出窗口
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                // 这些设置将使得弹出窗口在点击界面外的时候消失
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                // 为每个按钮设置点击事件
                Button button1 = popupView.findViewById(R.id.button1);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseQuestionAndAnswer();
                        popupWindow.dismiss();
                    }
                });

                Button button2 = popupView.findViewById(R.id.button2);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popupWindow.dismiss();
                    }
                });

                // 显示弹出窗口，你可以根据需要调整显示的位置
                popupWindow.showAsDropDown(v);
                return true;
            }
        });
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

    void setOnClickAddToQuizCardButton(ChatItemViewHolder vItem) {
        if (vItem.addToQuizCardButton != null) {
            vItem.addToQuizCardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseQuestionAndAnswer();
                }
            });
        }
    }


    private int playingItemIndex = -1;

    void setOnClickPlayVoiceButton(ChatItemViewHolder vItem, TextMessage m, int position) {
        if (vItem.playVoiceButton != null) {
            vItem.playVoiceButton.setImageResource(R.drawable.ic_play_arrow);
            vItem.playVoiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(m.voiceStatus == VOICE_INITIAL) {

                        // google cloud 的文字转语音模型，读中文的时候，会把最后的句号读出来，所以，直接把句号替换成逗号
                        String realS =((TextMessage) m).getContent().replace("。", ",");
                        MediaPlayer.OnCompletionListener comletionCallback = new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                vItem.playVoiceButton.setImageResource(R.drawable.ic_play_arrow);
                                m.voiceStatus = VOICE_INITIAL;
                            }
                        };
                        m.getText2VoiceModel().onSpeak(realS, comletionCallback, new CompletableObserver() {
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
                        m.voiceStatus = VOICE_PLAYING;
                        vItem.playVoiceButton.setImageResource(R.drawable.ic_pause_black);

                        setOldPlayingItemIcon();
                        playingItemIndex = position;

                    } else if ( m.voiceStatus == VOICE_PLAYING) {
                        vItem.playVoiceButton.setImageResource(R.drawable.ic_play_arrow);
                        m.voiceStatus = VOICE_PAUSED;
                        m.getText2VoiceModel().pause();
                        playingItemIndex = -1;
                    } else if (m.voiceStatus == VOICE_PAUSED) {
                        vItem.playVoiceButton.setImageResource(R.drawable.ic_pause_black);
                        m.voiceStatus = VOICE_PLAYING;
                        // todo 如果一开始因为网络问题，没有能获取语音，这里不应该是resume，而是重新获取语音
                        m.getText2VoiceModel().resume();

                        setOldPlayingItemIcon();
                        playingItemIndex = position;
                    }
                }
            });
        }
    }


    /**
     * 当正在播放语音，而用户又新点击了另外一个语音的时候，
     * 需要把正在播放的语音的图标恢复成原来的样子
     */
    private void setOldPlayingItemIcon() {
        if (playingItemIndex != -1) {
            ((TextMessage) items.get(playingItemIndex)).setVoiceStatus(VOICE_INITIAL);
            notifyItemChanged(playingItemIndex);
        }
    }

    public void stopAllVoice() {
        if (playingItemIndex != -1) {
            if (items.get(playingItemIndex) instanceof TextMessage) {
                ((TextMessage) items.get(playingItemIndex)).getText2VoiceModel().stop();
            }
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
