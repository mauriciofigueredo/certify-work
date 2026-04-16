package com.mst.claudecamerax.model

import androidx.camera.core.ImageCapture

enum class Flash {


    OFF, ON, AUTO;

    /** Cicla al siguiente modo */
    fun next(): Flash = when (this) {
        OFF  -> ON
        ON   -> AUTO
        AUTO -> OFF
    }

    /** Ícono visual para el botón */
    val icon: String get() = when (this) {
        OFF  -> "🔦✕"
        ON   -> "⚡"
        AUTO -> "⚡A"
    }

    /** Etiqueta legible */
    val label: String get() = when (this) {
        OFF  -> "Flash: Apagado"
        ON   -> "Flash: Encendido"
        AUTO -> "Flash: Automático"
    }

    /** Constante de CameraX */
    val cameraxValue: Int get() = when (this) {
        OFF  -> ImageCapture.FLASH_MODE_OFF
        ON   -> ImageCapture.FLASH_MODE_ON
        AUTO -> ImageCapture.FLASH_MODE_AUTO
    }

}