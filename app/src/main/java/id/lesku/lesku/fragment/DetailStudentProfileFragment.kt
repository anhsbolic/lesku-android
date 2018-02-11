package id.lesku.lesku.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.lesku.lesku.R
import id.lesku.lesku.activity.DetailStudentActivity
import id.lesku.lesku.model.Student
import kotlinx.android.synthetic.main.fragment_detail_student_profile.*

class DetailStudentProfileFragment : Fragment() {

    private var student: Student? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_student_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Get student data
        student = (activity as DetailStudentActivity).getStudentData()

        //Set data to UI
        if(student != null){
            detailStudentProfileTxtName.text = student!!.name!!
            detailStudentProfileTxtPhone.text = student!!.phone!!
            detailStudentProfileTxtWhatsapp.text = student!!.whatsapp!!
            detailStudentProfileTxtAddress.text = student!!.address!!
            detailStudentProfileTxtSchool.text = student!!.school!!
            detailStudentProfileTxtSchoolLevel.text = student!!.school_level!!
            detailStudentProfileTxtGradeLevel.text = student!!.grade_level!!
            detailStudentProfileTxtSubject.text = student!!.subject!!
        }

        //UI Handling & listener
        detailStudentProfileBtnEdit.setOnClickListener {
            editStudentData()
        }

        detailStudentProfileBtnDelete.setOnClickListener {
            deleteStudentData()
        }

    }

    private fun editStudentData() {
        Log.d("TES", "UBAH PROFIl")
    }

    private fun deleteStudentData() {
        Log.d("TES", "HAPUS SISWA")
    }

    companion object {
        fun newInstance(): DetailStudentProfileFragment {
            val fragment = DetailStudentProfileFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
