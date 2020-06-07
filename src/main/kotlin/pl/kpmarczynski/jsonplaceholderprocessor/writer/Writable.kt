package pl.kpmarczynski.jsonplaceholderprocessor.writer

sealed class Writable {
    abstract fun write(writerProvider: WriterProvider, config: Map<String, String>)
    class WritableObject(val identifier: String, val content: String) : Writable() {
        override fun write(writerProvider: WriterProvider, config: Map<String, String>) {
            writerProvider.getWriter(this, config).use { it.write(content) }
        }
    }

    class WritableCollection(val objects: List<WritableObject>) : Writable() {
        override fun write(writerProvider: WriterProvider, config: Map<String, String>) {
            objects.forEach { it.write(writerProvider, config) }
        }
    }
}


