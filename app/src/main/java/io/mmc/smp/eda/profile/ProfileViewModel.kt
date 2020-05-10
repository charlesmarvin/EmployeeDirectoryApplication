package io.mmc.smp.eda.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mmc.smp.eda.clients.sqmobileinterview.EmployeeApiClient
import io.mmc.smp.eda.clients.sqmobileinterview.EmployeeList
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val employeeApiClient: EmployeeApiClient
) : ViewModel() {
    val employees = MutableLiveData<EmployeeList>()

    init {
        viewModelScope.launch {
            val employeeList = employeeApiClient.getEmployees()
            employees.value = employeeList.employees
        }
    }
}
