package pl.kpmarczynski.dataprocessor.parser

import pl.kpmarczynski.dataprocessor.writer.Writable

interface Parser {
    fun parse(source: String, config: Map<String, String>): Writable
}

