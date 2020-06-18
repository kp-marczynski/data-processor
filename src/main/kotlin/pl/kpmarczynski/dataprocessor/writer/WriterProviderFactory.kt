package pl.kpmarczynski.dataprocessor.writer

object WriterProviderFactory {
    fun getWriterProvider(destinationType: String): WriterProvider =
        when (destinationType) {
            "file" -> FileWriterProvider
            else -> throw WriterException("No writer available for destination type $destinationType")
        }
}
