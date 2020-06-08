package pl.kpmarczynski.jsonplaceholderprocessor.writer

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Test
import java.io.BufferedWriter
import java.io.File

class FileWriterProviderTest {

    @Test
    fun `For WritableObject should return FileWriter`() {
        val file = mockk<File>()
        val writer = mockk<BufferedWriter>()
        val writerProvider = spyk<FileWriterProvider>(recordPrivateCalls = true)
        every { writerProvider["getFile"](any<String>()) } returns file
        every { writerProvider["getBufferedWriter"](file) } returns writer
        every { file.exists() } returns true
        every { file.path } returns ""

        writerProvider.getWriter(Writable.WritableObject("1", "asdf"), emptyMap())
    }

    @Test
    fun `Should create file with expected extension`(){}

    @Test
    fun `Should add dot to extension if necessary`(){}

    @Test
    fun `Should create parent directory if not exists`(){}

    @Test
    fun `Should not create parent directory if exists`(){}

    @Test
    fun `Should create file in default location`(){}

    @Test
    fun `Should create file in parent directory`(){}

    @Test
    fun `Should create file with id as name`(){}


}
