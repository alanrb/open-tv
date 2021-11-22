package io.github.alanrb.opentv.presentation.providers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import io.github.alanrb.opentv.R
import io.github.alanrb.opentv.domain.entities.WatchProviderModel

/**
 * Created by Tuong (Alan) on 6/15/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

class WatchProviderActivity : FragmentActivity() {

    companion object {
        const val TITLE = "TITLE"
        const val PROVIDER = "PROVIDER"

        fun getStartedIntent(
            context: Context,
            title: String,
            provider: List<WatchProviderModel>
        ): Intent {
            return Intent(context, WatchProviderActivity::class.java).apply {
                putExtra(TITLE, title)
                putParcelableArrayListExtra(PROVIDER, ArrayList(provider))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watch_provider)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.watch_provider_fragment, WatchProviderFragment())
                .commitNow()
        }
    }
}