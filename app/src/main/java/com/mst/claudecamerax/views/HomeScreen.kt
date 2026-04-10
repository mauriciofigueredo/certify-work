package com.mst.claudecamerax.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    photoName: String,
    onPhotoNameChange: (String) -> Unit,
    totalShots: Int,
    onTotalShotsChange: (Int) -> Unit,
    onOpenCamera: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Fotos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nombre para las fotos",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = photoName,
                onValueChange = onPhotoNameChange,
                label = { Text("Nombre base") },
                placeholder = { Text("Ej: 12557") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = if (totalShots > 0) totalShots.toString() else "",
                onValueChange = { raw ->
                    val n = raw.filter { it.isDigit() }.toIntOrNull() ?: 0
                    onTotalShotsChange(n.coerceIn(1, 99))
                },
                label = { Text("Cantidad de fotos") },
                placeholder = { Text("Ej: 4") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Text(
                text = if (totalShots > 1)
                    "Se tomarán $totalShots fotos: ${photoName.ifEmpty { "IMG_…" }}_1 … _$totalShots"
                else
                    "Se tomará 1 foto sin sufijo de repetición",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onOpenCamera,
                enabled = totalShots >= 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    if (totalShots == 1) "Abrir Cámara (1 foto)"
                    else "Abrir Cámara ($totalShots fotos)",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
