package com.example.chatgpt.adapter.chat;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgpt.R;
import com.example.chatgpt.voiceplay.RecordingPlayerView;

public class VoiceMessageItemViewHolder extends RecyclerView.ViewHolder {

    RecordingPlayerView recordingPlayerView;

    public VoiceMessageItemViewHolder(View v) {
        super(v);
        recordingPlayerView = v.findViewById(R.id.record_player_view);
    }


}