package com.mst.claudecamerax.views

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.mst.claudecamerax.model.RepeatConfig


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    imageUri: Uri?,
    photoName: String,
    repeatConfig: RepeatConfig,
    onDiscard: () -> Unit,
    onSave: () -> Unit
) {
    val context = LocalContext.current
    val isLastShot = repeatConfig.currentIndex == repeatConfig.totalShots

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vista Previa — Foto ${repeatConfig.currentIndex} / ${repeatConfig.totalShots}") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                imageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Foto capturada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Nombre: $photoName",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = if (isLastShot) "Última foto de la serie" else "Siguiente: ${repeatConfig.currentIndex + 1} / ${repeatConfig.totalShots}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            imageUri?.let { uri ->
                                context.contentResolver.delete(uri, null, null)
                            }
                            Toast.makeText(context, "Foto descartada", Toast.LENGTH_SHORT).show()
                            onDiscard()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Descartar serie")
                    }

                    Button(
                        onClick = {
                            val msg = if (isLastShot) "Serie completa guardada" else "Foto guardada — siguiente toma"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            onSave()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (isLastShot) "Guardar y terminar" else "Guardar y continuar")
                    }
                }
            }
        }
    }
}
