package pl.kpmarczynski.dataprocessor.parser

import mu.KotlinLogging
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pl.kpmarczynski.dataprocessor.writer.Writable

object JsonArrayParser : Parser {
    private const val DEFAULT_PRETTIFY = 0
    private const val DEFAULT_IDENTIFIER_KEY = "id"

    private val logger = KotlinLogging.logger {}

    override fun parse(source: String, config: Map<String, String>): Writable {
        val identifierKey = config["identifierKey"].let { if (it.isNullOrBlank()) DEFAULT_IDENTIFIER_KEY else it }
        val indentation = config["indentation"]?.toIntOrNull() ?: DEFAULT_PRETTIFY

        logger.info("Attempting to parse jsonarray data to WritableCollection with identifierKey '$identifierKey' and indentation $indentation")

        val result = Writable.WritableCollection(parseJsonArray(source).map {
            mapJsonObjectToWritable(it as JSONObject, identifierKey, indentation)
        })
        logger.info("Data successfully parsed to WritableCollection")
        return result
    }

    private fun mapJsonObjectToWritable(
        jsonObject: JSONObject,
        identifierKey: String,
        indentation: Int
    ): Writable.WritableObject {
        val identifier = if (jsonObject.has(identifierKey)) {
            jsonObject[identifierKey]
        } else throw ParserException("Identifier for key '$identifierKey' is not present in JsonObject")

        return Writable.WritableObject(identifier.toString(), jsonObject.toString(indentation))
    }

    private fun parseJsonArray(data: String): JSONArray = try {
        JSONArray(data)
    } catch (ex: JSONException) {
        throw ParserException("Given string is not in json array format")
    }
}
