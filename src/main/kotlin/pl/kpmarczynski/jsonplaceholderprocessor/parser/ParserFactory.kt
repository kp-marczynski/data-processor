package pl.kpmarczynski.jsonplaceholderprocessor.parser

object ParserFactory {
    fun getParser(resultType: String): Parser =
        when (resultType) {
            "jsonarray" -> JsonArrayParser
            else -> throw ParserException("No parser available for result type $resultType")
        }
}
