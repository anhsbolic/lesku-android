package id.lesku.lesku

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import id.lesku.lesku.helper.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(dashboardToolbar)
        dashboardToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        actionBar = supportActionBar!!

        BottomNavigationViewHelper.disableShiftMode(dashboardBottomNav)
    }
}
