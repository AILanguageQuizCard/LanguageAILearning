package com.chunxia.chatgpt.voicerecord.recorder

import java.io.FileDescriptor

interface Recorder {
    fun setOutputFile(path: String)
    fun setOutputFile(fileDescriptor: FileDescriptor)
    fun prepare()
    fun start()
    fun stop()
    fun pause()
    fun resume()
    fun release()
    fun getMaxAmplitude(): Int
}
