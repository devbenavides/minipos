package co.com.computingsoftdev.minipos.data.datasource.database

import app.cash.sqldelight.db.SqlDriver
import co.com.computingsoftdev.minipos.database.MiniPosDatabase
import co.com.computingsoftdev.minipos.database.ProductQueries
import co.com.computingsoftdev.minipos.database.SaleItemQueries
import co.com.computingsoftdev.minipos.database.SaleQueries

expect class DatabaseFactory(context: Any? = null) {
    val database: MiniPosDatabase
    val productQueries: ProductQueries
    val saleQueries: SaleQueries
    val saleItemQueries: SaleItemQueries
}