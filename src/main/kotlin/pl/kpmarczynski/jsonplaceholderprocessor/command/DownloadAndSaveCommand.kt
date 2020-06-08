package pl.kpmarczynski.jsonplaceholderprocessor.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.associate
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import mu.KotlinLogging
import pl.kpmarczynski.jsonplaceholderprocessor.parser.ParserException
import pl.kpmarczynski.jsonplaceholderprocessor.parser.ParserFactory
import pl.kpmarczynski.jsonplaceholderprocessor.supplier.SupplierException
import pl.kpmarczynski.jsonplaceholderprocessor.supplier.SupplierFactory
import pl.kpmarczynski.jsonplaceholderprocessor.writer.Writable
import pl.kpmarczynski.jsonplaceholderprocessor.writer.WriterException
import pl.kpmarczynski.jsonplaceholderprocessor.writer.WriterProviderFactory

class DownloadAndSaveCommand : CliktCommand() {
    private val source: String
            by option("-s", "--source", help = "Source to download jsons")
                .default("https://jsonplaceholder.typicode.com/posts")
    private val requestType: String
            by option("-r", "--request-type", help = "Request type protocol")
                .default("https")
    private val suppliedDataType: String
            by option("-d", "--supplied-data-type", help = "Type of result received from source")
                .default("jsonarray")
    private val destinationType: String
            by option("-o", "--output-type", help = "Output type")
                .default("file")
    private val parserConfig: Map<String, String>
            by option("-p", "--parser-config", help = "Collection of configuration pairs for parser")
                .associate()
    private val writerConfig: Map<String, String>
            by option("-w", "--writer-config", help = "Collection of configuration pairs for writer")
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
        SupplierFactory.getSupplier(requestType).getData(source)
    } catch (ex: SupplierException) {
        logger.error(ex.message)
        null
    }

    private fun parseData(data: String): Writable? = try {
        ParserFactory.getParser(suppliedDataType).parse(data, parserConfig)
    } catch (ex: ParserException) {
        logger.error(ex.message)
        null
    }

    private fun writeData(writable: Writable) = try {
        writable.write(WriterProviderFactory.getWriterProvider(destinationType), writerConfig)
    } catch (ex: WriterException) {
        logger.error(ex.message)
    }

}

