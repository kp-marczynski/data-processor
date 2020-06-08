package pl.kpmarczynski.jsonplaceholderprocessor

import pl.kpmarczynski.jsonplaceholderprocessor.command.DownloadAndSaveCommand
import pl.kpmarczynski.jsonplaceholderprocessor.parser.ParserFactory
import pl.kpmarczynski.jsonplaceholderprocessor.supplier.SupplierFactory
import pl.kpmarczynski.jsonplaceholderprocessor.writer.WriterProviderFactory

fun main(args: Array<String> = emptyArray()) {
    DownloadAndSaveCommand(SupplierFactory, ParserFactory, WriterProviderFactory).main(args)
}
