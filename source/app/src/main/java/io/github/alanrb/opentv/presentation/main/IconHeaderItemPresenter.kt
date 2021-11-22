package io.github.alanrb.opentv.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowHeaderPresenter
import io.github.alanrb.opentv.R
import io.github.alanrb.opentv.domain.entities.ListType

/**
 * Created by Tuong (Alan) on 6/14/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

class IconHeaderItemPresenter : RowHeaderPresenter() {

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view = LayoutInflater.from(parent?.context).run {
            inflate(R.layout.header_icon_item, null)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder?, item: Any?) {
        val headerItem = (item as ListRow).headerItem
        val rootView = viewHolder!!.view
        val listType = ListType.parse(headerItem.id.toInt())
        rootView.focusable = View.FOCUSABLE

        rootView.findViewById<ImageView>(R.id.header_icon).apply {
            ResourcesCompat.getDrawable(rootView.resources, listType.icon, null)
                .also { icon ->
                    setImageDrawable(icon)
                }
        }
        rootView.findViewById<TextView>(R.id.header_label).apply {
            text = headerItem.name
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder?) {

    }
}