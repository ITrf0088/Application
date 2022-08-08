package org.rasulov.application.utils

import android.app.Activity


fun <T> Activity.requireExtraOrThrow(key: String): T {
    val data = intent.extras?.get(key) ?: throw IllegalStateException("It's required arg with key: $key")
    return data as T

}