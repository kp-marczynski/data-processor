package pl.kpmarczynski.jsonplaceholderprocessor.parser

object ParserFactory {
    fun getParser(resultType: String): Parser =
        when (resultType) {
            "jsonarray" -> JsonArrayParser
            else -> throw RuntimeException("No parser")
        }
}
