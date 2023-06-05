package com.chunxia.chatgpt.voicerecord.models

import android.net.Uri

class Events {
    class RecordingDuration internal constructor(val duration: Int)
    class RecordingStatus internal constructor(val status: Int)
    class RecordingAmplitude internal constructor(val amplitude: Int)
    class RecordingCompleted internal constructor(val path: String)
    class RecordingSaved internal constructor(val uri: Uri?)
    class TopicTrainingPending internal constructor()
    class TopicTrainingPendingEnd internal constructor()

    class ShowAddToQuizCardView internal constructor()

}
