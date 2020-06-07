//package pl.kpmarczynski.jsonplaceholderprocessor.supplier
//
//import org.junit.jupiter.api.Assertions
//import org.junit.jupiter.api.DynamicTest.dynamicTest
//import org.junit.jupiter.api.TestFactory
//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.Arguments
//import org.junit.jupiter.params.provider.MethodSource
//import kotlin.test.assertEquals
//
//internal class SupplierFactoryTest {
//    @ParameterizedTest
//    @MethodSource("squares")
//    fun testSquares(input: Int, expected: Int) {
//        Assertions.assertEquals(expected, input * input)
//    }
//
//    @TestFactory
//    fun `test email id validity`() =
//        listOf(
//            "mary@testdomain.com" to true,
//            "mary.smith@testdomain.com" to true,
//            "mary_smith123@testdomain.com" to true,
//            "mary@testdomaindotcom" to false,
//            "mary-smith@testdomain" to false,
//            "testdomain.com" to false
//        ).map {
//            dynamicTest("email ${it.first} should be ${if (it.second) "valid" else "not valid" }") {
//                val actual = "asdf"
//                assertEquals(it.first, actual)
//            }
//        }
//
//
//    companion object {
//        @JvmStatic
//        fun squares() = listOf(
//            Arguments.of(1, 1),
//            Arguments.of(2, 4)
//        )
//    }
//}
