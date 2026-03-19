package co.com.computingsoftdev.minipos.domain.usecase.sale

class CalculateTotalUseCase {
    fun execute(subtotal: Long, tax: Long = 0L, discount: Long = 0L): Long {
        return subtotal + tax - discount
    }
}