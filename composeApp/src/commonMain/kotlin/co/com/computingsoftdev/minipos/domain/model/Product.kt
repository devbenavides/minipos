package co.com.computingsoftdev.minipos.domain.model

data class Product(
    val id: Long,
    val name: String,
    val price: Long,
    val description: String?
)
