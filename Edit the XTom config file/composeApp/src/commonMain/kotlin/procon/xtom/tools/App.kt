package procon.xtom.tools

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.text.KeyboardOptions // Required for KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType    // Required for KeyboardType
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun ConfigEditorScreen(initialConfig: FileHelper?, onSave: (FileHelper) -> Unit) {
    var serverUrl = initialConfig?.serverUrl ?: ""
    var maxConnections = initialConfig?.maxConnections?.toString() ?: ""
    var featureEnabled = initialConfig?.featureEnabled == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
                // .safeContentPadding(), // safeContentPadding is Android specific, consider alternatives or expect/actual for padding if needed across platforms
                horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Konfiguration bearbeiten", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = serverUrl,
            onValueChange = { serverUrl = it },
            label = {Text("Server URL")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = maxConnections,
            onValueChange = { maxConnections = it },
            label = { Text("Maximale Verbindungen") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = featureEnabled,
                onCheckedChange = { featureEnabled = it }
            )
            Spacer(Modifier.width(8.dp))
            Text("Feature aktivieren")
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                val currentMaxConnections = maxConnections.toIntOrNull() ?: initialConfig?.maxConnections
                val updatedConfig = FileHelper(
                    serverUrl = serverUrl,
                    maxConnections = currentMaxConnections as Int,
                    featureEnabled = featureEnabled
                )
                onSave(updatedConfig)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Speichern")
        }
    }
}

@Composable
@Preview // Preview will likely work best when building for Android where LocalContext is available.
fun App() {
    MaterialTheme {
        var fileHelperState: FileHelper? by remember { mutableStateOf<FileHelper?>(
            loadConfigFromPlatform("user_config.json") as FileHelper?
        ) }
        var showSaveConfirmation by remember { mutableStateOf(false) } // State to control the confirmation message

        if (fileHelperState != null) {
            ConfigEditorScreen(
                initialConfig = fileHelperState,
                onSave = { updatedConfig ->
                    saveConfigToPlatform("user_config.json", updatedConfig)
                    fileHelperState = updatedConfig // Update the state with the new config
                    showSaveConfirmation = true // Show confirmation message
                }
            )

            // Optionally, display a Snackbar or a simple Text for confirmation
            if (showSaveConfirmation) {
                // You might want to use a Snackbar for a less intrusive message
                // For simplicity, a Text composable is shown here.
                // This Text will appear below the ConfigEditorScreen.
                // You'll need to adjust your layout if you want it to overlay or appear differently.
                LaunchedEffect(Unit) { // Use LaunchedEffect to hide the message after a delay
                    kotlinx.coroutines.delay(3000) // Display for 3 seconds
                    showSaveConfirmation = false
                }
                Text("Konfiguration gespeichert.", modifier = Modifier.padding(16.dp))
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Konfiguration konnte nicht geladen werden.")
            }
        }
    }
}