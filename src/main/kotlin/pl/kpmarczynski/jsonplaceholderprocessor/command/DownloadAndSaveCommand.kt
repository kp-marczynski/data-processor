package pl.kpmarczynski.dataprocessor.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.associate
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import mu.KotlinLogging
import pl.kpmarczynski.dataprocessor.parser.ParserException
import pl.kpmarczynski.dataprocessor.parser.ParserFactory
import pl.kpmarczynski.dataprocessor.supplier.SupplierException
import pl.kpmarczynski.dataprocessor.supplier.SupplierFactory
import pl.kpmarczynski.dataprocessor.writer.Writable
import pl.kpmarczynski.dataprocessor.writer.WriterException
import pl.kpmarczynski.dataprocessor.writer.WriterProviderFactory

class DownloadAndSaveCommand(
    private val supplierFactory: SupplierFactory,
    private val parserFactory: ParserFactory,
    private val writerProviderFactory: WriterProviderFactory
) : CliktCommand(help = "This script allows user to download data from provided source and save it with selected writer") {
    private val requestType: String
            by option("-r", "--request-type", help = "Request type protocol. Supported options: http, https. Default: https")
                .default("https")
    private val suppliedDataType: String
            by option("-p", "--parser-type", help = "Data type for parser. Supported options: jsonarray. Default: jsonarray")
                .default("jsonarray")
    private val destinationType: String
            by option("-o", "--output-type", help = "Output type. Supported options: file. Default: file")
                .default("file")

    private val source: String
            by option("-s", "--source", help = "Source from data should be downloaded. Default: https://jsonplaceholder.typicode.com/posts")
                .default("https://jsonplaceholder.typicode.com/posts")
    private val parserConfig: Map<String, String>
            by option("--parser-config", help = "Collection of configuration pairs for parser")
                .associate()
    private val writerConfig: Map<String, String>
            by option("--writer-config", help = "Collection of configuration pairs for writer")
                .associate()

    private val logger = KotlinLogging.logger {}

    override fun run() {
        logger.info("Starting download and save command")
        getData().let { data ->
            if (data != null)
                parseData(data).let { parsedData ->
                    if (parsedData != null)
                        writeData(parsedData)
                }
        }
        logger.info("Download and save command has finished execution")
    }

    private fun getData(): String? = try {
        supplierFactory.getSupplier(requestType).getData(source)
    } catch (ex: SupplierException) {
        logger.error(ex.message)
        null
    }

    private fun parseData(data: String): Writable? = try {
        parserFactory.getParser(suppliedDataType).parse(data, parserConfig)
    } catch (ex: ParserException) {
        logger.error(ex.message)
        null
    }

    private fun writeData(writable: Writable) = try {
        writable.write(writerProviderFactory.getWriterProvider(destinationType), writerConfig)
    } catch (ex: WriterException) {
        logger.error(ex.message)
    }

}

