package com.chunxia.chatgpt.adapter.review

import androidx.recyclerview.widget.DiffUtil
import com.chunxia.chatgpt.model.review.SentenceCard

class SpotDiffCallback(
    private val old: List<SentenceCard>,
    private val new: List<SentenceCard>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition].sentence.equals(new[newPosition].sentence)
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return old[oldPosition] == new[newPosition]
    }

}
