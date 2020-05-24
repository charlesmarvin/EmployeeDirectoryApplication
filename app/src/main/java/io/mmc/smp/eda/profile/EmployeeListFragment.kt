package io.mmc.smp.eda.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.mmc.smp.eda.MainApplication
import io.mmc.smp.eda.R
import io.mmc.smp.eda.clients.sqmobileinterview.EmployeeList
import io.mmc.smp.eda.viewutils.getViewModel


class EmployeeListFragment : Fragment() {

    companion object {
        fun newInstance() = EmployeeListFragment()
    }

    private val viewModel: ProfileViewModel by lazy {
        activity?.let { activity ->
            activity.getViewModel {
                ProfileViewModel((activity.application as MainApplication).context.employeeApiClient)
            }
        } ?: throw IllegalStateException("Attempt to access ViewModel before Fragment is attached!")
    }

    private lateinit var adapter: EmployeeListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var fragmentContext: Context
    private lateinit var employeeListUpdateListener: Observer<EmployeeList>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = EmployeeListAdapter(fragmentContext)
        adapter.setItemClickListener(this::onSelect)
        layoutManager = LinearLayoutManager(context)

        employeeListUpdateListener = Observer { employees ->
            adapter.employeeList = employees
        }
        viewModel.employees.observe(this, employeeListUpdateListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.employee_list_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.employees.removeObserver(employeeListUpdateListener)
    }

    private fun onSelect(position: Int) {
        val activity = fragmentContext as AppCompatActivity
        val detailFragment: Fragment = EmployeeDetailFragment(adapter.employeeList[position])
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.main, detailFragment)
            .addToBackStack(null)
            .commit()

    }
}
