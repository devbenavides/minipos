package co.com.computingsoftdev.minipos.core.extensions

import co.com.computingsoftdev.minipos.domain.model.SaleStatus

fun String.toSaleStatus(): SaleStatus {
    return try {
        SaleStatus.valueOf(this.uppercase())
    } catch (e: Exception) {
        SaleStatus.PENDING
    }
}