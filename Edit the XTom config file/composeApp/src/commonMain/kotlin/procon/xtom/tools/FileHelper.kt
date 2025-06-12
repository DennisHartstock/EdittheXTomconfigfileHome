package procon.xtom.tools

// Define the data class in commonMain as it's platform-agnostic
data class FileHelper(
    val serverUrl: String,
    val maxConnections: Int,
    val featureEnabled: Boolean
)