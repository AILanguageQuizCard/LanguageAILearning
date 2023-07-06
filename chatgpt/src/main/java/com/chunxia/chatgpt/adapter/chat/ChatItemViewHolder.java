package com.chunxia.chatgpt.adapter.chat;

import static com.chunxia.chatgpt.adapter.chat.Constant.CHAT_ME;
import static com.chunxia.chatgpt.adapter.chat.Constant.CHAT_YOU;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.chunxia.chatgpt.R;
import com.chunxia.chatgpt.texttovoice.Text2VoiceModel;

public class ChatItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textContentView;
        public TextView textTimeView;
        public LinearLayout lytParentView;
        public ImageView playVoiceButton;
        public ImageView copyContentButton;
        public int viewType;
        private Text2VoiceModel text2VoiceModel;

        public CardView cardView;

        public boolean selected = false;
        public ImageView chooseButton;

        public ConstraintLayout meCardViewContainer;


        public ChatItemViewHolder(View v, int viewType) {
            super(v);
            textContentView = v.findViewById(R.id.text_content);
            lytParentView = (LinearLayout) v.findViewById(R.id.lyt_parent);
            cardView = v.findViewById(R.id.item_chat_card_view);
            chooseButton = v.findViewById(R.id.item_chat_choose_view);

            if (viewType == CHAT_ME) {
                meCardViewContainer = (ConstraintLayout) v.findViewById(R.id.item_chat_card_view_container);
                textTimeView = v.findViewById(R.id.text_time);
            } else if (viewType == CHAT_YOU) {
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
