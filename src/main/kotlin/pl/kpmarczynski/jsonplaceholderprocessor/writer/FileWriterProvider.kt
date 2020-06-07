package pl.kpmarczynski.jsonplaceholderprocessor.writer

import java.io.File
import java.io.Writer

object FileWriterProvider : WriterProvider {
    private const val DEFAULT_FILE_EXTENSION = ".json"
    private const val DEFAULT_PATH = "posts"

    override fun getWriter(writable: Writable, config: Map<String, String>): Writer {
        val path: String = config["path"]
            .let { if (it.isNullOrBlank()) DEFAULT_PATH else it }
            .let { if (it.last() != '/') "$it/" else it }
        val extension = config["extension"] ?: DEFAULT_FILE_EXTENSION
            .let {
                if (it.first() != '.') ".$it" else it
            }
        return when (writable) {
            is Writable.WritableObject -> {
                val filepath = "$path${writable.identifier}$extension"
                File(File(filepath).parent).let { parent -> if (!parent.exists()) parent.mkdirs() }
                File(filepath).bufferedWriter()
            }
            else -> throw WriterProviderException("File writer is applicable only for WritableObject instances.")
        }

    }
}
