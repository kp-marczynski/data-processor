package pl.kpmarczynski.jsonplaceholderprocessor.parser

import org.json.JSONArray
import org.json.JSONObject
import pl.kpmarczynski.jsonplaceholderprocessor.writer.Writable

object JsonArrayParser : Parser {
    private const val DEFAULT_PRETTIFY = 0
    private const val DEFAULT_IDENTIFIER_KEY = "id"

    override fun parse(source: String, config: Map<String, String>): Writable {
        val identifierKey = config["identifierKey"] ?: DEFAULT_IDENTIFIER_KEY
        val prettify = config["prettify"]?.toIntOrNull() ?: DEFAULT_PRETTIFY

        val jsonArray = JSONArray(source)
        return Writable.WritableCollection(jsonArray.map {
            val jsonObject = it as JSONObject
            val identifier = jsonObject[identifierKey]
                ?: throw RuntimeException("Identifier for given key not present in JsonObject")

            Writable.WritableObject(identifier.toString(), jsonObject.toString(prettify))
        })
    }
}
