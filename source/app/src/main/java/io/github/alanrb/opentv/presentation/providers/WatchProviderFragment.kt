package io.github.alanrb.opentv.presentation.providers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.*
import com.bumptech.glide.Glide
import io.github.alanrb.opentv.R
import io.github.alanrb.opentv.data.api.IMAGE_PREFIX
import io.github.alanrb.opentv.domain.entities.ListType
import io.github.alanrb.opentv.domain.entities.WatchProviderModel

/**
 * Created by Tuong (Alan) on 6/15/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

class WatchProviderFragment : RowsSupportFragment() {

    companion object {
        private val GRID_ITEM_WIDTH = 128
        private val GRID_ITEM_HEIGHT = 128
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onItemViewClickedListener = ItemViewClickedListener()

        val title = requireActivity().intent.getStringExtra(WatchProviderActivity.TITLE)
        val providers =
            requireActivity().intent.getParcelableArrayListExtra<WatchProviderModel>(
                WatchProviderActivity.PROVIDER
            )

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val gridHeader =
            HeaderItem(ListType.PREFERENCES.key.toLong(), title)

        val mGridPresenter = GridItemPresenter()
        val gridRowAdapter = ArrayObjectAdapter(mGridPresenter)
        providers?.forEach { provider ->
            gridRowAdapter.add(provider)
        }
        rowsAdapter.add(ListRow(gridHeader, gridRowAdapter))
        adapter = rowsAdapter
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            if (item is WatchProviderModel) {
                if (item.providerId == 8) { //Netflix
                    val intent = Intent(Intent.ACTION_VIEW).apply {

                    }

                }
            }
        }

    }

    private inner class GridItemPresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
            val view = ImageView(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(
                GRID_ITEM_WIDTH,
                GRID_ITEM_HEIGHT
            )
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.default_background))
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
            val provider = item as WatchProviderModel
            val img = viewHolder.view as ImageView
            Glide.with(viewHolder.view.context)
                .load("$IMAGE_PREFIX${provider.logoPath}")
                .centerCrop()
                .into(img)
        }

        override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
    }
}