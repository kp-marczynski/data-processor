package pl.kpmarczynski.jsonplaceholderprocessor.writer

import java.io.Writer

interface WriterProvider {
    fun getWriter(writable: Writable, config: Map<String, String>): Writer
}
