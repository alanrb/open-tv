package io.github.alanrb.opentv.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import io.github.alanrb.opentv.R
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Tuong (Alan) on 6/11/21.
 * Copyright (c) 2021 Buuuk. All rights reserved.
 */

interface SharePreferenceManager {
    var accessToken: String
    var refreshToken: String
    var pushToken: String
    var uuid: String

    class SharePreferenceManagerImpl(context: Context) :
        SharePreferenceManager {
        companion object {
            const val MASTER_KEY_ALIAS = "tuongnt.dev@gmail.com.Opentv.keyAlias"
            const val KEY_SIZE = 256
        }

        private val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE)
            .build()

        private val masterKeyAlias = MasterKey.Builder(context, MASTER_KEY_ALIAS)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .build()

        private val preferences = EncryptedSharedPreferences.create(
            context,
            context.getString(R.string.preference_file_key),
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        override var accessToken by PreferenceFieldDelegate.string(
            "access_token"
        )

        override var refreshToken by PreferenceFieldDelegate.string(
            "refresh_token"
        )

        override var pushToken by PreferenceFieldDelegate.string(
            "push_token"
        )

        override var uuid by PreferenceFieldDelegate.string(
            "device_uuid"
        )

        private sealed class PreferenceFieldDelegate<T>(protected val key: String) :
            ReadWriteProperty<SharePreferenceManagerImpl, T> {

            class bool(key: String) : PreferenceFieldDelegate<Boolean>(key) {

                override fun getValue(thisRef: SharePreferenceManagerImpl, property: KProperty<*>) =
                    thisRef.preferences.getBoolean(key, false)

                override fun setValue(
                    thisRef: SharePreferenceManagerImpl,
                    property: KProperty<*>,
                    value: Boolean
                ) = thisRef.preferences.edit().putBoolean(key, value).apply()
            }

            class Int(key: String) : PreferenceFieldDelegate<kotlin.Int>(key) {

                override fun getValue(thisRef: SharePreferenceManagerImpl, property: KProperty<*>) =
                    thisRef.preferences.getInt(key, 0)

                override fun setValue(
                    thisRef: SharePreferenceManagerImpl,
                    property: KProperty<*>,
                    value: kotlin.Int
                ) = thisRef.preferences.edit().putInt(key, value).apply()
            }

            class string(key: String) : PreferenceFieldDelegate<String>(key) {

                override fun getValue(
                    thisRef: SharePreferenceManagerImpl,
                    property: KProperty<*>
                ): String = thisRef.preferences.getString(key, "") ?: ""

                override fun setValue(
                    thisRef: SharePreferenceManagerImpl,
                    property: KProperty<*>,
                    value: String
                ) = thisRef.preferences.edit().putString(key, value).apply()
            }
        }

    }
}