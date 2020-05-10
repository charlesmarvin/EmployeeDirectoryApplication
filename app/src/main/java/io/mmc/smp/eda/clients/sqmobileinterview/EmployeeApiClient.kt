package io.mmc.smp.eda.clients.sqmobileinterview

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.net.URL
import java.util.*

const val EmployeeApiBaseUrl = "/sq-mobile-interview"

interface EmployeeApiClient {
    @GET("$EmployeeApiBaseUrl/employees.json")
    suspend fun getEmployees(): GetEmployeesResponse
}

object EmployeeApiClientFactory {
    fun create(baseOkHttpClient: OkHttpClient = OkHttpClient(), baseUrl: URL): EmployeeApiClient {
        // create customizable client from base client
        val client = baseOkHttpClient
            .newBuilder()
            .followRedirects(false)
            .build()
        val moshi = Moshi.Builder()
            .add(UUIDAdapter())
            .add(URLAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
        // build retrofit proxy
        return Retrofit.Builder()
            .baseUrl("$baseUrl$EmployeeApiBaseUrl/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(EmployeeApiClient::class.java)
    }
}
class UUIDAdapter {
    @ToJson
    fun toJson(uuid: UUID): String {
        return uuid.toString()
    }

    @FromJson
    fun fromJson(json: String): UUID {
        return UUID.fromString(json)
    }
}

class URLAdapter {
    @ToJson
    fun toJson(url: URL): String {
        return url.toString()
    }

    @FromJson
    fun fromJson(json: String): URL {
        return URL(json)
    }
}

data class GetEmployeesResponse(
    @Json(name = "employees") val employees: EmployeeList
)

typealias EmployeeList = List<Employee>

data class Employee(
    @Json(name = "uuid") val uuid: UUID,
    @Json(name = "full_name") val fullName: String,
    @Json(name = "phone_number") val phoneNumber: String?,
    @Json(name = "email_address") val emailAddress: String,
    @Json(name = "biography") val biography: String?,
    @Json(name = "photo_url_small") val photoUrlSmall: URL?,
    @Json(name = "photo_url_large") val photoUrlLarge: URL?,
    @Json(name = "team") val team: String,
    @Json(name = "employee_type") val employeeType: EmployeeType
)

enum class EmployeeType {
    FULL_TIME, PART_TIME, CONTRACTOR
}
