package io.mmc.smp.eda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.mmc.smp.eda.profile.EmployeeListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EmployeeListFragment.newInstance())
                .commitNow()
        }
    }

}
