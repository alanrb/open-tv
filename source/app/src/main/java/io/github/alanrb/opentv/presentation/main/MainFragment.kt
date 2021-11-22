package io.github.alanrb.opentv.presentation.main

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.forEach
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.github.alanrb.opentv.MovieCardPresenter
import io.github.alanrb.opentv.R
import io.github.alanrb.opentv.domain.entities.ListType
import io.github.alanrb.opentv.domain.entities.MovieModel
import io.github.alanrb.opentv.domain.entities.ShowModel
import io.github.alanrb.opentv.presentation.BrowseErrorActivity
import io.github.alanrb.opentv.presentation.details.DetailsActivity
import io.github.alanrb.opentv.presentation.search.SearchActivity
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import java.util.*

/**
 * Created by Tuong (Alan) on 6/11/21.
 * Copyright (c) 2021. All rights reserved.
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseSupportFragment(), KoinComponent {

    companion object {
        private val TAG = "MainFragment"

        private val BACKGROUND_UPDATE_DELAY = 300
        private val GRID_ITEM_WIDTH = 200
        private val GRID_ITEM_HEIGHT = 200
    }

    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null
    private lateinit var mMetrics: DisplayMetrics
    private var mBackgroundTimer: Timer? = null
    private var mBackgroundUri: String? = null

    private val viewModel: MainViewModel by viewModel()
    private val rows: SparseArray<ArrayObjectAdapter> = SparseArray()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareBackgroundManager()

        setupUIElements()

        val cardPresenter = MovieCardPresenter()
        rows.put(ListType.TRENDING.key, ArrayObjectAdapter(cardPresenter))
        rows.put(ListType.POPULAR.key, ArrayObjectAdapter(cardPresenter))
        rows.put(ListType.TRENDING_SHOW.key, ArrayObjectAdapter(cardPresenter))

        loadRows()

        setupEventListeners()

        viewModel.popularResult.observe(viewLifecycleOwner, {
            rows.get(ListType.POPULAR.key).addAll(0, it)
            startEntranceTransition()
        })

        viewModel.trendingResult.observe(viewLifecycleOwner, {
            rows.get(ListType.TRENDING.key).addAll(0, it)
            startEntranceTransition()
        })
        viewModel.trendingShowResult.observe(viewLifecycleOwner, {
            rows.get(ListType.TRENDING_SHOW.key).addAll(0, it)
            startEntranceTransition()
        })

        viewModel.get()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: " + mBackgroundTimer?.toString())
        mBackgroundTimer?.cancel()
    }

    private fun prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(activity).apply {
            attach(activity?.window)
        }
        mDefaultBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_background)
        mMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(mMetrics)
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        // over title
        headersState = BrowseSupportFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(requireContext(), R.color.fastlane_background)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.search_opaque)

        setHeaderPresenterSelector(object : PresenterSelector() {
            override fun getPresenter(item: Any?): Presenter {
                return IconHeaderItemPresenter()
            }
        })
    }

    private fun loadRows() {

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        rows.forEach { key, value ->
            val header = HeaderItem(key.toLong(), getString(ListType.parse(key).title))
            rowsAdapter.add(ListRow(header, value))
        }
        val gridHeader =
            HeaderItem(ListType.PREFERENCES.key.toLong(), getString(ListType.PREFERENCES.title))

        val mGridPresenter = GridItemPresenter()
        val gridRowAdapter = ArrayObjectAdapter(mGridPresenter)
        gridRowAdapter.add(resources.getString(R.string.grid_view))
        gridRowAdapter.add(getString(R.string.error_fragment))
        gridRowAdapter.add(resources.getString(R.string.personal_settings))
        rowsAdapter.add(ListRow(gridHeader, gridRowAdapter))

        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            val intent = Intent(requireActivity(), SearchActivity::class.java)
            startActivity(intent)
        }

        onItemViewClickedListener = ItemViewClickedListener()
        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {

            if (item is MovieModel) {
                Log.d(TAG, "Item: " + item.toString())
                val intent = Intent(context!!, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.MOVIE, item)

                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity!!,
                    (itemViewHolder.view as ImageCardView).mainImageView,
                    DetailsActivity.SHARED_ELEMENT_NAME
                )
                    .toBundle()
                startActivity(intent, bundle)
            } else if (item is String) {
                if (item.contains(getString(R.string.error_fragment))) {
                    val intent = Intent(context!!, BrowseErrorActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(context!!, item, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?, item: Any?,
            rowViewHolder: RowPresenter.ViewHolder, row: Row
        ) {
            when (item) {
                is MovieModel -> {
                    mBackgroundUri = item.backgroundImageUrl
                    startBackgroundTimer()
                }
                is ShowModel -> {
                    mBackgroundUri = item.backgroundImageUrl
                    startBackgroundTimer()
                }
            }
        }
    }

    private fun updateBackground(uri: String?) {
        val width = mMetrics.widthPixels
        val height = mMetrics.heightPixels
        Glide.with(requireContext())
            .load(uri)
            .centerCrop()
            .error(mDefaultBackground)
            .into<CustomTarget<Drawable>>(
                object : CustomTarget<Drawable>(width, height) {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        mBackgroundManager.drawable = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        mBackgroundTimer?.cancel()
    }

    private fun startBackgroundTimer() {
        mBackgroundTimer?.cancel()
        mBackgroundTimer = Timer()
        mBackgroundTimer?.schedule(UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY.toLong())
    }

    private inner class UpdateBackgroundTask : TimerTask() {

        override fun run() {
            Handler(Looper.getMainLooper()).post {
                updateBackground(mBackgroundUri)
            }
        }
    }

    private inner class GridItemPresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
            val view = TextView(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT)
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.default_background))
            view.setTextColor(Color.WHITE)
            view.gravity = Gravity.CENTER
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
            (viewHolder.view as TextView).text = item as String
        }

        override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
    }

}