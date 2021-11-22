package io.github.alanrb.opentv.presentation.search

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.widget.*
import io.github.alanrb.opentv.MovieCardPresenter
import io.github.alanrb.opentv.domain.entities.ListType
import io.github.alanrb.opentv.domain.entities.MovieModel
import io.github.alanrb.opentv.domain.entities.Result
import io.github.alanrb.opentv.domain.entities.ShowModel
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by Tuong (Alan) on 6/16/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

class AppSearchFragment : SearchSupportFragment(), SearchSupportFragment.SearchResultProvider {

    private val viewModel: AppSearchViewModel by viewModel()

    private val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSearchResultProvider(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movieResult.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    val cardPresenter = MovieCardPresenter()
                    val header = HeaderItem(1, "Movie")
                    val adapter = ArrayObjectAdapter(cardPresenter)
                    adapter.addAll(0,it.data ?: listOf<MovieModel>())
                    rowsAdapter.add(ListRow(header, adapter))

                }
                is Result.Failure -> {
                }
            }
        })

        viewModel.showResult.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    val cardPresenter = MovieCardPresenter()
                    val header = HeaderItem(2, "Show")
                    val adapter = ArrayObjectAdapter(cardPresenter)
                    adapter.addAll(0,it.data ?: listOf<ShowModel>())
                    rowsAdapter.add(ListRow(header, adapter))
                }
                is Result.Failure -> {
                }
            }
        })
    }

    override fun getResultsAdapter(): ObjectAdapter {
        return rowsAdapter
    }

    override fun onQueryTextChange(newQuery: String?): Boolean {
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        rowsAdapter.clear()
        val cardPresenter = MovieCardPresenter()
        val header = HeaderItem(0, "Search result for '$query'")
        rowsAdapter.add(ListRow(header, ArrayObjectAdapter(cardPresenter)))

        if (!TextUtils.isEmpty(query)) {
            viewModel.submit(query!!)
        }
        return true
    }
}