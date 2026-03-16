package co.com.computingsoftdev.minipos.data.datasource.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import co.com.computingsoftdev.minipos.database.MiniPosDatabase

actual class DatabaseFactory actual constructor(context: Any?) {
    private val driver = AndroidSqliteDriver(
        schema = MiniPosDatabase.Schema,
        context = context as Context,
        name = "minipos.db"
    )

    actual val database: MiniPosDatabase by lazy { MiniPosDatabase(driver) }
    actual val productQueries get() = database.productQueries
    actual val saleQueries get() = database.saleQueries
    actual val saleItemQueries get() = database.saleItemQueries
}