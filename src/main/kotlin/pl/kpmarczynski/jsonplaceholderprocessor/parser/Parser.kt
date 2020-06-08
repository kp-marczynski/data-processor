package pl.kpmarczynski.jsonplaceholderprocessor.parser

import pl.kpmarczynski.jsonplaceholderprocessor.writer.Writable

interface Parser {
    fun parse(source: String, config: Map<String, String>): Writable
}

