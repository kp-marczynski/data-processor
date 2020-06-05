package pl.kpmarczynski.jsonplaceholderprocessor

import com.github.kittinunf.fuel.core.*
import io.mockk.*
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Test
import java.io.StringWriter
import org.hamcrest.CoreMatchers.`is` as isEqualTo

class MainTest {
    val json1 = "{" +
            "\"id\":1," +
            "\"title\":\"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\"," +
            "\"body\":\"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\"," +
            "\"userId\":1" +
            "}"

    @Test
    fun test() {
        setupFuelMock("[$json1]")
        val writerProvider: WriterProvider = mockk()
        val writer = StringWriter()
        every { writerProvider.getWriter(any()) } returns writer
        //when
        JsonProcessor(writerProvider).main(emptyArray())
        //then
        assertThat(writer.toString(), isEqualTo(json1))
    }

    fun setupFuelMock(resultData: String, statusCode: Int = 200){
        val client = mockk<Client>()

        every { client.executeRequest(any()).statusCode } returns statusCode
        every { client.executeRequest(any()).data } returns resultData.toByteArray()

        FuelManager.instance.client = client
    }
}
