package pl.kpmarczynski.jsonplaceholderprocessor.supplier

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

object HttpSupplier : Supplier {
    override fun getData(request: String): String =
        when (val result = request.httpGet().responseString().third) {
            is Result.Success -> result.get()
            is Result.Failure -> throw RuntimeException("Request failed")
        }
}
