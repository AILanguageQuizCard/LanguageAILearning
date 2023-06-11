package com.chunxia.chatgpt.adapter.review

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.media.MediaPlayer.OnCompletionListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.chunxia.chatgpt.R
import com.chunxia.chatgpt.model.review.SentenceCard
import com.chunxia.chatgpt.texttovoice.Text2VoiceModel
import com.chunxia.chatgpt.tools.Tools
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable


class ReviewCardStackAdapter(
    private var topicReviewSets: List<SentenceCard> = emptyList(),
) : RecyclerView.Adapter<ReviewCardStackAdapter.ViewHolder>() {

    private val TAG = "ReviewCardStackAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_reivew_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = topicReviewSets[position]
        holder.reviewCardView.sentence = card.sentence
        holder.reviewCardView.translation = card.translation
        holder.reviewCardView.setQuizMode()
    }

    override fun getItemCount(): Int {
        return topicReviewSets.size
    }

    fun setLearnCards(sentenceCards1: List<SentenceCard>) {
        this.topicReviewSets = sentenceCards1
    }

    fun getLearnCards(): List<SentenceCard> {
        return topicReviewSets
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val reviewCardView: ReviewCardView = view.findViewById(R.id.review_card_view)
        val playView: ImageButton = view.findViewById(R.id.review_card_play)
        val copyView: ImageButton = view.findViewById(R.id.review_card_copy)
        val voiceModel: Text2VoiceModel = Text2VoiceModel(view.context.applicationContext as Application)

        init {
            view.setOnClickListener(View.OnClickListener {
                reviewCardView.setAnswerMode()
            })


            playView.setOnClickListener(View.OnClickListener {
                // google cloud 的文字转语音模型，读中文的时候，会把最后的句号读出来，所以，直接把句号替换成逗号

                val realS = reviewCardView.sentence.replace("。", ",")

                val comletionCallback = OnCompletionListener { }
                // todo 已经从google cloud获取到语音的，不需要再次请求
                voiceModel.onSpeak(realS, comletionCallback, object : CompletableObserver {
                        override fun onSubscribe(d: Disposable) {}
                        override fun onComplete() {
                            Log.i("ReviewCardStackAdapter", "speak success")
                        }

                        override fun onError(e: Throwable) {
                            Log.e("ReviewCardStackAdapter", "Speak failed", e)
                        }
                    })
            })

            copyView.setOnClickListener(View.OnClickListener {
                Tools.copyToClipboard(view.context, reviewCardView.sentence)
                Toast.makeText(view.context, "已复制到剪切板", Toast.LENGTH_SHORT).show()
            })

        }
    }

}
