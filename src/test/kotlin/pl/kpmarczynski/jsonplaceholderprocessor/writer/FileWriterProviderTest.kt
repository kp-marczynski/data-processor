package pl.kpmarczynski.dataprocessor.writer

import io.mockk.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.BufferedWriter
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.assertFailsWith

class FileWriterProviderTest {

    private val file = mockk<File>()
    private val writer = mockk<BufferedWriter>()
    private val writerProvider = spyk<FileWriterProvider>(recordPrivateCalls = true)
    private val writable = Writable.WritableObject("1", "content")

    @BeforeTest
    fun setup() {
        clearAllMocks()
        every { writerProvider["getFile"](any<String>()) } returns file
        every { writerProvider["getBufferedWriter"](file) } returns writer
        every { file.exists() } returns true
        every { file.path } returns ""
    }

    @Test
    fun `For WritableObject should return BufferedWriter`() {
        //given

        //when
        val result = writerProvider.getWriter(writable, emptyMap())

        //then
        assertThat(result, instanceOf(BufferedWriter::class.java))
    }

    @Test
    fun `Should create file with expected extension`() {
        //given

        //when
        writerProvider.getWriter(writable, emptyMap())

        //then
        verify { writerProvider["getFile"]("${FileWriterProvider.DEFAULT_PATH}/${writable.identifier}${FileWriterProvider.DEFAULT_FILE_EXTENSION}") }
    }

    @Test
    fun `Should add dot to extension if necessary`() {
        //given
        val extension = "xml"
        val config = mapOf("extension" to extension)

        //when
        writerProvider.getWriter(writable, config)

        //then
        verify { writerProvider["getFile"]("${FileWriterProvider.DEFAULT_PATH}/${writable.identifier}.$extension") }
    }

    @Test
    fun `Should create parent directory if not exists`() {
        //given
        val parentFile = mockk<File>()
        val parentPath = "posts"
        val config = mapOf("path" to parentPath)
        every { writerProvider["getFile"]("$parentPath/") } returns parentFile
        every { parentFile.exists() } returns false
        every { parentFile.mkdirs() } returns true

        //when
        writerProvider.getWriter(writable, config)

        //then
        verify { parentFile.mkdirs() }
    }

    @Test
    fun `Should not create parent directory if exists`() {
        //given
        val parentFile = mockk<File>()
        val parentPath = "posts"
        val config = mapOf("path" to parentPath)
        every { writerProvider["getFile"]("$parentPath/") } returns parentFile
        every { parentFile.exists() } returns true

        //when
        writerProvider.getWriter(writable, config)

        //then
        verify(exactly = 0) { parentFile.mkdirs() }
    }


    @Test
    fun `Should create file in parent directory`() {
        //given
        val path = "a/b/c"
        val config = mapOf("path" to path)

        //when
        writerProvider.getWriter(writable, config)

        //then
        verify { writerProvider["getFile"]("$path/${writable.identifier}${FileWriterProvider.DEFAULT_FILE_EXTENSION}") }
    }

    @Test
    fun `Creating Writer instance for WritableCollection should throw exception`() {
        //given
        val writableCollection = Writable.WritableCollection(listOf(writable))
        //when
        val exception = assertFailsWith<WriterException> { writerProvider.getWriter(writableCollection, emptyMap()) }

        //then
        assertThat(exception.message, `is`("File writer is applicable only for WritableObject instances."))
    }

}
