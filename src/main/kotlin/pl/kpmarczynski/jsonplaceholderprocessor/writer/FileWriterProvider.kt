package pl.kpmarczynski.jsonplaceholderprocessor.writer

import java.io.File
import java.io.Writer

object FileWriterProvider : WriterProvider {
    private const val DEFAULT_FILE_EXTENSION = ".json"
    private const val DEFAULT_PATH = "posts"

    override fun getWriter(writable: Writable, config: Map<String, String>): Writer {
        val path = config["path"] ?: DEFAULT_PATH
        val extension = config["extension"] ?: DEFAULT_FILE_EXTENSION
        return when(writable){
            is Writable.WritableObject -> {
                val filepath = "$path/${writable.identifier}.$extension"
                File(File(filepath).parent).let { parent -> if (!parent.exists()) parent.mkdirs() }
                File(filepath).bufferedWriter()
            }
            else -> throw RuntimeException("File writer is applicable only for WritableObject")
        }

    }
}
