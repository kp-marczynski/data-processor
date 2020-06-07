package pl.kpmarczynski.jsonplaceholderprocessor.supplier

object SupplierFactory {
    fun getSupplier(requestType: String): Supplier =
        when (requestType) {
            "http", "https" -> HttpSupplier
            else -> throw RuntimeException("No supplier")
        }
}
