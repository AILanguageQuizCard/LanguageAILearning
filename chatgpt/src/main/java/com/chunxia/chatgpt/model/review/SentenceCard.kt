package com.chunxia.chatgpt.model.review

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class SentenceCard() : Parcelable {
    var sentence: String? = null
    var translation: String? = null
    var voicePath: String? = null
    var learnRecord: LearnRecord? = null

    constructor(parcel: Parcel) : this() {
        sentence = parcel.readString()
        translation = parcel.readString()
        voicePath = parcel.readString()
        learnRecord = parcel.readParcelable(LearnRecord::class.java.classLoader)
    }

    constructor(sentence: String?) : this() {
        this.sentence = sentence
    }

    constructor(sentence: String?, translation: String?) : this() {
        this.sentence = sentence
        this.translation = translation
    }

    fun shouldReviewNow(): Boolean {
        return if (learnRecord == null) false else learnRecord!!.shouldReview()
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SentenceCard

        if (sentence != other.sentence) return false
        if (translation != other.translation) return false
        if (voicePath != other.voicePath) return false
        if (learnRecord != other.learnRecord) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sentence?.hashCode() ?: 0
        result = 31 * result + (translation?.hashCode() ?: 0)
        result = 31 * result + (voicePath?.hashCode() ?: 0)
        result = 31 * result + (learnRecord?.hashCode() ?: 0)
        return result
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sentence)
        parcel.writeString(translation)
        parcel.writeString(voicePath)
        parcel.writeParcelable(learnRecord, flags)
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