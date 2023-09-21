package com.chunxia.chatgpt.model.review

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import java.util.Date

class SentenceCard() : Parcelable {
    var question: String? = null
    var answer: String? = null
    var voicePath: String? = null
    var learnRecord: LearnRecord? = null
    var topic: String? = null

    constructor(parcel: Parcel) : this() {
        question = parcel.readString()
        answer = parcel.readString()
        voicePath = parcel.readString()
        learnRecord = parcel.readParcelable(LearnRecord::class.java.classLoader)
        topic = parcel.readString()
    }

    constructor(sentence: String?) : this() {
        this.question = sentence
    }

    constructor(sentence: String?, translation: String?) : this() {
        this.question = sentence
        this.answer = translation
        this.learnRecord = LearnRecord()
    }

    constructor(sentence: String, translation: String, topic: String) : this() {
        this.question = sentence
        this.answer = translation
        this.learnRecord = LearnRecord()
        this.topic = topic
    }

    fun getLatestReviewTime() : Date? {
        return learnRecord?.latestReviewTime
    }

    fun shouldReviewNow(): Boolean {
        return if (learnRecord == null) true else learnRecord!!.shouldReview()
    }

    fun getReviewLevel(): Int {
        return if (learnRecord == null || learnRecord!!.reviewRecordList == null || learnRecord!!.reviewRecordList!!.isEmpty()) {
            0
        } else if (learnRecord!!.shouldReview()) {
            1
        } else {
            2
        }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SentenceCard

        if (question != other.question) return false
        if (answer != other.answer) return false
        if (voicePath != other.voicePath) return false
//        if (learnRecord != other.learnRecord) return false
        if (topic != other.topic) return false

        return true
    }

    override fun hashCode(): Int {
        var result = question?.hashCode() ?: 0
        result = 31 * result + (answer?.hashCode() ?: 0)
        result = 31 * result + (voicePath?.hashCode() ?: 0)
        result = 31 * result + (learnRecord?.hashCode() ?: 0)
        result = 31 * result + (topic?.hashCode() ?: 0)
        return result
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(question)
        parcel.writeString(answer)
        parcel.writeString(voicePath)
        parcel.writeParcelable(learnRecord, flags)
        parcel.writeString(topic)
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Creator<SentenceCard> {
        override fun createFromParcel(parcel: Parcel): SentenceCard {
            return SentenceCard(parcel)
        }

        override fun newArray(size: Int): Array<SentenceCard?> {
            return arrayOfNulls(size)
        }
    }


}

