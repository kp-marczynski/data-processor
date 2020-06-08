package pl.kpmarczynski.dataprocessor.supplier

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import mu.KotlinLogging

object HttpSupplier : Supplier {
    private val logger = KotlinLogging.logger {}

    override fun getData(request: String): String {
        logger.info("Attempting to retrieve data from $request")
        return when (val result = request.httpGet().responseString().third) {
            is Result.Success -> {
                logger.info("Data successfully retrieved from $request")
                result.get()
            }
            is Result.Failure -> throw SupplierException("Http request failed")
        }
    }

}
