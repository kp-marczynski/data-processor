package pl.kpmarczynski.jsonplaceholderprocessor.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.associate
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import mu.KotlinLogging
import pl.kpmarczynski.jsonplaceholderprocessor.parser.ParserFactory
import pl.kpmarczynski.jsonplaceholderprocessor.supplier.SupplierFactory
import pl.kpmarczynski.jsonplaceholderprocessor.writer.Writable
import pl.kpmarczynski.jsonplaceholderprocessor.writer.WriterProviderFactory

class DownloadAndSaveCommand : CliktCommand() {
    private val source: String by option("-s", "--source", help = "Source to download jsons")
        .default("https://jsonplaceholder.typicode.com/posts")
    private val requestType: String by option("-r", "--request-type", help = "Request type protocol").default("https")
    private val sourceResultType: String by option(
        "--source-result-type",
        help = "Type of result received from source"
    ).default("jsonarray")
    private val destinationType: String by option("--output-type", help = "Output type").default("file")
    private val supplierConfig: Map<String, String> by option().associate()
    private val parserConfig: Map<String, String> by option().associate()
    private val writerConfig: Map<String, String> by option().associate()

    //    private val outputDir: String by option("-o", "--output-dir", help = "Output directory").default("posts")

    //    private val prettify: String by option("-p", "--prettify", help = "Flag for prettifying output json").default("0")
//    private val expectedFields: List<String> by option("-f", "--fields").multiple()

//    private val identifierKey: String by option("--identifier-key").default("id")

    private val logger = KotlinLogging.logger {}

    override fun run() {
        getData().let { data ->
            parseData(data).let { parsedData ->
                writeData(parsedData)
            }
        }
    }

    private fun getData() = SupplierFactory.getSupplier(requestType).getData(source)
    private fun parseData(data: String) = ParserFactory.getParser(sourceResultType).parse(data, parserConfig)
    private fun writeData(writable: Writable) =
        writable.write(WriterProviderFactory.getWriterProvider(destinationType), writerConfig)
}
