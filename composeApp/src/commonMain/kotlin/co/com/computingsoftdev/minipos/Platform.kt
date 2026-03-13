package co.com.computingsoftdev.minipos

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform