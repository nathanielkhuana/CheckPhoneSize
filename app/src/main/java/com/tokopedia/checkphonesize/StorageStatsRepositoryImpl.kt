package com.tokopedia.checkphonesize

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Process
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.O)
class StorageStatsRepositoryImpl(context: Context) : StorageStatsRepository {

    private val storageStatsManager: StorageStatsManager = requireNotNull(
        ContextCompat.getSystemService(context, StorageStatsManager::class.java)
    )

    override suspend fun getFreeSpaceBytes(): Long {
        val uuid = StorageManager.UUID_DEFAULT
        return storageStatsManager.getFreeBytes(uuid)
    }
}