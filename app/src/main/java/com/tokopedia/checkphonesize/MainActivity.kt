package com.tokopedia.checkphonesize

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.coroutines.CoroutineContext
import android.os.Build

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + handler

    private val handler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, _ -> }
    }

    private var storageFileFreeSpaceInMB: Double = 0.0
    private var storageStatsRepositoryFreeSpaceInMB: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val storageStatsRepository = StorageStatsRepository.create(applicationContext)
        val osInfo = getAndroidVersion()
        phoneTypeInfo_textView.text = osInfo
        storageStatsRepoTitle_textView.text = getStorageStatsMethod()
        codeFileFreeSpace()
        codeStorageStatsRepository(storageStatsRepository)
        calculateDifference(storageFileFreeSpaceInMB, storageStatsRepositoryFreeSpaceInMB)

    }

    private fun codeFileFreeSpace() {
        val totalSize = File(filesDir.absoluteFile.toString()).freeSpace.toDouble()
        val storageFileFreeSpaceInKB = totalSize / 1000
        storageFileFreeSpaceInMB = storageFileFreeSpaceInKB / 1000
        val totalFileFreeSpaceSizeInMB = String.format("%.2f", storageFileFreeSpaceInMB)
        fileFreeSpaceInMB_textView.text = "Free Space : $totalFileFreeSpaceSizeInMB MB"
    }

    private fun codeStorageStatsRepository(storageStatsRepository: StorageStatsRepository) {
        runBlocking {
            val storageFreeFileSizeInKB = storageStatsRepository.getFreeSpaceBytes().toDouble() / 1000
            storageStatsRepositoryFreeSpaceInMB = storageFreeFileSizeInKB / 1000
            val totalStorageStatsRepositoryFreeSpaceSizeInMB = String.format("%.2f", storageStatsRepositoryFreeSpaceInMB)
            storageStatsRepositoryFreeSpaceInMB_textView.text = "Free Space : $totalStorageStatsRepositoryFreeSpaceSizeInMB MB"
        }
    }

    private fun calculateDifference(storageFileFreeSpaceInMB: Double, storageStatsRepositoryFreeSpaceInMB: Double) {
        val differenceCalculation = storageFileFreeSpaceInMB - storageStatsRepositoryFreeSpaceInMB
        val totalDifference = String.format("%.2f", differenceCalculation)

        val differenceInPercentage = differenceCalculation / storageFileFreeSpaceInMB * 100
        val totalDifferenceInPercentage = String.format("%.2f", differenceInPercentage)

        freeSpaceDiff_textView.text = "Difference : $totalDifference MB"
        freeSpaceDiffInPercentage_textView.text = "Difference in percentage : $totalDifferenceInPercentage %"
    }

    private fun getAndroidVersion(): String {
        val osVersion = Build.VERSION.SDK
        var osName = "-"
        when (osVersion) {
            ("29") -> osName = "Q"
            ("28") -> osName = "Pie"
            ("27") -> osName = "Oreo"
            ("26") -> osName = "Oreo"
            ("25") -> osName = "Nougat"
            ("24") -> osName = "Nougat"
            ("23") -> osName = "Marshmallow"
            ("22") -> osName = "Lollipop"
            ("21") -> osName = "Lollipop"
            ("20") -> osName = "KitKat"
            ("19") -> osName = "KitKat"
            ("18") -> osName = "Jelly Bean"
            ("17") -> osName = "Jelly Bean"
            ("16") -> osName = "Jelly Bean"
        }
        val versionRelease = Build.VERSION.RELEASE
        val versionSDK = Build.VERSION.SDK_INT
        return "Phone Type: ${getDeviceName()}.\nAndroidOS: $osName\n$versionRelease. API: $versionSDK"
    }

    private fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            capitalize(model)
        } else {
            capitalize(manufacturer) + " " + model
        }
    }


    private fun capitalize(s: String?): String {
        if (s == null || s.length == 0) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first) + s.substring(1)
        }
    }

    private fun getStorageStatsMethod(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return "Code (storageStatsManager\n.getFreeBytes(uuid))"
        }
        return "Code (statFs.availableBytes)"
    }
}