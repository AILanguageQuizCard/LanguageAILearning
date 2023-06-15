package com.chunxia.chatgpt.voicerecord.models

import android.net.Uri
import com.chunxia.chatgpt.model.review.SentenceCard

class Events {
    class RecordingDuration internal constructor(val duration: Int)
    class RecordingStatus internal constructor(val status: Int)
    class RecordingAmplitude internal constructor(val amplitude: Int)
    class RecordingCompleted internal constructor(val path: String)
    class RecordingSaved internal constructor(val uri: Uri?)
    class TopicTrainingPending internal constructor()
    class TopicTrainingPendingEnd internal constructor()

    class ShowAddToQuizCardView internal constructor()
    class PresentEditedCard internal constructor(val newCard: SentenceCard)

    class ShowSnackBar internal constructor(val message: String)

}
