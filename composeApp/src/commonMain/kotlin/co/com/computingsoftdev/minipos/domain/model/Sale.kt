package co.com.computingsoftdev.minipos.domain.model

data class Sale(
    val id: Long,
    val items: List<SaleItem>,
    val subtotal: Long,
    val total: Long,
    val date: Long
)
