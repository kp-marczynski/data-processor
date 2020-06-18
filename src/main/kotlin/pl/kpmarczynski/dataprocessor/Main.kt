package pl.kpmarczynski.dataprocessor

import pl.kpmarczynski.dataprocessor.command.DownloadAndSaveCommand
import pl.kpmarczynski.dataprocessor.parser.ParserFactory
import pl.kpmarczynski.dataprocessor.supplier.SupplierFactory
import pl.kpmarczynski.dataprocessor.writer.WriterProviderFactory

fun main(args: Array<String> = emptyArray()) {
    DownloadAndSaveCommand(SupplierFactory, ParserFactory, WriterProviderFactory).main(args)
}
