package com.mst.claudecamerax.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PhotoName {
    fun build(base: String, index: Int): String {
        val suffix = "_$index"
        return if (base.isNotEmpty()) {
            "$base$suffix"
        } else {
            "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}$suffix"
        }
    }
}