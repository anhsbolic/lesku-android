package id.lesku.lesku.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.view.MenuItem
import id.lesku.lesku.R
import id.lesku.lesku.fragment.TodayFragment
import id.lesku.lesku.helper.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var actionBar: ActionBar
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(dashboardToolbar)
        dashboardToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        actionBar = supportActionBar!!

        displaySelectedScreen(R.id.menu_today)

        BottomNavigationViewHelper.disableShiftMode(dashboardBottomNav)
        dashboardBottomNav.setOnNavigationItemSelectedListener {item: MenuItem ->
            displaySelectedScreen(item.itemId)
            return@setOnNavigationItemSelectedListener true
        }

    }

    private fun displaySelectedScreen(itemId: Int) {
        var fragment: Fragment? = null

        when(itemId){
            R.id.menu_today ->{
                fragment = TodayFragment()
                actionBar.title = "Jadwal Hari Ini"
            }
            R.id.menu_schedule ->{

            }
            R.id.menu_students ->{

            }
            R.id.menu_profile ->{

            }
            else -> {
                fragment = TodayFragment()
                actionBar.title = "Jadwal Hari Ini"
            }
        }

        if(fragment != null){
            supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.dashboardFrameContainer, fragment)
                    .commit()
        }
    }

}
