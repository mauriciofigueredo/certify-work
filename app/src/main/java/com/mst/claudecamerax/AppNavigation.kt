package com.mst.claudecamerax

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mst.claudecamerax.model.Flash
import com.mst.claudecamerax.model.PhotoName
import com.mst.claudecamerax.model.Screen
import com.mst.claudecamerax.views.CameraScreen
import com.mst.claudecamerax.views.HomeScreen
import com.mst.claudecamerax.views.PreviewScreen


@Composable
fun AppNavigation() {
    var currentScreen    by remember { mutableStateOf<Screen>(Screen.Home) }
    var photoName        by remember { mutableStateOf("") }
    var shotIndex        by remember { mutableStateOf(1) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    var flashMode        by remember { mutableStateOf(Flash.OFF) }


    when (currentScreen) {

        Screen.Home -> HomeScreen(
            photoName        = photoName,
            onPhotoNameChange = { photoName = it },
            onOpenCamera     = {
                shotIndex     = 1           // reinicia contador al comenzar nueva serie
                currentScreen = Screen.Camera
            }
        )

        Screen.Camera -> CameraScreen(
            photoName    = PhotoName.build(photoName, shotIndex),
            shotIndex    = shotIndex,
            flashMode    = flashMode,
            onFlashToggle = { flashMode = flashMode.next() },
            onPhotoTaken = { uri ->
                capturedImageUri = uri
                currentScreen    = Screen.Preview
            },
            onBack = { currentScreen = Screen.Home }
        )

        Screen.Preview -> PreviewScreen(
            imageUri  = capturedImageUri,
            photoName = PhotoName.build(photoName, shotIndex),
            shotIndex = shotIndex,
            onDiscard = {
                // Elimina la foto recién tomada y vuelve al inicio
                capturedImageUri = null
                currentScreen    = Screen.Camera
            },
            onNext = {
                // Guarda (ya está en MediaStore) y va a la siguiente toma
                capturedImageUri = null
                shotIndex       += 1
                currentScreen    = Screen.Camera
            },
            onFinish = {
                // Guarda la última y vuelve al inicio reseteando todo
                capturedImageUri = null
                photoName        = ""
                shotIndex        = 1
                currentScreen    = Screen.Home
            }
        )
    }
}
