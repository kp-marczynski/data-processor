package pl.kpmarczynski.dataprocessor.supplier

object SupplierFactory {
    fun getSupplier(requestType: String): Supplier =
        when (requestType) {
            "http", "https" -> HttpSupplier
            else -> throw SupplierException("No supplier available for request type $requestType")
        }
}
