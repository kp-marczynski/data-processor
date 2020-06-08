package pl.kpmarczynski.jsonplaceholderprocessor.writer

import mu.KotlinLogging
import java.io.IOException

sealed class Writable {
    abstract fun write(writerProvider: WriterProvider, config: Map<String, String>)

    data class WritableObject(val identifier: String, private val content: String) : Writable() {
        override fun write(writerProvider: WriterProvider, config: Map<String, String>) {
            writerProvider.getWriter(this, config).use {
                try {
                    it.write(content)
                } catch (ex: IOException) {
                    throw WriterException("Writing of object with id $identifier has failed")
                }
            }
            logger.info("Object with id $identifier written")
        }

        companion object {
            private val logger = KotlinLogging.logger {}
        }
    }

    data class WritableCollection(private val objects: List<WritableObject>) : Writable() {
        override fun write(writerProvider: WriterProvider, config: Map<String, String>) {
            logger.info { "Attempting to write collection of ${objects.size} objects" }
            objects.forEach { it.write(writerProvider, config) }
            logger.info("All of ${objects.size} objects in collection written")
        }

        companion object {
            private val logger = KotlinLogging.logger {}
        }
    }
}


