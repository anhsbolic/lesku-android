package id.lesku.lesku.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.lesku.lesku.R
import id.lesku.lesku.activity.AddStudentActivity
import id.lesku.lesku.activity.DashboardActivity
import id.lesku.lesku.activity.DetailStudentActivity
import id.lesku.lesku.model.Student
import id.lesku.lesku.helper.SqliteDbHelper
import id.lesku.lesku.adapter.StudentsAdapter
import kotlinx.android.synthetic.main.fragment_students.*

class StudentsFragment : Fragment() {

    private var dataStudents: ArrayList<Student> = ArrayList()

    private lateinit var adapterRvStudents: RecyclerView.Adapter<*>
    private lateinit var lmRvStudents: RecyclerView.LayoutManager
    private lateinit var animator: DefaultItemAnimator
    private lateinit var dividerItemDecoration: DividerItemDecoration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_students, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataStudents = loadDataStudentFromSQLiteDb()

        if(dataStudents.isNotEmpty()){
            studentsImgPlaceholder.visibility = View.GONE
            studentsTxtPlaceholder.visibility = View.GONE
            studentsRv.visibility = View.VISIBLE

            //set recycler view
            lmRvStudents = LinearLayoutManager(activity)
            studentsRv.layoutManager = lmRvStudents
            animator = DefaultItemAnimator()
            studentsRv.itemAnimator = animator
            dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            studentsRv.addItemDecoration(dividerItemDecoration)
            adapterRvStudents = StudentsAdapter(dataStudents, object: StudentsAdapter.OnItemClickListener{
                override fun onItemClick(student: Student, position: Int) {
                    goToDetailStudent(student)
                }
            })
            studentsRv.adapter = adapterRvStudents

        }else{
            studentsRv.visibility = View.GONE
            studentsImgPlaceholder.visibility = View.VISIBLE
            studentsTxtPlaceholder.visibility = View.VISIBLE
        }

        studentsFabAddStudents.setOnClickListener {
            addStudent()
        }
    }

    private fun loadDataStudentFromSQLiteDb(): ArrayList<Student> {
        val dbStudent = SqliteDbHelper(activity as DashboardActivity)
        return dbStudent.getListStudents()
    }

    private fun addStudent() {
        val intent = Intent(activity, AddStudentActivity::class.java)
        startActivity(intent)
    }

    private fun goToDetailStudent(student: Student){
        val intent = Intent(activity, DetailStudentActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): StudentsFragment {
            val fragment = StudentsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
