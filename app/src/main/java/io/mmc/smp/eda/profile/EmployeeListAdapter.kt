package io.mmc.smp.eda.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.mmc.smp.eda.R
import io.mmc.smp.eda.clients.sqmobileinterview.EmployeeList
import io.mmc.smp.eda.viewutils.formatAsPhoneNumber
import io.mmc.smp.eda.viewutils.loadAvatar

typealias ItemClickListener = (Int) -> Unit

class EmployeeListAdapter(context: Context) :
    RecyclerView.Adapter<EmployeeListAdapter.EmployeeListHolder>() {
    private val layoutInflater = LayoutInflater.from(context)
    var employeeList: EmployeeList = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    private var itemClickListener: ItemClickListener? = null

    fun setItemClickListener(listener: ItemClickListener?) {
        itemClickListener = listener
    }

    class EmployeeListHolder(
        itemView: View,
        itemClickListener: ItemClickListener?
    ) : RecyclerView.ViewHolder(itemView) {
        val fullNameView: TextView = itemView.findViewById(
            R.id.full_name
        )
        val teamView: TextView = itemView.findViewById(
            R.id.team
        )
        val phoneNumber: TextView = itemView.findViewById(
            R.id.phone_number
        )
        val email: TextView = itemView.findViewById(
            R.id.email
        )
        val avatar: ImageView = itemView.findViewById(
            R.id.employee_avatar
        )

        init {
            itemClickListener?.let {
                itemView.setOnClickListener {
                    itemClickListener(adapterPosition)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeListHolder {
        val itemView: View = layoutInflater.inflate(
            R.layout.employee_list_item, parent, false
        )
        return EmployeeListHolder(
            itemView,
            itemClickListener
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
        holder.phoneNumber.text = employee.phoneNumber?.formatAsPhoneNumber()
        employee.photoUrlSmall?.let { holder.avatar.loadAvatar(it) }
    }
}