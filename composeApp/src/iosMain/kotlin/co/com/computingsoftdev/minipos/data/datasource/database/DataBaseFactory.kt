package co.com.computingsoftdev.minipos.data.datasource.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.com.computingsoftdev.minipos.database.MiniPosDatabase

actual class DatabaseFactory actual constructor(context: Any?) {
    private val driver: SqlDriver = NativeSqliteDriver(MiniPosDatabase.Schema, "minipos.db")

    actual val database: MiniPosDatabase by lazy { MiniPosDatabase(driver) }
    actual val productQueries get() = database.productQueries
    actual val saleQueries get() = database.saleQueries
    actual val saleItemQueries get() = database.saleItemQueries
}