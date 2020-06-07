package pl.kpmarczynski.jsonplaceholderprocessor.supplier

import com.github.kittinunf.fuel.core.Client
import com.github.kittinunf.fuel.core.FuelManager
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.fail


class HttpSupplierTest {

    @Test
    fun `For successful request should return expected data `() {
        //given
        val url = "http://test.com"
        val expectedResult = "sample result"
        setupFuelMock(expectedResult)

        //when
        val result = HttpSupplier.getData(url)

        //then
        assertThat(result, `is`(expectedResult))
    }

    @Test
    fun `For failed request should throw exception`() {
        //given
        val url = "http://test.com"
        setupFuelMock("data", 404)

        //when
        val exception = assertFailsWith<SupplierException> { HttpSupplier.getData(url) }

        //then
        assertThat(exception.message, `is`("Http request failed"))
    }

    private fun setupFuelMock(resultData: String, statusCode: Int = 200) {
        val client = mockk<Client>()

        every { client.executeRequest(any()).statusCode } returns statusCode
        every { client.executeRequest(any()).data } returns resultData.toByteArray()

        FuelManager.instance.client = client
    }
}
