package io.github.alanrb.opentv.presentation.details

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import io.github.alanrb.opentv.MovieCardPresenter
import io.github.alanrb.opentv.R
import io.github.alanrb.opentv.domain.entities.MovieModel
import io.github.alanrb.opentv.domain.entities.Result
import io.github.alanrb.opentv.presentation.PlaybackActivity
import io.github.alanrb.opentv.presentation.main.MainActivity
import io.github.alanrb.opentv.presentation.providers.WatchProviderActivity
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * A wrapper fragment for leanback details screens.
 * It shows a detailed view of video and its metadata plus related videos.
 */
class VideoDetailsFragment : DetailsSupportFragment() {

    private val viewModel: VideoDetailsViewModel by viewModel {
        parametersOf(
            requireActivity().intent.getParcelableExtra<MovieModel>(DetailsActivity.MOVIE)
        )
    }
    private var mSelectedMovie: MovieModel? = null

    private lateinit var mDetailsBackground: DetailsSupportFragmentBackgroundController
    private lateinit var mPresenterSelector: ClassPresenterSelector
    private lateinit var mAdapter: ArrayObjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate DetailsFragment")
        super.onCreate(savedInstanceState)

        mDetailsBackground = DetailsSupportFragmentBackgroundController(this)

        mSelectedMovie =
            requireActivity().intent.getParcelableExtra(DetailsActivity.MOVIE)
        if (mSelectedMovie != null) {
            mPresenterSelector = ClassPresenterSelector()
            mAdapter = ArrayObjectAdapter(mPresenterSelector)
            setupDetailsOverviewRow()
            setupDetailsOverviewRowPresenter()
            adapter = mAdapter
            onItemViewClickedListener = ItemViewClickedListener()
        } else {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.get()

        viewModel.movieResult.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Loading -> {
                    println("movieResult: Loading")
                }
                is Result.Success -> {
                    initializeBackground(it.data)
                }
                is Result.Failure -> {
                    println("movieResult: Failure")
                }
            }
        })
        viewModel.watchProviderResult.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Loading -> {
                    println("movieResult: Loading")
                }
                is Result.Success -> {
                    println("movieResult: Success")
                }
                is Result.Failure -> {
                    println("movieResult: Failure")
                }
            }
        })
        viewModel.relatedResult.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    it.data?.let { data ->
                        setupRelatedMovieListRow(data)
                    }
                }
                is Result.Failure -> {
                }
            }
        })
    }

    private fun initializeBackground(movie: MovieModel?) {
        mDetailsBackground.enableParallax()
        Glide.with(requireContext())
            .asBitmap()
            .centerCrop()
            .error(R.drawable.default_background)
            .load(movie?.backgroundImageUrl)
            .into<SimpleTarget<Bitmap>>(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(
                    bitmap: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    mDetailsBackground.coverBitmap = bitmap
                    mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size())
                }
            })
    }

    private fun setupDetailsOverviewRow() {
        val row = DetailsOverviewRow(mSelectedMovie)
        row.imageDrawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.default_background)
        val width = convertDpToPixel(requireContext(), DETAIL_THUMB_WIDTH)
        val height = convertDpToPixel(requireContext(), DETAIL_THUMB_HEIGHT)
        Glide.with(requireContext())
            .load(mSelectedMovie?.cardImageUrl)
            .centerCrop()
            .error(R.drawable.default_background)
            .into<SimpleTarget<Drawable>>(object : SimpleTarget<Drawable>(width, height) {
                override fun onResourceReady(
                    drawable: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    Log.d(TAG, "details overview card image url ready: " + drawable)
                    row.imageDrawable = drawable
                    mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size())
                }
            })

        val actionAdapter = ArrayObjectAdapter()

        actionAdapter.add(
            Action(
                ACTION_WATCH,
                resources.getString(R.string.watch_now),
                resources.getString(R.string.var_service)
            )
        )
        actionAdapter.add(
            Action(
                ACTION_RENT,
                resources.getString(R.string.rent_1),
                resources.getString(R.string.rent_2)
            )
        )
        actionAdapter.add(
            Action(
                ACTION_BUY,
                resources.getString(R.string.buy_1),
                resources.getString(R.string.buy_2)
            )
        )
        row.actionsAdapter = actionAdapter

        mAdapter.add(row)
    }

    private fun setupDetailsOverviewRowPresenter() {
        // Set detail background.
        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
        detailsPresenter.backgroundColor =
            ContextCompat.getColor(requireContext(), R.color.selected_background)

        // Hook up transition element.
        val sharedElementHelper = FullWidthDetailsOverviewSharedElementHelper()
        sharedElementHelper.setSharedElementEnterTransition(
            activity, DetailsActivity.SHARED_ELEMENT_NAME
        )
        detailsPresenter.setListener(sharedElementHelper)
        detailsPresenter.isParticipatingEntranceTransition = true

        detailsPresenter.onActionClickedListener = OnActionClickedListener { action ->
            if (action.id == ACTION_WATCH) {
                showWatchProvider()
            } else {
                //Toast.makeText(requireContext(), action.toString(), Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), PlaybackActivity::class.java)
                intent.putExtra(DetailsActivity.MOVIE, mSelectedMovie)
                startActivity(intent)
            }
        }

        mPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
    }

    private fun showWatchProvider() {
        if (viewModel.watchProviderResult.value is Result.Success) {
            (viewModel.watchProviderResult.value as Result.Success).data?.let { provider ->
                val intent = WatchProviderActivity.getStartedIntent(
                    requireContext(),
                    mSelectedMovie?.title ?: "",
                    provider
                )
                startActivity(intent)
            }
        }
    }

    private fun setupRelatedMovieListRow(list: List<MovieModel>) {
        mAdapter.add(ListRow(HeaderItem(2, "Cast"), ArrayObjectAdapter(MovieCardPresenter())))

        val listRowAdapter = ArrayObjectAdapter(MovieCardPresenter())
        list.forEach {
            listRowAdapter.add(it)
        }
        val header = HeaderItem(0, getString(R.string.related_movies))
        mAdapter.add(ListRow(header, listRowAdapter))
        mPresenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
    }

    private fun convertDpToPixel(context: Context, dp: Int): Int {
        val density = context.applicationContext.resources.displayMetrics.density
        return Math.round(dp.toFloat() * density)
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item is MovieModel) {
                Log.d(TAG, "Item: " + item.toString())
                val intent = Intent(context!!, DetailsActivity::class.java)
                intent.putExtra(resources.getString(R.string.movie), item)

                val bundle =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity!!,
                        (itemViewHolder?.view as ImageCardView).mainImageView,
                        DetailsActivity.SHARED_ELEMENT_NAME
                    )
                        .toBundle()
                startActivity(intent, bundle)
            }
        }
    }

    companion object {
        private val TAG = "VideoDetailsFragment"

        private val ACTION_WATCH = 1L
        private val ACTION_RENT = 2L
        private val ACTION_BUY = 3L

        private val DETAIL_THUMB_WIDTH = 274
        private val DETAIL_THUMB_HEIGHT = 274

        private val NUM_COLS = 10
    }
}