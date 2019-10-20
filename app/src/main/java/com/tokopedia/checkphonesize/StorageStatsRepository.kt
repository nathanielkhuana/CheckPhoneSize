package com.tokopedia.checkphonesize

import android.content.Context
import android.os.Build

interface StorageStatsRepository {

    suspend fun getFreeSpaceBytes(): Long

    companion object {
        fun create(context: Context) =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                StorageStatsRepositoryImpl(context)
            } else {
                StorageStatsRepositoryLegacy()
            }
    }
}