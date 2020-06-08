package pl.kpmarczynski.dataprocessor.parser

import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.junit.Assert.assertThat
import org.junit.Test
import kotlin.test.assertFailsWith

internal class ParserFactoryTest {
    @Test
    fun `For jsonarray result type should return JsonArrayParser instance`() {
        //given
        val resultType = "jsonarray"

        //when
        val result = ParserFactory.getParser(resultType)

        //then
        assertThat(result, instanceOf(JsonArrayParser::class.java))
    }

    @Test
    fun `For xml result type should throw exception`() {
        //given
        val resultType = "xml"

        //when
        val exception = assertFailsWith<ParserException> { ParserFactory.getParser(resultType)}

        //then
        MatcherAssert.assertThat(exception.message, CoreMatchers.`is`("No parser available for result type $resultType"))
    }
}
