package pl.kpmarczynski.dataprocessor.writer

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.IOException
import java.io.Writer
import kotlin.test.assertFailsWith

class WritableTest {
    @Test
    fun `Should write WritableObject with provided writer`() {
        //given
        val writer = mockk<Writer>()
        val writerProvider = mockk<WriterProvider>()
        val config = emptyMap<String, String>()
        val writable = Writable.WritableObject("1", "content")

        every { writerProvider.getWriter(writable, config) } returns writer
        every { writer.write(any<String>()) } returns Unit
        every { writer.close() } returns Unit

        //when
        writable.write(writerProvider, config)

        //then
        verify { writer.write(writable.content) }
    }

    @Test
    fun `Should write all objects in WritableCollection with provided writer`() {
        //given
        val writer1 = mockk<Writer>()
        val writer2 = mockk<Writer>()
        val writerProvider = mockk<WriterProvider>()
        val config = emptyMap<String, String>()
        val writable1 = Writable.WritableObject("1", "content1")
        val writable2 = Writable.WritableObject("2", "content2")
        val writableCollection = Writable.WritableCollection(listOf(writable1, writable2))

        every { writerProvider.getWriter(writable1, config) } returns writer1
        every { writer1.write(any<String>()) } returns Unit
        every { writer1.close() } returns Unit

        every { writerProvider.getWriter(writable2, config) } returns writer2
        every { writer2.write(any<String>()) } returns Unit
        every { writer2.close() } returns Unit

        //when
        writableCollection.write(writerProvider, config)

        //then
        verify { writer1.write(writable1.content) }
        verify { writer2.write(writable2.content) }
    }

    @Test
    fun `When writing fails then should throw exception`() {
        //given
        val writer = mockk<Writer>()
        val writerProvider = mockk<WriterProvider>()
        val config = emptyMap<String, String>()
        val writable = Writable.WritableObject("1", "content")

        every { writerProvider.getWriter(writable, config) } returns writer
        every { writer.write(any<String>()) } throws IOException()
        every { writer.close() } returns Unit

        //when
        val exception = assertFailsWith<WriterException> { writable.write(writerProvider, config) }

        //then
        assertThat(exception.message, `is`("Writing of object with id ${writable.identifier} has failed"))
    }
}
