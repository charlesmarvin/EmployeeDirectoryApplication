package io.mmc.smp.eda.clients.sqmobileinterview

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.runBlocking
import org.junit.*
import java.lang.IllegalArgumentException
import java.net.URL
import java.util.*


class EmployeeApiClientTest {

    private val employeeApiClient =
        EmployeeApiClientFactory.create(baseUrl = URL(wireMockServer.baseUrl()))

    @After
    fun after() {
        wireMockServer.resetAll()
    }

    companion object {
        var wireMockServer = WireMockServer()

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            wireMockServer.start()
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            wireMockServer.stop()
        }
    }

    @Test
    fun testGetEmployeesParsesApiResponseWhenAllEmployeeFieldsArePresent() {
        // given
        val testEmployees = listOf(
            Employee(
                uuid = UUID.fromString("0d8fcc12-4d0c-425c-8355-390b312b909c"),
                fullName = "Jane Doe",
                phoneNumber = "1-800-273-8255",
                emailAddress = "jane@example.com",
                biography = "Chief Happiness Officer.",
                photoUrlLarge = URL("https://example.com/large.jpg"),
                photoUrlSmall = URL("https://example.com/small.jpg"),
                team = "S-Team",
                employeeType = EmployeeType.FULL_TIME
            )
        )
        stubGet(
            path = "/sq-mobile-interview/employees.json",
            responseBodyResourcePath = "/test/clients/sqmobileinterview/employees.json"
        )

        // when
        val actualEmployeeList = runBlocking {
            employeeApiClient.getEmployees()
        }

        // then
        val expectedEmployeeList = GetEmployeesResponse(employees = testEmployees)
        Assert.assertEquals(expectedEmployeeList, actualEmployeeList)

    }

    @Test
    fun testGetEmployeesParsesApiResponseWhenNoEmployeesAreReturned() {
        // given
        val testEmployees = emptyList<Employee>()
        stubGet(
            path = "/sq-mobile-interview/employees.json",
            responseBodyResourcePath = "/test/clients/sqmobileinterview/employees_empty.json"
        )

        // when
        val actualEmployeeList = runBlocking {
            employeeApiClient.getEmployees()
        }

        // then
        val expectedEmployeeList = GetEmployeesResponse(employees = testEmployees)
        Assert.assertEquals(expectedEmployeeList, actualEmployeeList)
    }

    @Test(expected = JsonDataException::class)
    fun testGetEmployeesThrowsErrorWhenBadDataIsReturned() {
        // given
        stubGet(
            path = "/sq-mobile-interview/employees.json",
            responseBodyResourcePath = "/test/clients/sqmobileinterview/employees_malformed.json"
        )

        // when
        runBlocking {
            employeeApiClient.getEmployees()
        }

        // then
        Assert.fail("Exception should be thrown")
    }

    private fun stubGet(
        path: String,
        responseBody: String? = null,
        responseBodyResourcePath: String? = null
    ) {
        val body = when {
            responseBody != null -> responseBody
            responseBodyResourcePath != null -> javaClass.getResource(responseBodyResourcePath)!!.readText()
            else -> throw IllegalArgumentException("Must provide either response body or resource path")
        }
        wireMockServer.stubFor(
            WireMock.get(WireMock.urlPathEqualTo(path))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(body)
                )
        )
    }
}
