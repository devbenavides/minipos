package co.com.computingsoftdev.minipos.data.datasource.local


import co.com.computingsoftdev.minipos.database.ProductQueries
import co.com.computingsoftdev.minipos.domain.model.Product

class ProductLocalDataSource(
    private val queries: ProductQueries
) {
    fun getAll() =
        queries.selectAllProducts().executeAsList()

    fun getById(id: Long): Product? {
        val entity = queries.selectProductById(id).executeAsOneOrNull()
        return entity?.let {
            Product(
                id = it.id,
                name = it.name,
                price = it.price,
                description = it.description
            )
        }
    }

    fun insert(name: String, price: Long, description: String?) {
        queries.insertProduct(
            name=name,
            price=price,
            description=description
        )
    }

    fun update(
        id: Long,
        name: String,
        price: Long,
        description: String?
    ) {
        queries.updateProduct(
            name = name,
            price = price,
            description = description,
            id = id
        )
    }

    fun delete(id: Long) {
        queries.deleteProduct(id)
    }
}