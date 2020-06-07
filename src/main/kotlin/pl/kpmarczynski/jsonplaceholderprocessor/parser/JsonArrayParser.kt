package pl.kpmarczynski.jsonplaceholderprocessor.parser

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pl.kpmarczynski.jsonplaceholderprocessor.writer.Writable

object JsonArrayParser : Parser {
    private const val DEFAULT_PRETTIFY = 0
    private const val DEFAULT_IDENTIFIER_KEY = "id"

    override fun parse(source: String, config: Map<String, String>): Writable {
        val identifierKey = config["identifierKey"] ?: DEFAULT_IDENTIFIER_KEY
        val prettify = config["prettify"]?.toIntOrNull() ?: DEFAULT_PRETTIFY

        val jsonArray = try {
            JSONArray(source)
        } catch (ex: JSONException) {
            throw ParserException("Given string is not in json array format")
        }
        return Writable.WritableCollection(jsonArray.map {
            val jsonObject = it as JSONObject
            val identifier = if (jsonObject.has(identifierKey)) {
                jsonObject[identifierKey]
            } else throw ParserException("Identifier for key '$identifierKey' is not present in JsonObject")

            Writable.WritableObject(identifier.toString(), jsonObject.toString(prettify))
        })
    }
}
