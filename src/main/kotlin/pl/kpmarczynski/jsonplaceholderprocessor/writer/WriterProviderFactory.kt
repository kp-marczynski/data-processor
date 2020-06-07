package pl.kpmarczynski.jsonplaceholderprocessor.writer

object WriterProviderFactory {
    fun getWriterProvider(destinationType: String): WriterProvider =
        when (destinationType) {
            "file" -> FileWriterProvider
            else -> throw RuntimeException("No supplier")
        }
}
