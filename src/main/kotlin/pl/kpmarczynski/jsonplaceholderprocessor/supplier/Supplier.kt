package pl.kpmarczynski.jsonplaceholderprocessor.supplier

interface Supplier {
    fun getData(request: String): String
}

