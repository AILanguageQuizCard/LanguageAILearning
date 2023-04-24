package com.example.chatgpt.voicerecord.recorder

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import com.example.chatgpt.voicerecord.extensions.config
import com.example.chatgpt.voicerecord.helpers.SAMPLE_RATE
import com.example.chatgpt.voicerecord.helpers.WAV_SAMPLE_RATE
import com.naman14.androidlame.AndroidLame
import com.naman14.androidlame.LameBuilder
import com.simplemobiletools.commons.extensions.showErrorToast
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

class WavRecorder(val context: Context) : Recorder {
    private var isPaused = AtomicBoolean(false)
    private var isStopped = AtomicBoolean(false)
    private var amplitude = AtomicInteger(0)
    private var outputPath: String? = null
    private var fileDescriptor: FileDescriptor? = null
    private var outputStream: FileOutputStream? = null
    private val minBufferSize = AudioRecord.getMinBufferSize(
        WAV_SAMPLE_RATE,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    @SuppressLint("MissingPermission")
    private val audioRecord = AudioRecord(
        context.config.audioSource,
        WAV_SAMPLE_RATE,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        minBufferSize * 2
    )

    override fun setOutputFile(path: String) {
        outputPath = path
    }

    override fun prepare() {}

    override fun start() {
        val rawData = ShortArray(minBufferSize)

        outputStream = try {
            if (fileDescriptor != null) {
                FileOutputStream(fileDescriptor)
            } else {
                FileOutputStream(File(outputPath))
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return
        }

        ensureBackgroundThread {
            try {
                audioRecord.startRecording()
            } catch (e: Exception) {
                context.showErrorToast(e)
                return@ensureBackgroundThread
            }

            while (!isStopped.get()) {
                if (!isPaused.get()) {
                    val count = audioRecord.read(rawData, 0, minBufferSize)
                    if (count > 0) {
                        try {
                            updateAmplitude(rawData)
                            outputStream!!.write(shortArrayToByteArray(rawData))
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }
            }
        }
    }

    private fun shortArrayToByteArray(shortArray: ShortArray): ByteArray {
        val byteArray = ByteArray(shortArray.size * 2)
        ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(shortArray)
        return byteArray
    }


    override fun stop() {
        isPaused.set(true)
        isStopped.set(true)
    }

    override fun pause() {
        isPaused.set(true)
    }

    override fun resume() {
        isPaused.set(false)
    }

    override fun release() {
        outputStream?.close()
    }

    override fun getMaxAmplitude(): Int {
        return amplitude.get()
    }

    override fun setOutputFile(fileDescriptor: FileDescriptor) {
        this.fileDescriptor = fileDescriptor
    }

    private fun updateAmplitude(data: ShortArray) {
        var sum = 0L
        for (i in 0 until minBufferSize step 2) {
            sum += abs(data[i].toInt())
        }
        amplitude.set((sum / (minBufferSize / 8)).toInt())
    }

}