package pl.kpmarczynski.dataprocessor.command

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import pl.kpmarczynski.dataprocessor.parser.Parser
import pl.kpmarczynski.dataprocessor.parser.ParserException
import pl.kpmarczynski.dataprocessor.parser.ParserFactory
import pl.kpmarczynski.dataprocessor.supplier.Supplier
import pl.kpmarczynski.dataprocessor.supplier.SupplierException
import pl.kpmarczynski.dataprocessor.supplier.SupplierFactory
import pl.kpmarczynski.dataprocessor.writer.Writable
import pl.kpmarczynski.dataprocessor.writer.WriterException
import pl.kpmarczynski.dataprocessor.writer.WriterProvider
import pl.kpmarczynski.dataprocessor.writer.WriterProviderFactory
import kotlin.test.BeforeTest

class DownloadAndSaveCommandTest {
    private val supplierFactory = mockk<SupplierFactory>()
    private val supplier = mockk<Supplier>()
    private val supplierResult = "supplier result"
    private val parserFactory = mockk<ParserFactory>()
    private val parser = mockk<Parser>()
    private val writerProviderFactory = mockk<WriterProviderFactory>()
    private val writerProvider = mockk<WriterProvider>()
    private val writable = mockk<Writable>()
    private val downloadAndSaveCommand = DownloadAndSaveCommand(supplierFactory, parserFactory, writerProviderFactory)

    @BeforeTest
    fun setup() {
        clearAllMocks()
        every { supplierFactory.getSupplier(any()) } returns supplier
        every { supplier.getData(any()) } returns supplierResult
        every { parserFactory.getParser(any()) } returns parser
        every { parser.parse(supplierResult, any()) } returns writable
        every { writerProviderFactory.getWriterProvider(any()) } returns writerProvider
        every { writable.write(writerProvider, any()) } returns Unit
    }

    @Test
    fun `Should download and save data`() {
        //given

        //when
        downloadAndSaveCommand.main(emptyArray())

        //then
        verify { writable.write(writerProvider, any()) }
    }

    @Test
    fun `Should pass config to supplier`() {
        //given
        val source = "test"
        val config = listOf("--source", source)

        //when
        downloadAndSaveCommand.main(config)

        //then
        verify(exactly = 1) { supplier.getData(source) }
    }

    @Test
    fun `Should pass config to parser`() {
        //given
        val key1 = "key1"
        val value1 = "val1"
        val key2 = "key2"
        val value2 = "val2"
        val args = listOf("--parser-config", "$key1=$value1", "--parser-config", "$key2=$value2")
        val config = mapOf(key1 to value1, key2 to value2)

        //when
        downloadAndSaveCommand.main(args)

        //then
        verify(exactly = 1) { parser.parse(any(), config) }
    }

    @Test
    fun `Should pass config to writer`() {
        //given
        val key1 = "key1"
        val value1 = "val1"
        val key2 = "key2"
        val value2 = "val2"
        val args = listOf("--writer-config", "$key1=$value1", "--writer-config", "$key2=$value2")
        val config = mapOf(key1 to value1, key2 to value2)

        //when
        downloadAndSaveCommand.main(args)

        //then
        verify(exactly = 1) { writable.write(writerProvider, config) }
    }

    @Test
    fun `Should request expected supplier type from factory`() {
        //given
        val type = "type"
        val args = listOf("--request-type", type)

        //when
        downloadAndSaveCommand.main(args)

        //then
        verify(exactly = 1) { supplierFactory.getSupplier(type) }
    }

    @Test
    fun `Should request expected parser type from factory`() {
        //given
        val type = "type"
        val args = listOf("--parser-type", type)

        //when
        downloadAndSaveCommand.main(args)

        //then
        verify(exactly = 1) { parserFactory.getParser(type) }
    }

    @Test
    fun `Should request expected writer provider type from factory`() {
        //given
        val type = "type"
        val args = listOf("--output-type", type)

        //when
        downloadAndSaveCommand.main(args)

        //then
        verify(exactly = 1) { writerProviderFactory.getWriterProvider(type) }
    }

    @Test
    fun `When supplier fails then parser should not be invoked`() {
        //given
        every { supplier.getData(any()) } throws SupplierException("")

        //when
        downloadAndSaveCommand.main(emptyArray())

        //then
        verify(exactly = 0) { parserFactory.getParser(any()) }
    }

    @Test
    fun `When parser fails then writerprovider should not be invoked`() {
        //given
        every { parser.parse(any(), any()) } throws ParserException("")

        //when
        downloadAndSaveCommand.main(emptyArray())

        //then
        verify(exactly = 0) { writerProviderFactory.getWriterProvider(any()) }
    }

    @Test
    fun `When write fails then exception is caught`() {
        //given
        every { writable.write(any(), any()) } throws WriterException("")

        //when
        downloadAndSaveCommand.main(emptyArray())

        //then
    }
}
