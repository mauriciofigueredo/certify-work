package com.mst.claudecamerax

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mst.claudecamerax.model.PhotoName
import com.mst.claudecamerax.model.RepeatConfig
import com.mst.claudecamerax.model.Screen
import com.mst.claudecamerax.views.CameraScreen
import com.mst.claudecamerax.views.HomeScreen
import com.mst.claudecamerax.views.PreviewScreen


@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var photoName by remember { mutableStateOf("") }
    var totalShots by remember { mutableStateOf(5) }
    var repeatConfig by remember { mutableStateOf(RepeatConfig()) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    when (currentScreen) {
        Screen.Home -> HomeScreen(
            photoName = photoName,
            onPhotoNameChange = { photoName = it },
            totalShots = totalShots,
            onTotalShotsChange = { totalShots = it },
            onOpenCamera = {
                repeatConfig = RepeatConfig(totalShots = totalShots)
                currentScreen = Screen.Camera
            }
        )

        Screen.Camera -> CameraScreen(
            photoName = PhotoName.build(photoName, repeatConfig.currentIndex, repeatConfig.totalShots),
            shotLabel = "${repeatConfig.currentIndex} / ${repeatConfig.totalShots}",
            onPhotoTaken = { uri ->
                capturedImageUri = uri
                currentScreen = Screen.Preview
            },
            onBack = { currentScreen = Screen.Home }
        )

        Screen.Preview -> PreviewScreen(
            imageUri = capturedImageUri,
            photoName = PhotoName.build(photoName, repeatConfig.currentIndex, repeatConfig.totalShots),
            repeatConfig = repeatConfig,
            onDiscard = {
                capturedImageUri?.let { uri ->
                    // La eliminación del URI se hace dentro de PreviewScreen
                }
                capturedImageUri = null
                currentScreen = Screen.Home
            },
            onSave = {
                val next = repeatConfig.next
                capturedImageUri = null
                repeatConfig = next
                if (next.isDone) {
                    photoName = ""
                    repeatConfig = RepeatConfig()
                    currentScreen = Screen.Home
                } else {
                    currentScreen = Screen.Camera
                }
            }
        )
    }
}