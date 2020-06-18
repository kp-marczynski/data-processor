package pl.kpmarczynski.dataprocessor.supplier

interface Supplier {
    fun getData(request: String): String
}

