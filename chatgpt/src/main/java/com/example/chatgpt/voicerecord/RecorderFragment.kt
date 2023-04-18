package com.example.chatgpt.voicerecord;

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatgpt.R
import com.example.chatgpt.voicerecord.helpers.*
import com.example.chatgpt.voicerecord.models.Events
import com.simplemobiletools.commons.extensions.*
import com.simplemobiletools.commons.helpers.isNougatPlus
import com.simplemobiletools.commons.views.MyTextView
import com.example.chatgpt.voicerecord.services.RecorderService
import com.visualizer.amplitude.AudioRecordView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class RecorderFragment(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {
    private var status = RECORDING_STOPPED
    private var pauseBlinkTimer = Timer()
    private var bus: EventBus? = null
    private var toggleRecordingButton: ImageView
    private var recorderVisualizer: AudioRecordView
    private var togglePauseButton: ImageView
    private var recordingDuration: MyTextView

    init {
        LayoutInflater.from(context).inflate(R.layout.fragment_recorder, this, true)
        toggleRecordingButton = findViewById(R.id.toggle_recording_button)
        recorderVisualizer = findViewById(R.id.recorder_visualizer)
        togglePauseButton = findViewById(R.id.toggle_pause_button)
        recordingDuration = findViewById(R.id.recording_duration)
    }

    override fun onAttachedToWindow() {
        Log.i("RecorderFragment", "onAttachedToWindow")
        super.onAttachedToWindow()
        recorderVisualizer.recreate()
        bus = EventBus.getDefault()
        bus!!.register(this)

        updateRecordingDuration(0)
        toggleRecordingButton.setOnClickListener {
            toggleRecording()
        }

        togglePauseButton.setOnClickListener {
            Intent(context, RecorderService::class.java).apply {
                action = TOGGLE_PAUSE
                context.startService(this)
            }
        }

        Intent(context, RecorderService::class.java).apply {
            action = GET_RECORDER_INFO
            try {
                Log.i("RecorderFragment", "start RecorderService in onAttachedToWindow()")
                context.startService(this)
            } catch (e: Exception) {
            }
        }
    }

    private fun updateRecordingDuration(duration: Int) {
        recordingDuration.text = duration.getFormattedDuration()
    }

    private fun getToggleButtonIcon(): Drawable {
        val drawable = if (status == RECORDING_RUNNING || status == RECORDING_PAUSED) R.drawable.ic_stop_vector else R.drawable.ic_microphone_vector
        return resources.getColoredDrawableWithColor(drawable, context.getProperPrimaryColor().getContrastColor())
    }

    private fun toggleRecording() {
        status = if (status == RECORDING_RUNNING || status == RECORDING_PAUSED) {
            RECORDING_STOPPED
        } else {
            RECORDING_RUNNING
        }

        toggleRecordingButton.setImageDrawable(getToggleButtonIcon())

        if (status == RECORDING_RUNNING) {
            startRecording()
        } else {
            togglePauseButton.beGone()
            stopRecording()
        }
    }

    private fun startRecording() {
        Intent(context, RecorderService::class.java).apply {
            Log.i("RecorderFragment", "start RecorderService in startRecording()")
            context.startService(this)
        }
        recorderVisualizer.recreate()
    }

    private fun stopRecording() {
        Intent(context, RecorderService::class.java).apply {
            context.stopService(this)
        }
    }

    private fun getPauseBlinkTask() = object : TimerTask() {
        override fun run() {
            if (status == RECORDING_PAUSED) {
                // update just the alpha so that it will always be clickable
                Handler(Looper.getMainLooper()).post {
                    togglePauseButton.alpha = if (togglePauseButton.alpha == 0f) 1f else 0f
                }
            }
        }
    }

    private fun refreshView() {
        toggleRecordingButton.setImageDrawable(getToggleButtonIcon())
        togglePauseButton.beVisibleIf(status != RECORDING_STOPPED && isNougatPlus())
        if (status == RECORDING_PAUSED) {
            pauseBlinkTimer = Timer()
            pauseBlinkTimer.scheduleAtFixedRate(getPauseBlinkTask(), 500, 500)
        } else {
            pauseBlinkTimer.cancel()
        }

        if (status == RECORDING_RUNNING) {
            togglePauseButton.alpha = 1f
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun gotDurationEvent(event: Events.RecordingDuration) {
        updateRecordingDuration(event.duration)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun gotStatusEvent(event: Events.RecordingStatus) {
        status = event.status
        refreshView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun gotAmplitudeEvent(event: Events.RecordingAmplitude) {
        val amplitude = event.amplitude
        if (status == RECORDING_RUNNING) {
            recorderVisualizer.update(amplitude)
        }
    }
}
