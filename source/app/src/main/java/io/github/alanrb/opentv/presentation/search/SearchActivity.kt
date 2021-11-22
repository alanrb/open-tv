package io.github.alanrb.opentv.presentation.search

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import io.github.alanrb.opentv.R

/**
 * Created by Tuong (Alan) on 6/16/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

class SearchActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.search_fragment, AppSearchFragment())
                .commitNow()
        }
    }
}