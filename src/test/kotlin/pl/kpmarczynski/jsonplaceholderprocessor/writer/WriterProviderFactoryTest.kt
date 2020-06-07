package pl.kpmarczynski.jsonplaceholderprocessor.writer

import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.junit.Assert.assertThat
import org.junit.Test
import kotlin.test.assertFailsWith

internal class WriterProviderFactoryTest {
    @Test
    fun `For file destination type should return HttpSupplier instance`() {
        //given
        val destinationType = "file"

        //when
        val result = WriterProviderFactory.getWriterProvider(destinationType)

        //then
        assertThat(result, instanceOf(FileWriterProvider::class.java))
    }

    @Test
    fun `For ftp destination type should throw exception`() {
        //given
        val destinationType = "ftp"

        //when
        val exception = assertFailsWith<WriterProviderException> { WriterProviderFactory.getWriterProvider(destinationType)}

        //then
        MatcherAssert.assertThat(exception.message, CoreMatchers.`is`("No writer available for destination type $destinationType"))
    }
}
