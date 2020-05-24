package io.mmc.smp.eda.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import io.mmc.smp.eda.R
import io.mmc.smp.eda.clients.sqmobileinterview.Employee
import io.mmc.smp.eda.viewutils.formatAsPhoneNumber
import io.mmc.smp.eda.viewutils.loadAvatar

class EmployeeDetailFragment(private val employee: Employee) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.employee_detail_fragment, container, false)
        view.findViewById<TextView>(R.id.full_name).text = employee.fullName
        view.findViewById<TextView>(R.id.team).text = employee.team
        view.findViewById<TextView>(R.id.email).text = employee.emailAddress
        view.findViewById<TextView>(R.id.biography).text = employee.biography
        view.findViewById<TextView>(R.id.phone_number).text = employee.phoneNumber?.formatAsPhoneNumber()
        employee.photoUrlLarge?.let { view.findViewById<ImageView>(R.id.avatar).loadAvatar(it) }
        return view
    }

}
