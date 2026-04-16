package com.mst.claudecamerax.views

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.mst.claudecamerax.model.Flash


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    photoName     : String,
    shotIndex     : Int,
    flashMode     : Flash,          // ← nuevo
    onFlashToggle : () -> Unit,         // ← nuevo
    onPhotoTaken  : (Uri) -> Unit,
    onBack        : () -> Unit
) {
    val context        = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    val previewView = remember { PreviewView(context) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) onBack()
    }

    LaunchedEffect(hasCameraPermission) {
        if (hasCameraPermission) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                    .build()
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al iniciar cámara", Toast.LENGTH_SHORT).show()
                }
            }, ContextCompat.getMainExecutor(context))
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Actualiza el flash mode en imageCapture cada vez que cambia
    LaunchedEffect(flashMode) {
        imageCapture?.flashMode = flashMode.cameraxValue
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foto #$shotIndex") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←", style = MaterialTheme.typography.headlineMedium)
                    }
                },
                // ── Botón de flash en la barra superior ──────────────────────
                actions = {
                    IconButton(onClick = onFlashToggle) {
                        Text(
                            text  = flashMode.icon,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

            Column(
                modifier            = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Chip de nombre de archivo
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text     = "Guardando como: $photoName",
                        modifier = Modifier.padding(12.dp),
                        style    = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Chip del estado de flash (tappable para cambiar)
//                Surface(
//                    color    = when (flashMode) {
//                        Flash.OFF  -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
//                        Flash.ON   -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.9f)
//                        Flash.AUTO -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.9f)
//                    },
//                    shape    = MaterialTheme.shapes.medium,
//                    onClick  = onFlashToggle          // también tappable desde acá
//                ) {
//                    Text(
//                        text     = flashMode.label,
//                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//                        style    = MaterialTheme.typography.bodySmall
//                    )
//                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de captura
                Button(
                    onClick  = {
                        // Aplica el flash mode justo antes de disparar
                        imageCapture?.flashMode = flashMode.cameraxValue
                        capturePhoto(context, photoName, imageCapture, onPhotoTaken)
                    },
                    modifier = Modifier.size(80.dp),
                    shape    = MaterialTheme.shapes.extraLarge,
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("📷", style = MaterialTheme.typography.headlineLarge)
                }
            }
        }
    }
}

// ─── Función de captura ───────────────────────────────────────────────────────

private fun capturePhoto(
    context      : android.content.Context,
    photoName    : String,
    imageCapture : ImageCapture?,
    onPhotoTaken : (Uri) -> Unit
) {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, photoName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MiRecorrido")
    }
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()

    imageCapture?.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                output.savedUri?.let(onPhotoTaken)
            }
            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, "Error al capturar foto", Toast.LENGTH_SHORT).show()
            }
        }
    )
}