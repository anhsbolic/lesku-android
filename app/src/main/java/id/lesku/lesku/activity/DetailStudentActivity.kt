package id.lesku.lesku.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import id.lesku.lesku.R
import kotlinx.android.synthetic.main.activity_detail_student.*

class DetailStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_student)
        setSupportActionBar(detailStudentToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        detailStudentToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
