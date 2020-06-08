package pl.kpmarczynski.jsonplaceholderprocessor.writer

import mu.KotlinLogging
import java.io.File
import java.io.Writer

object FileWriterProvider : WriterProvider {
    private const val DEFAULT_FILE_EXTENSION = ".json"
    private const val DEFAULT_PATH = "posts"

    private val logger = KotlinLogging.logger {}

    override fun getWriter(writable: Writable, config: Map<String, String>): Writer {
        return when (writable) {
            is Writable.WritableObject -> {
                val path: String = getPath(config)
                val extension = getExtension(config)
                val filepath = "$path${writable.identifier}$extension"
                logger.info("Preparing file writer for filepath '$filepath'")

                getFile(path).let {
                    if (!it.exists()){
                        it.mkdirs()
                        logger.info("Created directory $path")
                    }
                }
                getBufferedWriter(getFile(filepath))
            }
            else -> throw WriterException("File writer is applicable only for WritableObject instances.")
        }

    }

    private fun getPath(config: Map<String, String>) = config["path"]
        .let { if (it.isNullOrBlank()) DEFAULT_PATH else it }
        .let { if (it.last() != '/') "$it/" else it }

    private fun getExtension(config: Map<String, String>) = config["extension"]
        ?: DEFAULT_FILE_EXTENSION
            .let { if (it.first() != '.') ".$it" else it }

    private fun getFile(url: String) = File(url)
    private fun getBufferedWriter(file: File) = file.bufferedWriter()
}
