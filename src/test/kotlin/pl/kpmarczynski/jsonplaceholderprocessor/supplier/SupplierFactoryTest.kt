package pl.kpmarczynski.dataprocessor.supplier

import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.junit.Assert.assertThat
import org.junit.Test
import kotlin.test.assertFailsWith

internal class SupplierFactoryTest {
    @Test
    fun `For http request type should return HttpSupplier instance`() {
        //given
        val requestType = "http"

        //when
        val result = SupplierFactory.getSupplier(requestType)

        //then
        assertThat(result, instanceOf(HttpSupplier::class.java))
    }

    @Test
    fun `For https request type should return HttpSupplier instance`() {
        //given
        val requestType = "https"

        //when
        val result = SupplierFactory.getSupplier(requestType)

        //then
        assertThat(result, instanceOf(HttpSupplier::class.java))
    }

    @Test
    fun `For ftp request type should throw exception`() {
        //given
        val requestType = "ftp"

        //when
        val exception = assertFailsWith<SupplierException> { SupplierFactory.getSupplier(requestType)}

        //then
        MatcherAssert.assertThat(exception.message, CoreMatchers.`is`("No supplier available for request type $requestType"))
    }
}
