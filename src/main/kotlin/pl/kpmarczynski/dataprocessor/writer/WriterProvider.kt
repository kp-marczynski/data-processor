package pl.kpmarczynski.dataprocessor.writer

import java.io.Writer

interface WriterProvider {
    fun getWriter(writable: Writable, config: Map<String, String>): Writer
}
