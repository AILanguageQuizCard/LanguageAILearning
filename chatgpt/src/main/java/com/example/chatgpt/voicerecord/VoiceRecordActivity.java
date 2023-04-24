package com.example.chatgpt.voicerecord;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.chatgpt.R;
import com.example.chatgpt.voicerecord.models.Events;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class VoiceRecordActivity extends AppCompatActivity {

    private EventBus bus = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bus = EventBus.getDefault();
        bus.register(this);

        setContentView(R.layout.activity_voice_record);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void gotRecordEndEvent(Events.RecordingCompleted event){
        String path = event.getPath();
        Log.i("RecorderFragment", path);
        finish();
    }

}
