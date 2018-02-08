package id.lesku.lesku.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.lesku.lesku.R
import id.lesku.lesku.activity.AddStudentActivity
import kotlinx.android.synthetic.main.fragment_students.*

class StudentsFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_students, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        studentsFabAddStudents.setOnClickListener {
            addStudent()
        }
    }

    private fun addStudent() {
        val intent = Intent(activity, AddStudentActivity::class.java)
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
