package com.chunxia.chatgpt.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import com.blankj.utilcode.util.ActivityUtils
import com.chunxia.chatgpt.R
import com.chunxia.chatgpt.activity.ActivityIntentKeys.ACTIVITY_REVIEW_CARD_EDITED_SENTENCES_LIST
import com.chunxia.chatgpt.adapter.review.ReviewCardStackAdapter
import com.chunxia.chatgpt.adapter.review.ReviewCardView
import com.chunxia.chatgpt.common.XLIntent
import com.chunxia.chatgpt.model.review.LearnRecord
import com.chunxia.chatgpt.model.review.ReviewCardManager
import com.chunxia.chatgpt.model.review.SentenceCard
import com.chunxia.chatgpt.model.review.TopicReviewSets
import com.google.android.material.navigation.NavigationView
import com.material.components.utils.Tools
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.RewindAnimationSetting
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting
import com.yuyakaido.android.cardstackview.SwipeableMethod

class ReviewCardActivity : AppCompatActivity(), CardStackListener {

    private val drawerLayout by lazy { findViewById<DrawerLayout>(R.id.drawer_layout) }
    private val cardStackView by lazy { findViewById<CardStackView>(R.id.card_stack_view) }
    private val manager by lazy { CardStackLayoutManager(this, this) }

    // topicReviewSets 应该保持是通过mmkv获取的最新数据
    private var topicReviewSets: TopicReviewSets? = null
    private var adapter = ReviewCardStackAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_card)
        initData()
        setupSystemBar()
        setupNavigation()
        setupCardStackView()
        setupButton()
    }

    private fun setupSystemBar() {
        Tools.setSystemBarColor(this, R.color.white)
        Tools.setSystemBarLight(this)
    }

    private fun initData() {
        topicReviewSets = ReviewCardManager.getInstance().currentTopicReviewSets
        topicReviewSets?.let {
            adapter.setLearnCards(it.sentenceCardList)
        }
    }

    override fun onStop() {
        super.onStop()
        adapter.stopAllVoice()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        // todo
        if (direction == Direction.Right) {
            getTopSentenceCard().let {
                if (it.learnRecord == null) {
                    it.learnRecord = LearnRecord()
                }
                it.learnRecord?.addGoodReview()
            }
            ReviewCardManager.getInstance().saveAllReviewData()

        } else if (direction == Direction.Left) {
            getTopSentenceCard().let {
                if (it.learnRecord == null) {
                    it.learnRecord = LearnRecord()
                }
                it.learnRecord?.addBadReview()
            }
            ReviewCardManager.getInstance().saveAllReviewData()

        }

        if (manager.topPosition == adapter.itemCount) {
            paginate()
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        val reviewCardView = view.findViewById<ReviewCardView>(R.id.review_card_view)
        Log.d("CardStackView", "onCardAppeared: ($position) ${reviewCardView.sentence}")
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val reviewCardView = view.findViewById<ReviewCardView>(R.id.review_card_view)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${reviewCardView.sentence}")
    }

    private fun setupNavigation() {
        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // DrawerLayout
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        actionBarDrawerToggle.syncState()
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        // NavigationView
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.reload -> editCurrentCard()
                R.id.add_spot_to_first -> jumpToEmptyAddCardActivity()
                R.id.add_spot_to_last -> removeTop()
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun setupCardStackView() {
        initialize()
    }


    private fun setupButton() {
        val skip = findViewById<View>(R.id.skip_button)
        skip.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
            getTopSentenceCard().let {
                if (it.learnRecord == null) {
                    it.learnRecord = LearnRecord()
                }
                it.learnRecord?.addBadReview()
            }
            ReviewCardManager.getInstance().saveAllReviewData()
        }

        val rewind = findViewById<View>(R.id.rewind_button)
        rewind.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            cardStackView.rewind()
        }

        val like = findViewById<View>(R.id.like_button)
        like.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
            getTopSentenceCard().let {
                if (it.learnRecord == null) {
                    it.learnRecord = LearnRecord()
                }
                it.learnRecord?.addGoodReview()
            }
            ReviewCardManager.getInstance().saveAllReviewData()
        }
    }

    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun paginate() {
        adapter.setLearnCards(topicReviewSets!!.sentenceCardList)
        adapter.notifyDataSetChanged()
    }


    val requestCode = 10086 // 请求码可以是任何整数，用于标识此次请求
    private fun editCurrentCard() {
        val sentenceCard = getTopSentenceCard()
        val intent: Intent =
            XLIntent(ActivityUtils.getTopActivity(), AddReviewCardActivity::class.java)
                .putString(ActivityIntentKeys.ACTIVITY_ADD_REVIEW_SENTENCE_CARD_ANSWER, sentenceCard.sentence)
                .putString(ActivityIntentKeys.ACTIVITY_ADD_REVIEW_SENTENCE_CARD_QUESTION, sentenceCard.translation)
                .putString(ActivityIntentKeys.ACTIVITY_ADD_REVIEW_SENTENCE_CARD_TOPIC, getCurrentTopic())
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == this.requestCode) {
                val editedSentenceCardList: ArrayList<SentenceCard>?  = data?.getParcelableArrayListExtra(ACTIVITY_REVIEW_CARD_EDITED_SENTENCES_LIST)
                editedSentenceCardList?.let {
                    // 哪怕editedSentenceCardList传递了多个数据，也只替换一个
                    replace(editedSentenceCardList[0])
                }
            } else if (requestCode == this.addCardRequestCode) {
                val editedSentenceCardList: ArrayList<SentenceCard>?  = data?.getParcelableArrayListExtra(ACTIVITY_REVIEW_CARD_EDITED_SENTENCES_LIST)
                editedSentenceCardList?.let {
                    addCards(editedSentenceCardList)
                }
            }
        }
    }


    private fun getTopSentenceCard(): SentenceCard {
        return adapter.getLearnCards()[manager.topPosition]
    }


    private val addCardRequestCode: Int = 20350743
    private fun jumpToEmptyAddCardActivity() {
        val intent: Intent =
            XLIntent(ActivityUtils.getTopActivity(), AddReviewCardActivity::class.java)
                .putString(ActivityIntentKeys.ACTIVITY_ADD_REVIEW_SENTENCE_CARD_TOPIC, getCurrentTopic())
        startActivityForResult(intent, addCardRequestCode)
    }

    private fun addCards(sentenceCardList: ArrayList<SentenceCard> ) {
        val old = adapter.getLearnCards()
        val new = mutableListOf<SentenceCard>().apply {
            addAll(old)
            addAll(sentenceCardList)
        }
        adapter.setLearnCards(new)
        adapter.notifyDataSetChanged()
        topicReviewSets?.update()
    }


    private fun getCurrentTopic() : String? {
        return getTopSentenceCard().topic
    }


    private fun removeTop() {
        // todo 当remove的是最后一张卡片的时候，将不会触发循环添加。当remove的时候，manager.topPosition并没有改变
        if (adapter.getLearnCards().isEmpty()) {
            return
        }

        ReviewCardManager.getInstance().deleteOneSentenceCardInTopicReviewSets(getCurrentTopic(), getTopSentenceCard())
        // 这里可以不用update，事实上topicReviewSets就是同一份引用
        topicReviewSets?.update()
        var pos = -1
        if (manager.topPosition == adapter.itemCount) {
            manager.resetTopPositionWhenRemoveLastElement()
            pos = 0
        } else {
            pos = manager.topPosition
        }

        adapter.notifyDataSetChanged()
        manager.scrollToPosition(pos)
    }


    private fun replace(newsCard: SentenceCard) {
        val old = adapter.getLearnCards()
        val new = mutableListOf<SentenceCard>().apply {
            addAll(old)
            removeAt(manager.topPosition)
            add(manager.topPosition, newsCard)
        }
        adapter.setLearnCards(new)
        adapter.notifyItemChanged(manager.topPosition)

        topicReviewSets?.update()
    }

}
