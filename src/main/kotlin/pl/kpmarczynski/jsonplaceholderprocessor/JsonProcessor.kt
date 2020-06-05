package pl.kpmarczynski.jsonplaceholderprocessor

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import mu.KotlinLogging.logger
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

private const val JSON_FILE_EXTENSION = "json"
private const val PRETTIFY_INDENTATION = 4

class JsonProcessor(private val writerProvider: WriterProvider) : CliktCommand() {
    private val source: String by option("-s", "--source", help = "Source to download jsons")
        .default("https://jsonplaceholder.typicode.com/posts")
    private val outputDir: String by option("-o", "--output-dir", help = "Output directory").default("posts")
    private val prettify: Boolean by option("-p", "--prettify", help = "Flag for prettifying output json").flag()
    private val expectedFields: List<String> by option("-f", "--fields").multiple()

    private val logger = logger {}

    override fun run() {
        downloadJsonArray()?.let {
            saveJsonArray(it)
        }
    }

    fun saveJsonArray(jsonArray: JSONArray) {
        jsonArray.forEach { json ->
            saveJsonObject(json as JSONObject)
        }
    }

    fun downloadJsonArray(): JSONArray? =
        when (val result = source.httpGet().responseJson().third) {
            is Result.Success -> {
                try {
                    result.get().array()
                } catch (ex: JSONException) {
                    logger.error("Can't parse result string to array")
                    null
                }
            }
            is Result.Failure -> {
                logger.error("Can't retrieve result")
                null
            }
        }

    fun saveJsonObject(jsonObject: JSONObject) {
        if (jsonObject.keySet().containsAll(expectedFields)) {
            if (jsonObject.has("id")) {
                val jsonObjectId = jsonObject["id"]
                val filePath = "$outputDir/$jsonObjectId.$JSON_FILE_EXTENSION"
                val indentation = if (prettify) PRETTIFY_INDENTATION else 0
                writerProvider.getWriter(filePath).use { it.write(jsonObject.toString(indentation)) }
                logger.info("Json with id $jsonObjectId saved!")
            } else {
                logger.error("Can't save json without id")
            }
        } else {
            logger.error { "Json doesn't have all required fields!" }
        }
    }
}
