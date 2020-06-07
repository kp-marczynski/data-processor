package pl.kpmarczynski.jsonplaceholderprocessor.writer

object WriterProviderFactory {
    fun getWriterProvider(destinationType: String): WriterProvider =
        when (destinationType) {
            "file" -> FileWriterProvider
            else -> throw WriterProviderException("No writer available for destination type $destinationType")
        }
}
