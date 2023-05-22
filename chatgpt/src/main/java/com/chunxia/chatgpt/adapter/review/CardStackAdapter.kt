package com.chunxia.chatgpt.adapter.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chunxia.chatgpt.R
import com.chunxia.chatgpt.model.review.LearnCard

class CardStackAdapter(
        private var learnCards: List<LearnCard> = emptyList()
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_reivew_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = learnCards[position]
        holder.sentence.text = card.sentence
        holder.translation.text = card.translation
    }

    override fun getItemCount(): Int {
        return learnCards.size
    }

    fun setLearnCards(learnCards1: List<LearnCard>) {
        this.learnCards = learnCards1
    }

    fun getLearnCards(): List<LearnCard> {
        return learnCards
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sentence: TextView = view.findViewById(R.id.item_review_card_sentence)
        var translation: TextView = view.findViewById(R.id.item_review_card_translation)
    }

}
