package com.tokopedia.checkphonesize

import android.os.Build
import android.os.Environment
import android.os.StatFs

class StorageStatsRepositoryLegacy : StorageStatsRepository {

    override suspend fun getFreeSpaceBytes(): Long {
        val statFs = StatFs(Environment.getDataDirectory().absolutePath)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            statFs.availableBytes
        } else {
            (statFs.blockSize * statFs.availableBlocks).toLong()
        }
    }
}