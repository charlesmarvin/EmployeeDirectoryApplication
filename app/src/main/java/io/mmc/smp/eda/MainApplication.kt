package io.mmc.smp.eda

import android.app.Application
import io.mmc.smp.eda.clients.sqmobileinterview.EmployeeApiClientFactory
import io.mmc.smp.eda.profile.ProfileViewModel
import okhttp3.OkHttpClient
import java.net.URL

class MainApplication : Application() {
    val context = MainAppContext()
}

class MainAppContext {
    private val baseOkHttpClient = OkHttpClient()
    private val baseUrl = URL("https://s3.amazonaws.com")
    val employeeApiClient = EmployeeApiClientFactory.create(baseOkHttpClient, baseUrl)
}