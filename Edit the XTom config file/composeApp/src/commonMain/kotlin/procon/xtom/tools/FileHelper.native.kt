package procon.xtom.tools

fun loadPlatformSpecificConfig(fileName: String): FileHelper? {
    // Implementation for loading config on desktop (JVM)
    return try {
        val configFile = File(fileName) // Creates a java.io.File object representing the file path
        if (configFile.exists() && configFile.isFile) { // Check if it exists and is a regular file
            val jsonString = configFile.readText() // Reads the entire content of the file as a String
            Gson().fromJson(jsonString, FileHelper::class.java) // Parses the JSON string into a procon.xtom.tools.FileHelper object
        } else {
            println("Warning: Configuration file '$fileName' not found or is not a regular file.")
            null // Return null if the file doesn't exist or isn't a file
        }
    } catch (e: Exception) {
        // Handle exceptions (e.g., file access issues, JSON parsing error)
        println("Error loading configuration from '$fileName': ${e.message}")
        e.printStackTrace() // Print stack trace for debugging
        null // Return null in case of any error
    }
}

fun loadConfigFromPlatform(fileName: String): FileHelper? {
    // This implementation delegates to loadPlatformSpecificConfig.
    // This is fine if the loading logic is identical for both on this platform.
    return loadPlatformSpecificConfig(fileName)
}

fun saveConfigToPlatform(fileName: String, configData: FileHelper) {
    // Implementation for saving config on desktop (JVM)
    try {
        val configFile = File(fileName) // Creates a java.io.File object
        // Ensure parent directory exists if the file is nested
        configFile.parentFile?.mkdirs()

        val jsonString = Gson().toJson(configData) // Serializes the procon.xtom.tools.FileHelper object to a JSON string
        configFile.writeText(jsonString) // Writes the JSON string to the file, overwriting if it exists
        println("Configuration saved to '$fileName'")
    } catch (e: Exception) {
        // Handle exceptions (e.g., file access issues, serialization error)
        println("Error saving configuration to '$fileName': ${e.message}")
        e.printStackTrace() // Print stack trace for debugging
    }
}