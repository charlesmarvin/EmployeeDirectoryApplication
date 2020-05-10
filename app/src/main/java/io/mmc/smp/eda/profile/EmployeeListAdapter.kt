package io.mmc.smp.eda.profile

import android.content.Context
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.mmc.smp.eda.R
import io.mmc.smp.eda.clients.sqmobileinterview.EmployeeList
import java.net.URL
import java.util.*

class EmployeeListAdapter(context: Context) :
    RecyclerView.Adapter<EmployeeListAdapter.EmployeeListHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    var employeeList: EmployeeList = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class EmployeeListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullNameView: TextView = itemView.findViewById(
            R.id.employee_full_name
        )
        val teamView: TextView = itemView.findViewById(
            R.id.employee_team
        )
        val phoneNumber: TextView = itemView.findViewById(
            R.id.employee_phone_number
        )
        val email: TextView = itemView.findViewById(
            R.id.employee_email
        )
        val avatar: ImageView = itemView.findViewById(
            R.id.employee_avatar
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeListHolder {
        val itemView: View = layoutInflater.inflate(
            R.layout.employee_list_item, parent, false
        )
        return EmployeeListHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }

    override fun onBindViewHolder(holder: EmployeeListHolder, position: Int) {
        val employee = employeeList[position]
        holder.fullNameView.text = employee.fullName
        holder.teamView.text = employee.team
        holder.email.text = employee.emailAddress
        holder.phoneNumber.text = employee.phoneNumber?.let(this::fmtPhoneNumber)
        employee.photoUrlSmall?.let(avatarLoader(holder.avatar))
        // TODO add binding to launch detail fragment on click
    }

    // need to investigate the performance impact of this partial function call
    private fun avatarLoader(imageView: ImageView) = { url: URL ->
        Picasso.get()
            .load(url.toString())
            .into(imageView)
    }

    private fun fmtPhoneNumber(number: String) = PhoneNumberUtils.formatNumber(
        number,
        Locale.getDefault().country
    )
}