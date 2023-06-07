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
import androidx.recyclerview.widget.DiffUtil
import com.blankj.utilcode.util.ActivityUtils
import com.chunxia.chatgpt.R
import com.chunxia.chatgpt.adapter.review.ReviewCardStackAdapter
import com.chunxia.chatgpt.adapter.review.ReviewCardView
import com.chunxia.chatgpt.adapter.review.SpotDiffCallback
import com.chunxia.chatgpt.common.XLIntent
import com.chunxia.chatgpt.model.review.ReviewCardManager
import com.chunxia.chatgpt.model.review.SentenceCard
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
    private val learnCards = ReviewCardManager.getInstance().currentPresentingCards

    private val adapter by lazy {
        ReviewCardStackAdapter(learnCards)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_card)
        setupSystemBar()
        setupNavigation()
        setupCardStackView()
        setupButton()
    }

    private fun setupSystemBar() {
        Tools.setSystemBarColor(this, R.color.white)
        Tools.setSystemBarLight(this)
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
        if (manager.topPosition == adapter.itemCount - 1) {
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
                R.id.add_spot_to_first -> deleteCurrentCard()
                R.id.add_spot_to_last -> addLast(1)
                R.id.remove_spot_from_first -> removeFirst(1)
                R.id.remove_spot_from_last -> removeLast(1)
                R.id.replace_first_spot -> replace()
                R.id.swap_first_for_last -> swap()
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
        val old = adapter.getLearnCards()
        val new = old.plus(learnCards)
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setLearnCards(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun editCurrentCard() {
        val sentenceCard = getTopSentenceCard()
        val intent: Intent =
            XLIntent(ActivityUtils.getTopActivity(), AddReviewCardActivity::class.java)
                .putString(ActivityIntentKeys.SENTENCE_CARD_ANSWER, sentenceCard.translation)
                .putString(ActivityIntentKeys.SENTENCE_CARD_QUESTION, sentenceCard.sentence)
        ActivityUtils.getTopActivity().startActivity(intent)
    }

    private fun getTopSentenceCard(): SentenceCard {
        val topPosition = manager.topPosition
        return adapter.getLearnCards()[topPosition]
    }


    private fun deleteCurrentCard() {
        val old = adapter.getLearnCards()
        val new = mutableListOf<SentenceCard>().apply {
            addAll(old)
            removeAt(manager.topPosition)
        }
        learnCards.remove(getTopSentenceCard())
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setLearnCards(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addLast(size: Int) {
        val old = adapter.getLearnCards()
        val new = mutableListOf<SentenceCard>().apply {
            addAll(old)
            addAll(List(size) { createSpot() })
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setLearnCards(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeFirst(size: Int) {
        if (adapter.getLearnCards().isEmpty()) {
            return
        }

        val old = adapter.getLearnCards()
        val new = mutableListOf<SentenceCard>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(manager.topPosition)
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setLearnCards(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeLast(size: Int) {
        if (adapter.getLearnCards().isEmpty()) {
            return
        }

        val old = adapter.getLearnCards()
        val new = mutableListOf<SentenceCard>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(this.size - 1)
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setLearnCards(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun replace() {
        val old = adapter.getLearnCards()
        val new = mutableListOf<SentenceCard>().apply {
            addAll(old)
            removeAt(manager.topPosition)
            add(manager.topPosition, createSpot())
        }
        adapter.setLearnCards(new)
        adapter.notifyItemChanged(manager.topPosition)
    }

    private fun swap() {
        val old = adapter.getLearnCards()
        val new = mutableListOf<SentenceCard>().apply {
            addAll(old)
            val first = removeAt(manager.topPosition)
            val last = removeAt(this.size - 1)
            add(manager.topPosition, last)
            add(first)
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setLearnCards(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun createSpot(): SentenceCard {
        if (learnCards.size > 0) {
            return learnCards[0]
        }
        return SentenceCard(
            sentence = "Danish girls possess a natural and effortless beauty that is captivating.",
            translation = "丹麦女孩拥有一种自然而不费力的迷人美"
        )
    }

}
