package pl.kpmarczynski.jsonplaceholderprocessor

import java.io.File
import java.io.Writer

interface WriterProvider {
    fun getWriter(filepath: String): Writer
}

object FileWriterProvider : WriterProvider {
    override fun getWriter(filepath: String): Writer {
        File(File(filepath).parent).let { parent -> if (!parent.exists()) parent.mkdirs() }
        return File(filepath).bufferedWriter()
    }
}
