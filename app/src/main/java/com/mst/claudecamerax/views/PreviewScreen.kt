package com.mst.claudecamerax.views

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    imageUri  : Uri?,
    photoName : String,
    shotIndex : Int,
    onDiscard : () -> Unit,
    onNext    : () -> Unit,
    onFinish  : () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vista Previa — Foto #$shotIndex") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Imagen ocupa todo el espacio disponible
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                imageUri?.let { uri ->
                    Image(
                        painter            = rememberAsyncImagePainter(uri),
                        contentDescription = "Foto capturada",
                        modifier           = Modifier.fillMaxSize(),
                        contentScale       = ContentScale.Fit
                    )
                }
            }

            // Panel inferior
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text     = "Nombre: $photoName",
                    style    = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Fila 1: Descartar | Guardar y continuar
                Row(
                    modifier            = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick  = {
                            imageUri?.let { uri ->
                                context.contentResolver.delete(uri, null, null)
                            }
                            Toast.makeText(context, "Foto descartada", Toast.LENGTH_SHORT).show()
                            onDiscard()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reintentar")
                    }

                    Button(
                        onClick  = {
                            Toast.makeText(context, "Foto guardada — siguiente toma", Toast.LENGTH_SHORT).show()
                            onNext()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Fila 2: Finalizar serie (ancho completo)
                Button(
                    onClick  = {
                        Toast.makeText(context, "Serie finalizada", Toast.LENGTH_SHORT).show()
                        onFinish()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Finalizar serie")
                }
            }
        }
    }
}