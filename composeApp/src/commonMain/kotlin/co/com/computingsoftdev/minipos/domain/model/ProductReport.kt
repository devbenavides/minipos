package co.com.computingsoftdev.minipos.domain.model

data class ProductReport(
    val productId: Long,
    val productName: String,
    val totalQuantity: Int,
    val totalRevenue: Long
)