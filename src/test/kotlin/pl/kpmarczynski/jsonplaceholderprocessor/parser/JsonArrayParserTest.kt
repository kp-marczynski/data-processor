package pl.kpmarczynski.jsonplaceholderprocessor.parser

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test
import pl.kpmarczynski.jsonplaceholderprocessor.writer.Writable
import kotlin.test.assertFailsWith

class JsonArrayParserTest {
    val jsonObject1 =
        "{\"id\":1,\"title\":\"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\"body\":\"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\",\"userId\":1}"
    val jsonObject2 =
        "{\"id\":2,\"title\":\"qui est esse\",\"body\":\"est rerum tempore vitae\\nsequi sint nihil reprehenderit dolor beatae ea dolores neque\\nfugiat blanditiis voluptate porro vel nihil molestiae ut reiciendis\\nqui aperiam non debitis possimus qui neque nisi nulla\",\"userId\":1}"
    val jsonObjectWithCustomId =
        "{\"name\":3,\"title\":\"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\"body\":\"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\",\"userId\":1}"
    val prettyJsonObject1 = "{\n" +
            "    \"id\": 1,\n" +
            "    \"title\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\n" +
            "    \"body\": \"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\",\n" +
            "    \"userId\": 1\n" +
            "}"

    @Test
    fun `JsonArray string should be parsed to WritableCollection`() {
        //given
        val expectedResult = Writable.WritableCollection(
            listOf(
                Writable.WritableObject("1", jsonObject1),
                Writable.WritableObject("2", jsonObject2)
            )
        )

        //when
        val result = JsonArrayParser.parse("[$jsonObject1,$jsonObject2]", emptyMap())

        //then
        assertThat(result, instanceOf(Writable.WritableCollection::class.java))
        assertThat(result as Writable.WritableCollection, `is`(expectedResult))
    }

    @Test
    fun `Parsing JsonArray with custom identifier should throw exception`() {
        //given

        //when
        val exception =
            assertFailsWith<ParserException> { JsonArrayParser.parse("[$jsonObjectWithCustomId]", emptyMap()) }

        //then
        assertThat(exception.message, `is`("Identifier for key 'id' is not present in JsonObject"))
    }

    @Test
    fun `JsonArray with configured custom identifier should be parsed to WritableCollection`() {
        //given
        val expectedResult = Writable.WritableCollection(
            listOf(
                Writable.WritableObject("3", jsonObjectWithCustomId)
            )
        )
        //when
        val result = JsonArrayParser.parse("[$jsonObjectWithCustomId]", mapOf("identifierKey" to "name"))

        //then
        assertThat(result, instanceOf(Writable.WritableCollection::class.java))
        assertThat(result as Writable.WritableCollection, `is`(expectedResult))
    }

    @Test
    fun `When prettify is configured then should be parsed to pretty WritableCollection`() {
        //given
        val expectedResult = Writable.WritableCollection(
            listOf(
                Writable.WritableObject("1", prettyJsonObject1)
            )
        )

        //when
        val result = JsonArrayParser.parse("[$jsonObject1]", mapOf("prettify" to "4"))

        //then
        assertThat(result, instanceOf(Writable.WritableCollection::class.java))
        assertThat(result as Writable.WritableCollection, `is`(expectedResult))
    }

    @Test
    fun `Parsing malformed JsonArray string should throw exception`() {
        //given

        //when
        val exception =
            assertFailsWith<ParserException> { JsonArrayParser.parse("[$jsonObject1", emptyMap()) }

        //then
        assertThat(exception.message, `is`("Given string is not in json array format"))
    }
}
