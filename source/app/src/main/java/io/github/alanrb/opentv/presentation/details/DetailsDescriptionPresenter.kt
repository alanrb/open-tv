package io.github.alanrb.opentv.presentation.details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.Presenter
import io.github.alanrb.opentv.R
import io.github.alanrb.opentv.domain.entities.MovieModel

class DetailsDescriptionPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view = LayoutInflater.from(parent?.context).run {
            inflate(R.layout.view_details_description, null)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        val rootView = viewHolder!!.view
        val movie = item as MovieModel

        rootView.findViewById<TextView>(R.id.tvTitle).apply {
            text = movie.title
        }
        rootView.findViewById<TextView>(R.id.tvSubtitle).apply {
            text = "${item.certification} • ${item.year} • ${item.runtime} minutes"
        }
        rootView.findViewById<TextView>(R.id.tvBody).apply {
            text = "${movie.overview}"
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
    }
}