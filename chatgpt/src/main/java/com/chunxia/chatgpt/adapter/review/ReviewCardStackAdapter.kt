package com.chunxia.chatgpt.adapter.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chunxia.chatgpt.R
import com.chunxia.chatgpt.model.review.SentenceCard

class ReviewCardStackAdapter(
    private var sentenceCards: List<SentenceCard> = emptyList()
) : RecyclerView.Adapter<ReviewCardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_reivew_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = sentenceCards[position]
        holder.reviewCardView.sentence = card.sentence
        holder.reviewCardView.translation = card.translation
    }

    override fun getItemCount(): Int {
        return sentenceCards.size
    }

    fun setLearnCards(sentenceCards1: List<SentenceCard>) {
        this.sentenceCards = sentenceCards1
    }

    fun getLearnCards(): List<SentenceCard> {
        return sentenceCards
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reviewCardView: ReviewCardView = view.findViewById(R.id.review_card_view)
    }

}
