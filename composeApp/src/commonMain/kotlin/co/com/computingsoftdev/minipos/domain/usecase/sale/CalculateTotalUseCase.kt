package co.com.computingsoftdev.minipos.domain.usecase.sale

class CalculateTotalUseCase(
    private val taxPercentage: Double = 0.0 // opcional, 0 si no hay impuesto
) {

    fun execute(subtotal: Long): Long {
        return subtotal + (subtotal * taxPercentage / 100).toLong()
    }
}