package co.com.computingsoftdev.minipos.data.datasource.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import co.com.computingsoftdev.minipos.database.MiniPosDatabase

actual class DatabaseFactory actual constructor(context: Any?) {
    private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
        MiniPosDatabase.Schema.create(it) // crea las tablas
    }

    actual val database: MiniPosDatabase by lazy { MiniPosDatabase(driver) }
    actual val productQueries get() = database.productQueries
    actual val saleQueries get() = database.saleQueries
    actual val saleItemQueries get() = database.saleItemQueries
}