package com.tokopedia.checkphonesize

import android.os.Build
import android.os.Environment
import android.os.StatFs
import androidx.annotation.RequiresApi

class StorageStatsRepositoryLegacy : StorageStatsRepository {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override suspend fun getFreeSpaceBytes(): Long {
        val statFs = StatFs(Environment.getRootDirectory().absolutePath)
        return statFs.availableBytes
    }
}