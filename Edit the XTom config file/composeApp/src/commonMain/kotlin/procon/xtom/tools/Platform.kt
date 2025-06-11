package procon.xtom.tools

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform